package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.3
 * 
 * <p>
 * Write a program that takes three integer command-line arguments and prints
 * equal if all three are equal, and not equal otherwise.
 * </p>
 */
public class Excercise_1_1_3 {
    private static String checkEquality(int x, int y, int z) {
        if (x == y && y == z) {
            return "equal";
        }

        return "not equal";
    }

    public static void main() {
        StdOut.println("Excercise 1.1.3");
        String result = checkEquality(10, 10, 11);
        StdOut.println("arguments are " + result);
    }
}
