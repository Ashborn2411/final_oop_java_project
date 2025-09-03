import java.io.Serializable;

public class Member implements Serializable {
    private String id;
    private String name;
    private String email;

    // Constructor, getters, setters
    public Member(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setEmail(String newEmail) {
    }

    public void setName(String newName) {
    }
}
