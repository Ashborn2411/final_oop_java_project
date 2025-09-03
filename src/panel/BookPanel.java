import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BookPanel extends JPanel {
    private LibraryManager manager;

    public BookPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        // Table to display books
        String[] columns = {"ID", "Title", "Author"};
        Object[][] data = manager.getBooks().stream()
                .map(book -> new Object[]{book.getId(), book.getTitle(), book.getAuthor()})
                .toArray(Object[][]::new);
        JTable table = new JTable(data, columns);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form to add books
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField();
        formPanel.add(idField);
        formPanel.add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        JTextField authorField = new JTextField();
        formPanel.add(authorField);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener((ActionEvent e) -> {
            manager.addBook(new Book(idField.getText(), titleField.getText(), authorField.getText()));
            JOptionPane.showMessageDialog(this, "Book added!");
        });

        add(formPanel, BorderLayout.NORTH);
        add(addButton, BorderLayout.SOUTH);
    }
}
