package Gui;

import Entity.Loan;
import Manager.LibraryManager;
import util.FontUtil;
import util.ImagePathUtil;
import util.ImagePanel;
import util.TextFieldUtil;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class LoanPanel extends JPanel {
    private LibraryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField bookIdField, memberIdField;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public LoanPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        initFormPanel();
        initTable();
        initButtonPanel();
    }

    private void initFormPanel() {
        JPanel formPanel = new ImagePanel(ImagePathUtil.bookbackgorund, new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addLabeledField("Book ID", bookIdField = TextFieldUtil.styledField(), formPanel);
        addLabeledField("Member ID", memberIdField = TextFieldUtil.styledField(), formPanel);

        add(formPanel, BorderLayout.NORTH);
    }

    private void addLabeledField(String labelText, JTextField field, JPanel panel) {
        JLabel label = new JLabel(labelText);
        label.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        label.setForeground(Color.GRAY);
        panel.add(label);
        panel.add(field);
    }

    private void initTable() {
        String[] columns = {"Loan ID", "Book ID", "Member ID", "Loan Date", "Return Date", "Returned"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 5 ? Boolean.class : String.class;
            }
        };

        table = new JTable(tableModel);
        table.getColumnModel().getColumn(5).setCellEditor(table.getDefaultEditor(Boolean.class));
        table.getColumnModel().getColumn(5).setCellRenderer(table.getDefaultRenderer(Boolean.class));

        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == 5) {
                int row = e.getFirstRow();
                Boolean isSelected = (Boolean) tableModel.getValueAt(row, 5);
                handleReturnedCheckbox(row, isSelected);
            }
        });

        styleTable();
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                bookIdField.setText((String) tableModel.getValueAt(selectedRow, 1));
                memberIdField.setText((String) tableModel.getValueAt(selectedRow, 2));
            }
        });

        loadLoans();
        add(new JScrollPane(table), BorderLayout.CENTER);
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

    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(true);

        JButton addButton = createButton("Add Loan", this::addLoan);
        JButton updateButton = createButton("Update Loan", this::updateLoan);
        JButton deleteButton = createButton("Delete Loan", this::deleteLoan);
        JButton refreshButton = createButton("Refresh", this::refreshLoans);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(FontUtil.COURIER_BOLD_15);
        button.setPreferredSize(new Dimension(140, 40));
        button.setBackground(new Color(60, 120, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(15));
        button.addActionListener(listener);
        return button;
    }

    private void loadLoans() {
        tableModel.setRowCount(0);
        for (Loan loan : manager.getLoans()) {
            String returnDateStr = loan.getReturnDate() != null ?
                    loan.getReturnDate().format(formatter) : "Not returned";
            boolean isReturned = loan.getReturnDate() != null;
            tableModel.addRow(new Object[]{
                    loan.getLoanId(),
                    loan.getBookId(),
                    loan.getMemberId(),
                    loan.getLoanDate().format(formatter),
                    returnDateStr,
                    isReturned
            });
        }
    }

    private void clearFields() {
        bookIdField.setText("");
        memberIdField.setText("");
    }

    private void addLoan(ActionEvent e) {
        String bookId = bookIdField.getText().trim();
        String memberId = memberIdField.getText().trim();

        if (bookId.isEmpty() || memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book ID and Member ID are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isBookAndMemberValid(bookId, memberId)) {
            JOptionPane.showMessageDialog(this, "Book ID or Member ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String loanId = UUID.randomUUID().toString();
        manager.addLoan(new Loan(bookId, memberId));
        JOptionPane.showMessageDialog(this, "Loan added successfully!");
        clearFields();
        loadLoans();
    }

    private void updateLoan(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a loan to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedLoanId = (String) tableModel.getValueAt(selectedRow, 0);
        String newBookId = bookIdField.getText().trim();
        String newMemberId = memberIdField.getText().trim();

        if (newBookId.isEmpty() || newMemberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book ID and Member ID cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isBookAndMemberValid(newBookId, newMemberId)) {
            JOptionPane.showMessageDialog(this, "Book ID or Member ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Loan loan : manager.getLoans()) {
            if (loan.getLoanId().equals(selectedLoanId)) {
                loan.setBookId(newBookId);
                loan.setMemberId(newMemberId);
                break;
            }
        }

        JOptionPane.showMessageDialog(this, "Loan updated successfully!");
        loadLoans();
    }

    private boolean isBookAndMemberValid(String bookId, String memberId) {
        boolean bookExists = manager.getBooks().stream().anyMatch(book -> book.getBookId().equals(bookId));
        boolean memberExists = manager.getMembers().stream().anyMatch(member -> member.getMemberId().equals(memberId));
        return bookExists && memberExists;
    }

    private void deleteLoan(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a loan to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this loan?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String loanId = (String) tableModel.getValueAt(selectedRow, 0);
            manager.getLoans().removeIf(loan -> loan.getLoanId().equals(loanId));
            JOptionPane.showMessageDialog(this, "Loan deleted successfully!");
            clearFields();
            loadLoans();
        }
    }

    private void refreshLoans(ActionEvent e) {
        loadLoans();
        JOptionPane.showMessageDialog(this, "Data refreshed!");
    }

    private void handleReturnedCheckbox(int row, boolean isSelected) {
        String loanId = (String) tableModel.getValueAt(row, 0);
        for (Loan loan : manager.getLoans()) {
            if (loan.getLoanId().equals(loanId)) {
                loan.setReturnDate(isSelected ? LocalDateTime.now() : null);
                break;
            }
        }
        loadLoans();
    }

    private static class RoundedBorder implements Border {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 1, radius + 1, radius + 2, radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
