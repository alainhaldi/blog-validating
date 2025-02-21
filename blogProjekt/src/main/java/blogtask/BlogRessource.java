package blogtask;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/blog")
public class BlogRessource {

    @Channel("blog-topic")
    Emitter<String> quoteRequestEmitter;

    // http http://localhost:8080/blog/request title='Mein neuer Blog'
    // content='Content meines neuen Bloges'
    @POST
    @Path("/request")
    public Response sendBlogPost(Blog blog) {
        // Senden des Blog-Objekts als String ans Topic "blog-topic" welches vom
        // ValidierungsService empfangen wird
        quoteRequestEmitter.send(blog.toString());
        System.out.println(">> Backend received Blog");
        return Response.accepted().build();
    }
}
