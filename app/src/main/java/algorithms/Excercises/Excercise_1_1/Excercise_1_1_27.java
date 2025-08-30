package algorithms.Excercises.Excercise_1_1;

// import java.math.BigInteger;

// import com.google.common.math.BigIntegerMath;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.27
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
public class Excercise_1_1_27 {
    /**
     * Creates a binomial distribution.
     * 
     * @param N Number of trials
     * @param k Number of successes
     * @param p Probability of success on a single trial
     * @throws IllegalArgumentException if {@code p < 0} or {@code p > 1}.
     * @return Probability of k successes in N
     */
    public static double binomial(int N, int k, double p) {
        if (p < 0 || p > 1) {
            throw new IllegalArgumentException("Out of range");
        }

        if (N == 0 || k < 0)
            return 1.0;

        return (1.0 - p) * binomial(N - 1, k, p) + p * binomial(N - 1, k - 1, p);
    }

    public static void main() {
        StdOut.println("Excercise 1.1.27");
        double probability = binomial(10, 5, 0.5);
        StdOut.println("Binomial distribution: " + probability);
    }
}
