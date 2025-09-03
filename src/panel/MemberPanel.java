import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class MemberPanel extends JPanel {
    private LibraryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;

    public MemberPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        // Table setup
        String[] columns = {"ID", "Name", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        table = new JTable(tableModel);
        loadMembers();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form to add/update members
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField();
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Member");
        JButton updateButton = new JButton("Update Member");
        JButton deleteButton = new JButton("Delete Member");
        JButton refreshButton = new JButton("Refresh");

        // Add button logic
        addButton.addActionListener((ActionEvent e) -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if member ID already exists
            boolean idExists = manager.getMembers().stream().anyMatch(member -> member.getId().equals(id));
            if (idExists) {
                JOptionPane.showMessageDialog(this, "Member ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.addMember(new Member(id, name, email));
            JOptionPane.showMessageDialog(this, "Member added successfully!");
            clearFields(idField, nameField, emailField);
            loadMembers();
        });

        // Update button logic
        updateButton.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a member to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String memberId = (String) table.getValueAt(selectedRow, 0);
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Email cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Member member : manager.getMembers()) {
                if (member.getId().equals(memberId)) {
                    member.setName(newName);
                    member.setEmail(newEmail);
                    break;
                }
            }

            FileHandler.saveData(manager.getMembers(), "members.dat");
            JOptionPane.showMessageDialog(this, "Member updated successfully!");
            loadMembers();
        });

        // Delete button logic
        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a member to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this member?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                String memberId = (String) table.getValueAt(selectedRow, 0);
                manager.getMembers().removeIf(member -> member.getId().equals(memberId));
                FileHandler.saveData(manager.getMembers(), "members.dat");
                JOptionPane.showMessageDialog(this, "Member deleted successfully!");
                loadMembers();
            }
        });

        // Refresh button logic
        refreshButton.addActionListener((ActionEvent e) -> {
            loadMembers();
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

    // Load members into the table
    private void loadMembers() {
        tableModel.setRowCount(0); // Clear the table
        for (Member member : manager.getMembers()) {
            tableModel.addRow(new Object[]{member.getId(), member.getName(), member.getEmail()});
        }
    }

    // Clear input fields
    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
}
