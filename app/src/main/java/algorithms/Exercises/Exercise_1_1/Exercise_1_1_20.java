package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.20
 * 
 * <p>
 * Write a recursive static method that computes the value of ln(N!)
 * </p>
 */
public class Exercise_1_1_20 {
    private static double logFactorial(int n) {
        if (n < 0)
            throw new IllegalArgumentException("n must be >= 0");

        if (n <= 1)
            return 0.0; // ln(0!) = ln(1!) = 0

        return Math.log(n) + logFactorial(n - 1);
    }

    public static void main(String[] args) {
        StdOut.println(logFactorial(8)); // 10.60460290274525
        StdOut.println(logFactorial(5)); // 4.787491742782046
    }
}
