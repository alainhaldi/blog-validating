package blogtask.boundry;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.ws.rs.core.Context;
import blogtask.boundry.dto.BlogDTO;
import blogtask.control.BlogService;
import blogtask.entity.Blog;
import blogtask.entity.ValidationRequest;
import blogtask.entity.ValidationResponse;
import io.quarkus.logging.Log;

/*
Rest-Service for sending Blog-Posts to the "blog-topic" topic
*/
// Haupt Pfad: http://localhost:8080/blog
@Path("/blog")
public class BlogRessource {

    // ------------------------------------------------------------------
    // First Communications Test
    // ------------------------------------------------------------------

    // Topic for sending Blog-Posts
    @Channel("blog-topic")
    Emitter<String> quoteRequestEmitter;

    /*
     * http http://localhost:8080/blog/request title='Mein neuer Blog'
     * content='Content meines neuen Bloges'
     */
    @POST
    @Path("/request")
    public Response sendBlogPost(Blog blog) {
        // Send the Blog-Object as String to the "blog-topic" topic
        quoteRequestEmitter.send(blog.toString());
        System.out.println("\n>> blog-backend: Received Blog via request");
        return Response.accepted().build();
    }

    // ------------------------------------------------------------------
    // Recieve Add Blog Request via HTTP
    // ------------------------------------------------------------------

    @Inject
    @Channel("validation-request")
    Emitter<ValidationRequest> validationRequEmitter;

    /*
     * http http://localhost:8080/blog/addBlog id=1 title='Mein neuer Blog'
     * content='Content meines neuen Bloges'
     */
    @POST
    @Path("/addBlog")
    public Response addBlog(Blog entry) {
        this.blogService.addBlog(entry);
        // Send the Blog-Object as String to the "blog-topic" topic
        validationRequEmitter.send(new ValidationRequest(entry.getId(), entry.getTitle() + " " + entry.getContent()));
        System.out.println("\n>> blog-backend: Received Blog via addBlog");
        return Response.accepted().build();
    }

    // ------------------------------------------------------------------
    // Receive Validation
    // ------------------------------------------------------------------

    @Incoming("validation-response")
    @Transactional
    public void sink(ValidationResponse validationResponse) {
        System.out.println("Validation Response: " + validationResponse);
        this.blogService.updateValidationStatus(validationResponse.id(), validationResponse.valid());
        // Optional<Entry> entryOptional =
        // Entry.findByIdOptional(validationResponse.id);
        // if (entryOptional.isEmpty()) {
        // System.out.println("Entry not found");
        // return;
        // }
        // entryOptional.get().approved = validationResponse.valid;
    }

    // ------------------------------------------------------------------
    // Default Methods
    // ------------------------------------------------------------------

    // http GET http://localhost:8080/blog/hello
    @GET
    @Path("/hello")
    public String hello() {
        return "Moin Meyster!";
    }

    @Inject
    BlogService blogService;

    // Gibt alle Blogs im Text Format aus, sofern das Passwort richtig ist
    // Request: http GET http://localhost:8080/blog/listAll
    @GET
    @Path("listAll")
    // @Produces(MediaType.TEXT_PLAIN)
    public String getBlogs() {
        List<Blog> blogs = this.blogService.getBlogs();
        return blogs.toString();
    }

    // Gibt alle Blogs im Text Format aus, sofern das Passwort richtig ist
    // Request: http GET http://localhost:8080/blog/showBlogsPsswd
    // password:mySecretPassword
    @GET
    @Path("showBlogsPsswd")
    public Response getEntriesPsswd(@HeaderParam("password") String password) {

        if ("mySecretPassword".equals(password)) {
            List<Blog> blogs = this.blogService.getBlogs();
            return Response.ok(blogs.toString()).build();
        } else {
            return Response.status(401).build();
        }
    }

    // Fügt einen Blog hinzu via eine Post Requests
    // Request: http POST http://localhost:8080/blog/add title="Mein neuer Blogpost"
    // content="Dies ist der Inhalt meines neuen Blogposts."
    // publishedAt="2023-08-10"
    @POST
    @Path("/add")
    @Tag(name = "Manage Blogs")
    @Operation(description = "Add new Blog-Post.")
    @RequestBody(content = @Content(example = "{\"title\": \"string\", \"content\": \"string\", \"publishedAt\": \"string\"}"))
    @APIResponses({ @APIResponse(responseCode = "204", description = "Successful") })
    public Response addBlog(BlogDTO blogDto, @Context UriInfo uriInfo) {
        Blog blog = blogDto.toBlog();
        this.blogService.addBlog(blog);
        Log.info("=> Adding Blog " + blog.getId());
        return Response.ok("Neuer Blog: \"" + blog.getTitle() + "\" erfolgreich hinzugefügt").build();
    }

