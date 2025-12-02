package Cards;

import java.awt.Color;

public class Foundation extends Pile{
    private int height;
    private int y;
    private Suit suit;

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

    public Foundation() {}

    public Foundation(int height, int y) {
        super(800, y);
        this.height = height;
    }

    public Foundation(int height, int y, Suit suit) {
        super(800);
        this.height = height;
        this.suit = suit;
        this.y = y;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return this.height;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return this.y;
    }

    public void setSuit (Suit suit) {
        this.suit = suit;
    }
    public Suit getSuit () {
        return this.suit;
    }

    public void removeCard(Card card) {
        if(this.cards.contains(card)) {
            this.cards.remove(card);
        }
    }

    public void reveal() {
        if (!this.cards.isEmpty()) {
            this.cards.get(this.cards.size() - 1).setVisible(true); // Set last card to visible
        }    
    }

    @Override
    public void addCard(Card card) {

        card.setX(800);
        card.setY(this.getY());

        this.cards.add(card);
    }
}