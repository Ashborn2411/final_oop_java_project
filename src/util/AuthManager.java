package panel;

import javax.swing.*;

public class AuthManager {
    private static final String CORRECT_PASSWORD = "admin123"; // Replace with secure hash in production

    public boolean authenticate() {
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
                "Enter Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(
                null, message, "Library Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            String enteredPassword = new String(passwordField.getPassword());
            return CORRECT_PASSWORD.equals(enteredPassword);
        }
        return false;
    }
}