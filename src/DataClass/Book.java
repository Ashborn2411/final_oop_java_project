import java.io.Serializable;

public class Book implements Serializable {
    private String id;
    private String title;
    private String author;

    // Constructor, getters, setters
    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
}
