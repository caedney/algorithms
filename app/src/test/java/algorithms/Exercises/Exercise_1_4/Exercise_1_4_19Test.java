package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/******************************************************************************
 * Exercise_1_4_19Test
 * 
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_19Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_19Test {
    /**
     * A local minimum is not unique, so most assertions here check the returned
     * pair against the definition rather than against specific indices. The one
     * exception is the worked 8-by-8 example, which was constructed with exactly
     * one local minimum precisely so that equality is assertable.
     */
    // @formatter:off
    private static final int[][] WORKED_EXAMPLE = {
        { 28, 13, 23, 20, 30, 43, 53, 61 },
        { 14,  3,  9, 18, 24, 32, 45, 54 },
        { 10,  2,  1,  8, 22, 25, 34, 46 },
        { 26, 12,  4, 16, 27, 36, 47, 55 },
        { 29, 31,  6, 33, 35, 37, 39, 41 },
        { 48, 38, 44, 40, 49, 56, 62, 66 },
        { 57, 50, 73, 51, 58, 63, 67, 69 },
        { 64, 59, 72, 60, 65, 68, 70, 71 },
    };
    // @formatter:on

    /**
     * A memoryless alternating sweep fails on this grid: its first pass keeps the
     * bottom strip on the strength of the 0, and its second pass -- having no
     * record of that 0 -- discards the columns containing it, along with every
     * other local minimum. Kept as a regression test for the remembered-best rule.
     */
    // @formatter:off
    private static final int[][] AMNESIA_TRAP = {
        { 48,  7, 16, 34, 17, 37, 39 },
        { 43, 47, 28, 42, 35, 15, 18 },
        {  8, 26,  6, 22,  1, 23, 38 },
        { 21, 10, 12, 20, 14,  4, 27 },
        { 24, 33,  3, 40, 36,  0, 46 },
        { 32, 25, 11, 44, 45,  9, 13 },
        { 29,  2,  5, 31, 30, 19, 41 },
    };
    // @formatter:on

    @Test
    public void isLocalMinTreatsTheGridEdgeAsWalls() {
        int[][] a = { { 12, 8, 9 }, { 7, 3, 11 }, { 15, 6, 10 } };

        assertTrue(Exercise_1_4_19.isLocalMin(a, 1, 1)); // 3 beats 8, 11, 6, 7
        assertFalse(Exercise_1_4_19.isLocalMin(a, 2, 1)); // 6 has the 3 above it
        assertFalse(Exercise_1_4_19.isLocalMin(a, 0, 0)); // corner, but 12 > 8
        assertTrue(Exercise_1_4_19.isLocalMin(new int[][] { { 42 } }, 0, 0));
    }

    @Test
    public void singleCellIsItsOwnLocalMinimum() {
        assertArrayEquals(new int[] { 0, 0 }, Exercise_1_4_19.findLocalMin(new int[][] { { 42 } }));
    }

    @Test
    public void findsThePlantedMinimumInTheWorkedExample() {
        assertArrayEquals(new int[] { 2, 2 }, Exercise_1_4_19.findLocalMin(WORKED_EXAMPLE));
        assertArrayEquals(new int[] { 2, 2 }, Exercise_1_4_19.findLocalMinBruteForce(WORKED_EXAMPLE));
    }

    @Test
    public void survivesTheGridThatDefeatsAMemorylessSweep() {
        assertAllImplementationsFindAValidLocalMinimum(AMNESIA_TRAP);
    }

    @Test
    public void agreesWithTheDefinitionOnEveryPermutationUpToThreeByThree() {
        for (int n = 1; n <= 3; n++) {
            int[] flat = new int[n * n];

            for (int k = 0; k < flat.length; k++)
                flat[k] = k;

            do {
                assertAllImplementationsFindAValidLocalMinimum(reshape(flat, n));
            } while (nextPermutation(flat));
        }
    }

    @Test
    public void agreesWithTheDefinitionOnRandomShuffles() {
        Random random = new Random(1419);

        for (int n : new int[] { 2, 5, 17, 100 })
            for (int trial = 0; trial < 200; trial++)
                assertAllImplementationsFindAValidLocalMinimum(shuffledDistinctGrid(n, random));

        for (int trial = 0; trial < 20; trial++)
            assertAllImplementationsFindAValidLocalMinimum(shuffledDistinctGrid(1000, random));
    }

    @Test
    public void survivesGridsWhereTheOnlyDescentIsALongSnake() {
        for (int n : new int[] { 2, 3, 8, 101 }) {
            int[][] a = snakeGrid(n);
            int[] p = Exercise_1_4_19.findLocalMin(a);
            assertTrue(bruteForceIsLocalMin(a, p[0], p[1]), "findLocalMin returned (" + p[0] + ", " + p[1] + ")");
        }
    }

    /**
     * The linear budget, asserted rather than eyeballed: the sweeps of an N-by-N
     * grid may inspect at most N + 2(N/2 + N/4 + ...) < 3N cells, and that must
     * hold on every individual run. The exploratory doubling table lives in the
     * Exercise_1_4_19Doubling client, out of the test suite's way.
     */
    @Test
    public void sweepsStayWithinTheLinearBudget() {
        Random random = new Random(1420);

        for (int n : new int[] { 250, 500, 1000, 2000 }) {
            for (int trial = 0; trial < 20; trial++) {
                int[][] a = shuffledDistinctGrid(n, random);
                Exercise_1_4_19.inspections = 0;

                int[] p = Exercise_1_4_19.findLocalMin(a);

                assertTrue(bruteForceIsLocalMin(a, p[0], p[1]), "invalid result at n = " + n);
                assertTrue(Exercise_1_4_19.inspections <= 3L * n,
                        "budget blown at n = " + n + ": " + Exercise_1_4_19.inspections + " > " + 3L * n);
            }
        }
    }

    /**
     * Runs both implementations on the same grid and checks each result against the
     * definition. Validity is all that can be asserted in general: the two methods
     * may legitimately return different local minima of the same grid.
     */
    private static void assertAllImplementationsFindAValidLocalMinimum(int[][] a) {
        int[] fast = Exercise_1_4_19.findLocalMin(a);
        assertTrue(bruteForceIsLocalMin(a, fast[0], fast[1]),
                "findLocalMin returned (" + fast[0] + ", " + fast[1] + ")");

        int[] brute = Exercise_1_4_19.findLocalMinBruteForce(a);
        assertTrue(bruteForceIsLocalMin(a, brute[0], brute[1]),
                "findLocalMinBruteForce returned (" + brute[0] + ", " + brute[1] + ")");
    }

    /**
     * The definition, written out independently of the implementation: a[i][j] is
     * smaller than all four neighbours, with a missing neighbour treated as
     * +infinity.
     */
    private static boolean bruteForceIsLocalMin(int[][] a, int i, int j) {
        boolean up = (i == 0) || a[i - 1][j] > a[i][j];
        boolean down = (i == a.length - 1) || a[i + 1][j] > a[i][j];
        boolean left = (j == 0) || a[i][j - 1] > a[i][j];
        boolean right = (j == a.length - 1) || a[i][j + 1] > a[i][j];

        return up && down && left && right;
    }

    /** A shuffle of 0..n*n-1 laid out as a grid, so every value is distinct. */
    private static int[][] shuffledDistinctGrid(int n, Random random) {
        int[] flat = new int[n * n];

        for (int k = 0; k < flat.length; k++)
            flat[k] = k;

        for (int k = flat.length - 1; k > 0; k--) {
            int s = random.nextInt(k + 1);
            int swap = flat[k];
            flat[k] = flat[s];
            flat[s] = swap;
        }

        return reshape(flat, n);
    }

    /**
     * Values strictly decreasing along a boustrophedon path, so the only local
     * minimum is the path's end and any descent must wind through most of the grid
     * -- the adversarial case for anything resembling gradient descent.
     */
    private static int[][] snakeGrid(int n) {
        int[][] a = new int[n][n];
        int value = n * n;

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                a[i][i % 2 == 0 ? j : n - 1 - j] = value--;

        return a;
    }

    private static int[][] reshape(int[] flat, int n) {
        int[][] a = new int[n][n];

        for (int k = 0; k < flat.length; k++)
            a[k / n][k % n] = flat[k];

        return a;
    }

    /**
     * Rearranges the array into the next permutation in lexicographic order,
     * returning false once it is the last one. Lets a test walk every arrangement
     * of a small grid without allocating them all.
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
