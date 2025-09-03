import panel.*;
import util.AuthManager;
import util.ImagePathUtil;
import util.Utilstring;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class MainFrame extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AuthManager auth = new AuthManager();
            if (auth.authenticate()) {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Access Denied", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
    }
    public MainFrame() {
        LibraryManager manager = new LibraryManager();
        setTitle(Utilstring.softwaretitle);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ImageIcon appIcon = new ImageIcon(ImagePathUtil.app_icon);
        setIconImage(appIcon.getImage());
        JTabbedPane tabbedPane = new JTabbedPane();
        Supplier<JPanel>[] panelSuppliers = new Supplier[]{
                () -> new BookPanel(manager),
                () -> new MemberPanel(manager),
                () -> new LoanPanel(manager),
                () -> new LibraryCardPanel(manager),
                () -> new PublisherPanel(manager),
                () -> new AuthorPanel(manager)
        };

        for (int i = 0; i < Utilstring.tabNames.length; i++) {
            tabbedPane.addTab(Utilstring.tabNames[i], panelSuppliers[i].get());
        }

        add(tabbedPane, BorderLayout.CENTER);
    }


}

