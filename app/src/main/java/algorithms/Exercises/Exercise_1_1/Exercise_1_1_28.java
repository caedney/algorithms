package algorithms.Exercises.Exercise_1_1;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.28
 * 
 * <p>
 * <i>Remove duplicates</i>. Modify the test client in {@code BinarySearch} to
 * remove any duplicate keys in the whitelist after the sort.
 * </p>
 */
public class Exercise_1_1_28 {
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
        StdOut.println("Exercise 1.1.28");

        In in = new In(args[0]);

        int[] whitelist = in.readAllInts();

        Arrays.sort(whitelist);

        Set<Integer> set = new LinkedHashSet<>();
        for (int num : whitelist) {
            set.add(num);
        }

        StdOut.println("Set " + set);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();

            if (rank(key, whitelist) < 0) {
                StdOut.println(key);
            }
        }
    }
}
