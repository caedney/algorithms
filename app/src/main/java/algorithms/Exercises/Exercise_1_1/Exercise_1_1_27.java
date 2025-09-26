package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.27
 * 
 * <p>
 * <i>Binomial distribution</i>. Estimate the number of recursive calls that
 * would be used by the code
 * </p>
 * 
 * {@snippet :
 * public static double binomial(int N, int k, double p) {
 *     if ((N == 0) || (k < 0))
 *         return 1.0;
 *
 *     return (1.0 - p) * binomial(N - 1, k) + p * binomial(N - 1, k - 1);
 * }
 * }
 * 
 * <p>
 * to compute {@code binomial(100, 50)}. Develop a better implementation that is
 * based on saving computed values in an array.
 * </p>
 */
public class Exercise_1_1_27 {
    public static double binomial(int N, int k, double p) {
        if (p < 0 || p > 1)
            throw new IllegalArgumentException("Out of range");

        if (N == 0 || k < 0)
            return 1.0;

        return (1.0 - p) * binomial(N - 1, k, p) + p * binomial(N - 1, k - 1, p);
    }

    public static void main(String[] args) {
        double probability = binomial(10, 5, 0.5);
        StdOut.println(probability); // 1.0
    }
}
