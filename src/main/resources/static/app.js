const API_BASE = '/api';

async function fetchJson(url, options = {}) {
  const opts = Object.assign({
    headers: { 'Content-Type': 'application/json' }
  }, options);
  const res = await fetch(url, opts);
  const text = await res.text();
  const data = text ? JSON.parse(text) : null;
  if (!res.ok) {
    const msg = (data && (data.message || (data.errors && data.errors[0] && data.errors[0].message))) || `Request failed with status ${res.status}`;
    const error = new Error(msg);
    error.status = res.status;
    error.body = data;
    throw error;
  }
  return data;
}

function el(id) { return document.getElementById(id); }

function showError(message) {
  const gb = el('globalError');
  gb.textContent = message;
  gb.style.display = 'block';
  setTimeout(() => { gb.style.display = 'none'; }, 6000);
}

function clearFormEmployee() {
  el('employeeInternalId').value = '';
  el('employeeForm').reset();
  el('currentlyWorking').checked = true;
}

function clearFormDepartment() {
  el('deptInternalId').value = '';
  el('departmentForm').reset();
}

function renderTableEmployees(page) {
  const tbody = el('employeesTable').querySelector('tbody');
  tbody.innerHTML = '';
  if (!page || !page.content) return;
  page.content.forEach(emp => {
    const tr = document.createElement('tr');

    const dept = emp.department ? (emp.department.code || emp.department.name) : '';

    tr.innerHTML = `
      <td>${emp.employeeId || ''}</td>
      <td>${emp.fullName || ''}</td>
      <td>${emp.role || ''}</td>
      <td>${dept}</td>
      <td>${emp.salary != null ? emp.salary : ''}</td>
      <td>${emp.joiningDate || ''}</td>
      <td>${emp.currentlyWorking ? 'Yes' : 'No'}</td>
      <td>
        <button class="btn" data-action="view" data-id="${emp.id}">View</button>
        <button class="btn" data-action="edit" data-id="${emp.id}">Edit</button>
        <button class="btn btn-danger" data-action="delete" data-id="${emp.id}">Delete</button>
      </td>
    `;
    tbody.appendChild(tr);
  });

  const pageInfo = el('pageInfo');
  pageInfo.textContent = `Page ${page.number + 1} of ${page.totalPages || 1}`;
  el('employeesPagination').dataset.pageIndex = page.number;
  el('employeesPagination').dataset.totalPages = page.totalPages;
}

