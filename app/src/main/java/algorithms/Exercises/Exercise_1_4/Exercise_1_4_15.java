package algorithms.Exercises.Exercise_1_4;

import algorithms.Stopwatch;
import algorithms.ThreeSumFaster;
import algorithms.TwoSumFaster;

import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * Exercise 1.4.15
 * 
 * <p>
 * Faster 3-sum. As a warmup, develop an implementation {@code TwoSumFaster}
 * that uses a linear algorithm to count the pairs that sum to zero after the
 * array is sorted (instead of the binary-search-based linearithmic algorithm).
 * Then apply a similar idea to develop a quadratic algorithm for the 3-sum
 * problem.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_15
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_15 {
    public static void main(String[] args) {
        int[] ints1 = { -8, -5, -2, 0, 0, 0, 1, 3, 5, 8, 9, 10, 11 };
        int[] ints2 = { -2, -2, 0, 2, 2, 2 };
        Stopwatch timer;
        long count;
        double time;
        // TwoSumFaster
        timer = new Stopwatch();
        count = TwoSumFaster.count(ints1);
        time = timer.elapsedTime();
        StdOut.println(count + " in " + time + " seconds");
        timer = new Stopwatch();
        count = TwoSumFaster.count(ints2);
        time = timer.elapsedTime();
        StdOut.println(count + " in " + time + " seconds");
        // ThreeSumFaster
        timer = new Stopwatch();
        count = ThreeSumFaster.count(ints1);
        time = timer.elapsedTime();
        StdOut.println(count + " in " + time + " seconds");
        timer = new Stopwatch();
        count = ThreeSumFaster.count(ints2);
        time = timer.elapsedTime();
        StdOut.println(count + " in " + time + " seconds");
    }
}
