package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdDraw;

/**
 * Exercise 1.1.31
 * 
 * <p>
 * <i>Random connections</i>. Write a program that takes as command-line
 * arguments an integer {@code N} and a {@code double} value {@code p} (between
 * 0 and 1), plots {@code N} equally spaced dots of size .05 on the
 * circumference of a circle, and then, with probability {@code p} for each pair
 * of points, draws a gray line connecting them.
 * </p>
 */
public class Exercise_1_1_31 {
    public static void draw(int N, double p) throws IllegalArgumentException {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Out of range");
        }

        StdDraw.setPenRadius(0.005);
        StdDraw.setScale(-1.1, 1.1);

        // Compute positions of N equally spaced points on the circle
        double[] x = new double[N];
        double[] y = new double[N];
        for (int i = 0; i < N; i++) {
            double angle = 2 * Math.PI * i / N;
            x[i] = Math.cos(angle);
            y[i] = Math.sin(angle);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledCircle(x[i], y[i], 0.025); // Dot size 0.05 diameter
        }

        // Draw random connections with probability p
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (Math.random() < p) {
                    StdDraw.line(x[i], y[i], x[j], y[j]);
                }
            }
        }
    }

    public static void main(String[] args) {
        draw(12, 0.5);
    }
}
