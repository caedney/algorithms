package algorithms.Excercises.Excercise_1_1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
// import java.util.Arrays;
import java.util.Random;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.35
 * 
 * <p>
 * <i>Dice simulation</i>. The following code computes the exact probability
 * distribution for the sum of two dice:
 * </p>
 * 
 * {@snippet :
 * int SIDES = 6;
 * double[] dist = new double[2 * SIDES + 1];
 * for (int i = 1; i <= SIDES; i++)
 *     for (int j = 1; j <= SIDES; j++)
 *         dist[i + j] += 1.0;
 * 
 * for (int k = 2; k <= 2 * SIDES; k++)
 *     dist[k] /= 36.0;
 * }
 * 
 * <p>
 * The value {@code dist[i]} is the probability that the dice sum to {@code k}.
 * Run experiments to validate this calculation simulating 𝑁 dice throws,
 * keeping track of the frequencies of occurrence of each value when you compute
 * the sum of two random integers between 1 and 6. How large does 𝑁 have to be
 * before your empirical results match the exact results to three decimal
 * places?
 * </p>
 * 
 * 
 */
public class Excercise_1_1_35 {
    /**
     * <table>
     * <thead>
     * <tr>
     * <th>Sum (k)</th>
     * <th>Count (disk[k])</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <td>2</td>
     * <td>1</td>
     * </tr>
     * <tr>
     * <td>3</td>
     * <td>2</td>
     * </tr>
     * <tr>
     * <td>4</td>
     * <td>3</td>
     * </tr>
     * <tr>
     * <td>5</td>
     * <td>4</td>
     * </tr>
     * <tr>
     * <td>6</td>
     * <td>5</td>
     * </tr>
     * <tr>
     * <td>7</td>
     * <td>6</td>
     * </tr>
     * <tr>
     * <td>7</td>
     * <td>6</td>
     * </tr>
     * <tr>
     * <td>8</td>
     * <td>5</td>
     * </tr>
     * <tr>
     * <td>9</td>
     * <td>4</td>
     * </tr>
     * <tr>
     * <td>9</td>
     * <td>4</td>
     * </tr>
     * <tr>
     * <td>10</td>
     * <td>3</td>
     * </tr>
     * <tr>
     * <td>11</td>
     * <td>2</td>
     * </tr>
     * <tr>
     * <td>12</td>
     * <td>1</td>
     * </tr>
     * </tbody>
     * </table>
     * 
     * <p>
     * Sum all counts: 1 + 2 + 3 + 4 + 5 + 6 + 5 + 4 + 3 + 2 + 1 = 36
     * </p>
     * 
     * @return double[] dist
     */
    public static double[] calculateProbabilityDistribution() {
        int SIDES = 6;
        double POSSIBLE_OUTCOMES = SIDES * SIDES;
        double[] dist = new double[2 * SIDES + 1];

        // iterates over all possible pairs (1,1), (1,2), ..., (6,6)
        for (int i = 1; i <= SIDES; i++)
            for (int j = 1; j <= SIDES; j++)
                dist[i + j] += 1.0; // counts how many pairs sum to a particular value

        StdOut.println("dist " + Arrays.toString(dist));

        for (int k = 2; k <= 2 * SIDES; k++)
            dist[k] = round(dist[k] / POSSIBLE_OUTCOMES, 3);

        StdOut.println("probability " + Arrays.toString(dist));

        return dist;
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    public static void main() {
        StdOut.println("Excercise 1.1.35");

        calculateProbabilityDistribution();

        int SIDES = 6;
        double[] freq = new double[2 * SIDES + 1];

        Random rand = new Random();

        int N = 1000000; // number of simulations
        for (int t = 0; t < N; t++) {
            int die1 = rand.nextInt(SIDES) + 1; // 1..6
            int die2 = rand.nextInt(SIDES) + 1; // 1..6
            freq[die1 + die2] += 1.0;
        }

        double[] empirical = new double[2 * SIDES + 1];

        StdOut.println("freq " + Arrays.toString(freq));

        for (int k = 2; k <= 2 * SIDES; k++) {
            empirical[k] = round(freq[k] / N, 3);

            // StdOut.printf("Sum %d: Empirical = %s, Exact = %s%n", k, empirical[k],
            // dist[k]);
        }

        StdOut.println("empirical " + Arrays.toString(empirical));

    }
}
