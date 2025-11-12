package algorithms;

import java.util.Random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/******************************************************************************
 * StaticSETofIntsTest (Exercise 1.4.11: howMany)
 *
 * <pre>
 * ./gradlew test --tests "StaticSETofIntsTest"
 * </pre>
 ******************************************************************************/
public class StaticSETofIntsTest {
    @Test
    public void countsEveryOccurrenceOfADuplicatedKey() {
        StaticSETofInts set = new StaticSETofInts(new int[] { 1, 1, 2, 2, 2, 2, 3, 5, 5, 5 });
        assertEquals(2, set.howMany(1));
        assertEquals(4, set.howMany(2));
        assertEquals(1, set.howMany(3));
        assertEquals(3, set.howMany(5));
    }

    @Test
    public void countsWhenEntireArrayIsOneValue() {
        // The case that breaks "count while searching" implementations:
        // binary search skips over most of the run.
        StaticSETofInts set = new StaticSETofInts(new int[] { 2, 2, 2, 2, 2 });
        assertEquals(5, set.howMany(2));
        assertEquals(0, set.howMany(1)); // below the range
        assertEquals(0, set.howMany(3)); // above the range
    }

    @Test
    public void countsRunsAtTheStartMiddleAndEnd() {
        assertEquals(3, new StaticSETofInts(new int[] { 1, 1, 1, 4, 8 }).howMany(1));
        assertEquals(3, new StaticSETofInts(new int[] { 0, 4, 4, 4, 9 }).howMany(4));
        assertEquals(3, new StaticSETofInts(new int[] { 0, 3, 6, 6, 6 }).howMany(6));
    }

    @Test
    public void countsUniqueKeys() {
        StaticSETofInts set = new StaticSETofInts(new int[] { 3, 5, 7, 8, 9 });
        assertEquals(1, set.howMany(3));
        assertEquals(1, set.howMany(7));
        assertEquals(1, set.howMany(9));
    }

    @Test
    public void returnsZeroWhenKeyIsAbsent() {
        StaticSETofInts set = new StaticSETofInts(new int[] { 3, 5, 5, 5, 5, 8, 9 });
        assertEquals(0, set.howMany(4)); // in a gap
        assertEquals(0, set.howMany(1)); // below the range
        assertEquals(0, set.howMany(10)); // above the range
    }

    @Test
    public void handlesEmptyArray() {
        assertEquals(0, new StaticSETofInts(new int[] {}).howMany(5));
    }

    @Test
    public void handlesSingleElementArray() {
        assertEquals(1, new StaticSETofInts(new int[] { 5 }).howMany(5));
        assertEquals(0, new StaticSETofInts(new int[] { 5 }).howMany(4));
    }

    @Test
    public void sortsUnsortedInputBeforeCounting() {
        StaticSETofInts set = new StaticSETofInts(new int[] { 5, 1, 5, 3, 5, 1 });
        assertEquals(3, set.howMany(5));
        assertEquals(2, set.howMany(1));
    }

    @Test
    public void handlesNegativeValuesAndExtremeInts() {
        StaticSETofInts set = new StaticSETofInts(new int[] { -9, -9, -1, 0, 0, 0, 4 });
        assertEquals(2, set.howMany(-9));
        assertEquals(3, set.howMany(0));

        // Guards against overflow bugs like (lo + hi) / 2 or key + 1
        StaticSETofInts extremes = new StaticSETofInts(new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE, 0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE });
        assertEquals(2, extremes.howMany(Integer.MIN_VALUE));
        assertEquals(3, extremes.howMany(Integer.MAX_VALUE));
        assertEquals(0, extremes.howMany(1));
    }

    @Test
    public void agreesWithLinearScanOnRandomArrays() {
        Random random = new Random(42); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 1000; trial++) {
            int[] array = new int[random.nextInt(51)]; // size 0..50
            for (int i = 0; i < array.length; i++)
                array[i] = random.nextInt(21) - 10; // values -10..10, forcing duplicates

            StaticSETofInts set = new StaticSETofInts(array);

            // Query every value in and just outside the range,
            // so absent keys are exercised too.
            for (int key = -12; key <= 12; key++) {
                assertEquals(linearCount(key, array), set.howMany(key),
                        "mismatch for key " + key + " in " + java.util.Arrays.toString(array));
            }
        }
    }

    /** The obvious O(n) reference implementation: count every match. */
    private static int linearCount(int key, int[] array) {
        int count = 0;

        for (int value : array)
            if (value == key)
                count++;

        return count;
    }
}