package algorithms.Exercises.Exercise_1_3;

import algorithms.Bag;
import algorithms.RandomBag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.34
 * 
 * <p>
 * <i>Random bag</i>. A <i>random bag</i> stores a collection of items and
 * supports the following API:
 * </p>
 * 
 * {@code public class RandomBag<Item> implements Iterable<Item>}
 * <table>
 * <tbody>
 * </tr>
 * <tr>
 * <td></td>
 * <td>{@code RandomBag()}</td>
 * <td><i>create an empty random bag</i></td>
 * </tr>
 * <tr>
 * <td>{@code boolean}</td>
 * <td>{@code isEmpty()}</td>
 * <td><i>is the bag empty?</i></td>
 * </tr>
 * <tr>
 * <td>{@code int}</td>
 * <td>{@code size()}</td>
 * <td><i>number of items in the bag</i></td>
 * </tr>
 * <tr>
 * <td>{@code void}</td>
 * <td>{@code add(Item item)}</td>
 * <td><i>add an item</i></td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * <p>
 * Write a class {@code RandomBag} that implements this API. Note that this API
 * is the same as for {@code Bag}, except for the adjective <i>random</i>, which
 * indicates that the iteration should provide the items in <i>random</i> order
 * (all 𝑁! permutations equally likely, for each iterator). <i>Hint</i>: Put
 * the items in an array and randomize their order in the iterator's
 * constructor.
 * </p>
 */
public class Exercise_1_3_34 {
    public static void main(String[] args) {
        StdOut.println("Bag");
        In in = new In("src/main/resources/data/keys.txt");
        Bag<Integer> bag = new Bag<Integer>();

        while (!in.isEmpty())
            bag.add(in.readInt());

        for (int value : bag)
            StdOut.print(value + " ");
        StdOut.println("\n");

        StdOut.println("RandomBag");
        in = new In("src/main/resources/data/keys.txt");
        RandomBag<Integer> randomBag = new RandomBag<Integer>();

        while (!in.isEmpty())
            randomBag.add(in.readInt());

        for (int value : randomBag)
            StdOut.print(value + " ");
        StdOut.println();
    }
}
