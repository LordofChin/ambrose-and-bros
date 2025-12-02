
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class SimpleGame extends Canvas implements Runnable, KeyListener {
    private int ballX = 100, ballY = 100;
    private int ballSpeed = 5;
    private boolean running = false;
    private Thread gameThread;

    private boolean moveUp, moveDown, moveLeft, moveRight;

    private Frame frame; // Reference to Frame

    public SimpleGame() {
        frame = new Frame("Heavyweight Java Game");
        frame.setSize(800, 600);
        frame.add(this);
        frame.setVisible(true);
        frame.setResizable(false);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Ensure Canvas is focusable for key input
        addKeyListener(this);
        setFocusable(true);

        // 🚨 Fix: Wait until the frame is visible before creating BufferStrategy
        createBufferStrategy(2);  

        start();
    }

    public synchronized void start() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (running) {
            updateGame();
            render();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        if (moveUp) ballY -= ballSpeed;
        if (moveDown) ballY += ballSpeed;
        if (moveLeft) ballX -= ballSpeed;
        if (moveRight) ballX += ballSpeed;

        ballX = Math.max(0, Math.min(getWidth() - 50, ballX));
        ballY = Math.max(0, Math.min(getHeight() - 50, ballY));
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2); // Ensure double buffering
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, 50, 50);

        g.dispose();
        bs.show();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) moveUp = true;
        if (key == KeyEvent.VK_DOWN) moveDown = true;
        if (key == KeyEvent.VK_LEFT) moveLeft = true;
        if (key == KeyEvent.VK_RIGHT) moveRight = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) moveUp = false;
        if (key == KeyEvent.VK_DOWN) moveDown = false;
        if (key == KeyEvent.VK_LEFT) moveLeft = false;
        if (key == KeyEvent.VK_RIGHT) moveRight = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new SimpleGame();
    }
}
