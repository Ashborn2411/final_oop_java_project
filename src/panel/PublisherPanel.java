package panel;

import com.toedter.calendar.JDateChooser;
import util.FontUtil;
import util.ImagePanel;
import util.TextFieldUtil;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class PublisherPanel extends JPanel {
    private JTextField nameField, addressField, contactField;
    private JDateChooser registrationDateChooser;
    private JButton registerButton;

    public PublisherPanel() {
        this.setLayout(new BorderLayout());

        ImagePanel formPanel = new ImagePanel("images/publisher_background.png", new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameField = TextFieldUtil.styledField();
        addressField = TextFieldUtil.styledField();
        contactField = TextFieldUtil.styledField();
        registrationDateChooser = new JDateChooser();
        registrationDateChooser.setDateFormatString("yyyy-MM-dd");

        addLabeledField(formPanel, "Publisher Name", nameField);
        addLabeledField(formPanel, "Address", addressField);
        addLabeledField(formPanel, "Contact", contactField);
        addLabeledField(formPanel, "Registration Date", registrationDateChooser);

        this.add(formPanel, BorderLayout.CENTER);

        registerButton = new JButton("Register Publisher");
        registerButton.setFont(FontUtil.COURIER_BOLD_15);
        registerButton.addActionListener(e -> {
            String regDate = new SimpleDateFormat("yyyy-MM-dd").format(registrationDateChooser.getDate());
            JOptionPane.showMessageDialog(this, "Publisher " + nameField.getText() + " registered on " + regDate);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(registerButton);
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