package Manager;

import Entity.*;
import FileHandler.FileHandler;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LibraryManager {
    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private ArrayList<Loan> loans;
    private ArrayList<LibraryCard> cards;
    private ArrayList<Publisher> publishers;
    private ArrayList<Author> authors;

    public LibraryManager() {
        this.books = load("books.dat");
        this.members = load("members.dat");
        this.loans = load("loans.dat");
        this.cards = load("cards.dat");
        this.publishers = load("publishers.dat");
        this.authors = load("authors.dat");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            save(books, "books.dat");
            save(members, "members.dat");
            save(loans, "loans.dat");
            save(cards, "cards.dat");
            save(publishers, "publishers.dat");
            save(authors, "authors.dat");
        }));
    }

    @SuppressWarnings("unchecked")
    private <T> ArrayList<T> load(String filename) {
        Object data = FileHandler.loadData(filename);
        return data != null ? (ArrayList<T>) data : new ArrayList<>();
    }

    private <T> void save(ArrayList<T> list, String filename) {
        FileHandler.saveData(list, filename);
    }

    public void addBook(Book book) {
        books.add(book);
        save(books, "books.dat");
    }

    public void removeBook(String bookId) {
        books.removeIf(book -> book.getBookId().equals(bookId));
        save(books, "books.dat");
    }

    public void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId().equals(updatedBook.getBookId())) {
                books.set(i, updatedBook);
                break;
            }
        }
        save(books, "books.dat");
    }

    public ArrayList<Book> searchBooks(String keyword) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        book.getAuthorId().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public void addMember(Member member) {
        members.add(member);
        save(members, "members.dat");
    }


    public void addLoan(Loan loan) {
        loans.add(loan);
        save(loans, "loans.dat");
    }


    public void addCard(LibraryCard card) {
        cards.add(card);
        save(cards, "cards.dat");
    }

    public ArrayList<LibraryCard> getCards() { return cards; }


    public void addPublisher(Publisher publisher) {
        publishers.add(publisher);
        save(publishers, "publishers.dat");
    }

    public ArrayList<Publisher> getPublishers() { return publishers; }

    public void addAuthor(Author author) {
        authors.add(author);
        save(authors, "authors.dat");
    }

    public ArrayList<Author> getAuthors() { return authors; }

    public ArrayList<Book> getBooks() { return books; }
    public ArrayList<Member> getMembers() { return members; }
    public ArrayList<Loan> getLoans() { return loans; }
}