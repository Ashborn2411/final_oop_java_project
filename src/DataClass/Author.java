package DataClass;
import java.io.Serializable;
import java.util.UUID;

public class Author implements Serializable {
    private String authorId;
    private String name;
    private String nationality;
    private String birthDate;
    private String bio;

    public Author(String name, String nationality, String birthDate, String bio) {
        this.authorId = UUID.randomUUID().toString();
        this.name = name;
        this.nationality = nationality;
        this.birthDate = birthDate;
        this.bio = bio;
    }

    // Getters
    public String getAuthorId() { return authorId; }
    public String getName() { return name; }
    public String getNationality() { return nationality; }
    public String getBirthDate() { return birthDate; }
    public String getBio() { return bio; }

    // Setters
    public void setBio(String bio) { this.bio = bio; }
    public void setNationality(String nationality) { this.nationality = nationality; }
}
