package image;

import java.io.File;

import javax.swing.JFrame;

public class ImageBasics extends JFrame{
    public static void main(String[] args) {
        ImageBasics hi = new ImageBasics();
        hi.setVisible(true);
        hi.setSize(600, 400);
        hi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        File imageFile = new File("05a73177-e079-48f9-972e-29fd857d7b0f.jpg");


    }
    
}
