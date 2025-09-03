package Gui;

import Entity.Book;
import Manager.LibraryManager;
import com.toedter.calendar.JDateChooser;
import util.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookPanel extends JPanel {
    private JTable table;
    private JPanel formPanel;
    private JTextField titleField, authorField, publisherField, isbnField, publicationField,
            numberOfCopiesField, genreField, sectionIdField, searchField, imagePathField;
    private JDateChooser publicationDateChooser;
    private JButton addButton, chooseImageButton;
    private LibraryManager manager;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private String selectedImagePath = "";

    public BookPanel(LibraryManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());
        initFormPanel();
        initTable();
        initBottomPanel();
    }

    private void initFormPanel() {
        formPanel = new ImagePanel("images/books_background.png", new GridLayout(11, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleField = TextFieldUtil.styledField();
        authorField = TextFieldUtil.styledField();
        publisherField = TextFieldUtil.styledField();
        isbnField = TextFieldUtil.styledField();
        publicationField = TextFieldUtil.styledField();
        numberOfCopiesField = TextFieldUtil.styledField();
        genreField = TextFieldUtil.styledField();
        sectionIdField = TextFieldUtil.styledField();
        imagePathField = TextFieldUtil.styledField();
        imagePathField.setEditable(false);

        publicationDateChooser = new JDateChooser();
        publicationDateChooser.setDateFormatString("yyyy-MM-dd");

        chooseImageButton = new JButton("Choose Image");
        chooseImageButton.setFont(FontUtil.POPPINS_REGULAR_14);
        chooseImageButton.setBackground(new Color(60, 120, 180));
        chooseImageButton.setForeground(Color.WHITE);
        chooseImageButton.setFocusPainted(false);
        chooseImageButton.addActionListener(this::chooseImage);

        addLabeledField("Title", titleField);
        addLabeledField("Author ID", authorField);
        addLabeledField("Publisher ID", publisherField);
        addLabeledField("ISBN", isbnField);
        addLabeledField("Publication", publicationField);
        addLabeledField("Publication Date", publicationDateChooser);
        addLabeledField("Genre", genreField);
        addLabeledField("Number of Copies", numberOfCopiesField);
        addLabeledField("Section ID", sectionIdField);
        addLabeledField("Image Path", imagePathField);
        formPanel.add(new JLabel("")); // Empty label for alignment
        formPanel.add(chooseImageButton);

        this.add(formPanel, BorderLayout.NORTH);
    }

    private void chooseImage(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();
            imagePathField.setText(selectedImagePath);
        }
    }

    private void initTable() {
        String[] columns = {"ID", "Title", "Author ID", "Publisher ID", "ISBN", "Publication Date", "Genre", "Copies", "Section ID", "Image"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 9 ? Icon.class : String.class;
            }
        };
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (column == 9 && getValueAt(row, column) != null) {
                    ImageIcon icon = (ImageIcon) getValueAt(row, column);
                    if (icon != null) {
                        Image img = icon.getImage();
                        Image scaledImg = img.getScaledInstance(60, 80, Image.SCALE_SMOOTH);
                        setRowHeight(row, 80);
                        setValueAt(new ImageIcon(scaledImg), row, column);
                    }
                }
                return c;
            }
        };
        styleTable();
        updateTableData();
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
    }

    private void initBottomPanel() {
        addButton = createAddButton("Add Book", "images/plus.png");
        addButton.addActionListener(e -> addBook());

        searchField = createSearchField("Search by Title, Author, ISBN, or Publisher...");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(true);
        bottomPanel.add(addButton);
        bottomPanel.add(searchPanel);

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addLabeledField(String labelText, Component field) {
        JLabel label = new JLabel(labelText);
        label.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        label.setForeground(Color.GRAY);
        formPanel.add(label);
        formPanel.add(field);
    }

    private void styleTable() {
        table.setFont(FontUtil.POPPINS_REGULAR_14);
        table.setGridColor(Color.DARK_GRAY);
        table.setRowHeight(28);
        table.setBorder(BorderFactory.createBevelBorder(2));
        JTableHeader header = table.getTableHeader();
        header.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        header.setForeground(new Color(40, 40, 40));
        header.setBackground(new Color(220, 220, 220));
        header.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private JButton createAddButton(String text, String iconPath) {
        ImageIcon rawIcon = new ImageIcon(iconPath);
        Image scaledImg = rawIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton button = new JButton(text, new ImageIcon(scaledImg));
        button.setFont(FontUtil.COURIER_BOLD_15);
        button.setPreferredSize(new Dimension(140, 40));
        button.setBackground(new Color(60, 120, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(15));
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        return button;
    }

    private JTextField createSearchField(String tooltip) {
        JTextField field = TextFieldUtil.styledField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setToolTipText(tooltip);
        return field;
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String authorId = authorField.getText().trim();
        String publisherId = publisherField.getText().trim();
        String isbn = isbnField.getText().trim();
        String publication = publicationField.getText().trim();
        String genre = genreField.getText().trim();
        String sectionId = sectionIdField.getText().trim();

        if (title.isEmpty() || authorId.isEmpty() || publisherId.isEmpty() || isbn.isEmpty() || genre.isEmpty() || sectionId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title, Author ID, Publisher ID, ISBN, Genre, and Section ID are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int numberOfCopies = Integer.parseInt(numberOfCopiesField.getText().trim());
            Date selectedDate = publicationDateChooser.getDate();
            String formattedDate = selectedDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(selectedDate) : "";

            boolean authorExists = manager.getAuthors().stream().anyMatch(author -> author.getAuthorId().equals(authorId));
            boolean publisherExists = manager.getPublishers().stream().anyMatch(publisher -> publisher.getPublisherId().equals(publisherId));

            if (!authorExists || !publisherExists) {
                JOptionPane.showMessageDialog(this, "Author ID or Publisher ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.addBook(new Book(
                    title, authorId, publisherId, sectionId, isbn, formattedDate, genre, numberOfCopies, selectedImagePath, ""
            ));

            updateTableData();
            clearFormFields();
            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for 'Number of Copies'.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableData() {
        tableModel.setRowCount(0);
        manager.getBooks().forEach(book -> {
            ImageIcon icon = null;
            if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
                icon = new ImageIcon(book.getImagePath());
            }
            tableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthorId(),
                    book.getPublisherId(),
                    book.getIsbn(),
                    book.getPublicationDate(),
                    book.getGenre(),
                    book.getNumberOfCopies(),
                    book.getSectionId(),
                    icon
            });
        });
    }

    private void clearFormFields() {
        titleField.setText("");
        authorField.setText("");
        publisherField.setText("");
        isbnField.setText("");
        publicationField.setText("");
        numberOfCopiesField.setText("");
        genreField.setText("");
        sectionIdField.setText("");
        imagePathField.setText("");
        selectedImagePath = "";
        publicationDateChooser.setDate(null);
    }

    private void filterTable() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManager manager = new LibraryManager();
            JFrame frame = new JFrame("Library Book Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.add(new BookPanel(manager));
            frame.setVisible(true);
        });
    }
}
