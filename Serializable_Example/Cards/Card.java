package Cards;

import java.awt.Color;

public class Card {
    private double width;
    private double height;
    private Suit suit;
    private Rank rank;
    private boolean visible;
    private double x, y;
    private int z;
    private int location;

    public enum Suit {  
        HEARTS(Color.RED), 
        CLUBS(Color.BLACK), 
        DIAMONDS(Color.RED), 
        SPADES(Color.BLACK);

        private Color color;

        Suit(Color color) {
            this.color = color;
        }
        
        public Color getColor() {
            return color;
        }
    }

    // Enum for ranks (2-10, J, Q, K, A)
    public enum Rank {
        ACE(1, 14, "A"),
        TWO(2, "2"),
        THREE(3, "3"),
        FOUR(4, "4"),
        FIVE(5, "5"),
        SIX(6, "6"),
        SEVEN(7, "7"),
        EIGHT(8, "8"),
        NINE(9, "9"),
        TEN(10, "10"),
        JACK(11, "J"),
        QUEEN(12, "Q"),
        KING(13, "K");

        private int foundationValue, tableauValue;
        private String display;


        Rank(int foundationValue, int tableauValue, String display) {
            this.foundationValue = foundationValue;
            this.tableauValue = tableauValue;
            this.display = display;
        }

        Rank(int value, String display) {
            this.foundationValue = value;
            this.tableauValue = value;
            this.display = display;
        }

        public int getFoundationValue() {
            return this.foundationValue;
        }

        public int getTableauValue() {
            return this.tableauValue;
        }

        public String getDisplay() {
            return this.display;
        }
    }

    public Card () {}

    public Card(Suit suit, Rank rank, double width) {
        this.suit = suit;
        this.rank = rank;
        this.width = width;
        this.height = width * 1.4;
        this.visible = false;
        this.x = 0;
        this.y = 50; 
        this.z = 0;
    }

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.height = width * 1.4;
        this.visible = false;
        this.x = 0;
        this.y = 50;  
        this.z = 0;
    }
    // Getters and setters for position
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getLocation() {
        return this.location;
    }
    public void setLocation(int location) {
        this.location = location;
    }

    @Override 
    public String toString() {
        return String.format("%s of %s ", rank.getDisplay(), suit);
    }
}
