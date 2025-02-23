package blogtask;

import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

/*
Rest-Service for sending Blog-Posts to the "blog-topic" topic
*/
@Path("/blog")
public class BlogRessource {

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
    // Recieve Add Blog Request
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

}
