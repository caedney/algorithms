package algorithms.Exercises.Exercise_1_2;

import edu.princeton.cs.algs4.Interval1D;
import edu.princeton.cs.algs4.Interval2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.3
 * 
 * <p>
 * Write an {@code Interva12D} client that takes command-line arguments 𝑁,
 * {@code min}, and {@code max} and generates 𝑁 random 2D intervals whose width
 * and height are uniformly distributed between {@code min} and {@code max} in
 * the unit square. Draw them on {@code StdDraw} and print the number of pairs
 * of intervals that intersect and the number of intervals that are contained in
 * one another.
 * </p>
 */
public class Exercise_1_2_3 {
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        double min = Double.parseDouble(args[1]);
        double max = Double.parseDouble(args[2]);
        Interval2D[] intervals = new Interval2D[N];

        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);

        for (int i = 0; i < N; i++) {
            double x1 = Math.random() * (1 - min);
            double y1 = Math.random() * (1 - min);

            double width = min + Math.random() * (max - min);
            double height = min + Math.random() * (max - min);

            double x2 = Math.min(x1 + width, 1);
            double y2 = Math.min(y1 + height, 1);

            Interval1D xInterval = new Interval1D(x1, x2);
            Interval1D yInterval = new Interval1D(y1, y2);

            intervals[i] = new Interval2D(xInterval, yInterval);
            intervals[i].draw();
        }

        int intersectCount = 0;
        int containedCount = 0;

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (intervals[i].intersects(intervals[j]))
                    intersectCount++;
                if (intervals[i].contains(intervals[j]) || intervals[j].contains(intervals[i]))
                    containedCount++;
            }
        }

        StdOut.println("Number of intersecting pairs: " + intersectCount);
        StdOut.println("Number of contained intervals: " + containedCount);

    }
}
