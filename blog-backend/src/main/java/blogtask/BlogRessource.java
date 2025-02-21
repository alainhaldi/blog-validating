package blogtask;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Path;
import jakarta.inject.Inject;

/*
Rest-Service for sending Blog-Posts to the "blog-topic" topic
*/
@Path("/blog")
public class BlogRessource {

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
        System.out.println("\n>> blog-backend: Received Blog");
        return Response.accepted().build();
    }

    public record ValidationRequest(long id, String text) {
    }

    @Inject
    @Channel("validation-request")
    Emitter<ValidationRequest> validationRequEmitter;

    /*
     * http http://localhost:8080/blog/addBlog title='Mein neuer Blog'
     * content='Content meines neuen Bloges'
     */
    @POST
    @Path("/addBlog")
    public Response addBlog(Blog entry) {
        // Send the Blog-Object as String to the "blog-topic" topic
        validationRequEmitter.send(new ValidationRequest(entry.getId(), entry.getTitle() + " " + entry.getContent()));
        System.out.println("\n>> blog-backend: Received Blog");
        return Response.accepted().build();
    }

    // // Injects the quotes channel using the @Channel qualifier
    // @Channel("validated")
    // Multi<String> validation;

    // @GET
    // // Indicates that the content is sent using Server Sent Events
    // @Produces(MediaType.SERVER_SENT_EVENTS)
    // public Multi<String> stream() {
    // // Returns the stream (Reactive Stream)
    // System.out.println("\n>> blog-backend: Received Validation");
    // return validation;
    // }
}
