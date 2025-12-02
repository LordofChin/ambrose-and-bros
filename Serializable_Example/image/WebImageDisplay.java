package image;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.TransferHandler;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class WebImageDisplay extends Frame {
    private Image originalImage; // Original image
    private Image scaledImage;   // Scaled image

    public WebImageDisplay(String imagePathOrUrl) {
        super("AWT Image Display");

        // Load image from URL or local file
        try {
            if (imagePathOrUrl.startsWith("http://") || imagePathOrUrl.startsWith("https://")) {
                // ✅ Fix for deprecated URL(String) constructor
                URI uri = new URI(imagePathOrUrl);
                URL imageUrl = uri.toURL();
                originalImage = ImageIO.read(imageUrl);
            } else {
                // ✅ Load from local file
                originalImage = ImageIO.read(new File(imagePathOrUrl));
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            System.exit(1);
        }

        // Set frame properties
        setSize(800, 600);  // Default window size
        setVisible(true);
        setLocationRelativeTo(null);

        // Scale the image initially
        updateScaledImage();

        // Add window listener to close window
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        // Add component listener to handle resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                updateScaledImage(); // Rescale the image when the window resizes
                repaint();
            }
        });
    }

    /** ✅ Method to update the scaled image while keeping aspect ratio */
    private void updateScaledImage() {
        if (originalImage == null) return;

        int frameWidth = getWidth();
        int frameHeight = getHeight();

        int imgWidth = originalImage.getWidth(null);
        int imgHeight = originalImage.getHeight(null);

        // Calculate aspect ratio scaling
        double widthRatio = (double) frameWidth / imgWidth;
        double heightRatio = (double) frameHeight / imgHeight;
        double scaleFactor = Math.min(widthRatio, heightRatio); // Maintain aspect ratio

        int newWidth = (int) (imgWidth * scaleFactor);
        int newHeight = (int) (imgHeight * scaleFactor);

        // Scale the image
        scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (scaledImage != null) {
            int x = (getWidth() - scaledImage.getWidth(null)) / 2;
            int y = (getHeight() - scaledImage.getHeight(null)) / 2;
            g.drawImage(scaledImage, x, y, this); // Center the image
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java image.WebImageDisplay <image_path_or_url>");
            System.exit(0);
        }
        
        webImageClip(args[0]);
            }
        
    public static void webImageClip(String imagePathOrUrl) {
        Image origImg;
        try {
            URI uri = new URI(imagePathOrUrl);
            URL imageUrl = uri.toURL();
            origImg = ImageIO.read(imageUrl);
            TransferableImage img = new TransferableImage(origImg);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(img, null);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            System.exit(1);
        }

    }
        public static class TransferableImage implements Transferable{
            private Image image;
            TransferableImage(Image image) {
                this.image = image;
            }
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }
            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return flavor.equals(DataFlavor.imageFlavor);
            }
            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (!isDataFlavorSupported(flavor)) {
                    throw new UnsupportedFlavorException(flavor);
                }
                return image;            }
        }
}
