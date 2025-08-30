package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.14
 * 
 * <p>
 * Write a static method {@code lg()} that takes an int value {@code N} as
 * argument and returns the largest {@code int} not larger than the base-2
 * logarithm of {@code N}. Do <i>not</i> use {@code Math}.
 * </p>
 */
public class Excercise_1_1_14 {
    private static int lg(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be positive");
        }

        int result = 0;
        while (N > 1) {
            N = N / 2;
            result++;
        }

        return result;
    }

    public static void main() {
        StdOut.println("Excercise 1.1.14");
        int N = 20;
        StdOut.println("lg(" + N + ") = " + lg(N));
    }
}
