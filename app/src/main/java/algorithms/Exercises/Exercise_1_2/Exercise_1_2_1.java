package algorithms.Exercises.Exercise_1_2;

import java.util.Random;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.1
 * 
 * <p>
 * Write a {@code Point2D} client that takes an integer value 𝑁 from the
 * command line, generates 𝑁 random points in the unit square, and computes the
 * distance separating the <i>closest pair</i> points.
 * </p>
 */
public class Exercise_1_2_1 {
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        Random random = new Random();

        // Generate N random points
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++)
            points[i] = new Point2D(random.nextDouble(), random.nextDouble());

        // Compute closest pair distance
        double minDist = Double.POSITIVE_INFINITY;
        Point2D p1 = null, p2 = null;

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dist = points[i].distanceTo(points[j]);

                if (dist < minDist) {
                    minDist = dist;
                    p1 = points[i];
                    p2 = points[j];
                }
            }
        }

        StdOut.println("Closest pair distance = " + minDist);
        StdOut.println("Between points " + p1 + " and " + p2);
    }
}