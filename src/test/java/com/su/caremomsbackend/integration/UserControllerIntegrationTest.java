package com.su.caremomsbackend.integration;

import com.su.caremomsbackend.service.TokenValidationService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(TestContainersConfiguration.class)
public class UserControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    TokenValidationService tokenValidationService;


    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    @Sql(scripts = "insert-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserTest(){
        assertThat(tokenValidationService.getAdminUser("john.doe@example.com").getUserName()).isEqualTo("John doe");
    }

    @Test
    @Sql(scripts = "insert-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testGetUser() {
        given()
                .port(port)
                .contentType("application/json").header("Admin", "john.doe@example.com")
                .when()
                .get("api/users/me")
                .then()
                .statusCode(200)
                .body("supabaseId", equalTo("cxdsfreywhjnfgtjsks145587965248ghtd"))
                .body("email", equalTo("john.doe@example.com"))
                .body("userName", equalTo("John doe"))
                .body("role", equalTo("Admin"))
                .body("dob", equalTo("12/12/2000"));
    }
}
