package Cards;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CardRenderer extends JPanel {
    private static final int WIDTH = 1200, HEIGHT = 700;
    private ArrayList<Tableau> tableaus = new ArrayList<>();
    private ArrayList<Foundation> foundations = new ArrayList<>();
    private ArrayList<Card> stock = new ArrayList<>();

    private Card draggedCard = null;
    private List<Card> draggedCards = null;
    private Point mouseOffset = new Point(0, 0);
    private Rectangle tableauBounds = new Rectangle(100, 50, 700, 600);
    private Rectangle stockBounds = new Rectangle(0, 50, 100, 140);
    private Rectangle foundationBounds = new Rectangle(800, 50, 100, 560);

    private static Deck deck;
    private static int runningIndex;
    private static int runningZ;

    private static double prevX;
    private static double prevY;

    public CardRenderer() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        for (int i = 0; i < 7; i++) {
            tableaus.add(new Tableau(100, 100 * (i+ 1)));  
            for (int j = 0; j < i + 1; ++j) {
                tableaus.get(i).addCard(deck.getCards().get(runningIndex));
                deck.getCards().get(runningIndex).setLocation(i);
                runningIndex++;
            }
            tableaus.get(i).reveal();
        }

        for (int i = runningIndex; i < deck.getCards().size(); ++i) {
            stock.add(deck.getCards().get(runningIndex));
            runningIndex++;
        }

        int i = 0;
        for (Suit s : Suit.values()) {
            foundations.add(new Foundation(140, 50 + (i * 140), s)); 
            System.out.println(foundations.get(i).getY());
            i++;
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
    }

    private void handleMousePressed(MouseEvent e) {
            for (int i = deck.getCards().size(); i > 0; --i) {
                Rectangle cardBounds = getCardBounds(deck.getCards().get(i-1));
                if (cardBounds.contains(e.getPoint()) && deck.getCards().get(i-1).getVisible()) {
                    draggedCard = deck.getCards().get(i-1);
                    prevX = draggedCard.getX();
                    prevY = draggedCard.getY();
                    if (tableauBounds.contains(e.getPoint()) && !draggedCard.toString().equals(tableaus.get(draggedCard.getLocation()).getCards().getLast().toString())) {
                        draggedCards = tableaus.get(draggedCard.getLocation()).getCards().subList(
                            tableaus.get(draggedCard.getLocation()).getCards().indexOf(draggedCard), 
                            tableaus.get(draggedCard.getLocation()).getCards().indexOf(tableaus.get(draggedCard.getLocation()).getCards().getLast()) + 1);
                        draggedCard = null;
                    }
                    mouseOffset = new Point(e.getX() - cardBounds.x, e.getY() - cardBounds.y); 
                    break;
                }
            }

        if (stockBounds.contains(e.getPoint())) {
            int numFlipped = 0;
                for (int i = deck.getCards().size(); i > 0; --i) {
                    Rectangle cardBounds = getCardBounds(deck.getCards().get(i-1));
                        if (cardBounds.contains(e.getPoint())) {
                            numFlipped++;

                            if (numFlipped > 3) {
                                break;
                            }

                            stock.get(stock.indexOf(deck.getCards().get(i-1))).setVisible(true);
                            stock.get(stock.indexOf(deck.getCards().get(i-1))).setY(200 + (numFlipped * 20));
                            stock.get(stock.indexOf(deck.getCards().get(i-1))).setZ(runningZ);
                            runningZ++;

                            mouseOffset = new Point(e.getX() - cardBounds.x, e.getY() - cardBounds.y); 
                        }
                    }
                if (numFlipped < 1) {
                    for (Card c : stock) {
                        c.setZ(runningZ);
                        runningZ++;

                        c.setVisible(false);
                        c.setY(50);
                    }
                }
        }

        else if (foundationBounds.contains(e.getPoint())) {
            System.out.println("pressedFoundation");

            
        }
    }

    private void handleMouseDragged(MouseEvent e) {
        if (draggedCard != null) {
            int newX = e.getX() - mouseOffset.x;
            int newY = e.getY() - mouseOffset.y;
            draggedCard.setX(newX);
            draggedCard.setY(newY);
            repaint();
        }
        else if (draggedCards != null) {
            for (Card c : draggedCards) {
                int newX = e.getX() - mouseOffset.x;
                int newY = e.getY() - mouseOffset.y;
                c.setX(newX);
                c.setY(newY);
                repaint();
            }
        }
    }

    private void handleMouseReleased(MouseEvent e) {
        Point pointCapture = e.getPoint();
        System.out.println(pointCapture);

        int nearestX;
        Tableau currTab;
        List<Card> cardsToRemove;

        if (draggedCard != null) {
            if (isValidDrop(pointCapture)) {

                if (tableauBounds.contains(pointCapture)) {          
                    nearestX = ((int) e.getPoint().getX()) / 100;

                    tableaus.get(nearestX - 1).addCard(deck.getCards().get(deck.getCards().indexOf(draggedCard)));
                    tableaus.get(draggedCard.getLocation()).getCards().remove(draggedCard);
                    tableaus.get(draggedCard.getLocation()).reveal();
                    draggedCard.setLocation(nearestX - 1);
                    draggedCard.setZ(runningZ);
                    runningZ++;

                    if(stock.contains(draggedCard)) {
                        stock.remove(draggedCard);
                    }
                }

                else if (foundationBounds.contains(pointCapture)) {
                    int nearestY = ((((int) pointCapture.getY()) - 50) / 140) + 1;
                    System.out.printf("Nearest Y hierarchy %d\n", nearestY);

                    if(stock.contains(draggedCard)){// if the card is in a tablau
                        currTab = tableaus.get(draggedCard.getLocation());

                        cardsToRemove = new ArrayList<>();

                        currTab.reveal();
                        foundations.get(nearestY - 1).addCard(draggedCard);
                        //System.out.println(foundations.get(nearestY).getSuit() + " arr " + foundations.get(nearestY - 1) + "\n");
                        //System.out.println(foundations.get(nearestY -1 ).getCards().get(0).getSuit());

                        cardsToRemove.add(draggedCard);
                        draggedCard.setZ(runningZ);
                        runningZ++;
                        stock.remove(draggedCard);


                        for (Card c : cardsToRemove) {
                            currTab.getCards().remove(c);
                        }
                  }
                    else {
                        foundations.get(nearestY - 1).addCard(draggedCard);
                        System.out.println(foundations.get(nearestY -1 ).getSuit() + " righty " + foundations.get(nearestY - 1) + "\n");
                        System.out.println(foundations.get(nearestY -1 ).getCards().get(0).getSuit());

                        draggedCard.setX(800);
                        draggedCard.setZ(runningZ);
                        stock.remove(draggedCard);
                        runningZ++;
                    }


                }
            } else {
                System.out.println("resetting pos");
                draggedCard.setX(prevX);
                draggedCard.setY(prevY);
            
                draggedCard.setZ(runningZ++);
            }
        }

        else if (draggedCards != null) {
            prevX = draggedCards.get(0).getX();
            prevY = draggedCards.get(0).getY();
            if (isValidDrop(pointCapture)) {

                System.out.println("Card dropped at: " + e.getPoint());

                nearestX = ((int) pointCapture.getX()) / 100;
                currTab = tableaus.get(draggedCards.get(1).getLocation());
                cardsToRemove = new ArrayList<>();
                for (Card c : draggedCards) {
                    tableaus.get(nearestX - 1).addCard(c);
                    cardsToRemove.add(c);
                    tableaus.get(c.getLocation()).reveal();
                    c.setLocation(nearestX - 1);
                    c.setZ(runningZ);
                    runningZ++;
                }
                for (Card c : cardsToRemove) {
                    currTab.getCards().remove(c);
                }
                currTab.reveal();
            } else {
                for (Card card : draggedCards) {
                    card.setX(tableaus.get(draggedCards.get(1).getLocation()).getX());
                    card.setY(50 + 
                    tableaus.get(card.getLocation()).getCards().indexOf(card) 
                    * (card.getHeight() / 7)); // Example Y positioning
            
                    // Reset Z index if needed
                    card.setZ(runningZ++);
                }
            }
        }
        
        deck.getCards().sort((card1, card2) -> Integer.compare(card1.getZ(), card2.getZ()));

        draggedCard = null;
        draggedCards = null; 
        repaint();
    }

    private boolean isValidDrop(Point dropPoint) {
        boolean validPlay = false;
        int nearestX = ((int) dropPoint.getX()) / 100;
        int nearestY = (((int) dropPoint.getY()) - 50) / 140;
        System.out.println(nearestY);


        if (tableauBounds.contains(dropPoint)) {
            if (tableaus.get(nearestX - 1).getCards().size() > 0) {
                Card currCard = tableaus.get(nearestX - 1).getCards().getLast();    

                if (draggedCard != null) {
                    if (currCard.getRank().getFoundationValue() - draggedCard.getRank().getFoundationValue() == 1 
                    && !currCard.getSuit().getColor().toString().equals(draggedCard.getSuit().getColor().toString())) {
                        validPlay = true;
                    }
                }
                else if (draggedCards != null) {
                    if (currCard.getRank().getFoundationValue() - draggedCards.get(0).getRank().getFoundationValue() == 1 
                    && !currCard.getSuit().getColor().toString().equals(draggedCards.get(0).getSuit().getColor().toString())) {
                        validPlay = true;
                    }
                }
            } else {
                if (draggedCard != null) {
                    if (draggedCard.getRank().toString().equals("KING")){
                        validPlay = true;
                    }
                }
                else if (draggedCards != null) {
                    if (draggedCards.get(0).getRank().toString().equals("KING")){
                        validPlay = true;
                    }
                }
                }
            }
            else if(foundationBounds.contains(dropPoint)) {
                System.out.println("within foundation bounds");
                Foundation foundation = foundations.get(nearestY);
                if (foundations.get(nearestY).getCards().size() > 0 ) {
                    Card currCard = foundation.getCards().getLast();    
                    if (draggedCard != null) {
                        if (draggedCard.getRank().getFoundationValue() - currCard.getRank().getFoundationValue() == 1 
                            && !currCard.getSuit().toString().equals(draggedCard.getSuit().toString())) {
                            validPlay = true;
                        }
                    }
                }
                else {
                    if (draggedCard != null) {
                        if (draggedCard.getRank().toString().equals("ACE") 
                            && draggedCard.getSuit() == foundation.getSuit()){
                            validPlay = true;
                        }
                    }
                }
            }
        return validPlay; 
    }

    private Tableau getTableauOfCard(Card card) {
        for (Tableau t : tableaus) {
            if (t.getCards().contains(card)) return t;
        }
        return new Tableau();
    }

    private Rectangle getCardBounds(Card card) {
        double x = card.getX();  
        double y = card.getY();
        double cardWidth = card.getWidth();
        double cardHeight = card.getHeight();

        return new Rectangle((int) x, (int) y, (int) cardWidth, (int) cardHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Tableau tableau : tableaus) {
            drawTableau(g2d, tableau);
        }

        for (Foundation f : foundations) {
            drawFoundation(g2d, f);
        }

        for (Card card : deck.getCards()) {
            drawCard(g2d, card);
        }


        if (draggedCard != null) {
            drawCard(g2d, draggedCard);  
        }

        g2d.drawRect((int)foundationBounds.getX(), (int)foundationBounds.getY(), (int)foundationBounds.getWidth(), (int) foundationBounds.getHeight());
    }

    private void drawTableau(Graphics2D g2d, Tableau tableau) {
        int x = tableau.getX();
        int width = tableau.getWidth();

        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, 50, width, 600);  
    }

    private void drawFoundation(Graphics2D g2d, Foundation f) {
        int y = f.getY();
        int height = f.getHeight();

        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(800, y, 100, height);  

        g2d.setColor(f.getSuit().getColor());

        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        String rankDisplay = f.getSuit().toString();
        g2d.drawString(rankDisplay, (int) (800), (int) (y + 70));

    }

    private void drawCard(Graphics2D g2d, Card card) {
        double x = card.getX();
        double y = card.getY();
        double cardWidth = card.getWidth();
        double cardHeight = card.getHeight();
    
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect((int) x, (int) y, (int) cardWidth, (int) cardHeight, 15, 15);  // Rounded corners
    
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect((int) x, (int) y, (int) cardWidth, (int) cardHeight, 15, 15);
    
        if (card.getVisible()) {
            g2d.setColor(card.getSuit().getColor());

            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            String rankDisplay = card.getRank().getDisplay() + card.getSuit();
            g2d.drawString(rankDisplay, (int) (x + 5), (int) (y + 17));
        }
    }

    public void update() {
        repaint(); 
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Card Renderer");

        deck = new Deck();
        runningIndex = 0;
        runningZ = 0;

        CardRenderer rend = new CardRenderer();

        for (Card c : deck.getCards()) {
            c.setZ(runningZ);
            runningZ++;
        }

        deck.getCards().sort((card1, card2) -> Integer.compare(card1.getZ(), card2.getZ()));

        frame.add(rend);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        rend.update();
    }
}