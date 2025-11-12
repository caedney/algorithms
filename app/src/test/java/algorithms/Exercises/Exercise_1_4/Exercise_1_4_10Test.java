package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/******************************************************************************
 * Exercise_1_4_10Test
 * 
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_10Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_10Test {
    @Test
    public void returnsSmallestIndexWhenKeyIsDuplicated() {
        int[] array = { 3, 5, 5, 5, 5, 8, 9 };
        assertEquals(1, Exercise_1_4_10.rank(5, array));
    }

    @Test
    public void returnsSmallestIndexWhenEntireArrayIsOneValue() {
        int[] array = { 7, 7, 7, 7, 7, 7, 7, 7 };
        assertEquals(0, Exercise_1_4_10.rank(7, array));
    }

    @Test
    public void findsUniqueKey() {
        int[] array = { 3, 5, 5, 5, 5, 8, 9 };
        assertEquals(0, Exercise_1_4_10.rank(3, array));
        assertEquals(5, Exercise_1_4_10.rank(8, array));
        assertEquals(6, Exercise_1_4_10.rank(9, array));
    }

    @Test
    public void returnsMinusOneWhenKeyIsAbsent() {
        int[] array = { 3, 5, 5, 5, 5, 8, 9 };
        assertEquals(-1, Exercise_1_4_10.rank(4, array));
        assertEquals(-1, Exercise_1_4_10.rank(1, array)); // below the range
        assertEquals(-1, Exercise_1_4_10.rank(10, array)); // above the range
    }

    @Test
    public void handlesEmptyArray() {
        assertEquals(-1, Exercise_1_4_10.rank(5, new int[] {}));
    }

    @Test
    public void handlesSingleElementArray() {
        assertEquals(0, Exercise_1_4_10.rank(5, new int[] { 5 }));
        assertEquals(-1, Exercise_1_4_10.rank(4, new int[] { 5 }));
    }

    @Test
    public void duplicatesAtTheStartAndEndOfTheArray() {
        int[] array = { 2, 2, 2, 4, 6, 9, 9, 9 };
        assertEquals(0, Exercise_1_4_10.rank(2, array));
        assertEquals(5, Exercise_1_4_10.rank(9, array));
    }

    @Test
    public void agreesWithLinearScanOnEveryKeyInRange() {
        int[] array = { 1, 1, 2, 2, 2, 3, 5, 5, 5, 5, 8, 8, 9 };

        for (int key = 0; key <= 10; key++) {
            assertEquals(linearRank(key, array), Exercise_1_4_10.rank(key, array), "mismatch for key " + key);
        }
    }

    /** The obvious O(n) reference implementation: first index that matches. */
    private static int linearRank(int key, int[] array) {
        for (int i = 0; i < array.length; i++)
            if (array[i] == key)
                return i;

        return -1;
    }
}