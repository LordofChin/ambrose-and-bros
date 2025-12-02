package Cards;

import java.util.ArrayList;

public class Tableau extends Pile{
    private int width;

    public Tableau() {}

    public Tableau(int width, int x) {
        super(x);
        this.width = width;
    }

    public Tableau(int width, int x, ArrayList<Card> cards) {
        super(cards, x);
        this.width = width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() {
        return this.width;
    }

    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return this.x;
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

        card.setX(this.getX());
        card.setY((this.getCards().size() * (card.getHeight() / 7)) + 50);

        this.cards.add(card);
    }
}
