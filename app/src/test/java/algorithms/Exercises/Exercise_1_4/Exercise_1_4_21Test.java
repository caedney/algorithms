package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/******************************************************************************
 * Exercise_1_4_21Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_21Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_21Test {
    private static final int[] SAMPLE = { 5, 9, 5, 5, 2, 9, 5, 2, 5, 9, 2, 5 };

    @Test
    public void containsEveryValueOfTheSampleArray() {
        Exercise_1_4_21 set = new Exercise_1_4_21(SAMPLE);
        assertTrue(set.contains(2));
        assertTrue(set.contains(5));
        assertTrue(set.contains(9));
    }

    @Test
    public void rejectsValuesAbsentFromTheSampleArray() {
        Exercise_1_4_21 set = new Exercise_1_4_21(SAMPLE);
        assertFalse(set.contains(1)); // below the range
        assertFalse(set.contains(4)); // in a gap
        assertFalse(set.contains(7)); // in a gap
        assertFalse(set.contains(10)); // above the range
    }

    @Test
    public void reducesTheSampleArrayToItsDistinctValues() {
        Exercise_1_4_21 set = new Exercise_1_4_21(SAMPLE);
        assertEquals("[2, 5, 9]", set.toString());
        // rank() indexes the deduplicated array, so the largest value sits at R - 1
        assertEquals(0, set.rank(2));
        assertEquals(1, set.rank(5));
        assertEquals(2, set.rank(9));
    }

    @Test
    public void keepsTheLargestValueWhenItAppearsOnlyOnce() {
        // The last element is the first of its run whenever the maximum is unique;
        // a loop bound of a.length - 1 silently drops it.
        assertEquals("[1, 2, 3]", new Exercise_1_4_21(new int[] { 1, 2, 3 }).toString());
        assertEquals("[0, 6, 8]", new Exercise_1_4_21(new int[] { 6, 6, 8, 0 }).toString());
        assertTrue(new Exercise_1_4_21(new int[] { 6, 6, 8, 0 }).contains(8));
    }

    @Test
    public void handlesEmptyArray() {
        Exercise_1_4_21 set = new Exercise_1_4_21(new int[] {});
        assertEquals("[]", set.toString());
        assertFalse(set.contains(0));
        assertEquals(-1, set.rank(0));
    }

    @Test
    public void handlesSingleElementArray() {
        Exercise_1_4_21 set = new Exercise_1_4_21(new int[] { 7 });
        assertEquals("[7]", set.toString());
        assertTrue(set.contains(7));
        assertFalse(set.contains(6));
        assertFalse(set.contains(8));
    }

    @Test
    public void collapsesAnAllEqualArrayToOneValue() {
        Exercise_1_4_21 set = new Exercise_1_4_21(new int[] { 2, 2, 2, 2, 2 });
        assertEquals("[2]", set.toString());
        assertTrue(set.contains(2));
        assertFalse(set.contains(1));
        assertFalse(set.contains(3));
    }

    @Test
    public void leavesAnAlreadyDistinctArrayIntact() {
        Exercise_1_4_21 set = new Exercise_1_4_21(new int[] { 9, 3, 7, 1, 5 });
        assertEquals("[1, 3, 5, 7, 9]", set.toString());
        for (int key : new int[] { 1, 3, 5, 7, 9 })
            assertTrue(set.contains(key), "key " + key);
        for (int key : new int[] { 0, 2, 4, 6, 8, 10 })
            assertFalse(set.contains(key), "key " + key);
    }

    @Test
    public void doesNotMutateTheCallersArray() {
        int[] keys = SAMPLE.clone();
        new Exercise_1_4_21(keys);
        assertArrayEquals(SAMPLE, keys);
    }

    @Test
    public void handlesNegativeValuesAndExtremeInts() {
        Exercise_1_4_21 set = new Exercise_1_4_21(new int[] { -9, -9, -1, 0, 0, 0, 4 });
        assertEquals("[-9, -1, 0, 4]", set.toString());
        assertTrue(set.contains(-9));
        assertTrue(set.contains(0));
        assertFalse(set.contains(-5));

        // Guards against overflow bugs like (lo + hi) / 2
        Exercise_1_4_21 extremes = new Exercise_1_4_21(new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE, 0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE });
        assertEquals(3, extremes.rank(Integer.MAX_VALUE) + 1); // R == 3
        assertTrue(extremes.contains(Integer.MIN_VALUE));
        assertTrue(extremes.contains(Integer.MAX_VALUE));
        assertFalse(extremes.contains(1));
    }

    @Test
    public void agreesWithHashSetOracleOnRandomArrays() {
        Random random = new Random(21L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 1000; trial++) {
            int[] array = new int[random.nextInt(51)]; // size 0..50
            for (int i = 0; i < array.length; i++)
                array[i] = random.nextInt(21) - 10; // values -10..10, forcing duplicates

            Set<Integer> oracle = new HashSet<>();
            for (int value : array)
                oracle.add(value);

            Exercise_1_4_21 set = new Exercise_1_4_21(array);

            // The internal array must hold exactly the R distinct values, in order.
            assertEquals(new TreeSet<>(oracle).toString(), set.toString(), "distinct values of " + Arrays.toString(array));

            // Query every value in and just outside the range,
            // so absent keys are exercised too.
            for (int key = -12; key <= 12; key++) {
                assertEquals(oracle.contains(key), set.contains(key),
                        "mismatch for key " + key + " in " + Arrays.toString(array));
            }
        }
    }
}
