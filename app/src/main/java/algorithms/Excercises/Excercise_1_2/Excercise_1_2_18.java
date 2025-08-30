package algorithms.Excercises.Excercise_1_2;

/**
 * Excercise 1.2.18
 * 
 * <p>
 * <i>Variance for accumulator</i>. Validate that the following code, which adds
 * the methods {@code var()} and {@code stddev()} to {@code Accumulator},
 * computes both the mean and variance of the numbers presented as arguments to
 * {@code addDataValue()}:
 * </p>
 * 
 * {@snippet :
 * public class Accumulator {
 *     private double m;
 *     private double s;
 *     private int N;
 * 
 *     public void addDataValue(double x) {
 *         N++;
 *         s = s + 1.0 * (N - 1) / N * (x - m) * (x - m);
 *         m = m + (x - m) / N;
 *     }
 * 
 *     public double mean() {
 *         return m;
 *     }
 * 
 *     public double var() {
 *         return s / (N - 1);
 *     }
 * 
 *     public double stddev() {
 *         return Math.sqrt(this.var());
 *     }
 * }
 * }
 * 
 * <p>
 * This implementation is less susceptible to roundoff error than the
 * straightforward implementation based on saving the sum of the squares of the
 * numbers.
 * </p>
 */
public class Excercise_1_2_18 {
    public static void main(String[] args) {
    }
}
