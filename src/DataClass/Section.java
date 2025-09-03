package DataClass;
import java.io.Serializable;
import java.util.UUID;

public class Section implements Serializable {
    private String sectionId;
    private String name;
    private String location;

    public Section(String name, String location) {
        this.sectionId = UUID.randomUUID().toString();
        this.name = name;
        this.location = location;
    }

    // Getters
    public String getSectionId() { return sectionId; }
    public String getName() { return name; }
    public String getLocation() { return location; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
}
