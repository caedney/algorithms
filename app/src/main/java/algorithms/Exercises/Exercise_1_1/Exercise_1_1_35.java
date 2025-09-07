package algorithms.Exercises.Exercise_1_1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
// import java.util.Arrays;
import java.util.Random;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.35
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
 */
public class Exercise_1_1_35 {
    public static double[] calculateDistribution() {
        int SIDES = 6;
        double POSSIBLE_OUTCOMES = SIDES * SIDES;
        double[] dist = new double[2 * SIDES + 1];

        // iterates over all possible pairs (1,1), (1,2), ..., (6,6)
        for (int i = 1; i <= SIDES; i++)
            for (int j = 1; j <= SIDES; j++)
                dist[i + j] += 1.0; // counts how many pairs sum to a particular value

        for (int k = 2; k <= 2 * SIDES; k++)
            dist[k] = round(dist[k] / POSSIBLE_OUTCOMES, 3);

        return dist;
    }

    public static double[] simulateDistribution(int dice, int sides, int trials) {
        int maxSum = dice * sides;
        double[] dist = new double[maxSum + 1];

        Random rand = new Random();

        for (int t = 0; t < trials; t++) {
            int sum = 0;

            for (int d = 0; d < dice; d++)
                sum += rand.nextInt(sides) + 1;

            dist[sum] += 1.0;
        }

        for (int i = dice; i <= maxSum; i++)
            dist[i] = round(dist[i] / trials, 3);

        return dist;
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    public static void main(String[] args) {
        double[] exact = calculateDistribution();
        StdOut.println("exact " + Arrays.toString(exact));

        double[] empirical = simulateDistribution(2, 6, 1000000);
        StdOut.println("empirical " + Arrays.toString(empirical));
    }
}
