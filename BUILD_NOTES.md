Build Notes & Tips

- When building inside Docker or CI, set MAVEN_OPTS to provide enough memory:
  export MAVEN_OPTS="-Xmx1024m"

- JVM options recommended for small apps:
  java -Xms256m -Xmx512m -jar target/employee-data-management-system-0.0.1-SNAPSHOT.jar

- Lombok: if you decide to use Lombok annotations, enable annotation processing in your IDE:
  - IntelliJ: Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> Enable

- Debugging:
  mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
