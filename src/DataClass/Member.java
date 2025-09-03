package DataClass;

import java.io.Serializable;

public class Member implements Serializable {
    private String memberId;
    private String imagePath;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private String localCardNumber;

    public Member(String memberId, String name, String email, String address, String phoneNumber, String localCardNumber, String imagePath) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.localCardNumber = localCardNumber;
        this.imagePath = imagePath;
    }

    // Getters
    public String getMemberId() { return memberId; }
    public String getImagePath() { return imagePath; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getLocalCardNumber() { return localCardNumber; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setLocalCardNumber(String localCardNumber) { this.localCardNumber = localCardNumber; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
