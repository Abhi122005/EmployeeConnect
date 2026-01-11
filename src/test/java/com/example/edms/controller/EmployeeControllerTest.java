package com.example.edms.controller;

import com.example.edms.dto.EmployeeRequestDto;
import com.example.edms.dto.EmployeeResponseDto;
import com.example.edms.mapper.DtoMapper;
import com.example.edms.service.EmployeeService;
import com.example.edms.service.ImageUploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(controllers = EmployeeController.class)
@WithMockUser(username = "admin", roles = {"ADMIN"})
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    /**
     * THIS IS THE FIX:
     * We must provide mocks for ALL dependencies required by the controller's constructor.
     * These were missing, which caused the ApplicationContext to fail to load.
     */
    @MockBean
    private DtoMapper dtoMapper;

    @MockBean
    private ImageUploadService imageUploadService;

    @Test
    void testSearchEmployeesEndpoint() throws Exception {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(1L);
        dto.setEmployeeId("EMP1001");
        dto.setFullName("Alice Smith");

        Page<EmployeeResponseDto> page = new PageImpl<>(Collections.singletonList(dto), PageRequest.of(0, 20), 1);
        Mockito.when(employeeService.search(Mockito.isNull(), Mockito.eq("Alice"), Mockito.isNull(), Mockito.isNull(), Mockito.any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/employees")
                        .param("name", "Alice")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].employeeId", is("EMP1001")));
    }

    @Test
    void testCreateEmployeeValidationFails() throws Exception {
        EmployeeRequestDto dto = new EmployeeRequestDto(); // Invalid DTO

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}

