package algorithms;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * Faster 3-sum
 * 
 * <p>
 * Counts the triples of integers that sum to zero in quadratic time. The array
 * is sorted once; then, for each element in turn, the remaining subarray to its
 * right is swept inward from both ends, counting the pairs that sum to that
 * element's negation in linear time — replacing {@link ThreeSumFast}'s binary
 * search for each pair's complement. Unlike {@link ThreeSumFast}, the count is
 * correct when the array contains duplicate values (including runs of zeros).
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=ThreeSumFaster --args="src/data/algs4/8Kints.txt"
 * </pre>
 ******************************************************************************/
public class ThreeSumFaster {
    public static long count(int[] a) {
        Arrays.sort(a);
        long count = 0;

        for (int i = 0; i < a.length - 2; i++)
            count += countPairs(a, -(long) a[i], i + 1, a.length - 1);

        return count;
    }

    private static long countPairs(int[] a, long target, int lo, int hi) {
        long count = 0;

        while (lo < hi) {
            long sum = (long) a[lo] + a[hi];

            if (sum < target) {
                lo++;
            } else if (sum > target) {
                hi--;
            } else if (a[lo] == a[hi]) {
                // Equal ends means the whole remaining region is one run of identical values:
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