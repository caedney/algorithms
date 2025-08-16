package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.20
 * 
 * <p>
 * Write a recursive static method that computes the value of ln(N!)
 * </p>
 * 
 */
public class Excercise_1_1_20 {
    /**
     * Computes ln(N!) recursively.
     * 
     * @param n nonnegative integer
     * @return natural log of N! (ln(N!))
     * @throws IllegalArgumentException if n < 0
     */
    private static double logFactorial(int n) {
        if (n < 0)
            throw new IllegalArgumentException("n must be >= 0");
        if (n <= 1)
            return 0.0; // ln(0!) = ln(1!) = 0

        return Math.log(n) + logFactorial(n - 1);
    }

    public static void main() {
        StdOut.println("Excercise 1.1.20");
        StdOut.println(logFactorial(5));
    }
}