function renderDepartmentsTable(page) {
  const tbody = el('departmentsTable').querySelector('tbody');
  tbody.innerHTML = '';
  if (!page || !page.content) return;
  page.content.forEach(dept => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${dept.id}</td>
      <td>${dept.name}</td>
      <td>${dept.code}</td>
      <td>${dept.description || ''}</td>
      <td>
        <button class="btn" data-action="edit" data-id="${dept.id}">Edit</button>
        <button class="btn btn-danger" data-action="delete" data-id="${dept.id}">Delete</button>
        <button class="btn" data-action="viewEmployees" data-id="${dept.id}">Employees</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

async function loadDepartments(page = 0, size = 100) {
  try {
    const data = await fetchJson(`${API_BASE}/departments?page=${page}&size=${size}`);
    // populate select
    const select = el('departmentSelect');
    select.innerHTML = '<option value="">-- None --</option>';
    data.content.forEach(d => {
      const opt = document.createElement('option');
      opt.value = d.id;
      opt.textContent = `${d.code} - ${d.name}`;
      select.appendChild(opt);
    });
    renderDepartmentsTable(data);
  } catch (err) {
    console.error(err);
    showError(err.message || 'Failed to load departments');
  }
}

async function loadEmployees(params = {}) {
  const page = params.page || 0;
  const size = params.size || 20;
  const qs = new URLSearchParams();
  if (params.employeeId) qs.append('employeeId', params.employeeId);
  if (params.name) qs.append('name', params.name);
  if (params.role) qs.append('role', params.role);
  if (params.department) qs.append('department', params.department);
  qs.append('page', page);
  qs.append('size', size);
  if (params.sort) qs.append('sort', params.sort);
  try {
    const data = await fetchJson(`${API_BASE}/employees?${qs.toString()}`);
    renderTableEmployees(data);
  } catch (err) {
    console.error(err);
    showError(err.message || 'Failed to load employees');
  }
}

async function employeeCreateOrUpdate(e) {
  e.preventDefault();
  const internalId = el('employeeInternalId').value;
  const dto = {
    employeeId: el('employeeId').value.trim(),
    firstName: el('firstName').value.trim(),
    lastName: el('lastName').value.trim(),
    role: el('role').value.trim(),
    salary: parseFloat(el('salary').value),
    joiningDate: el('joiningDate').value || null,
    relievingDate: el('relievingDate').value || null,
    currentlyWorking: el('currentlyWorking').checked,
    experienceYears: parseInt(el('experienceYears').value, 10),
    departmentId: el('departmentSelect').value ? parseInt(el('departmentSelect').value, 10) : null
  };

  // basic client-side validation
  if (!dto.employeeId || !dto.firstName || !dto.lastName || !dto.role || !dto.salary || !dto.joiningDate || isNaN(dto.experienceYears)) {
    showError('Please fill required fields correctly');
    return;
  }

  try {
    if (internalId) {
      await fetchJson(`${API_BASE}/employees/${internalId}`, {
        method: 'PUT',
        body: JSON.stringify(dto)
      });
      showError('Employee updated');
    } else {
      await fetchJson(`${API_BASE}/employees`, {
        method: 'POST',
        body: JSON.stringify(dto)
      });
      showError('Employee created');
    }
    clearFormEmployee();
    loadEmployees({ page: 0, size: parseInt(el('employeesPagination').dataset.pageSize || 20, 10) });
    loadDepartments();
  } catch (err) {
    console.error(err);
    const body = err.body;
    if (body && body.errors) {
      showError(body.errors.map(x => x.message).join('; '));
    } else if (body && body.message) {
      showError(body.message);
    } else {
      showError(err.message || 'Failed to save employee');
    }
  }
}

function bindEmployeeTableActions() {
  el('employeesTable').addEventListener('click', async function (ev) {
    const btn = ev.target.closest('button');
    if (!btn) return;
    const action = btn.dataset.action;
    const id = btn.dataset.id;
    if (action === 'view') {
      try {
        const dto = await fetchJson(`${API_BASE}/employees/${id}`);
        el('employeeDetailContent').innerHTML = `<pre>${JSON.stringify(dto, null, 2)}</pre>`;
        el('employeeDetail').style.display = 'flex';
        el('employeeDetail').dataset.empId = dto.employeeId;
      } catch (err) {
        showError(err.message || 'Failed to fetch details');
      }
    } else if (action === 'edit') {
      try {
        const dto = await fetchJson(`${API_BASE}/employees/${id}`);
        el('employeeInternalId').value = dto.id;
        el('employeeId').value = dto.employeeId;
        el('firstName').value = dto.firstName;
        el('lastName').value = dto.lastName;
        el('role').value = dto.role;
        el('salary').value = dto.salary;
        el('joiningDate').value = dto.joiningDate || '';
        el('relievingDate').value = dto.relievingDate || '';
        el('currentlyWorking').checked = dto.currentlyWorking;
        el('experienceYears').value = dto.experienceYears;
        el('departmentSelect').value = dto.department ? dto.department.id : '';
      } catch (err) {
        showError(err.message || 'Failed to load employee for edit');
      }
    } else if (action === 'delete') {
      if (!confirm('Delete employee?')) return;
      try {
        await fetchJson(`${API_BASE}/employees/${id}`, { method: 'DELETE' });
        showError('Employee deleted');
        loadEmployees({ page: parseInt(el('employeesPagination').dataset.pageIndex || 0, 10), size: parseInt(el('employeesPagination').dataset.pageSize || 20, 10) });
      } catch (err) {
        showError(err.message || 'Failed to delete');
      }
    }
  });
}

function bindDepartmentTableActions() {
  el('departmentsTable').addEventListener('click', async function (ev) {
    const btn = ev.target.closest('button');
    if (!btn) return;
    const action = btn.dataset.action;
    const id = btn.dataset.id;
    if (action === 'edit') {
      try {
        const dto = await fetchJson(`${API_BASE}/departments/${id}`);
        el('deptInternalId').value = dto.id;
        el('deptName').value = dto.name;
        el('deptCode').value = dto.code;
        el('deptDescription').value = dto.description || '';
      } catch (err) {
        showError(err.message || 'Failed to load department for edit');
      }
    } else if (action === 'delete') {
      if (!confirm('Delete department?')) return;
      try {
        await fetchJson(`${API_BASE}/departments/${id}`, { method: 'DELETE' });
        showError('Department deleted');
        loadDepartments();
        loadEmployees();
      } catch (err) {
        showError(err.message || 'Failed to delete department');
      }
    } else if (action === 'viewEmployees') {
      try {
        const list = await fetchJson(`${API_BASE}/departments/${id}/employees`);
        if (list && list.length) {
          renderModalList('Employees in Department', list.map(e => `${e.employeeId} - ${e.fullName}`).join('\n'));
        } else {
          renderModalList('Employees in Department', 'No employees assigned');
        }
      } catch (err) {
        showError(err.message || 'Failed to fetch employees');
      }
    }
  });
}

function renderModalList(title, text) {
  el('employeeDetailContent').innerHTML = `<h4>${title}</h4><pre>${text}</pre>`;
  el('employeeDetail').style.display = 'flex';
}

function bindPagination() {
  el('prevPage').addEventListener('click', function () {
    let idx = parseInt(el('employeesPagination').dataset.pageIndex || 0, 10);
    if (idx > 0) {
      loadEmployees({ page: idx - 1, size: parseInt(el('employeesPagination').dataset.pageSize || 20, 10) });
    }
  });
  el('nextPage').addEventListener('click', function () {
    let idx = parseInt(el('employeesPagination').dataset.pageIndex || 0, 10);
    const total = parseInt(el('employeesPagination').dataset.totalPages || 1, 10);
    if (idx + 1 < total) {
      loadEmployees({ page: idx + 1, size: parseInt(el('employeesPagination').dataset.pageSize || 20, 10) });
    }
  });
}

function debounce(fn, delay=300) {
  let t;
  return function (...args) {
    clearTimeout(t);
    t = setTimeout(() => fn.apply(this, args), delay);
  };
}

async function viewSalaryStatement() {
  const empId = el('employeeDetail').dataset.empId;
  if (!empId) return;
  try {
    const s = await fetchJson(`${API_BASE}/employees/${empId}/salary-statement`);
    renderModalList('Salary Statement', JSON.stringify(s, null, 2));
  } catch (err) {
    showError(err.message || 'Failed to load salary statement');
  }
}

document.addEventListener('DOMContentLoaded', function () {
  loadDepartments();
  loadEmployees({ page: 0, size: 20 });

  bindEmployeeTableActions();
  bindDepartmentTableActions();
  bindPagination();

  el('employeeForm').addEventListener('submit', employeeCreateOrUpdate);
  el('departmentForm').addEventListener('submit', async function (ev) {
    ev.preventDefault();
    const id = el('deptInternalId').value;
    const dto = {
      name: el('deptName').value.trim(),
      code: el('deptCode').value.trim(),
      description: el('deptDescription').value.trim()
    };
    if (!dto.name || !dto.code) {
      showError('Please provide name and code');
      return;
    }
    try {
      if (id) {
        await fetchJson(`${API_BASE}/departments/${id}`, { method: 'PUT', body: JSON.stringify(dto) });
        showError('Department updated');
      } else {
        await fetchJson(`${API_BASE}/departments`, { method: 'POST', body: JSON.stringify(dto) });
        showError('Department created');
      }
      clearFormDepartment();
      loadDepartments();
    } catch (err) {
      console.error(err);
      const body = err.body;
      if (body && body.errors) {
        showError(body.errors.map(x => x.message).join('; '));
      } else if (body && body.message) {
        showError(body.message);
      } else {
        showError(err.message || 'Failed to save department');
      }
    }
  });

  el('resetEmployeeBtn').addEventListener('click', function () { clearFormEmployee(); });
  el('resetDeptBtn').addEventListener('click', function () { clearFormDepartment(); });

  el('linkEmployees').addEventListener('click', function (e) {
    e.preventDefault();
    el('employeesSection').style.display = '';
    el('departmentsSection').style.display = 'none';
  });
  el('linkDepartments').addEventListener('click', function (e) {
    e.preventDefault();
    el('employeesSection').style.display = 'none';
    el('departmentsSection').style.display = '';
  });

  el('closeDetail').addEventListener('click', function () {
    el('employeeDetail').style.display = 'none';
    el('employeeDetailContent').innerHTML = '';
  });

  el('salaryStatementBtn').addEventListener('click', viewSalaryStatement);

  el('globalSearch').addEventListener('input', debounce(function (ev) {
    const q = ev.target.value.trim();
    loadEmployees({ name: q, page: 0, size: 20 });
  }, 400));
});
