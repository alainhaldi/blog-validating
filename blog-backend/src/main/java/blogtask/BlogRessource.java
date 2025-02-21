package blogtask;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

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
}
