
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogValidierung {

    // Empfangen des Blog-Posts als String von "blog-topic"
    @Incoming("request")
    @Blocking
    public void consumeBlogPost(String blogPost) {
        System.out.println(">> Validierung Empfangener Blog-Post: " + blogPost);
    }
}
