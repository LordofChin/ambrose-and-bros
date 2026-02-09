package Cards;

import java.awt.Color;

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
