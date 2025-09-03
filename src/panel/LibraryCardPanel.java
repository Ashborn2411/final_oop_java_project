package panel;

import com.toedter.calendar.JDateChooser;
import util.FontUtil;
import util.ImagePanel;
import util.TextFieldUtil;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LibraryCardPanel extends JPanel {
    private JTextField memberIdField, nameField, statusField;
    private JDateChooser issueDateChooser, expiryDateChooser;
    private JButton issueButton;

    public LibraryCardPanel() {
        this.setLayout(new BorderLayout());

        ImagePanel formPanel = new ImagePanel("images/card_background.png", new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        memberIdField = TextFieldUtil.styledField();
        nameField = TextFieldUtil.styledField();
        statusField = TextFieldUtil.styledField();
        issueDateChooser = new JDateChooser();
        expiryDateChooser = new JDateChooser();
        issueDateChooser.setDateFormatString("yyyy-MM-dd");
        expiryDateChooser.setDateFormatString("yyyy-MM-dd");

        addLabeledField(formPanel, "Member ID", memberIdField);
        addLabeledField(formPanel, "Name", nameField);
        addLabeledField(formPanel, "Issue Date", issueDateChooser);
        addLabeledField(formPanel, "Expiry Date", expiryDateChooser);
        addLabeledField(formPanel, "Status", statusField);

        this.add(formPanel, BorderLayout.CENTER);

        issueButton = new JButton("Issue Card");
        issueButton.setFont(FontUtil.COURIER_BOLD_15);
        issueButton.addActionListener(e -> {
            String issueDate = new SimpleDateFormat("yyyy-MM-dd").format(issueDateChooser.getDate());
            String expiryDate = new SimpleDateFormat("yyyy-MM-dd").format(expiryDateChooser.getDate());
            JOptionPane.showMessageDialog(this, "Card Issued to " + nameField.getText() +
                    "\nValid: " + issueDate + " to " + expiryDate);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(issueButton);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addLabeledField(JPanel panel, String labelText, Component field) {
        JLabel label = new JLabel(labelText);
        label.setFont(FontUtil.POPPINS_SEMIBOLD_16);
        label.setForeground(Color.GRAY);
        panel.add(label);
        panel.add(field);
    }
}