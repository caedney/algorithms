package algorithms.Exercises.Exercise_1_1;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.19
 * 
 * <p>
 * What is the largest value of {@code N} for which this program takes less than
 * 1 hour to compute the value of {@code F(N)}? Develop a better implementation
 * of {@code F(N)} that saves computed values in an array.
 * </p>
 */
public class Exercise_1_1_19 {
    public static long fib(int N) {
        if (N == 0)
            return 0;

        if (N == 1)
            return 1;

        return fib(N - 1) + fib(N - 2);
    }

    public static long fib(int N, long[] memo) {
        if (N == 0)
            return 0;

        if (N == 1)
            return 1;

        if (memo[N] != 0)
            return memo[N];

        memo[N] = fib(N - 1, memo) + fib(N - 2, memo);

        return memo[N];
    }

    public static void main(String[] args) {
        int N = 100;
        long[] memo = new long[N + 1];
        memo[0] = 0;
        memo[1] = 1;

        fib(N, memo);
        StdOut.println(Arrays.toString(memo));
    }
}
