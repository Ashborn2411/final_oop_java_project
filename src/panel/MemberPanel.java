package panel;

import DataClass.Member;
import util.FontUtil;
import util.ImagePathUtil;
import util.ImagePanel;
import util.TextFieldUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class MemberPanel extends JPanel {
    private LibraryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, emailField, addressField, phoneField, cardNumberField, searchField, imagePathField;
    private JButton chooseImageButton;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private String selectedImagePath = "";

    public MemberPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());
        initFormPanel();
        initTable();
        initBottomPanel();
    }

    private void initFormPanel() {
        JPanel formPanel = new ImagePanel(ImagePathUtil.bookbackgorund, new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameField = TextFieldUtil.styledField();
        emailField = TextFieldUtil.styledField();
        addressField = TextFieldUtil.styledField();
        phoneField = TextFieldUtil.styledField();
        cardNumberField = TextFieldUtil.styledField();
        imagePathField = TextFieldUtil.styledField();
        imagePathField.setEditable(false);

        chooseImageButton = new JButton("Choose Image");
        chooseImageButton.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        chooseImageButton.setBackground(new Color(60, 120, 180));
        chooseImageButton.setForeground(Color.WHITE);
        chooseImageButton.setFocusPainted(false);
        chooseImageButton.addActionListener(this::chooseImage);

        addLabeledField("Name", nameField, formPanel);
        addLabeledField("Email", emailField, formPanel);
        addLabeledField("Address", addressField, formPanel);
        addLabeledField("Phone", phoneField, formPanel);
        addLabeledField("Card Number", cardNumberField, formPanel);
        addLabeledField("Image Path", imagePathField, formPanel);
        formPanel.add(new JLabel("")); // Empty label for alignment
        formPanel.add(chooseImageButton);

        add(formPanel, BorderLayout.NORTH);
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
        String[] columns = {"Image", "Member ID", "Name", "Email", "Address", "Phone", "Card Number"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Icon.class : String.class;
            }
        };
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (column == 0 && getValueAt(row, column) != null) {
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
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        // Double-click to select and copy Member ID
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String memberId = (String) table.getValueAt(selectedRow, 1);

                        // Copy Member ID to clipboard
                        StringSelection stringSelection = new StringSelection(memberId);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);

                        // Show confirmation dialog
                        JOptionPane.showMessageDialog(
                                MemberPanel.this,
                                "Member ID '" + memberId + "' copied to clipboard!",
                                "Copied",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        // Populate form fields
                        nameField.setText((String) table.getValueAt(selectedRow, 2));
                        emailField.setText((String) table.getValueAt(selectedRow, 3));
                        addressField.setText((String) table.getValueAt(selectedRow, 4));
                        phoneField.setText((String) table.getValueAt(selectedRow, 5));
                        cardNumberField.setText((String) table.getValueAt(selectedRow, 6));
                    }
                }
            }
        });

        loadMembers();
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

    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(true);
        JButton addButton = createButton("Add Member", this::addMember);
        JButton updateButton = createButton("Update Member", this::updateMember);
        JButton deleteButton = createButton("Remove Member", this::deleteMember);
        JButton refreshButton = createButton("Refresh", this::refreshMembers);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = TextFieldUtil.styledField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setToolTipText("Search by ID, Name, or Email");

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        searchLabel.setForeground(Color.GRAY);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

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

        bottomPanel.add(searchPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addLabeledField(String labelText, JTextField field, JPanel panel) {
        JLabel label = new JLabel(labelText);
        label.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        label.setForeground(Color.GRAY);
        panel.add(label);
        panel.add(field);
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

    private void filterTable() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void loadMembers() {
        tableModel.setRowCount(0);
        List<Member> members = manager.getMembers();
        if (members != null) {
            for (Member member : members) {
                ImageIcon icon = null;
                if (member.getImagePath() != null && !member.getImagePath().isEmpty()) {
                    icon = new ImageIcon(member.getImagePath());
                }
                tableModel.addRow(new Object[]{
                        icon,
                        member.getMemberId(),
                        member.getName(),
                        member.getEmail(),
                        member.getAddress(),
                        member.getPhoneNumber(),
                        member.getLocalCardNumber()
                });
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        addressField.setText("");
        phoneField.setText("");
        cardNumberField.setText("");
        imagePathField.setText("");
        selectedImagePath = "";
    }

    private void addMember(ActionEvent e) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();
        String cardNumber = cardNumberField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Email are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Auto-generate a unique Member ID
        String memberId = UUID.randomUUID().toString();

        manager.addMember(new Member(memberId, name, email, address, phone, cardNumber, selectedImagePath));
        JOptionPane.showMessageDialog(this, "Member added successfully!");
        clearFields();
        loadMembers();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void updateMember(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String memberId = (String) table.getValueAt(selectedRow, 1);
        String newName = nameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newAddress = addressField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String newCardNumber = cardNumberField.getText().trim();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Email cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(newEmail)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Member member : manager.getMembers()) {
            if (member.getMemberId().equals(memberId)) {
                member.setName(newName);
                member.setEmail(newEmail);
                member.setAddress(newAddress);
                member.setPhoneNumber(newPhone);
                member.setLocalCardNumber(newCardNumber);
                member.setImagePath(selectedImagePath);
                break;
            }
        }

        JOptionPane.showMessageDialog(this, "Member updated successfully!");
        loadMembers();
    }

    private void deleteMember(ActionEvent e) {
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
            String memberId = (String) table.getValueAt(selectedRow, 1);
            manager.getMembers().removeIf(member -> member.getMemberId().equals(memberId));
            JOptionPane.showMessageDialog(this, "Member deleted successfully!");
            clearFields();
            loadMembers();
        }
    }

    private void refreshMembers(ActionEvent e) {
        loadMembers();
        JOptionPane.showMessageDialog(this, "Data refreshed!");
    }

    private static class RoundedBorder implements Border {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManager manager = new LibraryManager();
            JFrame frame = new JFrame("Library Member Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.add(new MemberPanel(manager));
            frame.setVisible(true);
        });
    }
}
