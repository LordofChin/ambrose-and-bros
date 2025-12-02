import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RotatingCameraCube extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 600, HEIGHT = 600;
    private static final double[][] CUBE_VERTICES = {
        {-1, -1, -1}, {1, -1, -1}, {1, 1, -1}, {-1, 1, -1},
        {-1, -1,  1}, {1, -1,  1}, {1, 1,  1}, {-1, 1,  1}
    };

    private static final int[][] EDGES = {
        {0, 1}, {1, 2}, {2, 3}, {3, 0}, // Back face
        {4, 5}, {5, 6}, {6, 7}, {7, 4}, // Front face
        {0, 4}, {1, 5}, {2, 6}, {3, 7}  // Connecting edges
    };

    private double cameraZ = 4;  // 🏆 Move camera forward/backward
    private double cameraX = 0;  // 🏆 Move camera left/right
    private double yaw = 0;      // 🏆 Rotate left/right (Y-axis rotation)
    private double pitch = 0;    // 🏆 Rotate up/down (X-axis rotation)
    
    private Timer timer;

    public RotatingCameraCube() {
        timer = new Timer(16, this); // 60 FPS update
        timer.start();
        setFocusable(true);
        addKeyListener(this); // Listen for keyboard input
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        g2d.setColor(Color.BLACK);
        double[][] transformedVertices = new double[8][2];

        for (int i = 0; i < CUBE_VERTICES.length; i++) {
            double[] v = CUBE_VERTICES[i];
            double[] rotated = rotateCamera(v, yaw, pitch);
            transformedVertices[i] = project(rotated);
        }

        for (int[] edge : EDGES) {
            int x1 = (int) transformedVertices[edge[0]][0];
            int y1 = (int) transformedVertices[edge[0]][1];
            int x2 = (int) transformedVertices[edge[1]][0];
            int y2 = (int) transformedVertices[edge[1]][1];
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    private double[] rotateCamera(double[] v, double yaw, double pitch) {
        // Apply Yaw (Left/Right rotation around Y-axis)
        double cosYaw = Math.cos(yaw);
        double sinYaw = Math.sin(yaw);
        double x = v[0] * cosYaw - v[2] * sinYaw;
        double z = v[2] * cosYaw + v[0] * sinYaw;

        // Apply Pitch (Up/Down rotation around X-axis)
        double cosPitch = Math.cos(pitch);
        double sinPitch = Math.sin(pitch);
        double y = v[1] * cosPitch - z * sinPitch;
        z = z * cosPitch + v[1] * sinPitch;

        return new double[]{x - cameraX, y, z + cameraZ}; // Apply camera movement
    }

    private double[] project(double[] v) {
        double fov = 400; // Field of View scaling
        return new double[]{
            (WIDTH / 2) + (v[0] * fov / v[2]),
            (HEIGHT / 2) - (v[1] * fov / v[2])
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Move Forward/Backward
        if (e.getKeyCode() == KeyEvent.VK_W) cameraZ -= 0.5;
        if (e.getKeyCode() == KeyEvent.VK_S) cameraZ += 0.5;

        // Move Left/Right
        if (e.getKeyCode() == KeyEvent.VK_A) cameraX -= 0.5;
        if (e.getKeyCode() == KeyEvent.VK_D) cameraX += 0.5;

        // Rotate Left/Right (Yaw)
        if (e.getKeyCode() == KeyEvent.VK_LEFT) yaw -= 0.1;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) yaw += 0.1;

        // Rotate Up/Down (Pitch)
        if (e.getKeyCode() == KeyEvent.VK_UP) pitch += 0.1;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) pitch -= 0.1;
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Wireframe Cube - Rotating Camera");
        RotatingCameraCube panel = new RotatingCameraCube();

        frame.add(panel);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
