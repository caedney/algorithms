package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.11
 * 
 * <p>
 * Write a code fragment that prints the contents of a two-dimensional boolean
 * array, using * to represent {@code true} and space to represent
 * {@code false}. Include row and column numbers.
 * </p>
 */
public class Exercise_1_1_11 {
    public static void main(String[] args) {
        boolean[][] array = { { true, false, true }, { false, true, false }, { true, true, false } };

        StdOut.print("   ");
        for (int col = 0; col < array[0].length; col++)
            StdOut.print(col + "  ");

        StdOut.println();

        for (int row = 0; row < array.length; row++) {
            StdOut.print(row + "  ");

            for (int col = 0; col < array[row].length; col++)
                StdOut.print(array[row][col] ? "*  " : "   ");

            StdOut.println();
        }
    }
}
