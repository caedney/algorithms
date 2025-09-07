package algorithms.Exercises.Exercise_1_2;

import edu.princeton.cs.algs4.StdDraw;
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
    private static class VisualCounter {
        private int count;
        private int maxOperations;
        private int operationTally;
        private int maxAbs;

        public VisualCounter(int N, int max) {
            this.count = 0;
            this.operationTally = 0;
            this.maxOperations = N;
            this.maxAbs = max;

            StdDraw.setXscale(0, N);
            StdDraw.setYscale(-max, max);
            StdDraw.setPenRadius(0.005);
        }

        public void increment() {
            if (operationTally < maxOperations && count < maxAbs) {
                count++;
                operationTally++;
                draw();
            }
        }

        public void decrement() {
            if (operationTally < maxOperations && count > -maxAbs) {
                count--;
                operationTally++;
                draw();
            }
        }

        private void draw() {
            StdDraw.point(operationTally, count);
        }

        public int tally() {
            return count;
        }
    }

    public static void main(String[] args) {
        VisualCounter counter = new VisualCounter(100, 50);

        for (int i = 0; i < 30; i++)
            counter.increment();
        for (int i = 0; i < 30; i++)
            counter.decrement();

        StdOut.println("Final tally: " + counter.tally());
    }
}
