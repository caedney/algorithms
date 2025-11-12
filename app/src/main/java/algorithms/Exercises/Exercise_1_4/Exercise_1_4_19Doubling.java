package algorithms.Exercises.Exercise_1_4;

import java.util.Random;

/******************************************************************************
 * Exercise_1_4_19Doubling
 * 
 * <p>
 * Doubling client for the sweep cost of <code>findLocalMin</code>: runs the
 * algorithm on random 𝑁-by-𝑁 grids of distinct values, averages the cells
 * inspected at each size, and prints the ratio between successive sizes.
 * Linear growth shows as a ratio settling near 2; every individual run is
 * bounded by 3𝑁. The time column measures <code>findLocalMin</code> alone,
 * excluding grid generation, and is indicative only -- inspection counts are
 * the machine-independent measurement.
 * </p>
 * 
 * <p>
 * Optional arguments: trials per size (default 20) and the largest 𝑁
 * (default 4000).
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_19Doubling
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_19Doubling --args="100 8000"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_19Doubling {
    public static void main(String[] args) {
        int trials = args.length > 0 ? Integer.parseInt(args[0]) : 20;
        int maxN = args.length > 1 ? Integer.parseInt(args[1]) : 4000;
        Random random = new Random();

        System.out.printf("%8s %16s %10s %8s %12s%n", "N", "avg inspections", "per N", "ratio", "avg time");
        double previous = -1;

        for (int n = 250; n <= maxN; n *= 2) {
            long total = 0;
            long elapsed = 0;

            for (int trial = 0; trial < trials; trial++) {
                int[][] a = shuffledDistinctGrid(n, random);
                Exercise_1_4_19.inspections = 0;

                long start = System.nanoTime();
                int[] p = Exercise_1_4_19.findLocalMin(a);
                elapsed += System.nanoTime() - start;

                if (!Exercise_1_4_19.isLocalMin(a, p[0], p[1]))
                    throw new AssertionError("invalid result at n = " + n);

                total += Exercise_1_4_19.inspections;
            }

            double average = total / (double) trials;
            double microseconds = elapsed / (double) trials / 1_000.0;
            System.out.printf("%8d %16.1f %10.2f %8s %10.1f \u00b5s%n", n, average, average / n,
                    previous < 0 ? "-" : String.format("%.2f", average / previous), microseconds);
            previous = average;
        }
    }

    /** A shuffle of 0..𝑁²-1 laid out as a grid, so every value is distinct. */
    private static int[][] shuffledDistinctGrid(int n, Random random) {
        int[] flat = new int[n * n];

        for (int k = 0; k < flat.length; k++)
            flat[k] = k;

        for (int k = flat.length - 1; k > 0; k--) {
            int s = random.nextInt(k + 1);
            int swap = flat[k];
            flat[k] = flat[s];
            flat[s] = swap;
        }

        int[][] a = new int[n][n];

        for (int k = 0; k < flat.length; k++)
            a[k / n][k % n] = flat[k];

        return a;
    }
}
