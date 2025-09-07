package algorithms.Exercises.Exercise_1_2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import edu.princeton.cs.algs4.Counter;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.9
 * 
 * <p>
 * Instrument {@code BinarySearch} (page 47) to use a {@code Counter} to count
 * the total number of keys examined during all searches and then print the
 * total after all searches are complete. <i>Hint</i>: Create a {@code Counter}
 * in {@code main()} and pass it as an argument to {@code rank()}.
 * </p>
 */
public class Exercise_1_2_9 {
    private static int rank(int key, int[] a, Counter count) {
        int lo = 0;
        int hi = a.length - 1;

        count.increment();

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (key < a[mid])
                hi = mid - 1;
            else if (key > a[mid])
                lo = mid + 1;
            else
                return mid;
        }

        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.setIn(new FileInputStream("src/data/algs4/tinyT.txt"));
        In in = new In("src/data/algs4/tinyW.txt");

        int[] whitelist = in.readAllInts();
        Arrays.sort(whitelist);
        Counter counter = new Counter("Counter");

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();

            if (rank(key, whitelist, counter) < 0)
                StdOut.println(key);
        }

        StdOut.println("Total number of keys examined: " + counter.tally());
    }
}
