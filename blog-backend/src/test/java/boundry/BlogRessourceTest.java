package boundry;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class BlogRessourceTest {

    // @Inject
    // BlogRepository blogRepository; // Inject the test repository

    // @BeforeEach
    // @TestTransaction // Ensures data is committed for testing
    // void setupTestData() {
    // blogRepository.persist(List.of(
    // new Blog(1L, "Test Blog 1", "Content 1", false),
    // new Blog(2L, "Test Blog 2", "Content 2", false)));
    // }

    // @Test
    // public void testGetBlogsEndpoint() {
    // given()
    // .when().get("http://localhost:8080/blog/listAll")
    // .then()
    // .statusCode(200)
    // .body(containsString("Test Blog 1"))
    // .body(containsString("Content 1"))
    // .body(containsString("Test Blog 2"))
    // .body(containsString("Content 2"));
    // }

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("http://localhost:8080/blog/hello")
                .then()
                .statusCode(200)
                .body(is("Moin Meyster!"));
    }

    @Test
    public void testGetBlogsEndpoint() {
        given()
                .when().get("http://localhost:8080/blog/listAll")
                .then()
                .statusCode(200)
                .body(containsString("Erster Blog"));
    }

    @Test
    public void testGetEntriesPasswdEndpointSuccess() {
        given()
                .header("password", "mySecretPassword")
                .when().get("http://localhost:8080/blog/showBlogsPsswd")
                .then()
                .statusCode(200)
                .body(containsString("Erster Blog"));
    }

    @Test
    public void testGetEntriesPasswdEndpointDenied() {
        given()
                .header("password", "wrongPassword")
                .when().get("http://localhost:8080/blog/showBlogsPsswd")
                .then()
                .statusCode(401);
    }

    @Test
    public void testAddBlogEndpoint() {
        given()
                .header("Content-Type", "application/json")
                .body("{\"title\":\"Neuer Blog\",\"content\":\"Neuer Inhalt\"}")
                .when().post("http://localhost:8080/blog/add")
                .then()
                .statusCode(200)
                .body(is("Neuer Blog: \"Neuer Blog\" erfolgreich hinzugefügt"));
    }

    @Test
    public void testUpdateBlogTitleEndpointSuccess() {
        given()
                .header("newTitle", "Neuer Titel")
                .when().patch("http://localhost:8080/blog/updateTitle/20")
                .then()
                .statusCode(200)
                .body(is("BlogTitle erfolgreich aktualisiert \n\nNeuer Titel: Neuer Titel"));
    }

    @Test
    public void testUpdateBlogTitleEndpointFail() {
        given()
                .header("newTitle", "Neuer Titel")
                .when().patch("http://localhost:8080/blog/updateTitle/200")
                .then()
                .statusCode(404)
                .body(is("Blog nicht gefunden"));
    }

    @Test
    public void testUpdateBlogContentEndpointSuccess() {
        given()
                .header("newContent", "Neuer Inhalt")
                .when().patch("http://localhost:8080/blog/updateContent/20")
                .then()
                .statusCode(200)
                .body(is("BlogContent erfolgreich aktualisiert \n\nNeuer Content: Neuer Inhalt"));
    }

    @Test
    public void testUpdateBlogContentEndpointFail() {
        given()
                .header("newContent", "Neuer Inhalt")
                .when().patch("http://localhost:8080/blog/updateContent/200")
                .then()
                .statusCode(404)
                .body(is("Blog nicht gefunden"));
    }

    @Test
    public void testUpdateBlogSuccess() {
        given()
                .header("newTitle", "Neuer Titel")
                .header("newContent", "Neuer Inhalt")
                .when().put("http://localhost:8080/blog/updateBlog/20")
                .then()
                .statusCode(200)
                .body(is("Kompletter Blog erfolgreich ersetzt \n\nNeuer Titel: Neuer Titel"
                        + "\nNeuer Content: Neuer Inhalt"));
    }

    @Test
    public void testUpdateBlogFail() {
        given()
                .header("newTitle", "Neuer Titel")
                .header("newContent", "Neuer Inhalt")
                .when().put("http://localhost:8080/blog/updateBlog/200")
                .then()
                .statusCode(404)
                .body(is("Die Anfrage konnte nicht erfolgreich verarbeitet werden. Verwende eine gültige ID."));
    }

    @Test
    public void testDeleteBlog() {
        given()
                .when().post("http://localhost:8080/blog/delete/30")
                .then()
                .statusCode(200)
                .body(containsString("erfolgreich gelöscht"));
    }

}
