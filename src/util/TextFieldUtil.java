package util;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TextFieldUtil {

    // Default font and colors
    private static final Font DEFAULT_FONT = FontUtil.POPPINS_REGULAR_14;
    private static final Color TEXT_COLOR = new Color(30, 30, 30);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);

    /**
     * Creates a styled JTextField with default settings.
     */
    public static JTextField styledField() {
        JTextField field = new JTextField();
        field.setFont(DEFAULT_FONT);
        field.setForeground(TEXT_COLOR);
        field.setBackground(BACKGROUND_COLOR);
        field.setBorder(createRoundedBorder());
        field.setCaretColor(TEXT_COLOR);
        field.setHorizontalAlignment(JTextField.LEFT);
        return field;
    }


    public static JTextField styledField(String placeholder) {
        JTextField field = styledField();
        field.setText(placeholder);
        return field;
    }


    private static Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
    }
}