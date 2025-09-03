import Gui.BookPanel;
import Manager.LibraryManager;

import javax.swing.*;

public class TestBookPanel {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Library Book Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            LibraryManager manager = new LibraryManager(); // make sure this class is implemented
            BookPanel panel = new BookPanel(manager);

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}