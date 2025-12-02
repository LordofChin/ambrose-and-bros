package CreatureZoo;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;


public class AUI extends JFrame{
    private static final String creatureDir = "Creatures/";
        private static ArrayList<File> creatureFiles;
        private static ArrayList<Creature> creatures;
        private static JList<String> jCreatures;


    private static AUI frame;
        private final static int WIDTH = 400, HEIGHT = 300;
        private static Rasterizer<Creature> rast = new Rasterizer<>();
        private static Animate<Creature> ani = new Animate<>(rast);

    private static boolean paused;
    private static boolean menuOpen;
        private static JTextField nameField;
        private static JTextField shapeField;
        private static JSlider agilitySlider;
        private static JSlider radiusSlider;
        private static JCheckBox visibilityBox;

     /*
     * ====================================
     * ---------------TO-DO----------------
     * ====================================
     */
    private static Action actionStack;
        private static final Stack<Runnable> undoStack = new Stack<>();
        private static final Stack<Runnable> redoStack = new Stack<>();  

    enum KeyTarget {
        RASTERIZER, LIST
    }
    private static KeyTarget currentKeyTarget = KeyTarget.RASTERIZER;

    public AUI() {
        super("Creature Zoo");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
    }

    public static void main(String[] args) {
        frame = new AUI();

        creatureFiles = readCreatures();
        creatures = loadCreatures(creatureFiles);
        jCreatures = new JList<String>(loadNames(creatures));

        frame.add(jCreatures, BorderLayout.WEST);

        frame.setupKeyBindings();

        rast.setRast(creatures);
        frame.add(rast, BorderLayout.CENTER);
        new Thread(ani).start(); // Start animation in a separate thread
        paused = false;
        menuOpen = false;
        frameListener();
    }
    private static ArrayList<File> readCreatures() {
        ArrayList<File >ret = new ArrayList<File>();
        File dir = new File(creatureDir);
        File files [] = dir.listFiles();

        for (File f : files) {
            ret.add(f);
            //System.out.printf("loaded %s file\n",f.getName());
        }

        return ret;
    }
    private static ArrayList<Creature> loadCreatures(ArrayList<File> fl) {
        ArrayList<Creature> ret = new ArrayList<>();

        for (File f : fl) {
            ret.add(Creature.read(f));
            //System.out.printf("loaded %s creature\n", f.getName());
        }

        return ret;
    }
    private static DefaultListModel<String> loadNames(ArrayList<Creature> cl) {
        DefaultListModel<String> ret = new DefaultListModel<String>();

        for (Creature c : cl) {
            ret.addElement(c.getName());
        }
        return ret;
    }
    
