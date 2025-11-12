package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/******************************************************************************
 * Exercise_1_4_22Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_22Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_22Test {
    /**
     * The values are distinct, so a present key has exactly one index and the
     * returned index can be asserted directly rather than just "found".
     */
    @Test
    public void findsEveryKeyInTheTraceExample() {
        // N = 20 is the write-up's trace: the range starts at [0, 21) with pair (21, 13)
        int[] a = ascending(20, 0, 1); // 0, 1, ..., 19

        for (int key = 0; key < 20; key++)
            assertEquals(key, Exercise_1_4_22.findTarget(a, key), "key " + key);

        assertEquals(-1, Exercise_1_4_22.findTarget(a, -1)); // below the range
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 20)); // index 20 is in [0, 21) but not in the array
    }

    @Test
    public void handlesEmptyArray() {
        // The pair is (1, 1) and the first probe is index 0 of a zero-length array;
        // the past-the-end test has to catch it, there is no separate guard.
        int[] a = {};
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 0));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 11));
    }

    @Test
    public void handlesSingleElementArray() {
        int[] a = { 7 };
        assertEquals(0, Exercise_1_4_22.findTarget(a, 7));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 6));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 8));
    }

    @Test
    public void findsTheFirstOfTwoElements() {
        // The tail case. From pair (2, 1) a "too big" probe at index 1 steps the pair
        // to (1, 0), whose probe offset is 1 - 0 = 1: index 1 again, index 0 never.
        // A draft without the clamp reported the present key 2 as absent.
        int[] a = { 2, 10 };
        assertEquals(0, Exercise_1_4_22.findTarget(a, 2));
        assertEquals(1, Exercise_1_4_22.findTarget(a, 10));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 1));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 5));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 11));
    }

    @Test
    public void treatsAProbePastTheEndAsTooBig() {
        // N = 4 gives a range of [0, 5); searching above the maximum walks the
        // probe onto index 4, which a draft without the padding test read.
        int[] a = { 1, 3, 4, 10 };
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 11));
        assertEquals(3, Exercise_1_4_22.findTarget(a, 10));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 2)); // in a gap
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 0)); // below the range
    }

    @Test
    public void worksOnFibonacciSizedArrays() {
        // When N is itself a Fibonacci number the range is exactly the array and no
        // probe can fall past the end; these sizes passed even the earliest draft.
        for (int n : new int[] { 1, 2, 3, 5, 8, 13, 21, 34, 55 })
            assertAgreesWithBruteForceOnEveryKey(ascending(n, 0, 2));
    }

    @Test
    public void worksOnEverySizeUpToSixty() {
        // The sizes in between are the ones where the range overhangs the array.
        for (int n = 0; n <= 60; n++)
            assertAgreesWithBruteForceOnEveryKey(ascending(n, 1, 3));
    }

    @Test
    public void handlesNegativeValuesAndExtremeInts() {
        int[] a = { Integer.MIN_VALUE, -5, 0, 7, Integer.MAX_VALUE };

        for (int index = 0; index < a.length; index++)
            assertEquals(index, Exercise_1_4_22.findTarget(a, a[index]), "key " + a[index]);

        assertEquals(-1, Exercise_1_4_22.findTarget(a, -6));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 1));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, Integer.MAX_VALUE - 1));
    }

    @Test
    public void agreesWithBruteForceOnRandomSortedDistinctArrays() {
        Random random = new Random(22L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            int n = random.nextInt(61); // size 0..60
            int[] a = sortedDistinct(n, 3 * n + 5, random);
            assertAgreesWithBruteForceOnEveryKey(a);
        }
    }

    @Test
    public void findsKeysInALargeArray() {
        // N = 1,000,000 is not a Fibonacci number; the range is [0, 1346269).
        int n = 1_000_000;
        int[] a = ascending(n, 0, 2);

        assertEquals(0, Exercise_1_4_22.findTarget(a, 0));
        assertEquals(1, Exercise_1_4_22.findTarget(a, 2));
        assertEquals(n / 2, Exercise_1_4_22.findTarget(a, n));
        assertEquals(n - 1, Exercise_1_4_22.findTarget(a, 2 * (n - 1)));

        assertEquals(-1, Exercise_1_4_22.findTarget(a, -2));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 1)); // odd values are absent
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 2 * n - 1));
        assertEquals(-1, Exercise_1_4_22.findTarget(a, 2 * n));
    }

    /**
     * Queries every key from one below the minimum to one above the maximum, so
     * every element and every gap is exercised, and compares with a linear scan.
     */
    private static void assertAgreesWithBruteForceOnEveryKey(int[] a) {
        int lo = a.length == 0 ? 0 : a[0] - 1;
        int hi = a.length == 0 ? 0 : a[a.length - 1] + 1;

        for (int key = lo; key <= hi; key++) {
            assertEquals(bruteForceIndexOf(a, key), Exercise_1_4_22.findTarget(a, key),
                    "mismatch for key " + key + " in " + Arrays.toString(a));
        }
    }

    /** The definition, independent of the implementation: a linear scan. */
    private static int bruteForceIndexOf(int[] a, int key) {
        for (int i = 0; i < a.length; i++)
            if (a[i] == key)
                return i;

        return -1;
    }

    /** n values start, start + step, start + 2 step, ... — ascending and distinct. */
    private static int[] ascending(int n, int start, int step) {
        int[] a = new int[n];

        for (int i = 0; i < n; i++)
            a[i] = start + i * step;

        return a;
    }

    /** n distinct values drawn from 0..bound-1, in ascending order. */
    private static int[] sortedDistinct(int n, int bound, Random random) {
        TreeSet<Integer> values = new TreeSet<>();

        while (values.size() < n)
            values.add(random.nextInt(bound));

        int[] a = new int[n];
        int i = 0;

        for (int value : values)
            a[i++] = value;

        return a;
    }
}
