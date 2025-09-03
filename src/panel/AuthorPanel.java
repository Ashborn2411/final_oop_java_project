package panel;

import DataClass.Author;
import util.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AuthorPanel extends JPanel {
    private JTable table;
    private JPanel formPanel;
    private JTextField nameField, nationalityField, bioField, searchField, birthdateField;
    private JButton addButton;
    private LibraryManager manager;
    private TableRowSorter<TableModel> rowSorter;
    private DefaultTableModel tableModel;

    public AuthorPanel(LibraryManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());
        initFormPanel();
        initTable();
        initBottomPanel();
    }

    private void initFormPanel() {
        formPanel = new ImagePanel(ImagePathUtil.bookbackgorund, new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameField = TextFieldUtil.styledField();
        nationalityField = TextFieldUtil.styledField();
        birthdateField = TextFieldUtil.styledField();
        bioField = TextFieldUtil.styledField();

        addLabeledField("Author Name", nameField);
        addLabeledField("Nationality", nationalityField);
        addLabeledField("Birth Date (YYYY-MM-DD)", birthdateField);
        addLabeledField("Short Bio", bioField);

        this.add(formPanel, BorderLayout.NORTH);
    }

    private void initTable() {
        String[] columns = {"ID", "Name", "Nationality", "Birth Date", "Bio"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        styleTable();
        updateTableData();

        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if (row >= 0 && col == 0) {
                        table.setRowSelectionInterval(row, row);
                        table.setColumnSelectionInterval(col, col);
                        Object value = table.getValueAt(row, col);
                        StringSelection selection = new StringSelection(value.toString());
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                        JOptionPane.showMessageDialog(table, "Author ID copied to clipboard:\n" + value);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        rowSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(rowSorter);
    }

    private void initBottomPanel() {
        addButton = createAddButton("Add Author", "images/plus.png");
        addButton.addActionListener(e -> addAuthor());

        searchField = createSearchField("Search by Name or Nationality...");
        searchField.getDocument().addDocumentListener(new FilterListener());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        bottomPanel.add(addButton);
        bottomPanel.add(new JLabel("Search:"));
        bottomPanel.add(searchField);
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
        table.setRowHeight(28);
        JTableHeader header = table.getTableHeader();
        header.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        header.setBackground(new Color(220, 220, 220));
        header.setForeground(new Color(40, 40, 40));
    }

    private JButton createAddButton(String text, String iconPath) {
        JButton button = new JButton(text, new ImageIcon(iconPath));
        button.setFont(FontUtil.COURIER_BOLD_15);
        button.setBackground(new Color(60, 120, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(15));
        ImageIcon icon = new ImageIcon(iconPath);
        button.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        return button;
    }

    private JTextField createSearchField(String tooltip) {
        JTextField field = TextFieldUtil.styledField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setToolTipText(tooltip);
        return field;
    }

    private void addAuthor() {
        String name = nameField.getText().trim();
        String nationality = nationalityField.getText().trim();
        String birthDate = birthdateField.getText().trim();
        String bio = bioField.getText().trim();

        if (name.isEmpty() || nationality.isEmpty() || birthDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Nationality, and Birth Date are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        manager.addAuthor(new Author(name, nationality, birthDate, bio));
        updateTableData();
        clearFormFields();
        JOptionPane.showMessageDialog(this, "Author added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTableData() {
        tableModel.setRowCount(0);
        manager.getAuthors().forEach(author -> {
            tableModel.addRow(new Object[]{
                    author.getAuthorId(),
                    author.getName(),
                    author.getNationality(),
                    author.getBirthDate(),
                    author.getBio()
            });
        });
    }

    private void clearFormFields() {
        nameField.setText("");
        nationalityField.setText("");
        birthdateField.setText("");
        bioField.setText("");
    }

    private class FilterListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) { filter(); }
        public void removeUpdate(DocumentEvent e) { filter(); }
        public void changedUpdate(DocumentEvent e) { filter(); }

        private void filter() {
            String text = searchField.getText().trim();
            if (text.isEmpty()) {
                rowSorter.setRowFilter(null);
            } else {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        }
    }


}