package algorithms.Exercises.Exercise_1_3;

import algorithms.Card;
import algorithms.RandomQueue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.36
 * 
 * <p>
 * <i>Random iterator</i>. Write an iterator for {@code RandomQueue<Item>} from
 * the previous exercise that returns the items in random order.
 * </p>
 */
public class Exercise_1_3_36 {
    public static void main(String[] args) {
        RandomQueue<Card> deck = new RandomQueue<>();

        for (Card card : Card.standardDeck())
            deck.enqueue(card);

        for (Card card : deck)
            StdOut.print(card + " ");

        StdOut.println();
    }
}
