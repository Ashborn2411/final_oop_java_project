package DataClass;
import java.io.Serializable;
import java.util.UUID;

public class Book implements Serializable {
    private String bookId;
    private String title;
    private String authorId; // Foreign Key: Author
    private String publisherId; // Foreign Key: Publisher
    private String sectionId; // Foreign Key: Section
    private String isbn;
    private String publicationDate;
    private String genre;
    private int numberOfCopies;
    private String imagePath;
    private String bookPath;

    public Book(String title, String authorId, String publisherId, String sectionId, String isbn,
                String publicationDate, String genre, int numberOfCopies, String imagePath, String bookPath) {
        this.bookId = UUID.randomUUID().toString();
        this.title = title;
        this.authorId = authorId;
        this.publisherId = publisherId;
        this.sectionId = sectionId;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.numberOfCopies = numberOfCopies;
        this.imagePath = imagePath;
        this.bookPath = bookPath;
    }

    // Getters
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthorId() { return authorId; }
    public String getPublisherId() { return publisherId; }
    public String getSectionId() { return sectionId; }
    public String getIsbn() { return isbn; }
    public String getPublicationDate() { return publicationDate; }
    public String getGenre() { return genre; }
    public int getNumberOfCopies() { return numberOfCopies; }
    public String getImagePath() { return imagePath; }
    public String getBookPath() { return bookPath; }

    // Setters
    public void setNumberOfCopies(int numberOfCopies) { this.numberOfCopies = numberOfCopies; }
    public void setSectionId(String sectionId) { this.sectionId = sectionId; }
}
