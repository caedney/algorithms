package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdDraw;

/**
 * Exercise 1.1.32
 * 
 * <p>
 * <i>Histogram</i>. Suppose that the standard input stream is a sequence of
 * {@code double} values. Write a program that takes an integer 𝑁 and two
 * {@code double} values 𝑙 and 𝑟 from the command line and uses
 * {@code StdDraw} to plot a histogram of the count of the numbers in the
 * standard input stream that fall in each of the 𝑁 intervals defined by
 * dividing (𝑙, 𝑟) into 𝑁 equal-sized intervals.
 * </p>
 */
public class Exercise_1_1_32 {
    public static void histogram(int N, double l, double r) {
        if (N <= 0 || l >= r) {
            System.out.println("Invalid arguments: N must be > 0 and l < r");
            return;
        }

        int trials = 1000; // how many random numbers to generate
        int[] counts = new int[N];
        double intervalWidth = (r - l) / N;

        // generate random numbers between l and r
        for (int t = 0; t < trials; t++) {
            double num = l + Math.random() * (r - l);
            int index = (int) ((num - l) / intervalWidth);
            if (index == N)
                index = N - 1;
            counts[index]++;
        }

        // find max count
        int maxCount = 0;
        for (int c : counts)
            if (c > maxCount)
                maxCount = c;

        // draw histogram
        StdDraw.setXscale(l, r);
        StdDraw.setYscale(0, maxCount * 1.1);

        for (int i = 0; i < N; i++) {
            double x = l + (i + 0.5) * intervalWidth;
            double y = counts[i] / 2.0;
            double halfWidth = intervalWidth / 2.0;
            double halfHeight = counts[i] / 2.0;
            StdDraw.filledRectangle(x, y, halfWidth, halfHeight);
        }
    }

    public static void main(String[] args) {
        histogram(5, 0.0, 1.0);
    }
}
