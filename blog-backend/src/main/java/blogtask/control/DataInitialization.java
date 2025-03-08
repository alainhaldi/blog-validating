package blogtask.control;

import blogtask.entity.Blog;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataInitialization {

    @Inject
    BlogService blogService; // Dein Service zum Verwalten der Blogs

    @Transactional
    public void init(@Observes StartupEvent event) {
        if (blogService.getBlogs().isEmpty()) { // Überprüfen, ob Daten bereits vorhanden sind

            // Beispiel-Blogs und -Kommentare hinzufügen
            Blog ersterBlog = new Blog(10, "Erster Blog", "Inhalt des ersten Blogs", false);
            Blog zweiterBlog = new Blog(20, "Zweiter Blog", "Inhalt des zweiten Blogs", true);
            Blog dritterBlog = new Blog(30, "Dritter Blog", "Inhalt des dritten Blogs", false);

            // In Datenbank speichern
            blogService.addBlog(ersterBlog);
            blogService.addBlog(zweiterBlog);
            blogService.addBlog(dritterBlog);
        }
    }
}
