package Cards;

import java.util.ArrayList;

public class Pile {
    protected ArrayList<Card> cards;  // Make cards protected to allow access in subclasses like Deck
    protected int x;
    protected int y;

    // Default constructor
    public Pile() {
        this.cards = new ArrayList<>();
        this.x = 50;
        this.y = 0;
    }

    public Pile(ArrayList<Card> cards) {
        this.cards = cards;
        this.x = 50;
        this.y = 0;
    }

    public Pile(ArrayList<Card> cards, int x, int y) {
        this.cards = cards;
        this.x = x;
        this.y = y;
    }

    public Pile(ArrayList<Card> cards, int x) {
        this.cards = cards;
        this.x = x;
        this.y = 0;
    }

    public Pile(int x, int y) {
        this.cards = new ArrayList<>();
        this.x = x;
        this.y = y;
    }

    public Pile(int x) {
        this.cards = new ArrayList<>();
        this.x = x;
        this.y = 0;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }
}
