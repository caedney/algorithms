package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.7
 * 
 * <p>
 * Give the value printed by each of the following code fragments:
 * </p>
 * 
 * <ol type="a">
 * <li>
 * {@snippet :
 * double t = 9.0;
 * while (Math.abs(t - 9.0 / t) > .001)
 *     t = (9.0 / t + t) / 2.0;
 * StdOut.printf("%.5f\n", t);
 * }
 * </li>
 * <li>
 * {@snippet :
 * int sum = 0;
 * for (int i = 1; i < 1000; i++)
 *     for (int j = 0; j < i; j++)
 *         sum++;
 * StdOut.printf(sum);
 * }
 * </li>
 * <li>
 * {@snippet :
 * int sum = 0;
 * for (int i = 1; i < 1000; i *= 2)
 *     for (int j = 0; j < 1000; j++)
 *         sum++;
 * StdOut.printf(sum);
 * }
 * </li>
 * </ol>
 */
public class Exercise_1_1_7 {
    private static double a() {
        double t = 9.0;

        while (Math.abs(t - 9.0 / t) > .001)
            t = (9.0 / t + t) / 2.0;

        return t;
    }

    private static int b() {
        int sum = 0;

        for (int i = 1; i < 1000; i++)
            for (int j = 0; j < i; j++)
                sum++;

        return sum;
    }

    private static int c() {
        int sum = 0;

        for (int i = 1; i < 1000; i *= 2)
            for (int j = 0; j < 1000; j++)
                sum++;

        return sum;
    }

    public static void main(String[] args) {
        double result_a = a();
        StdOut.printf("%.5f\n", result_a); // 3.00009

        double result_b = b();
        StdOut.println(result_b); // 499500.0

        double result_c = c();
        StdOut.println(result_c); // 10000.0
    }
}
