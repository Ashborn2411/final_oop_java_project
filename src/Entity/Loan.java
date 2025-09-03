package Entity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Loan implements Serializable {
    private String loanId;
    private String bookId; // Foreign Key: Book
    private String memberId; // Foreign Key: Member
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;

    public Loan(String bookId, String memberId) {
        this.loanId = UUID.randomUUID().toString();
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = LocalDateTime.now();
        this.returnDate = null;
    }

    // Getters
    public String getLoanId() { return loanId; }
    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public LocalDateTime getLoanDate() { return loanDate; }
    public LocalDateTime getReturnDate() { return returnDate; }

    // Setters
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public void setBookId(String newBookId) {
    }

    public void setMemberId(String newMemberId) {
        memberId=newMemberId;
    }
}
