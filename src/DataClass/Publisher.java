package DataClass;
import java.io.Serializable;
import java.util.UUID;

public class Publisher implements Serializable {
    private String publisherId;
    private String name;
    private String address;
    private String contact;
    private String registrationDate;

    public Publisher(String name, String address, String contact, String registrationDate) {
        this.publisherId = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.registrationDate = registrationDate;
    }

    // Getters
    public String getPublisherId() { return publisherId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
    public String getRegistrationDate() { return registrationDate; }

    // Setters
    public void setAddress(String address) { this.address = address; }
    public void setContact(String contact) { this.contact = contact; }
}
