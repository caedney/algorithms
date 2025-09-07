package algorithms;

import edu.princeton.cs.algs4.StdOut;

public class Card {
    private final String rank;
    private final String suit;
    private static final String[] RANKS = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
    private static final String[] SUITS = { "♣", "♦", "♥", "♠" };

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public static Card[] standardDeck() {
        Card[] deck = new Card[52];
        int i = 0;

        for (String suit : SUITS) {
            for (String rank : RANKS)
                deck[i++] = new Card(rank, suit);
        }

        return deck;
    }

    @Override
    public String toString() {
        return rank + suit;
    }

    public static void main(String[] args) {
        Queue<Card> deck = new Queue<>();

        for (Card card : Card.standardDeck())
            deck.enqueue(card);

        StdOut.println(deck.toString());
    }
}
