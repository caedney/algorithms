package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.14
 * 
 * <p>
 * Write a static method {@code lg()} that takes an int value {@code N} as
 * argument and returns the largest {@code int} not larger than the base-2
 * logarithm of {@code N}. Do <i>not</i> use {@code Math}.
 * </p>
 */
public class Exercise_1_1_14 {
    private static int lg(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("N must be positive");

        int result = 0;
        while (N > 1) {
            N = N / 2;
            result++;
        }

        return result;
    }

    private static int mathLg(int N) {
        return (int) (Math.log(N) / Math.log(2));
    }

    public static void main(String[] args) {
        StdOut.println(lg(20)); // 4
        StdOut.println(mathLg(20)); // 4
    }
}
