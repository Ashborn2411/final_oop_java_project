import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel(String imagePath, LayoutManager layout) {
        super(layout);
        this.backgroundImage = new ImageIcon(imagePath).getImage();
        setOpaque(false); // Let the image show through
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}