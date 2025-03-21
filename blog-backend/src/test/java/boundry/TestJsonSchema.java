package boundry;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

import org.junit.Test;

public class TestJsonSchema {

    @Test
    public void testJsonSchema() {
        given()
                .when().get("http://localhost:8080/blog/find/20")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema.json"));
    }
}
