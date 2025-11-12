package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/******************************************************************************
 * Exercise_1_4_12Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_12Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_12Test {
    @Test
    public void printsCommonElementsInSortedOrder() {
        int[] a = { 1, 3, 5, 7 };
        int[] b = { 3, 4, 7, 9 };
        assertArrayEquals(new int[] { 3, 7 }, intersectionOf(a, b));
    }

    @Test
    public void printsNothingWhenRangesAreDisjoint() {
        int[] a = { 1, 2, 3 };
        int[] b = { 7, 8, 9 };
        assertArrayEquals(new int[] {}, intersectionOf(a, b));
    }

    @Test
    public void printsNothingWhenValuesInterleaveButNeverMatch() {
        int[] a = { 1, 3, 5, 7 };
        int[] b = { 2, 4, 6, 8 };
        assertArrayEquals(new int[] {}, intersectionOf(a, b));
    }

    @Test
    public void printsEverythingWhenArraysAreIdentical() {
        int[] a = { 2, 4, 6, 8 };
        int[] b = { 2, 4, 6, 8 };
        assertArrayEquals(new int[] { 2, 4, 6, 8 }, intersectionOf(a, b));
    }

    @Test
    public void handlesEmptyArrays() {
        assertArrayEquals(new int[] {}, intersectionOf(new int[] {}, new int[] { 1, 2, 3 }));
        assertArrayEquals(new int[] {}, intersectionOf(new int[] { 1, 2, 3 }, new int[] {}));
        assertArrayEquals(new int[] {}, intersectionOf(new int[] {}, new int[] {}));
    }

    @Test
    public void handlesArraysOfDifferentLengths() {
        int[] a = { 5 };
        int[] b = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        assertArrayEquals(new int[] { 5 }, intersectionOf(a, b));
        assertArrayEquals(new int[] { 5 }, intersectionOf(b, a));
    }

    @Test
    public void matchesAtTheStartAndEndOfBothArrays() {
        int[] a = { 1, 4, 9 };
        int[] b = { 1, 6, 9 };
        assertArrayEquals(new int[] { 1, 9 }, intersectionOf(a, b));
    }

    /**
     * The merge prints one value per matched <em>pair</em>: a value appearing m
     * times in one array and n times in the other prints min(m, n) times. If you
     * switch to set semantics (each common value once), update these expectations.
     */
    @Test
    public void printsOncePerMatchedPairWhenBothArraysHaveDuplicates() {
        int[] a = { 3, 3, 3, 5 };
        int[] b = { 3, 3, 5, 5 };
        assertArrayEquals(new int[] { 3, 3, 5 }, intersectionOf(a, b));
    }

    @Test
    public void duplicatesInOnlyOneArrayMatchOnlyOnce() {
        int[] a = { 2, 2, 2, 2 };
        int[] b = { 2, 7 };
        assertArrayEquals(new int[] { 2 }, intersectionOf(a, b));
    }

    @Test
    public void entireArraysOfOneRepeatedValue() {
        int[] a = { 4, 4, 4, 4, 4 };
        int[] b = { 4, 4, 4 };
        assertArrayEquals(new int[] { 4, 4, 4 }, intersectionOf(a, b));
    }

    @Test
    public void agreesWithQuadraticReferenceOnDuplicateHeavyArrays() {
        int[] a = { 1, 1, 2, 2, 2, 3, 5, 5, 5, 5, 8, 8, 9 };
        int[] b = { 0, 1, 2, 2, 4, 5, 5, 5, 8, 9, 9, 10 };

        assertArrayEquals(quadraticIntersection(a, b), intersectionOf(a, b));
        assertArrayEquals(quadraticIntersection(b, a), intersectionOf(b, a));
    }

    /**
     * Runs printIntersection with System.out captured and parses the printed lines
     * back into an int[].
     *
     * Note: this assumes the implementation prints to System.out (directly or via
     * algs4's StdOut). StdOut binds itself to the console the first time the class
     * loads, so if another test in the suite touches StdOut before this one,
     * capture will come back empty — the simple fix is to print with
     * System.out.println in the implementation.
     */
    private static int[] intersectionOf(int[] a, int[] b) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer));
            Exercise_1_4_12.printIntersection(a, b);
        } finally {
            System.setOut(original);
        }

        String text = buffer.toString().trim();

        if (text.isEmpty())
            return new int[] {};

        String[] lines = text.split("\\R");
        int[] values = new int[lines.length];

        for (int i = 0; i < lines.length; i++)
            values[i] = Integer.parseInt(lines[i].trim());

        return values;
    }

    /**
     * The obvious O(n^2) reference implementation: for each element of a, in order,
     * consume the first unused matching element of b. Same pair semantics as the
     * merge, no reliance on sortedness of b.
     */
    private static int[] quadraticIntersection(int[] a, int[] b) {
        boolean[] used = new boolean[b.length];
        int[] result = new int[Math.min(a.length, b.length)];
        int count = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                if (!used[j] && b[j] == a[i]) {
                    used[j] = true;
                    result[count++] = a[i];
                    break;
                }
            }
        }

        int[] trimmed = new int[count];
        System.arraycopy(result, 0, trimmed, 0, count);
        return trimmed;
    }
}