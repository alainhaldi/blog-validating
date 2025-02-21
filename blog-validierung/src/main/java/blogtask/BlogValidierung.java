package blogtask;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;

/*
Consumer for the "request" topic
*/
@ApplicationScoped
public class BlogValidierung {

    // Receive and validate the Blog-Post via the "request" topic
    @Incoming("request")
    @Outgoing("validated")
    @Blocking
    public String consumeBlogPost(String blogPost) {
        System.out.println("\n>> blog-validierung: Empfangener Blog-Post: " + blogPost);
        return "Blog validated!";
    }

    @Incoming("validation-request")
    @Outgoing("validation-response")
    public Multi<ValidationResponse> validateTextMessages(Multi<ValidationRequest> requests) {
        return requests
                .onItem().transform(request -> {
                    boolean valid = !request.text().contains("hftm sucks");
                    System.out.println("Text-Validation: " + request.text() + " -> " + valid);
                    return new ValidationResponse(request.id(), valid);
                });
    }
}
