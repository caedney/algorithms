package algorithms.Exercises.Exercise_1_4;

import java.util.Arrays;

import algorithms.Stopwatch;

/******************************************************************************
 * Exercise 1.4.16
 *
 * <p>
 * <i>Closest pair (in one dimension)</i>. Write a program that, given an array
 * <code>a[]</code> of 𝑁 <code>double</code> values, finds a <i>closest
 * pair</i>: two values whose difference is no greater than the difference of
 * any other pair (in absolute value). The running time of your program should
 * be linearithmic in the worst case.
 * </p>
 *
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_16
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_16 {
    public record ClosestPair(double x, double y) {
    }

    public static ClosestPair closestPairBruteForce(double[] a) {
        int n = a.length;

        if (n < 2)
            throw new IllegalArgumentException("closest pair needs at least two values");

        double best = Double.POSITIVE_INFINITY;
        double x = 0, y = 0;

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                if (Math.abs(a[i] - a[j]) < best) {
                    best = Math.abs(a[i] - a[j]);
                    x = a[i];
                    y = a[j];
                }

        return new ClosestPair(x, y);
    }

    public static ClosestPair closestPair(double[] a) {
        int n = a.length;

        if (n < 2)
            throw new IllegalArgumentException("closest pair needs at least two values");

        a = a.clone();
        Arrays.sort(a);
        double best = Double.POSITIVE_INFINITY;
        double x = 0, y = 0;

        for (int i = 0; i < n - 1; i++) {
            double diff = a[i + 1] - a[i];
            if (diff < best) {
                best = diff;
                x = a[i];
                y = a[i + 1];
            }
        }

        return new ClosestPair(x, y);
    }

    public static void main(String[] args) {
        double[] a = { 2.7, -1.1, 9.4, 3.0, 6.8 };
        Stopwatch timer;
        ClosestPair result;
        double time;
        timer = new Stopwatch();
        result = closestPairBruteForce(a);
        time = timer.elapsedTime();
        System.out.println(result.x() + " " + result.y() + " in " + time + " seconds");
        timer = new Stopwatch();
        result = closestPair(a);
        time = timer.elapsedTime();
        System.out.println(result.x() + " " + result.y() + " in " + time + " seconds");
    }
}
