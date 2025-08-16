package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.15
 * 
 * <p>
 * Write a static method {@code histogram()} that takes an array {@code a[]} of
 * {@code int} values and an integer {@code M} as arguments and returns an array
 * of length {@code M} whose ith entry is the number of times the integer
 * {@code i} appeared in the argument array. If the values in {@code a[]} are
 * all between {@code 0} and {@code M-1}, the sum of the values in the returned
 * array should be equal to {@code a.length}.
 * </p>
 * 
 */
public class Excercise_1_1_15 {
    private static int[] histogram(int[] a, int M) {
        int[] counts = new int[M];

        for (int value : a) {
            if (value >= 0 && value < M) { // ensure the value is in the expected range
                counts[value]++;
            } else {
                throw new IllegalArgumentException("Array contains value out of range 0 to " + (M - 1));
            }
        }

        return counts;
    }

    public static void main() {
        StdOut.println("Excercise 1.1.15");

        int[] a = { 0, 2, 1, 3, 2, 1, 0, 2 };
        int M = 4;

        int[] hist = histogram(a, M);

        for (int i = 0; i < hist.length; i++) {
            StdOut.println(i + ": " + hist[i]);
        }
    }
}
