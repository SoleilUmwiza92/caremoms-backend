package com.su.caremomsbackend.e2e;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class MessagesApiE2ETest extends RestAssuredTestBase {

    @Test
    void shouldRejectPostWithoutToken() {
        String payload = """
      {
        "roomId":"2",
        "content":"Any update?",
        "receiverId":2
      }
      """;

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(payload)
                .when()
                .post("/api/messages")
                .then()
                .statusCode(anyOf(is(401), is(403)));
    }

    @Test
    void shouldCreateMessage_WithValidToken() {
        String token = System.getenv("API_TOKEN");
        Assumptions.assumeTrue(token != null && !token.isBlank(),
                "Skipping because API_TOKEN is not set.");

        String uniqueContent = "Any update? e2e-" + System.currentTimeMillis();

        String payload = """
      {
        "roomId":"2",
        "content":"%s",
        "receiverId":2
      }
      """.formatted(uniqueContent);

        given()
                .contentType("application/json")
                .accept("application/json")
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post("/api/messages")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .contentType(containsString("application/json"))
                .body("id", allOf(notNullValue(), greaterThan(0)))
                .body("roomId", equalTo("2"))
                .body("content", equalTo(uniqueContent))
                .body("receiver", equalTo("2"))
                .body("createdAt", allOf(notNullValue(), matchesPattern("^\\d{4}-\\d{2}-\\d{2}T.*Z$")))
                .body("sender", notNullValue())
                .body("sender.id", allOf(notNullValue(), greaterThan(0)))
                .body("sender.email", allOf(notNullValue(), containsString("@")))
                .body("sender.role", notNullValue())
                .body("sender.userName", notNullValue());
    }
}
