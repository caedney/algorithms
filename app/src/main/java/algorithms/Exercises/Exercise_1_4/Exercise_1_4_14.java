package algorithms.Exercises.Exercise_1_4;

import java.util.Arrays;

/******************************************************************************
 * Exercise 1.4.14
 *
 * <p>
 * <i>4-sum</i>. Develop an algorithm for the <i>4-sum</i> problem.
 * </p>
 *
 * <p>
 * Counts the quadruples <code>i &lt; j &lt; k &lt; l</code> whose values sum to
 * zero. {@link #count} reduces 4-sum to 2-sum over the 𝑁&#178;/2 pair sums: a
 * record (sum, i, j) for every pair, sorted by sum, then scanned from both ends
 * for two records whose sums cancel. ~𝑁&#178; lg 𝑁 time against the brute
 * force's ~𝑁&#8308;/24, but O(𝑁&#178;) space against O(1) — one object per
 * pair is ~4.6 GB at 𝑁 = 16,000, so the heap gives out before the clock does.
 * </p>
 *
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_14
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_14 {
    /** One pair of positions and the sum of their values, ordered by that sum. */
    private static class Pair implements Comparable<Pair> {
        private final long sum;
        private final int i;
        private final int j;

        Pair(long sum, int i, int j) {
            this.sum = sum;
            this.i = i;
            this.j = j;
        }

        public int compareTo(Pair that) {
            return Long.compare(this.sum, that.sum);
        }
    }

    /** Four nested loops, ~N⁴/24 array accesses. */
    public static long countBruteForce(int[] a) {
        int n = a.length;
        long count = 0;

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                for (int k = j + 1; k < n; k++)
                    for (int l = k + 1; l < n; l++)
                        if ((long) a[i] + a[j] + a[k] + a[l] == 0)
                            count++;

        return count;
    }

    /** 2-sum over the pair sums, ~N² lg N. */
    public static long count(int[] a) {
        int n = a.length;
        if (n < 4)
            return 0;

        Pair[] pairs = new Pair[(int) ((long) n * (n - 1) / 2)];
        int m = 0;

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                pairs[m++] = new Pair((long) a[i] + a[j], i, j);

        Arrays.sort(pairs);

        long count = 0;
        int lo = 0;
        int hi = m - 1;

        while (lo < hi) {
            long total = pairs[lo].sum + pairs[hi].sum;

            if (total < 0)
                lo++;
            else if (total > 0)
                hi--;
            else if (pairs[lo].sum == pairs[hi].sum) {
                // One run of equal sums, necessarily zero: every combination
                // within it is a candidate.
                for (int p = lo; p <= hi; p++)
                    for (int q = p + 1; q <= hi; q++)
                        if (isQuadruple(pairs[p], pairs[q]))
                            count++;

                break;
            } else {
                // Two runs of equal sums that cancel. A binary search would
                // find one match; every combination across the runs is one.
                int loEnd = lo;
                while (pairs[loEnd + 1].sum == pairs[lo].sum)
                    loEnd++;

                int hiStart = hi;
                while (pairs[hiStart - 1].sum == pairs[hi].sum)
                    hiStart--;

                for (int p = lo; p <= loEnd; p++)
                    for (int q = hiStart; q <= hi; q++)
                        if (isQuadruple(pairs[p], pairs[q]))
                            count++;

                lo = loEnd + 1;
                hi = hiStart - 1;
            }
        }

        return count;
    }

    /**
     * True when two pairs form one quadruple i < j < k < l, i.e. when one pair lies
     * entirely before the other. Of the three ways to split four indices into two
     * pairs only (w,x)+(y,z) passes, so each quadruple is counted once with no
     * correction factor, and a shared index fails too, since a pair cannot precede
     * itself.
     */
    private static boolean isQuadruple(Pair first, Pair second) {
        return first.j < second.i || second.j < first.i;
    }

    public static void main(String[] args) {
        int[] a = { 8, -1, 2, -9, 4, 3 };

        // {0,1,2,3} and {2,3,4,5}
        System.out.println(countBruteForce(a)); // 2
        System.out.println(count(a)); // 2
    }
}
