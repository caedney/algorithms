package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_23.Fraction;
import algorithms.Exercises.Exercise_1_4.Exercise_1_4_23.Oracle;

/******************************************************************************
 * Exercise_1_4_23Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_23Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_23Test {
    @Test
    public void findsTheTraceExample() {
        // N = 10, target 3/7 is the write-up's trace: the narrowing ends at
        // [54/128, 55/128) after seven queries. A draft returned
        // new Fraction(lo, hi) — 54/55, the two numerators with the shared
        // denominator dropped — instead of identifying the candidate inside.
        Oracle oracle = new Oracle(10, new Fraction(3, 7));
        Fraction found = Exercise_1_4_23.findFraction(oracle);

        assertFraction(3, 7, found);
        assertEquals(7, oracle.count);
    }

    @Test
    public void reportsTheTargetInLowestTerms() {
        // The oracle only ever compares values, so 6/8 and 3/4 are the same
        // target; the ascending denominator loop finds 3/4 first.
        assertFraction(3, 4, Exercise_1_4_23.findFraction(new Oracle(10, new Fraction(6, 8))));
        assertFraction(1, 2, Exercise_1_4_23.findFraction(new Oracle(10, new Fraction(2, 4))));
        assertFraction(1, 3, Exercise_1_4_23.findFraction(new Oracle(10, new Fraction(3, 9))));
    }

    @Test
    public void findsTheExtremeTargets() {
        // The smallest and largest legal values, and the one exactly at a midpoint.
        assertFraction(1, 9, Exercise_1_4_23.findFraction(new Oracle(10, new Fraction(1, 9))));
        assertFraction(8, 9, Exercise_1_4_23.findFraction(new Oracle(10, new Fraction(8, 9))));
        assertFraction(1, 2, Exercise_1_4_23.findFraction(new Oracle(10, new Fraction(1, 2))));
    }

    @Test
    public void findsTheOnlyTargetWhenNIsThree() {
        // The smallest N with a legal target: 1/2 and nothing else.
        assertFraction(1, 2, Exercise_1_4_23.findFraction(new Oracle(3, new Fraction(1, 2))));
    }

    @Test
    public void rejectsIllegalTargets() {
        assertThrows(IllegalArgumentException.class, () -> new Oracle(10, new Fraction(0, 5))); // p = 0
        assertThrows(IllegalArgumentException.class, () -> new Oracle(10, new Fraction(5, 5))); // p = q
        assertThrows(IllegalArgumentException.class, () -> new Oracle(10, new Fraction(7, 3))); // p > q
        assertThrows(IllegalArgumentException.class, () -> new Oracle(10, new Fraction(3, 10))); // q = N
        assertThrows(IllegalArgumentException.class, () -> new Oracle(10, new Fraction(-1, 5))); // negative
    }

    @Test
    public void findsEveryTargetForEverySmallN() {
        // Exhaustive: every legal (p, q) for every N up to 40, including
        // reducible pairs, which must come back as the same value in lowest terms.
        for (int N = 3; N <= 40; N++)
            for (int q = 2; q < N; q++)
                for (int p = 1; p < q; p++)
                    assertFindsTarget(N, p, q);
    }

    @Test
    public void usesExactlyTwoLgNQueriesRoundedUp() {
        // Phase 1 is a fixed-length loop: the denominator doubles from 1 until it
        // reaches N², so the count is the same for every target at a given N.
        for (int N : new int[] { 3, 4, 10, 16, 17, 100, 1000, 65_536 }) {
            Oracle oracle = new Oracle(N, new Fraction(1, 2));
            Exercise_1_4_23.findFraction(oracle);
            assertEquals(expectedQueries(N), oracle.count, "N = " + N);
        }
    }

    @Test
    public void handlesALargeN() {
        // N = 1,000,000: the denominator reaches 2^40 and lo · q reaches ~10^18,
        // which is why the working variables and Fraction fields are long.
        int N = 1_000_000;
        Oracle oracle = new Oracle(N, new Fraction(314_159, 999_983));

        assertFraction(314_159, 999_983, Exercise_1_4_23.findFraction(oracle));
        assertEquals(40, oracle.count);
    }

    @Test
    public void agreesWithBruteForceOnRandomTargets() {
        Random random = new Random(23L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            int N = 3 + random.nextInt(498); // 3..500
            int q = 2 + random.nextInt(N - 2); // 2..N-1
            int p = 1 + random.nextInt(q - 1); // 1..q-1
            assertFindsTarget(N, p, q);
        }
    }

    /**
     * Runs the finder on the target p/q and checks the three things the exercise
     * asks for: the same value, a legal fraction, and a logarithmic query count.
     */
    private static void assertFindsTarget(int N, int p, int q) {
        Oracle oracle = new Oracle(N, new Fraction(p, q));
        Fraction found = Exercise_1_4_23.findFraction(oracle);
        String context = "target " + p + "/" + q + " with N = " + N + ", got " + found;

        assertEquals(p * found.q, found.p * q, "value mismatch: " + context);
        assertTrue(0 < found.p && found.p < found.q && found.q < N, "illegal result: " + context);
        assertEquals(1, gcd(found.p, found.q), "not in lowest terms: " + context);
        assertTrue(oracle.count <= expectedQueries(N), "too many queries: " + context);
    }

    private static void assertFraction(int p, int q, Fraction found) {
        assertEquals(p, found.p, "numerator of " + found);
        assertEquals(q, found.q, "denominator of " + found);
    }

    /** ⌈2 lg 𝑁⌉ — the number of doublings that take 1 up to at least 𝑁². */
    private static int expectedQueries(int N) {
        long limit = (long) N * N;
        int k = 0;

        for (long d = 1; d < limit; d *= 2)
            k++;

        return k;
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
