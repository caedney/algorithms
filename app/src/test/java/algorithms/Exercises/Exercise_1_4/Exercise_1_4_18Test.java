package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/******************************************************************************
 * Exercise_1_4_18Test
 * 
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_18Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_18Test {
    /**
     * A local minimum is not unique, so every assertion here checks the returned
     * index against the definition rather than against a specific index. Asserting
     * equality would fail on correct answers.
     */
    @Test
    public void isLocalMinTreatsTheEndsAsWalls() {
        int[] a = { 2, 3, 1 };

        assertTrue(Exercise_1_4_18.isLocalMin(a, 0)); // no left neighbour
        assertFalse(Exercise_1_4_18.isLocalMin(a, 1));
        assertTrue(Exercise_1_4_18.isLocalMin(a, 2)); // no right neighbour
    }

    @Test
    public void findsADipInTheBookExample() {
        int[] a = { 9, 6, 3, 14, 5, 7, 4 };
        assertTrue(Exercise_1_4_18.isLocalMin(a, Exercise_1_4_18.findLocalMin(a)));
    }

    @Test
    public void singleElementIsItsOwnLocalMinimum() {
        assertEquals(0, Exercise_1_4_18.findLocalMin(new int[] { 42 }));
    }

    @Test
    public void twoElements() {
        assertEquals(0, Exercise_1_4_18.findLocalMin(new int[] { 1, 2 }));
        assertEquals(1, Exercise_1_4_18.findLocalMin(new int[] { 2, 1 }));
    }

    @Test
    public void strictlyAscendingHasItsOnlyLocalMinimumAtTheStart() {
        int[] a = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        assertEquals(0, Exercise_1_4_18.findLocalMin(a));
    }

    @Test
    public void strictlyDescendingHasItsOnlyLocalMinimumAtTheEnd() {
        int[] a = { 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        assertEquals(a.length - 1, Exercise_1_4_18.findLocalMin(a));
    }

    @Test
    public void anyLocalMinimumIsAcceptableNotJustTheGlobalOne() {
        int[] a = { 2, 3, 1 };
        int i = Exercise_1_4_18.findLocalMin(a);

        assertTrue(Exercise_1_4_18.isLocalMin(a, i), "index " + i + " is not a local minimum");
        assertTrue(i == 0 || i == 2, "the only local minima are 0 and 2, got " + i);
    }

    @Test
    public void agreesWithBruteForceOnEveryPermutationUpToLengthEight() {
        for (int n = 1; n <= 8; n++) {
            int[] a = new int[n];

            for (int i = 0; i < n; i++)
                a[i] = i;

            do {
                int i = Exercise_1_4_18.findLocalMin(a);
                assertTrue(bruteForceIsLocalMin(a, i), "index " + i + " is not a local minimum");
            } while (nextPermutation(a));
        }
    }

    @Test
    public void agreesWithBruteForceOnRandomShuffles() {
        Random random = new Random(1418);

        for (int n : new int[] { 3, 17, 1000, 100000 }) {
            for (int trial = 0; trial < 100; trial++) {
                int[] a = shuffledDistinct(n, random);
                int i = Exercise_1_4_18.findLocalMin(a);
                assertTrue(bruteForceIsLocalMin(a, i), "index " + i + " is not a local minimum");
            }
        }
    }

    @Test
    public void findsTheLocalMinimumWhenItSitsOnlyAtAnEnd() {
        // the only dip is the last index; nothing in the interior qualifies
        int[] a = { 8, 7, 6, 5, 4, 3, 2, 1 };
        assertEquals(7, Exercise_1_4_18.findLocalMin(a));

        // and the mirror image
        int[] b = { 1, 2, 3, 4, 5, 6, 7, 8 };
        assertEquals(0, Exercise_1_4_18.findLocalMin(b));
    }

    /**
     * The definition, written out independently of the implementation: a[i] is
     * smaller than both neighbours, with a missing neighbour treated as +infinity.
     */
    private static boolean bruteForceIsLocalMin(int[] a, int i) {
        boolean left = (i == 0) || a[i - 1] > a[i];
        boolean right = (i == a.length - 1) || a[i + 1] > a[i];

        return left && right;
    }

    /** A shuffle of 0..n-1, so every value is distinct as the exercise requires. */
    private static int[] shuffledDistinct(int n, Random random) {
        int[] a = new int[n];

        for (int i = 0; i < n; i++)
            a[i] = i;

        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int swap = a[i];
            a[i] = a[j];
            a[j] = swap;
        }

        return a;
    }

    /**
     * Rearranges a into the next permutation in lexicographic order, returning
     * false once it is the last one. Lets a test walk every arrangement of a small
     * array without allocating them all.
     */
    private static boolean nextPermutation(int[] a) {
        int i = a.length - 2;

        while (i >= 0 && a[i] >= a[i + 1])
            i--;

        if (i < 0)
            return false;

        int j = a.length - 1;

        while (a[j] <= a[i])
            j--;

        swap(a, i, j);

        for (int lo = i + 1, hi = a.length - 1; lo < hi; lo++, hi--)
            swap(a, lo, hi);

        return true;
    }

    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
}
