package algorithms.Excercises.Excercise_1_1;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.23
 * 
 * <p>
 * Add to the {@code BinarySearch} test client the ability to respond to a
 * second argument: + to print numbers from standard input that <i>are not</i>
 * in the whitelist, - to print numbers that <i>are</i> in the whitelist.
 * </p>
 */
public class Excercise_1_1_23 {
    public static int rank(int key, int[] a) {
        int lo = 0;
        int hi = a.length - 1;

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

    public static void main(String[] args) {
        StdOut.println("Excercise 1.1.23");

        In in = new In(args[0]);
        String included = args[1];

        int[] whitelist = in.readAllInts();

        Arrays.sort(whitelist);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            int ranked = rank(key, whitelist);

            if (ranked > -1 && included == "+")
                StdOut.println(key);
            else if (ranked < 0 && included == "-")
                StdOut.println(key);
        }
    }
}
