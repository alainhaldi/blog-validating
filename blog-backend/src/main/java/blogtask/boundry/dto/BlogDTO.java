package blogtask.boundry.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import blogtask.entity.Blog;

public class BlogDTO {

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String content;

    // Konstruktoren
    public BlogDTO() {
    }

    public BlogDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Mapping-Methoden
    public Blog toBlog() {
        Blog blog = new Blog();
        blog.setId(Math.abs(UUID.randomUUID().getMostSignificantBits())); // Generate a random ID
        blog.setTitle(this.title);
        blog.setContent(this.content);
        blog.setIsValidated(false);
        return blog;
    }

    public static BlogDTO fromBlog(Blog blog) {
        return new BlogDTO(blog.getTitle(), blog.getContent());

    }
}