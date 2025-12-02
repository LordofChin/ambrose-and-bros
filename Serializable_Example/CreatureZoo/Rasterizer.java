package CreatureZoo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Rasterizer<E extends Creature> extends JPanel {
    private int width, height;
    private ArrayList<E> rast = new ArrayList<>();
    private double zoom;
    private double frameX;
    private double frameY;

    public Rasterizer() {
        width = 300;
        height = 300;
        this.rast = new ArrayList<E>();
        this.zoom = 1;
        this.frameX = 0;
        this.frameY = 0;
    }

    public ArrayList<E> getRast() {
        return this.rast;
    }
    public void setRast(ArrayList<E> rast) {
        this.rast = rast;
    }
    public int getWidth() {
        return this.width;
    }  
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public double getFrameX(){
        return this.frameX;
    }
    public void setFrameX(double frameX){
        this.frameX = frameX;
    }
    public double getFrameY(){
        return this.frameY;
    }
    public void setFrameY(double frameY){
        this.frameY = frameY;
    }

    public void incrementFrameX() {
        this.frameX += 50;
    }
    public void incrementFrameY() {
        this.frameY += 50;
    }
    public void decrementFrameX() {
        this.frameX -= 50;
    }
    public void decrementFrameY() {
        this.frameY -= 50;
    }
    public double getZoom(){
        return this.zoom;
    }
    public void setZoom(double zoom){
        this.zoom = zoom;
    }
    public void incrementZoom() {
        this.zoom *= 1.1;
    }
    public void decrementZoom() {
        this.zoom /=  1.1;
    }

    public void add(E e) {
        this.rast.add(e);
    }

    public int rastSize() {
        return this.getRast().size();
    }

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(Color.BLACK);

    double scale = ((double)this.width / 1000.0) * this.zoom;

    for (E c : rast) {
        ArrayList<int[]> shape = c.getShape();
        int[] coords = c.getCoords();
        if (shape != null && shape.size() > 1 && c.getVisible()) {
            for (int i = 0; i < shape.size(); i++) {
                int[] current = shape.get(i);
                int[] next = shape.get((i + 1) % shape.size());
                int x1 = (int)((current[0] - c.getCenter()[0] + coords[0]) * scale + this.frameX + this.width / 2.0);
                int y1 = (int)((current[1] - c.getCenter()[1] + coords[1]) * scale + this.frameY + this.height / 2.0);
                int x2 = (int)((next[0] - c.getCenter()[0] + coords[0]) * scale + this.frameX + this.width / 2.0);
                int y2 = (int)((next[1] - c.getCenter()[1] + coords[1]) * scale + this.frameY +this.height / 2.0);                
                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }
}

    public void update() {
        repaint(); // Redraw creatures in their updated positions
    }
}
