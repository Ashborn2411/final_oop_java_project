package panel;

import DataClass.LibraryCard;
import com.toedter.calendar.JDateChooser;
import util.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LibraryCardPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel formPanel;
    private JTextField memberIdField, statusField, searchField;
    private JDateChooser issueDateChooser, expiryDateChooser;
    private JButton addButton;
    private LibraryManager manager;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public LibraryCardPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        initFormPanel();
        initTable();
        initBottomPanel();
    }

    private void initFormPanel() {
        formPanel = new ImagePanel(ImagePathUtil.bookbackgorund, new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        memberIdField = TextFieldUtil.styledField();
        statusField = TextFieldUtil.styledField();
        issueDateChooser = new JDateChooser();
        expiryDateChooser = new JDateChooser();
        issueDateChooser.setDateFormatString("yyyy-MM-dd");
        expiryDateChooser.setDateFormatString("yyyy-MM-dd");

        addLabeledField("Member ID", memberIdField);
        addLabeledField("Issue Date", issueDateChooser);
        addLabeledField("Expiry Date", expiryDateChooser);
        addLabeledField("Status", statusField);

        add(formPanel, BorderLayout.NORTH);
    }

    private void addLabeledField(String labelText, Component field) {
        JLabel label = new JLabel(labelText);
        label.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        label.setForeground(Color.GRAY);
        formPanel.add(label);
        formPanel.add(field);
    }

    private void initTable() {
        String[] columns = {"Card ID", "Member ID", "Issue Date", "Expiry Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        styleTable();
        loadCards();

        // Double-click to select and copy Card ID
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        LibraryCard card = manager.getCards().get(modelRow);

                        // Copy Card ID to clipboard
                        String cardId = card.getCardId();
                        StringSelection stringSelection = new StringSelection(cardId);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);

                        // Show confirmation dialog
                        JOptionPane.showMessageDialog(
                                LibraryCardPanel.this,
                                "Card ID '" + cardId + "' copied to clipboard!",
                                "Copied",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        // Populate form fields
                        memberIdField.setText(card.getMemberId());
                        issueDateChooser.setDate(card.getIssueDate());
                        expiryDateChooser.setDate(card.getExpiryDate());
                        statusField.setText(card.getStatus());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
    }

    private void styleTable() {
        table.setFont(FontUtil.POPPINS_REGULAR_14);
        table.setRowHeight(28);
        table.setGridColor(Color.DARK_GRAY);
        JTableHeader header = table.getTableHeader();
        header.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        header.setBackground(new Color(220, 220, 220));
        header.setForeground(new Color(40, 40, 40));
    }

    private void initBottomPanel() {
        addButton = createAddButton("Add Card", "images/plus.png");
        addButton.addActionListener(e -> addCard());

        searchField = createSearchField("Search by Member ID or Status...");
        searchField.getDocument().addDocumentListener(new FilterListener());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.add(addButton);
        bottomPanel.add(new JLabel("Search:"));
        bottomPanel.add(searchField);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createAddButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(FontUtil.COURIER_BOLD_15);
        button.setBackground(new Color(60, 120, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LibraryCardPanel.RoundedBorder(15));
        button.setBorder(BorderFactory.createCompoundBorder(
                button.getBorder(),
                BorderFactory.createEmptyBorder(8, 16, 8, 16) // Padding
        ));
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

    private void loadCards() {
        tableModel.setRowCount(0);
        manager.getCards().forEach(card -> {
            tableModel.addRow(new Object[]{
                    card.getCardId(),
                    card.getMemberId(),
                    card.getFormattedIssueDate(),
                    card.getFormattedExpiryDate(),
                    card.getStatus()
            });
        });
    }

    private void clearFields() {
        memberIdField.setText("");
        statusField.setText("");
        issueDateChooser.setDate(null);
        expiryDateChooser.setDate(null);
    }

    private void addCard() {
        String memberId = memberIdField.getText().trim();
        Date issueDate = issueDateChooser.getDate();
        Date expiryDate = expiryDateChooser.getDate();
        String status = statusField.getText().trim();

        if (memberId.isEmpty() || issueDate == null || expiryDate == null || status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (issueDate.after(expiryDate)) {
            JOptionPane.showMessageDialog(this, "Issue date must be before expiry date!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean memberExists = manager.getMembers().stream()
                .anyMatch(member -> member.getMemberId().equals(memberId));
        if (!memberExists) {
            JOptionPane.showMessageDialog(this, "Member ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        manager.addCard(new LibraryCard(memberId, issueDate, expiryDate, status));
        JOptionPane.showMessageDialog(this, "Library Card added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearFields();
        loadCards();
    }

    private class FilterListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            filter();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            filter();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            filter();
        }

        private void filter() {
            String text = searchField.getText().trim();
            if (text.isEmpty()) {
                rowSorter.setRowFilter(null);
            } else {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        }
    }
    static class RoundedBorder implements javax.swing.border.Border {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(0, 0, 0, 0);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }


}
