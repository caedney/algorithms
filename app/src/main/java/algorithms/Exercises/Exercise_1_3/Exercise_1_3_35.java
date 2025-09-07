package algorithms.Exercises.Exercise_1_3;

import algorithms.Card;
import algorithms.RandomQueue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.35
 * 
 * <p>
 * <i>Random queue</i>. A <i>random queue</i> stores a collection of items and
 * supports the following API:
 * </p>
 * 
 * {@code public class RandomQueue<Item>}
 * <table>
 * <tbody>
 * </tr>
 * <tr>
 * <td></td>
 * <td>{@code RandomQueue()}</td>
 * <td><i>create an empty random queue</i></td>
 * </tr>
 * <tr>
 * <td>{@code boolean}</td>
 * <td>{@code isEmpty()}</td>
 * <td><i>is the queue empty?</i></td>
 * </tr>
 * <tr>
 * <td>{@code void}</td>
 * <td>{@code enqueue(Item item}</td>
 * <td><i>add an item</i></td>
 * </tr>
 * <tr>
 * <td>{@code Item}</td>
 * <td>{@code dequeue()}</td>
 * <td><i>remove and return a random item (sample without replacement)</i></td>
 * </tr>
 * <tr>
 * <td>{@code Item}</td>
 * <td>{@code sample()}</td>
 * <td><i>return a random item, but do not remove (sample with
 * replacement)</i></td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * <p>
 * Write a class {@code RandomQueue} that implements this API. <i>Hint</i>: Use
 * an array representation (with resizing). To remove an item, swap one at a
 * random position (indexed {@code 0} through {@code N-1}) with the one at the
 * last position (index {@code N-1}). Then delete and return the last object, as
 * in {@code ResizingArrayStack}. Write a client that deals bridge hands (13
 * cards each) using {@code RandomQueue<Card>}.
 * </p>
 */
public class Exercise_1_3_35 {
    public static void main(String[] args) {
        RandomQueue<Card> deck = new RandomQueue<>();

        // enqueue all 52 cards
        for (Card card : Card.standardDeck())
            deck.enqueue(card);

        // deal 4 hands of 13 cards
        for (int hand = 1; hand <= 4; hand++) {
            StdOut.println("Hand " + hand + ":");

            for (int i = 0; i < 13; i++)
                StdOut.print(deck.dequeue() + " ");

            StdOut.println("\n");
        }
    }
}
