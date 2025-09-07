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
    public static void main(String[] args) {
        int[][] array = { { 1, 2, 3 }, { 4, 5, 6 } };
        int rowsCount = array.length;
        int colsCount = array[0].length;

        StdOut.println("Original array:");

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++)
                StdOut.print("  " + array[i][j] + "  ");

            StdOut.println();
        }

        StdOut.println("Transposed array:");

        for (int j = 0; j < colsCount; j++) {
            for (int i = 0; i < rowsCount; i++)
                StdOut.print("  " + array[i][j] + "  ");

            StdOut.println();
        }
    }
}
