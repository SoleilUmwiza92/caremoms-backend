package com.su.caremomsbackend.e2e;

import com.su.caremomsbackend.integration.TestContainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Testcontainers
@Import(TestContainersConfiguration.class)
@Sql(scripts = "insert-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class MessagesApiE2ETest extends RestAssuredTestBase {

    @Test
    void shouldRejectPostWithoutToken() {
        String payload = """
      {
        "roomId":"2",
        "content":"Any update?",
        "receiverEmail": "soleilmwiza1@example.com"
      }
      """;

        given()
                .port(port)
                .contentType("application/json")
                .accept("application/json")
                .body(payload)
                .when()
                .post("/api/messages")
                .then()
                .statusCode(anyOf(is(401), is(403)));
    }

    @Test
    @Sql(scripts = "delete-messages.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldCreateMessage_user_with_access() {

        String uniqueContent = "Any update? e2e-" + System.currentTimeMillis();

        String payload = """
      {
        "roomId":"2",
        "content":"%s",
        "receiverEmail": "soleilmwiza1@example.com"
      }
      """.formatted(uniqueContent);

        given()
                .port(port)
                .contentType("application/json")
                .accept("application/json")
                .header("Admin", "joe.doe@example.com")
                .body(payload)
                .when()
                .post("/api/messages")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .contentType(containsString("application/json"))
                .body("id", allOf(notNullValue(), greaterThan(0)))
                .body("roomId", equalTo("2"))
                .body("content", equalTo(uniqueContent))
                .body("receiver", equalTo("soleilmwiza1@example.com"))
                .body("createdAt", allOf(notNullValue(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T.*Z$")))
                .body("sender", notNullValue())
                .body("sender.id", allOf(notNullValue(), greaterThan(0)))
                .body("sender.email", allOf(notNullValue(), containsString("@")))
                .body("sender.role", notNullValue())
                .body("sender.userName", notNullValue());
    }
}
