package CreatureZoo;

import java.io.*;
import java.util.ArrayList;

public class Creature implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<int[]> shape;
    private int[] center;
    private int[] coords;
    private double angle;
    private int radius;
    private double agility;
    private boolean visible;

    public Creature() {}

    public Creature(String name, ArrayList<int[]> shape) {
        this.name = name;
        this.shape = shape;
        this.center = this.calcCenter();
        this.coords = new int [] {0, 0};
        this.agility = 5;
        this.radius = -10;
        this.angle = 0;
        this.visible = true;
    }    
    
    public Creature(String name, String shape, double agility, int radius, boolean visible) {
        this.name = name;
        this.setShape(shape);
        this.center = this.calcCenter();
        this.coords = new int [] {0, 0};
        this.agility = agility;
        this.radius = radius;
        this.angle = 0;
        this.visible = visible;
    }

    public Creature(Creature other) {
        this.name = other.name;
        this.shape = new ArrayList<>(other.shape); 
        this.center = other.center.clone();
        this.coords = other.coords.clone(); 
        this.angle = other.angle;
        this.radius = other.radius;
        this.agility = other.agility;
        this.visible = other.visible;
    }
    

    public String getName() {
        return this.name;
    }
    public ArrayList<int[]> getShape() {
        return this.shape;
    }
    public String getShapeStr() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < this.shape.size(); ++i) {
            if (i + 1 < this.shape.size()) {
                sb.append(String.format("%d,%d ", this.shape.get(i)[0], this.shape.get(i)[1]));
            }
            else {
                sb.append(String.format("%d,%d", this.shape.get(i)[0], this.shape.get(i)[1]));
            }
        }
        return sb.toString();
    }
    public int[] getCenter() {
        return this.center;
    }
    public int[] getCoords() {
        return this.coords;
    }
    public double getAgility() {
        return this.agility;
    }
    public boolean getVisible(){
        return this.visible;
    }
    public double getAngle() {
        return this.angle;
    }
    public int getRadius() {
        return this.radius;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setShape(ArrayList<int[]> shape) {
        this.shape = shape;
    }
    public void setShape(String shapeStr) {
        ArrayList<int[]> shape = new ArrayList<>();
        
        String[] parts = shapeStr.split(" ");
        
        for (String part : parts) {
            String[] coords = part.split(",");
            
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            
            shape.add(new int[] {x, y});
        }
        this.shape = shape;
    }

    public static ArrayList<int []> calcShape(String shapeStr) {
        ArrayList<int[]> shape = new ArrayList<>();
        
        String[] parts = shapeStr.split(" ");
        
        for (String part : parts) {
            String[] coords = part.split(",");
            
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            
            shape.add(new int[] {x, y});
        }
        return shape;
    }
    
    public void setCenter(int x, int y) {
        this.center = new int [] {x, y};
    }
    public void setCoords(int x, int y) {
        this.coords = new int [] {x, y};
    }
    public void setAgility(double agility) {
        this.agility = agility;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public void setVisible() {
        this.visible = true;
    }    
    public void setAngle(double angle) {
        this.angle = angle;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void addAngle(double add) {
        this.angle += add;
    }

    public void move(int [] c) {
        for(int i = 0; i < c.length; ++i) {
            this.coords[i] += c[i];
        }
    }
    
    public void circle() {
        this.addAngle(this.getAgility() / 100.0); 

        this.coords[0] = this.coords[0] + (int) (this.getRadius() * Math.cos(this.getAngle()));
        this.coords[1] = this.coords[1] + (int) (this.getRadius() * Math.sin(this.getAngle()));
    }
    
    protected int [] calcCenter() {
        int xSum = 0;
        int ySum = 0;
        for(int [] s : this.shape) {
            xSum += s[0];
            ySum += s[1];
        }
        this.center = new int [] {xSum / this.shape.size(), ySum / this.shape.size()};
        return new int [] {xSum / this.shape.size(), ySum / this.shape.size()};
    }

    public void write() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(String.format("Creatures/%s.dat", this.name)))) {
            oos.writeObject(this);
            System.out.println(String.format("%s written to file.", this));
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static Creature read(File f) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream((f)))) {
            Creature c = (Creature) ois.readObject();
            //System.out.println(String.format("%s read from file", c));
            return c;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s with %.2f agility, %d radius and %.2f angle", (this.visible) ? "visible" : "invisible", this.name, this.agility, this.radius, this.angle);
    }
}
