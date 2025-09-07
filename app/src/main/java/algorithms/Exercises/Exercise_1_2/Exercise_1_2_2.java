package algorithms.Exercises.Exercise_1_2;

import edu.princeton.cs.algs4.Interval1D;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.2
 * 
 * <p>
 * Write an {@code Interval1D} client that takes an {@code int} value 𝑁 as
 * command-line argument, reads 𝑁 intervals (each defined by a pair of
 * {@code double} values) from standard input, and prints all pairs that
 * intersect.
 * </p>
 */
public class Exercise_1_2_2 {
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        Interval1D[] intervals = new Interval1D[N];

        for (int i = 0; i < N; i++) {
            double lo = StdIn.readDouble();
            double hi = StdIn.readDouble();
            intervals[i] = new Interval1D(lo, hi);
        }

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (intervals[i].intersects(intervals[j]))
                    StdOut.println(intervals[i] + " intersects " + intervals[j]);
            }
        }
    }
}
