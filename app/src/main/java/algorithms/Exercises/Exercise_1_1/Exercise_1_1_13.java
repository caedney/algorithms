package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.13
 * 
 * <p>
 * Write a code fragment to print the <i>transposition</i> (rows and columns
 * changed) of a two-dimensional array with 𝑀 rows and 𝑁 columns.
 * </p>
 */
public class Exercise_1_1_13 {
    public static void main() {
        StdOut.println("Exercise 1.1.13");

        int[][] array = { { 1, 2, 3 }, { 4, 5, 6 } };

        int M = array.length; // number of rows
        int N = array[0].length; // number of columns

        StdOut.println("Original array:");
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                StdOut.print("    " + array[i][j] + " ");
            }
            StdOut.println();
        }

        StdOut.println("Transposed array:");

        // Transpose: swap rows and columns
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < M; i++) {
                StdOut.print("    " + array[i][j] + " ");
            }
            StdOut.println();
        }
    }
}
