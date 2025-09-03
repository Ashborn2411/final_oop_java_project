import DataClass.Book;
import DataClass.Loan;
import DataClass.Member;

import java.util.ArrayList;

public class LibraryManager {
    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private ArrayList<Loan> loans;

    public LibraryManager() {
        this.books = (ArrayList<Book>) FileHandler.loadData("books.dat");
        this.members = (ArrayList<Member>) FileHandler.loadData("members.dat");
        this.loans = (ArrayList<Loan>) FileHandler.loadData("loans.dat");
    }

    // Add methods for CRUD operations
    public void addBook(Book book) {
        books.add(book);
        FileHandler.saveData(books, "books.dat");
    }

    public void addMember(Member member) {
        members.add(member);
        FileHandler.saveData(members, "members.dat");
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
        FileHandler.saveData(loans, "loans.dat");
    }

    // Getters for GUI
    public ArrayList<Book> getBooks() { return books; }
    public ArrayList<Member> getMembers() { return members; }
    public ArrayList<Loan> getLoans() { return loans; }
}
