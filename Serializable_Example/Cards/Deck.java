package Cards;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends Pile {

    public Deck() {
        super(); 
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank, 100)); 
            }
        }
        shuffle();
    }

    Deck(Pile pile) {
        super(pile.getCards());
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        return cards.isEmpty() ? null : cards.remove(0);
    }

    public ArrayList<Card> dealCards(int numberOfCards) {
        ArrayList<Card> dealtCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            Card card = dealCard();
            if (card != null) {
                dealtCards.add(card);
            }
        }
        return dealtCards;
    }

    // Get the number of remaining cards in the deck
    public int remainingCards() {
        return cards.size();
    }
}