    private static void frameListener() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = frame.getContentPane().getSize();
                rast.setWidth(size.width);
                rast.setHeight(size.height);
                //System.out.printf("Resized to: %d x %d\n", size.width, size.height);
            }
        });
        jCreatures.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                currentKeyTarget = KeyTarget.LIST;
            }
        });
        rast.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                currentKeyTarget = KeyTarget.RASTERIZER;
                rast.requestFocusInWindow();
            }
        });
    }
    private void setupKeyBindings() {
        ActionMap actionMap = frame.getRootPane().getActionMap();

        Map<String, String> b = new HashMap<>();
        b.put("E", "editCreature");
        b.put("A", "addCreature");
        b.put("D","deselectCreature");
        b.put("P", "pauseAnimation");
        b.put("MINUS","zoomOut");
        b.put("EQUALS","zoomIn");
        b.put("RIGHT","right");
        b.put("LEFT","left");
        b.put("UP","up");
        b.put("DOWN","down");
        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());

        for (String s : b.keySet()) {
            frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(s), b.get(s));
            System.out.println("Setting up key binding for " + s);
        }
            frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(undoKeyStroke, "undo");
            frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(redoKeyStroke, "redo");
        
        actionMap.put("editCreature", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {    
                if (jCreatures.getSelectedValue() != null) {
                    System.out.printf("opening %s ...\n",creatures.get(jCreatures.getSelectedIndex()));
                    if(!menuOpen) {
                        menuOpen = true;
                        showAddCreatureDialog(true);
                    }
                } 
                else {
                    System.out.printf("creating new creature\n");
                    if(!menuOpen) {
                        menuOpen = true;
                        showAddCreatureDialog(false);
                    }
                }
            }
        });
        actionMap.put("addCreature", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {    
                if(!menuOpen) {
                    menuOpen = true;
                    showAddCreatureDialog(false);
                }
            }
        });
        actionMap.put("deselectCreature", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {    
                jCreatures.clearSelection();
                currentKeyTarget = KeyTarget.RASTERIZER;
                rast.requestFocusInWindow();
                System.out.println("Deselecting — back in rasterizer mode.");
            }
        });
        actionMap.put("pauseAnimation", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {    
                if (!paused) {
                    ani.setRunning(false);
                    paused = true;
                    System.out.println("paused");
                }
                else {
                    ani.setRunning(true);
                    paused = false;
                    System.out.println("unpaused");
                    new Thread(ani).start(); // Start animation in a separate thread

                }
            }
        });
        actionMap.put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {    
                System.out.println("you undid");
                //undoStack.pop();
            }
        });
        actionMap.put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {    
                System.out.println("you redid");
                //redoStack.pop();
            }
        });
        actionMap.put("zoomIn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {  
                System.out.println("zooming in...");
                rast.incrementZoom();
            }});
        actionMap.put("zoomOut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                System.out.println("zooming out...");
                rast.decrementZoom();;
        }});
        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                if (currentKeyTarget == KeyTarget.RASTERIZER) {
                    rast.incrementFrameX();
                    System.out.println("moving right... ");
                } 
        }});
        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                if (currentKeyTarget == KeyTarget.RASTERIZER) {
                    rast.decrementFrameX();
                    System.out.println("moving left... ");
                } 
        }});
        actionMap.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                if (currentKeyTarget == KeyTarget.RASTERIZER) {
                    rast.decrementFrameY();
                    System.out.println("moving up... ");
                } else {
                    jCreatures.setSelectedIndex(Math.max(0, jCreatures.getSelectedIndex() - 1));
                }
        }});
        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                if (currentKeyTarget == KeyTarget.RASTERIZER) {
                    rast.incrementFrameY();
                    System.out.println("moving down... ");
                } else {
                    jCreatures.setSelectedIndex(Math.max(0, jCreatures.getSelectedIndex() + 1));
                }
        }});
    }

    public void showAddCreatureDialog(boolean edit) {
        Creature selectedCreature = creatures.get(jCreatures.getSelectedIndex());
        nameField = new JTextField(10);
        shapeField = new JTextField(10);
        agilitySlider = new JSlider(0, 100, 50);
        radiusSlider = new JSlider(0, 100, 50);
        //radiusSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        visibilityBox = new JCheckBox("Visible", true);
        if(edit) {
            nameField.setText(selectedCreature.getName());
            shapeField.setText(selectedCreature.getShapeStr());
            agilitySlider.setValue((int)selectedCreature.getAgility());
            radiusSlider.setValue(selectedCreature.getRadius());
            //radiusSpinner.setValue(selectedCreature.getRadius());
            visibilityBox.setSelected(selectedCreature.getVisible());
        }
    
        // Layout
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Shape:"));
        panel.add(shapeField);
        panel.add(new JLabel("Agility:"));
        panel.add(agilitySlider);
        panel.add(new JLabel("Radius:"));
        panel.add(radiusSlider);
        //panel.add(radiusSpinner);
        panel.add(new JLabel("Visible:"));
        panel.add(visibilityBox);
    
        // Show dialog
        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Add New Creature",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
    
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String shape = shapeField.getText();
            double agility = (double) agilitySlider.getValue();
            int radius = (Integer) radiusSlider.getValue();
            boolean visible = visibilityBox.isSelected();
    
            Creature c = new Creature(name, shape, agility, radius, visible);
            c.write();
            creatures.remove(selectedCreature);
            creatures.add(c);
        }
        menuOpen = false;
    }
    
}

