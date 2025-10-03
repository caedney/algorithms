package algorithms.Exercises.Exercise_1_2;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.15
 * 
 * <p>
 * <i>File input.</i> Develop a possible implementation of the static
 * {@code readInts()} method from {@code In} (which we use for various test
 * clients, such as binary search on page 47) that is based on the
 * {@code split()} method in {@code String}.
 * </p>
 * 
 * <p>
 * <i>Solution</i>
 * </p>
 * 
 * {@snippet :
 * public static int[] readInts(String name) {
 *     In in = new In(name);
 *     String input = StdIn.readAll();
 *     String[] words = input.split("\\s+");
 *     int[] ints = new int[words.length];
 *     for (int i = 0; i < words.length; i++)
 *         ints[i] = Integer.parseInt(words[i]);
 *     return ints;
 * }
 * }
 * 
 * <p>
 * We will consider a different implementation in SECTION 1.3 (see page 126).
 * </p>
 */
public class Exercise_1_2_15 {
    public static int[] readInts(String name) {
        In in = new In(name);
        String input = in.readAll();
        String[] words = input.split("\\s+");
        int[] ints = new int[words.length];

        for (int i = 0; i < words.length; i++)
            ints[i] = Integer.parseInt(words[i]);

        return ints;
    }

    public static void main(String[] args) {
        StdOut.println(Arrays.toString(readInts("src/data/algs4/tinyW.txt")));
    }
}
