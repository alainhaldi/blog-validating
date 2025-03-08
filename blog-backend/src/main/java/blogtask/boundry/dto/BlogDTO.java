package blogtask.boundry.dto;

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
        blog.setTitle(this.title);
        blog.setContent(this.content);
        return blog;
    }

    public static BlogDTO fromBlog(Blog blog) {
        return new BlogDTO(blog.getTitle(), blog.getContent());

    }
}