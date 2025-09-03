package DataClass;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class LibraryCard implements Serializable {
    private String cardId;
    private String memberId; // Foreign Key: Member
    private Date issueDate;
    private Date expiryDate;
    private String status;

    public LibraryCard(String memberId, Date issueDate, Date expiryDate, String status) {
        this.cardId = UUID.randomUUID().toString();
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    // Getters
    public String getCardId() { return cardId; }
    public String getMemberId() { return memberId; }
    public Date getIssueDate() { return issueDate; }
    public Date getExpiryDate() { return expiryDate; }
    public String getStatus() { return status; }

    // Setters
    public void setStatus(String status) { this.status = status; }

    // Formatted Dates
    public String getFormattedIssueDate() { return new SimpleDateFormat("yyyy-MM-dd").format(issueDate); }
    public String getFormattedExpiryDate() { return new SimpleDateFormat("yyyy-MM-dd").format(expiryDate); }
}
