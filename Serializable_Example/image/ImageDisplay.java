package image;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageDisplay extends Frame {

    private Image image;

    public ImageDisplay(String imagePath) {
        // Set frame title
        super("AWT Image Display");

        // Load image using java.awt.Image
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
            System.exit(1);
        }

        // Set frame properties
        setSize(500, 500);
        setVisible(true);
        setLocationRelativeTo(null);

        // Add window listener to close window
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }   
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this); // Draw image at (50, 50)
        }
    }



    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ImageDisplay <image_path>");
            System.exit(0);
        }
        new ImageDisplay(args[0]);
    }
}
