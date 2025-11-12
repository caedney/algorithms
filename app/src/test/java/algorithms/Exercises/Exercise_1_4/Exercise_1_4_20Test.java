package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/******************************************************************************
 * Exercise_1_4_20Test
 * 
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_20Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_20Test {
    private static final int[] SAMPLE = { 2, 7, 11, 25, 19, 12, 5, 1 };

    @Test
    public void findsThePeakOfTheSampleArray() {
        assertEquals(3, Exercise_1_4_20.findPeak(SAMPLE));
    }

    @Test
    public void findsThePeakWhenItSitsAtEitherEnd() {
        assertEquals(6, Exercise_1_4_20.findPeak(new int[] { 1, 2, 3, 4, 5, 6, 7 })); // purely increasing
        assertEquals(0, Exercise_1_4_20.findPeak(new int[] { 7, 6, 5, 4, 3, 2, 1 })); // purely decreasing
    }

    @Test
    public void findsThePeakOfTinyArrays() {
        assertEquals(0, Exercise_1_4_20.findPeak(new int[] { 5 }));
        assertEquals(1, Exercise_1_4_20.findPeak(new int[] { 3, 9 }));
        assertEquals(0, Exercise_1_4_20.findPeak(new int[] { 9, 3 }));
    }

    @Test
    public void findsEveryValueOnBothSlopesOfTheSampleArray() {
        for (int i = 0; i < SAMPLE.length; i++) {
            assertEquals(i, Exercise_1_4_20.findTarget(SAMPLE, SAMPLE[i]), "value " + SAMPLE[i]);
        }
    }

    @Test
    public void returnsMinusOneWhenTargetIsAbsent() {
        assertEquals(-1, Exercise_1_4_20.findTarget(SAMPLE, 9)); // inside the range, between values
        assertEquals(-1, Exercise_1_4_20.findTarget(SAMPLE, 0)); // below the range
        assertEquals(-1, Exercise_1_4_20.findTarget(SAMPLE, 26)); // above the range
        assertEquals(-1, Exercise_1_4_20.findTarget(SAMPLE, 24)); // just under the peak
    }

    @Test
    public void searchesPurelyIncreasingAndPurelyDecreasingArrays() {
        int[] inc = { 1, 2, 3, 4, 5, 6, 7 };
        int[] dec = { 7, 6, 5, 4, 3, 2, 1 };

        for (int i = 0; i < inc.length; i++) {
            assertEquals(i, Exercise_1_4_20.findTarget(inc, inc[i]));
            assertEquals(i, Exercise_1_4_20.findTarget(dec, dec[i]));
        }

        assertEquals(-1, Exercise_1_4_20.findTarget(inc, 0));
        assertEquals(-1, Exercise_1_4_20.findTarget(dec, 8));
    }

    @Test
    public void handlesTinyArrays() {
        assertEquals(0, Exercise_1_4_20.findTarget(new int[] { 5 }, 5));
        assertEquals(-1, Exercise_1_4_20.findTarget(new int[] { 5 }, 4));
        assertEquals(1, Exercise_1_4_20.findTarget(new int[] { 3, 9 }, 9));
        assertEquals(0, Exercise_1_4_20.findTarget(new int[] { 9, 3 }, 9));
        assertEquals(1, Exercise_1_4_20.findTarget(new int[] { 9, 3 }, 3));
    }

    @Test
    public void handlesEmptyArray() {
        assertEquals(-1, Exercise_1_4_20.findTarget(new int[] {}, 5));
    }

    @Test
    public void agreesWithLinearScanOnRandomBitonicArrays() {
        Random random = new Random(20L);

        for (int n = 1; n <= 40; n++) {
            for (int rep = 0; rep < 50; rep++) {
                int[] a = randomBitonic(random, n);
                int min = Arrays.stream(a).min().getAsInt();
                int max = Arrays.stream(a).max().getAsInt();

                assertEquals(linearPeak(a), Exercise_1_4_20.findPeak(a), "peak of " + Arrays.toString(a));

                for (int target = min - 1; target <= max + 1; target++) {
                    assertEquals(linearSearch(a, target), Exercise_1_4_20.findTarget(a, target),
                            "target " + target + " in " + Arrays.toString(a));
                }
            }
        }
    }

    /**
     * Builds a bitonic array of {@code n} distinct values: the maximum goes at a
     * random position, the rest are split around it, sorted ascending on the left
     * and descending on the right. Covers the purely increasing and purely
     * decreasing shapes when the split lands at either end.
     */
    private static int[] randomBitonic(Random random, int n) {
        TreeSet<Integer> distinct = new TreeSet<>();
        while (distinct.size() < n)
            distinct.add(random.nextInt(10 * n + 10));

        List<Integer> values = new ArrayList<>(distinct);
        int max = values.remove(values.size() - 1);
        Collections.shuffle(values, random);

        int leftCount = random.nextInt(n);
        List<Integer> left = new ArrayList<>(values.subList(0, leftCount));
        List<Integer> right = new ArrayList<>(values.subList(leftCount, values.size()));
        Collections.sort(left);
        right.sort(Collections.reverseOrder());

        int[] a = new int[n];
        int i = 0;
        for (int v : left)
            a[i++] = v;
        a[i++] = max;
        for (int v : right)
            a[i++] = v;

        return a;
    }

    /** The obvious O(n) reference: index of the first match, or -1. */
    private static int linearSearch(int[] a, int target) {
        for (int i = 0; i < a.length; i++)
            if (a[i] == target)
                return i;

        return -1;
    }

    /** The obvious O(n) reference: index of the maximum. */
    private static int linearPeak(int[] a) {
        int peak = 0;
        for (int i = 1; i < a.length; i++)
            if (a[i] > a[peak])
                peak = i;

        return peak;
    }
}
