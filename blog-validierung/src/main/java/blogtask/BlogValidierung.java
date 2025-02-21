
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;

/*
Consumer for the "request" topic
*/
@ApplicationScoped
public class BlogValidierung {

    // Receive and validate the Blog-Post via the "request" topic
    @Incoming("request")
    @Blocking
    public void consumeBlogPost(String blogPost) {
        System.out.println("\n>> blog-validierung: Empfangener Blog-Post: " + blogPost);
    }
}
