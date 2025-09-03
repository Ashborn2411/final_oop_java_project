import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class LoanPanel extends JPanel {
    private LibraryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;

    public LoanPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        // Table setup
        String[] columns = {"Loan ID", "Book ID", "Member ID", "Loan Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        table = new JTable(tableModel);
        loadLoans();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form to add/update loans
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Loan ID:"));
        JTextField loanIdField = new JTextField();
        formPanel.add(loanIdField);
        formPanel.add(new JLabel("Book ID:"));
        JTextField bookIdField = new JTextField();
        formPanel.add(bookIdField);
        formPanel.add(new JLabel("Member ID:"));
        JTextField memberIdField = new JTextField();
        formPanel.add(memberIdField);
        formPanel.add(new JLabel("Loan Date:"));
        JTextField loanDateField = new JTextField();
        formPanel.add(loanDateField);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Loan");
        JButton updateButton = new JButton("Update Loan");
        JButton deleteButton = new JButton("Delete Loan");
        JButton refreshButton = new JButton("Refresh");

        // Add button logic
        addButton.addActionListener((ActionEvent e) -> {
            String loanId = loanIdField.getText().trim();
            String bookId = bookIdField.getText().trim();
            String memberId = memberIdField.getText().trim();
            String loanDate = loanDateField.getText().trim();

            if (loanId.isEmpty() || bookId.isEmpty() || memberId.isEmpty() || loanDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if loan ID already exists
            boolean idExists = manager.getLoans().stream().anyMatch(loan -> loan.getLoanId().equals(loanId));
            if (idExists) {
                JOptionPane.showMessageDialog(this, "Loan ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if book and member exist
            boolean bookExists = manager.getBooks().stream().anyMatch(book -> book.getId().equals(bookId));
            boolean memberExists = manager.getMembers().stream().anyMatch(member -> member.getId().equals(memberId));
            if (!bookExists || !memberExists) {
                JOptionPane.showMessageDialog(this, "Book ID or Member ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.addLoan(new Loan(loanId, bookId, memberId, loanDate));
            JOptionPane.showMessageDialog(this, "Loan added successfully!");
            clearFields(loanIdField, bookIdField, memberIdField, loanDateField);
            loadLoans();
        });

        // Update button logic
        updateButton.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a loan to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedLoanId = (String) table.getValueAt(selectedRow, 0);
            String newBookId = bookIdField.getText().trim();
            String newMemberId = memberIdField.getText().trim();
            String newLoanDate = loanDateField.getText().trim();

            if (newBookId.isEmpty() || newMemberId.isEmpty() || newLoanDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Book ID, Member ID, and Loan Date cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if book and member exist
            boolean bookExists = manager.getBooks().stream().anyMatch(book -> book.getId().equals(newBookId));
            boolean memberExists = manager.getMembers().stream().anyMatch(member -> member.getId().equals(newMemberId));
            if (!bookExists || !memberExists) {
                JOptionPane.showMessageDialog(this, "Book ID or Member ID does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Loan loan : manager.getLoans()) {
                if (loan.getLoanId().equals(selectedLoanId)) {
                    loan.setBookId(newBookId);
                    loan.setMemberId(newMemberId);
                    loan.setLoanDate(newLoanDate);
                    break;
                }
            }

            FileHandler.saveData(manager.getLoans(), "loans.dat");
            JOptionPane.showMessageDialog(this, "Loan updated successfully!");
            loadLoans();
        });

        // Delete button logic
        deleteButton.addActionListener((ActionEvent e) -> {
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
                String loanId = (String) table.getValueAt(selectedRow, 0);
                manager.getLoans().removeIf(loan -> loan.getLoanId().equals(loanId));
                FileHandler.saveData(manager.getLoans(), "loans.dat");
                JOptionPane.showMessageDialog(this, "Loan deleted successfully!");
                loadLoans();
            }
        });

        // Refresh button logic
        refreshButton.addActionListener((ActionEvent e) -> {
            loadLoans();
            JOptionPane.showMessageDialog(this, "Data refreshed!");
        });

        // Add components to button panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Add form and button panels
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Load loans into the table
    private void loadLoans() {
        tableModel.setRowCount(0); // Clear the table
        for (Loan loan : manager.getLoans()) {
            tableModel.addRow(new Object[]{loan.getLoanId(), loan.getBookId(), loan.getMemberId(), loan.getLoanDate()});
        }
    }

    // Clear input fields
    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
}
