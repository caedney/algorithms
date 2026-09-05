package algorithms.Exercises.Exercise_1_4;

/******************************************************************************
 * Exercise 1.4.18
 * 
 * <p>
 * <i>Local minimum of an array</i>. Write a program that, given an array
 * <code>a[]</code> of 𝑁 distinct integers, finds a <i>local minimum</i>: an
 * index <code>i</code> such that <code>a[i-1] > a[i] < a[i+1]</code>. Your
 * program should use ~2 lg 𝑁 compares in the worst case.
 * </p>
 * 
 * <p>
 * <i>Answer</i>: Examine the middle value <code>a[N/2]</code> and its two
 * neighbors <code>a[N/2 - 1]</code> and <code>a[N/2 + 1]</code>. If
 * <code>a[N/2]</code> is a local minimum, stop; otherwise search in the half
 * with the smaller neighbor.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_18
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_18 {
    public static boolean isLocalMin(int[] a, int i) {
        int n = a.length;
        // @formatter:off
        boolean leftOk  = (i == 0)     || a[i] < a[i - 1];
        boolean rightOk = (i == n - 1) || a[i] < a[i + 1];
        // @formatter:on

        return leftOk && rightOk;
    }

    /**
     * The obvious baseline: scan left to right and return the first index that
     * passes <code>isLocalMin</code>. Costs ~2𝑁 compares in the worst case — a
     * strictly descending array passes every <code>leftOk</code>, fails every
     * <code>rightOk</code>, and walks all the way to the last index. Always returns
     * the <i>leftmost</i> local minimum, which the logarithmic versions do not
     * promise, so tests comparing against it must check validity, not equality of
     * indices.
     */
    public static int findLocalMinBruteForce(int[] a) {
        for (int i = 0; i < a.length; i++) {
            if (isLocalMin(a, i))
                return i;
        }

        throw new AssertionError("unreachable: every array has a local minimum");
    }

    /**
     * Descends toward the <i>smaller</i> of the two neighbours, which costs ~3lg 𝑁
     * compares: <code>isLocalMin</code> makes two, then
     * <code>a[mid + 1] < a[mid - 1]</code> makes a third to decide which neighbour
     * that is, over at most lg 𝑁 halvings. Those first two compares already reveal
     * which neighbours lie below <code>a[mid]</code>, and descending into either is
     * enough — see <code>findLocalMin</code>, which drops the tie-break and reaches
     * ~2lg 𝑁.
     */
    public static int findLocalMinSlow(int[] a) {
        int lo = 0;
        int hi = a.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (isLocalMin(a, mid))
                return mid;

            if (mid == 0 || a[mid + 1] < a[mid - 1])
                lo = mid + 1;
            else
                hi = mid - 1;
        }

        throw new AssertionError("unreachable: every array has a local minimum");
    }

    /**
     * Descends into <i>either</i> neighbour that lies below <code>a[mid]</code>,
     * which costs ~2lg 𝑁 compares: each comparison is acted on before the next is
     * made, so a probe spends one compare when the left neighbour descends and two
     * otherwise, over at most lg 𝑁 halvings. The window excludes <code>mid</code>
     * so that it always shrinks, and a missing neighbour cannot descend, so the
     * index guards stand in for comparing against +∞.
     */
    public static int findLocalMin(int[] a) {
        int lo = 0;
        int hi = a.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (mid > 0 && a[mid - 1] < a[mid])
                hi = mid - 1;
            else if (mid < a.length - 1 && a[mid + 1] < a[mid])
                lo = mid + 1;
            else
                return mid;
        }

        throw new AssertionError("unreachable: every array has a local minimum");
    }

    public static void main(String[] args) {
        int[] a = { 9, 6, 3, 14, 5, 7, 4 };
        int localMin = findLocalMin(a);
        System.out.println("local minimum is at index: " + localMin);
    }
}
