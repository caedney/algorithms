package algorithms.Exercises.Exercise_1_1;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.22
 * 
 * <p>
 * Write a version of {@code BinarySearch} that uses the recursive
 * {@code rank()} given on page 25 and <i>traces</i> the method calls. Each time
 * the recursive method is called, print the argument values {@code lo} and
 * {@code hi}, indented by the depth of the recursion. <i>Hint</i>: Add an
 * argument to the recursive method that keeps track of the depth.
 * </p>
 */
public class Exercise_1_1_22 {
    private class BinarySearch {
        public static int rank(int key, int[] a, int lo, int hi, int depth) {
            printTrace(lo, hi, depth);

            if (lo > hi)
                return -1; // not found

            int mid = lo + (hi - lo) / 2;

            if (key < a[mid])
                return rank(key, a, lo, mid - 1, depth + 1);
            else if (key > a[mid])
                return rank(key, a, mid + 1, hi, depth + 1);
            else
                return mid; // found
        }

        private static void printTrace(int lo, int hi, int depth) {
            for (int i = 0; i < depth; i++)
                StdOut.print("  ");

            StdOut.println("lo=" + lo + " hi=" + hi);
        }
    }

    public static void main(String[] args) {
        int[] array = { 1, 3, 5, 7, 9, 11, 13, 15, 17, 19 };
        Arrays.sort(array); // Ensure array is sorted (required for binary search)

        int key = 7;
        int result = BinarySearch.rank(key, array, 0, array.length - 1, 0);

        StdOut.println();

        if (result > -1)
            StdOut.println("Found key " + key + " at index " + result);
        else
            StdOut.println("Key " + key + " not found.");
    }
}
