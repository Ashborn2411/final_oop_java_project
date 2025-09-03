import java.io.Serializable;

public class Loan implements Serializable {
    private String loanId;
    private String bookId;
    private String memberId;
    private String loanDate;

    // Constructor, getters, setters
    public Loan(String loanId, String bookId, String memberId, String loanDate) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
    }

    // Getters and setters
    public String getLoanId() { return loanId; }
    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public String getLoanDate() { return loanDate; }

    public void setBookId(String newBookId) {
    }

    public void setMemberId(String newMemberId) {
    }

    public void setLoanDate(String newLoanDate) {
    }
}
