package algorithms.Exercises.Exercise_1_3;

import algorithms.GeneralizedQueue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.38
 * 
 * <p>
 * <i>Delete {@code k}th element</i>. Implement a class that supports the
 * following API:
 * </p>
 * 
 * {@code public class GeneralizedQueue<Item>}
 * <table>
 * <tbody>
 * </tr>
 * <tr>
 * <td></td>
 * <td>{@code GeneralizedQueue()}</td>
 * <td><i>create an empty queue</i></td>
 * </tr>
 * <tr>
 * <td>{@code boolean}</td>
 * <td>{@code isEmpty()}</td>
 * <td><i>is the queue empty?</i></td>
 * </tr>
 * <tr>
 * <td>{@code void}</td>
 * <td>{@code insert(Item x)}</td>
 * <td><i>add an item</i></td>
 * </tr>
 * <tr>
 * <td>{@code Item}</td>
 * <td>{@code delete(int k)}</td>
 * <td><i>delete and return the {@code k}th least recently inserted
 * item</i></td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * <p>
 * First, develop an implementation that uses an array implementation, and then
 * develop one that uses a linked-list implementation. <i>Note</i>: the
 * algorithms and data structures that we introduce in CHAPTER 3 make it
 * possible to develop an implementation that can guarantee that both
 * {@code insert()} and {@code delete()} take time proportional to the logarithm
 * of the number of items in the queue—see EXERCISE 3.5.27.
 * </p>
 */
public class Exercise_1_3_38 {
    public static void main(String[] args) {
        In in = new In("src/main/resources/data/keys.txt");
        GeneralizedQueue<Integer> queue = new GeneralizedQueue<>();

        while (!in.isEmpty())
            queue.insert(in.readInt());

        StdOut.println("deleted item: " + queue.delete(12));

        for (int value : queue)
            StdOut.print(value + " ");

        StdOut.println();
    }
}
