package algorithms.Exercises.Exercise_1_3;

import algorithms.Deque;
import algorithms.ResizingArrayDeque;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.33
 * 
 * <p>
 * <i>Deque</i>. A double-ended queue or <i>deque</i> (pronounced "deck") is
 * like a stack or a queue but supports adding and removing items at both ends.
 * A deque stores a collection of items and supports the following API:
 * </p>
 * 
 * {@code public class Deque<Item> implements Iterable<Item>}
 * <table>
 * <tbody>
 * <tr>
 * <td></td>
 * <td>{@code Deque()}</td>
 * <td><i>create an empty deque</i></td>
 * </tr>
 * <tr>
 * <td>{@code boolean}</td>
 * <td>{@code isEmpty()}</td>
 * <td><i>is the deque empty?</i></td>
 * </tr>
 * <tr>
 * <td>{@code int}</td>
 * <td>{@code size()}</td>
 * <td><i>number of items in the deque</i></td>
 * </tr>
 * <tr>
 * <td>{@code void}</td>
 * <td>{@code pushLeft(Item item)}</td>
 * <td><i>add an item to the left end</i></td>
 * </tr>
 * <tr>
 * <td>{@code void}</td>
 * <td>{@code pushRight(Item item)}</td>
 * <td><i>add an item to the right end</i></td>
 * </tr>
 * <tr>
 * <td>{@code Item}</td>
 * <td>{@code popLeft()}</td>
 * <td><i>remove an item from the left end</i></td>
 * </tr>
 * <tr>
 * <td>{@code Item}</td>
 * <td>{@code popRight()}</td>
 * <td><i>remove an item from the right end</i></td>
 * </tr>
 * </tbody>
 * </table>
 * <p>
 * Write a class {@code Deque} that uses a doubly-linked list to implement this
 * API and a class {@code ResizingArrayDeque} that uses a resizing array.
 * </p>
 */
public class Exercise_1_3_33 {
    public static void main(String[] args) {
        StdOut.println("Deque");
        In in = new In("src/main/resources/data/keys.txt");
        Deque<Integer> deque = new Deque<Integer>();

        while (!in.isEmpty()) {
            int item = in.readInt();
            deque.pushRight(item);
        }

        StdOut.println("removed from left:  " + deque.popLeft());
        StdOut.println("removed from right: " + deque.popRight());

        for (int value : deque)
            StdOut.print(value + " ");

        StdOut.println("\n\nResizingArrayDeque");
        ResizingArrayDeque<Integer> arrayDeque = new ResizingArrayDeque<Integer>();

        arrayDeque.pushLeft(4);
        arrayDeque.pushLeft(8);
        arrayDeque.pushLeft(15);
        arrayDeque.pushLeft(16);
        arrayDeque.pushRight(23);
        arrayDeque.pushRight(42);
        arrayDeque.popLeft();
        arrayDeque.popLeft();
        arrayDeque.popLeft();
        arrayDeque.popRight();

        StdOut.println(arrayDeque);
        StdOut.println("capacity: " + arrayDeque.capacity());
    }
}
