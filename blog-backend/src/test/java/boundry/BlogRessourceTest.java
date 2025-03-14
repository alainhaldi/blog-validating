package boundry;

import org.junit.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BlogRessourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("http://localhost:8080/blog/hello")
                .then()
                .statusCode(200)
                .body(is("Moin Meyster!"));
    }

    // @Test
    // public void testGreetingEndpoint() {
    // String uuid = UUID.randomUUID().toString();
    // given()
    // .pathParam("name", uuid)
    // .when().get("/hello/greeting/{name}")
    // .then()
    // .statusCode(200)
    // .body(is("hello " + uuid));
    // }

}
