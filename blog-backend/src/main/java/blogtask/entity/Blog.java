package blogtask.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Blog {
    @Id
    private long id;
    private String title;
    private String content;
    private Boolean isValidated = false;

    public Blog() {
    }

    public Blog(long id, String title, String content, Boolean isValidated) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isValidated = isValidated;
    }

    // Getter und Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(Boolean isValidated) {
        this.isValidated = isValidated;
    }

    public String toString() {
        return "\nBlog-Post: " + id + " - " + title + " - " + content + " - " + isValidated;
    }
}