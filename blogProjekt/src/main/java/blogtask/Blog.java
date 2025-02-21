package blogtask;

public class Blog {
    private String title;
    private String content;

    // Getter und Setter
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

    public String toString() {
        return "Blog-Post: " + title + " - " + content;
    }
}