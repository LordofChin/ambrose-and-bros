package CreatureZoo;

import java.io.File;

public class Fixer {
    public static void main(String[] args) {
        Creature creature = Creature.read(new File("Creatures/line.dat"));

        if (creature != null) { 
            creature.calcCenter();
            creature.setAgility(8);
            creature.setRadius(5);
            creature.setAngle(10);
            
            System.out.println(creature.getCenter()[0]+ " " + creature.getCenter()[1]);

            creature.write();
            System.out.println("Updated and saved successfully.");
        } else {
            System.out.println("Failed to load creature.");
        }
    }
}
