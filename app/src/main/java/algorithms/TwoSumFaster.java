package algorithms;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * Faster 2-sum
 * 
 * <p>
 * Counts the pairs of integers that sum to zero in linear time once the array
 * is sorted, by sweeping a pair of indices inward from both ends instead of
 * binary searching for each element's complement. Unlike {@link TwoSumFast},
 * the count is correct when the array contains duplicate values (including runs
 * of zeros).
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=TwoSumFaster --args="src/data/algs4/1Mints.txt"
 * </pre>
 ******************************************************************************/
public class TwoSumFaster {
    public static long count(int[] a) {
        Arrays.sort(a);
        int lo = 0;
        int hi = a.length - 1;
        long count = 0;

        while (lo < hi) {
            long sum = (long) a[lo] + a[hi];

            if (sum < 0) {
                lo++;
            } else if (sum > 0) {
                hi--;
            } else if (a[lo] == a[hi]) {
                // Equal ends means the whole remaining region is one run of zeros:
                // count all pairs within it, and nothing is left in play.
                long m = hi - lo + 1;
                count += m * (m - 1) / 2;
                break;
            } else {
                // Two distinct runs: measure each, bank the grid, retire both.
                int L = 1;
                int R = 1;

                while (a[lo + L] == a[lo])
                    L++;

                while (a[hi - R] == a[hi])
                    R++;

                count += (long) L * R;
                lo += L;
                hi -= R;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int[] ints = new In(args[0]).readAllInts();
        Stopwatch timer = new Stopwatch();
        long count = count(ints);
        double time = timer.elapsedTime();
        StdOut.println(count + " in " + time + " seconds");
    }
}