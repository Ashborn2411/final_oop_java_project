package util;

import javax.swing.*;

public class AuthManager {
    private static final String CORRECT_PASSWORD = Utilstring.password; // Replace with secure hash in production

    public boolean authenticate() {
        String promptMessage = Utilstring.enterPassword;
        String dialogTitle = Utilstring.loginTitle; // Add this to Utilstring if not already present

        JPasswordField passwordField = new JPasswordField();
        Object[] message = { promptMessage, passwordField };

        int option = JOptionPane.showConfirmDialog(
                null, message, dialogTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            String enteredPassword = new String(passwordField.getPassword());
            return CORRECT_PASSWORD.equals(enteredPassword);
        }
        return false;
    }
}