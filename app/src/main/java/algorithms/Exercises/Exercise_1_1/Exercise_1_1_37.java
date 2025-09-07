package algorithms.Exercises.Exercise_1_1;

import algorithms.ShuffleTest;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.37
 * 
 * <p>
 * <i>Bad shuffling</i>. Suppose that you choose a random integer between
 * {@code 0} and {@code N-1} in our shuffling code instead of one between
 * {@code i} and {@code N-1}. Show that the resulting order is not equally
 * likely to be one of the 𝑁! possibilities. Run the test of the previous
 * exercise for this version.
 * </p>
 */
public class Exercise_1_1_37 {
    public static void main(String[] args) {
        int M = 5; // size of array
        int N = 100000; // number of trials
        int[][] counts = new int[M][M]; // counts[i][j] = #times i ends in pos j
        int[] a = new int[M];

        for (int trial = 0; trial < N; trial++) {
            for (int i = 0; i < M; i++)
                a[i] = i; // initialize

            ShuffleTest.badShuffle(a);

            for (int j = 0; j < M; j++)
                counts[a[j]][j]++; // record results
        }

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++)
                StdOut.printf("%7d", counts[i][j]);

            StdOut.println();
        }
    }
}
