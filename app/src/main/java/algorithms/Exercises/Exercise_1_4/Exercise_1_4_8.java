package algorithms.Exercises.Exercise_1_4;

import java.util.Arrays;

import algorithms.Stopwatch;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * Exercise 1.4.8
 * 
 * <p>
 * Write a program to determine the number pairs of values in an input file that
 * are equal. If your first try is quadratic, think again and use
 * {@code Arrays.sort()} to develop a linearithmic solution.
 * </p>
 *
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_8 --args="src/main/resources/data/2Pair.txt"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_8 {
    // C(n, k) via the multiplicative formula -- no factorials, no overflow until
    // the result itself overflows
    public static long C(long n, long k) {
        if (k < 0 || k > n)
            return 0;

        k = Math.min(k, n - k); // C(n, k) == C(n, n-k); use the smaller
        long result = 1;

        for (long i = 1; i <= k; i++)
            result = result * (n - k + i) / i; // always divides exactly

        return result;
    }

    // Linearithmic: sort (~N log N), then a single linear scan.
    public static long count(int[] a) {
        Arrays.sort(a); // ~ N log N
        long cnt = 0;
        int i = 0;

        while (i < a.length) { // Linear scan
            int j = i;

            while (j < a.length && a[j] == a[i])
                j++; // find end of run

            long run = j - i;
            cnt += run * (run - 1) / 2; // C(run, 2) pairs
            i = j; // jump past the run
        }

        return cnt;
    }

    // Quadratic version, for comparison.
    public static long countBrute(int[] a) {
        long cnt = 0;

        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                if (a[i] == a[j])
                    cnt++;

        return cnt;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] ints = in.readAllInts();

        Stopwatch timer = new Stopwatch();
        long cnt = countBrute(ints);
        double time = timer.elapsedTime();
        StdOut.println("Brute force: " + cnt + " in " + time + " seconds");

        timer = new Stopwatch();
        cnt = count(ints);
        time = timer.elapsedTime();
        StdOut.println("Linearithmic: " + cnt + " in " + time + " seconds");
    }
}