package io.quarkiverse.univocityparsers.univocity.parsers.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class UnivocityParsersResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/univocity-parsers")
                .then()
                .statusCode(200)
                .body(is("Hello univocity-parsers"));
    }
}
