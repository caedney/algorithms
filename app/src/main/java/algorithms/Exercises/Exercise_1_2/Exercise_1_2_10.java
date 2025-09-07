package algorithms.Exercises.Exercise_1_2;

import algorithms.VisualCounter;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.10
 * 
 * <p>
 * Develop a class {@code VisualCounter} that allows both increment and
 * decrement operations. Take two arguments {@code N} and {@code max} in the
 * constructor, where {@code N} specifies the maximum number of operations and
 * {@code max} specifies the maximum absolute value for the counter. As a side
 * effect, create a plot showing the value of the counter each time its tally
 * changes.
 * </p>
 */
public class Exercise_1_2_10 {
    public static void main(String[] args) {
        VisualCounter counter = new VisualCounter(100, 50);

        for (int i = 0; i < 30; i++)
            counter.increment();

        for (int i = 0; i < 30; i++)
            counter.decrement();

        StdOut.println("Final tally: " + counter.tally());
    }
}
