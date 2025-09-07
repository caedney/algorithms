package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.5
 * 
 * <p>
 * Write a code fragment that prints true if the double variables {@code x} and
 * {@code y} are both strictly between 0 and 1 and {@code false} otherwise.
 * </p>
 */
public class Exercise_1_1_5 {
    private static boolean betweenZeroAndOne(double x, double y) {
        if (x >= 0.0 && y <= 1.0)
            return true;

        return false;
    }

    public static void main(String[] args) {
        boolean result = betweenZeroAndOne(0.1, 0.9);
        StdOut.printf("doubles are between 0 and 1: %b\n", result);
    }
}