    // Ändere den Titel eines Blogs
    // Request: http PATCH http://localhost:8080/blog/updateTitle/2 newTitle:"Der
    // ultimative Blog"
    @PATCH
    @Path("/updateTitle/{id}")
    // @Produces(MediaType.TEXT_PLAIN)
    @Tag(name = "Edit Blog")
    public Response updateBlogTitle(@PathParam("id") Long id, @HeaderParam("newTitle") String newTitle) {
        Blog blog = null;

        if (blogService.containsBlog(id)) {
            blog = blogService.findBlogById(id);
        }

        if (blog != null && blog.getId().equals(id)) {
            blogService.updateBlogTitle(id, newTitle);
            Log.info("=> Der Titel von Blog " + id + " wurde mit ->" + newTitle + "<- aktualisiert");
            return Response.ok("BlogTitle erfolgreich aktualisiert \n\nNeuer Titel: " + newTitle).build();
        } else {
            Log.error("❗️ => Failed to find Blog" + id);
            return Response.status(Response.Status.NOT_FOUND).entity("Blog nicht gefunden").build();
        }
    }

    // Ändere den Text eines Blogs
    // Request: http PATCH http://localhost:8080/blog/updateContent/2
    // newContent:"Der ultimative Text"
    @PATCH
    @Path("/updateContent/{id}")
    @Tag(name = "Edit Blog")
    public Response updateBlogText(@PathParam("id") Long id, @HeaderParam("newContent") String newContent) {
        Blog blog = null;

        if (blogService.containsBlog(id)) {
            blog = blogService.findBlogById(id);
        }

        if (blog != null && blog.getId().equals(id)) {
            blogService.updateBlogContent(id, newContent);
            Log.info("=> Der Content von Blog " + id + " wurde mit ->" + newContent + "<- aktualisiert.");
            return Response.ok("BlogContent erfolgreich aktualisiert \n\nNeuer Content: " + newContent).build();
        } else {
            Log.error("❗️ => Failed to find Blog" + id);
            return Response.status(Response.Status.NOT_FOUND).entity("Blog nicht gefunden").build();
        }
    }

    // Ändere den ganzen Blog
    // Request: http PUT http://localhost:8080/blog/updateBlog/2 newTitle:"Der etwas
    // andere Blog" newContent:"Der andere Text"
    @PUT
    @Path("/updateBlog/{id}")
    @Tag(name = "Edit Blog")
    public Response updateEntireBlog(
            @PathParam("id") Long id,
            @HeaderParam("newTitle") String newTitle,
            @HeaderParam("newContent") String newContent) {

        Blog blog = null;

        if (blogService.containsBlog(id)) {
            blog = blogService.findBlogById(id);
        }

        if (blog != null) {
            blogService.updateEntireBlog(id, newTitle, newContent);
            Log.info("=> Blog " + id + " wurde aktualisiert");
            return Response.ok("Kompletter Blog erfolgreich ersetzt \n\nNeuer Titel: " + newTitle + "\nNeuer Content: "
                    + newContent).build();
        } else {
            String errorMessage = "Die Anfrage konnte nicht erfolgreich verarbeitet werden. Verwende eine gültige ID.";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }
    }

    // Löscht einen beliebigen Blog
    // Request: http POST http://localhost:8080/blog/delete/3
    @POST
    @Path("/delete/{id}")
    @Tag(name = "Manage Blogs")
    public Response deleteBlog(@PathParam("id") Long id) {
        Blog blog = blogService.findBlogById(id);
        this.blogService.deleteBlog(id);
        Log.info("=> Deleting Blog " + id);
        return Response.ok("Blog: \"" + blog.getTitle() + "\" erfolgreich gelöscht").build();
    }

    // Gibt den gewünschten Blog aus
    // Request: http GET http://localhost:8080/blog/find/2
    @GET
    @Path("/find/{id}")
    @Tag(name = "Filter Blogs")
    public BlogDTO getBlog(@PathParam("id") Long id) {
        Blog blog = this.blogService.findBlogById(id);
        return BlogDTO.fromBlog(blog);
    }

    // Durchsuche alle Blogs mit einem SearchString
    // Request: http GET http://localhost:8080/blog/search searchString==Zweiter
    @GET
    @Path("/search")
    @Tag(name = "Filter Blogs")
    public List<BlogDTO> searchBlogs(@QueryParam("searchString") String searchString) {
        return this.blogService.getBlogs().stream()
                .filter(blog -> blog.getTitle().contains(searchString) ||
                        blog.getContent().contains(searchString))
                .map(BlogDTO::fromBlog)
                .collect(Collectors.toList());
    }

}
