package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_34.Answer;
import algorithms.Exercises.Exercise_1_4.Exercise_1_4_34.Oracle;

/******************************************************************************
 * Exercise_1_4_34Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_34Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_34Test {
    @Test
    public void oracleAnswersTheTraceGuesses() {
        // The first four guesses of the write-up's Part 1 trace, secret 42.
        // 25 is colder than 51 (17 away against 9): the answer Part 1 ignores.
        // A draft returned FIRST without remembering the guess, so every call
        // after it answered FIRST as well.
        Oracle oracle = new Oracle(1, 100, 42);

        assertEquals(Answer.FIRST, oracle.guessSecret(50));
        assertEquals(Answer.COLDER, oracle.guessSecret(51));
        assertEquals(Answer.COLDER, oracle.guessSecret(25));
        assertEquals(Answer.HOTTER, oracle.guessSecret(26));
    }

    @Test
    public void oracleReportsAHitOnAnyGuess() {
        assertEquals(Answer.HIT, new Oracle(1, 100, 42).guessSecret(42)); // first guess

        Oracle oracle = new Oracle(1, 100, 42);
        oracle.guessSecret(50);
        oracle.guessSecret(51);

        assertEquals(Answer.HIT, oracle.guessSecret(42)); // a later guess
    }

    @Test
    public void oracleMeasuresDistanceNotDirection() {
        // 30 is 12 below the secret and 50 is 8 above it, so 50 is hotter even
        // though it is on the other side. A draft compared secret - guess
        // without the absolute value, which only works from one side.
        Oracle oracle = new Oracle(1, 100, 42);
        oracle.guessSecret(30);

        assertEquals(Answer.HOTTER, oracle.guessSecret(50));
        assertEquals(Answer.COLDER, oracle.guessSecret(20));
        assertEquals(Answer.HOTTER, oracle.guessSecret(60));
    }

    @Test
    public void oracleComparesAgainstThePreviousGuessOnly() {
        // 41 is one away, then 10 is far away, then 20 is closer than 10 but
        // much farther than 41: hotter, because only the last guess counts.
        Oracle oracle = new Oracle(1, 100, 42);
        oracle.guessSecret(41);
        oracle.guessSecret(10);

        assertEquals(Answer.HOTTER, oracle.guessSecret(20));
    }

    @Test
    public void oracleCallsEquallyDistantGuessesColder() {
        // 40 and 44 are both two away. Hotter means strictly closer, so a tie
        // is colder. The finder's pairs have an odd sum and never tie.
        Oracle oracle = new Oracle(1, 100, 42);
        oracle.guessSecret(40);

        assertEquals(Answer.COLDER, oracle.guessSecret(44));
    }

    @Test
    public void oracleTreatsZeroAsARealGuess() {
        // A draft used previousGuess == 0 to mean "no guess yet", so a guess of
        // 0 made the next call answer FIRST again.
        Oracle oracle = new Oracle(1, 100, 42);

        assertEquals(Answer.FIRST, oracle.guessSecret(0));
        assertEquals(Answer.HOTTER, oracle.guessSecret(77)); // 35 away against 42
        assertEquals(Answer.COLDER, oracle.guessSecret(0));
        assertEquals(Answer.HOTTER, oracle.guessSecret(12));
    }

    @Test
    public void oracleRejectsASecretOutsideTheRange() {
        assertThrows(IllegalArgumentException.class, () -> new Oracle(1, 100, 0)); // below lo
        assertThrows(IllegalArgumentException.class, () -> new Oracle(1, 100, 101)); // above hi
        assertThrows(IllegalArgumentException.class, () -> new Oracle(26, 50, 25));
        assertThrows(IllegalArgumentException.class, () -> new Oracle(26, 50, 51));
    }

    @Test
    public void pairsFindTheTraceExample() {
        // N = 100, secret 42 is the write-up's Part 1 trace: five pairs, the
        // last of which hits on its second guess.
        RecordingOracle oracle = new RecordingOracle(1, 100, 42);

        assertEquals(42, Exercise_1_4_34.findSecretWithPairs(oracle));
        assertEquals(List.of(50, 51, 25, 26, 38, 39, 44, 45, 41, 42), oracle.guesses);
    }

    @Test
    public void pairsStopGuessingAsSoonAsAGuessHits() {
        // 50 is the first guess for N = 100, so the game is one guess long. A
        // draft made both guesses of the pair before looking at either answer
        // and charged this game two.
        RecordingOracle first = new RecordingOracle(1, 100, 50);

        assertEquals(50, Exercise_1_4_34.findSecretWithPairs(first));
        assertEquals(List.of(50), first.guesses);

        RecordingOracle second = new RecordingOracle(1, 100, 51);

        assertEquals(51, Exercise_1_4_34.findSecretWithPairs(second));
        assertEquals(List.of(50, 51), second.guesses);
    }

    @Test
    public void pairsFindTheOnlyNumberWhenNIsOne() {
        // The range is a single number, so the middle is that number and the
        // first guess of the only pass hits. The game is one guess long.
        RecordingOracle oracle = new RecordingOracle(1, 1, 1);

        assertEquals(1, Exercise_1_4_34.findSecretWithPairs(oracle));
        assertEquals(List.of(1), oracle.guesses);
    }

    @Test
    public void pairsFindBothNumbersWhenNIsTwo() {
        assertPairsFindSecret(1, 2, 1);
        assertPairsFindSecret(1, 2, 2);
    }

    @Test
    public void pairsFindTheEndsOfTheRange() {
        for (int N : new int[] { 3, 4, 7, 8, 100, 1000, 65_536 }) {
            assertPairsFindSecret(1, N, 1);
            assertPairsFindSecret(1, N, N);
        }
    }

    @Test
    public void pairsFindEverySecretForEverySmallN() {
        // Exhaustive: every secret for every N up to 300.
        for (int N = 1; N <= 300; N++)
            for (int secret = 1; secret <= N; secret++)
                assertPairsFindSecret(1, N, secret);
    }

    @Test
    public void pairsFindEverySecretInARangeThatDoesNotStartAtOne() {
        // The finder reads both ends from the oracle rather than assuming 1.
        for (int secret = 26; secret <= 50; secret++)
            assertPairsFindSecret(26, 50, secret);

        for (int secret = 1000; secret <= 1100; secret++)
            assertPairsFindSecret(1000, 1100, secret);
    }

    @Test
    public void pairsUseExactlyTwoLgNGuessesInTheWorstCaseForAPowerOfTwo() {
        // When N is a power of two every halving is exact, and some secret
        // survives all lg N of them to be hit by the very last guess.
        for (int N : new int[] { 2, 4, 8, 16, 64, 1024, 4096, 65_536 })
            assertEquals(2 * halvings(N), worstCaseWithPairs(N), "N = " + N);
    }

    @Test
    public void pairsStayWithinTwoGuessesPerHalvingForOtherSizes() {
        // Between powers of two the worst case falls one short of 2⌈lg N⌉. The
        // draft that made both guesses before checking the first reached the
        // bound itself: 14 for N = 100 rather than 13.
        int[][] expected = { { 3, 3 }, { 5, 5 }, { 9, 7 }, { 17, 9 }, { 100, 13 }, { 1000, 19 } };

        for (int[] pair : expected) {
            assertEquals(pair[1], worstCaseWithPairs(pair[0]), "N = " + pair[0]);
            assertTrue(pair[1] <= 2 * halvings(pair[0]), "N = " + pair[0]);
        }
    }

    @Test
    public void pairsHandleALargeN() {
        // N = 1,000,000,000: thirty halvings, so at most sixty guesses.
        int N = 1_000_000_000;

        for (int secret : new int[] { 1, 2, 314_159_265, 500_000_000, 500_000_001, N - 1, N })
            assertPairsFindSecret(1, N, secret);
    }

    @Test
    public void pairsFindRandomSecretsInRandomRanges() {
        Random random = new Random(34L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            int N = 1 + random.nextInt(1_000_000); // 1..1,000,000
            int secret = 1 + random.nextInt(N); // 1..N
            assertPairsFindSecret(1, N, secret);
        }
    }

    @Test
    public void mirrorFindsTheTraceExample() {
        // N = 100, secret 42 is the write-up's Part 2 trace: one setup guess,
        // seven halvings, and a last guess at the one number left. A draft
        // returned that number without guessing it and stopped at 69.
        RecordingOracle oracle = new RecordingOracle(1, 100, 42);

        assertEquals(42, Exercise_1_4_34.findSecretByMirroring(oracle));
        assertEquals(List.of(50, 51, 0, 77, 12, 71, 16, 69, 42), oracle.guesses);
    }

    @Test
    public void mirrorStopsGuessingAsSoonAsAGuessHits() {
        RecordingOracle first = new RecordingOracle(1, 100, 50); // the setup guess

        assertEquals(50, Exercise_1_4_34.findSecretByMirroring(first));
        assertEquals(List.of(50), first.guesses);

        RecordingOracle second = new RecordingOracle(1, 100, 51); // the first mirror

        assertEquals(51, Exercise_1_4_34.findSecretByMirroring(second));
        assertEquals(List.of(50, 51), second.guesses);

        RecordingOracle later = new RecordingOracle(1, 100, 77); // a mirror mid-game

        assertEquals(77, Exercise_1_4_34.findSecretByMirroring(later));
        assertEquals(List.of(50, 51, 100, 77), later.guesses);
    }

    @Test
    public void mirrorKeepsTheUpperHalfWhenALowerGuessIsColder() {
        // After 50 and 51 come back hotter the interval is [51, 100] and the
        // mirror goes above; after colder it is [1, 50] and the mirror, 0, goes
        // below. For a secret in [26, 50] that 0 is colder, which means the
        // upper half. A rule of "above and hotter" alone sends it to the lower.
        for (int secret = 26; secret <= 50; secret++)
            assertMirrorFindsSecret(1, 100, secret);
    }

    @Test
    public void mirrorFindsTheOnlyNumberWhenNIsOne() {
        RecordingOracle oracle = new RecordingOracle(1, 1, 1);

        assertEquals(1, Exercise_1_4_34.findSecretByMirroring(oracle));
        assertEquals(List.of(1), oracle.guesses);
    }

    @Test
    public void mirrorFindsBothNumbersWhenNIsTwo() {
        assertMirrorFindsSecret(1, 2, 1);
        assertMirrorFindsSecret(1, 2, 2);
    }

    @Test
    public void mirrorFindsTheEndsOfTheRange() {
        for (int N : new int[] { 3, 4, 7, 8, 100, 1000, 65_536 }) {
            assertMirrorFindsSecret(1, N, 1);
            assertMirrorFindsSecret(1, N, N);
        }
    }

    @Test
    public void mirrorFindsEverySecretForEverySmallN() {
        // Exhaustive: every secret for every N up to 300.
        for (int N = 1; N <= 300; N++)
            for (int secret = 1; secret <= N; secret++)
                assertMirrorFindsSecret(1, N, secret);
    }

    @Test
    public void mirrorFindsEverySecretInARangeThatDoesNotStartAtOne() {
        for (int secret = 26; secret <= 50; secret++)
            assertMirrorFindsSecret(26, 50, secret);

        for (int secret = 1000; secret <= 1100; secret++)
            assertMirrorFindsSecret(1000, 1100, secret);
    }

    @Test
    public void mirrorUsesLgNPlusTwoGuessesInTheWorstCaseForAPowerOfTwo() {
        // One setup guess, lg N halvings, one guess at the number left.
        for (int N : new int[] { 4, 8, 16, 64, 1024, 4096, 65_536 })
            assertEquals(halvings(N) + 2, worstCaseByMirroring(N), "N = " + N);
    }

    @Test
    public void mirrorStaysWithinOneGuessPerHalvingPlusTwoForOtherSizes() {
        int[][] expected = { { 2, 2 }, { 3, 4 }, { 5, 4 }, { 9, 6 }, { 17, 7 }, { 100, 9 }, { 1000, 12 } };

        for (int[] pair : expected) {
            assertEquals(pair[1], worstCaseByMirroring(pair[0]), "N = " + pair[0]);
            assertTrue(pair[1] <= halvings(pair[0]) + 2, "N = " + pair[0]);
        }
    }

    @Test
    public void mirrorGuessesOutsideTheRangeButNotFar() {
        // For N = 100 the mirror reaches 16 below the range and 16 above it,
        // inside the half-range the write-up allows for.
        int lowest = Integer.MAX_VALUE;
        int highest = Integer.MIN_VALUE;

        for (int secret = 1; secret <= 100; secret++) {
            RecordingOracle oracle = new RecordingOracle(1, 100, secret);
            Exercise_1_4_34.findSecretByMirroring(oracle);

            for (int guess : oracle.guesses) {
                lowest = Math.min(lowest, guess);
                highest = Math.max(highest, guess);
            }
        }

        assertEquals(-16, lowest);
        assertEquals(116, highest);
    }

    @Test
    public void mirrorHandlesALargeN() {
        // N = 1,000,000,000: thirty halvings, so at most thirty-two guesses.
        // Beyond about 1.07 billion 2 * midpoint no longer fits in an int.
        int N = 1_000_000_000;

        for (int secret : new int[] { 1, 2, 314_159_265, 500_000_000, 500_000_001, N - 1, N })
            assertMirrorFindsSecret(1, N, secret);
    }

    @Test
    public void mirrorFindsRandomSecretsInRandomRanges() {
        Random random = new Random(34L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            int N = 1 + random.nextInt(1_000_000); // 1..1,000,000
            int secret = 1 + random.nextInt(N); // 1..N
            assertMirrorFindsSecret(1, N, secret);
        }
    }

    @Test
    public void mirrorBeatsThePairsInTheWorstCase() {
        // The point of Part 2. The two are level up to N = 4; from 8 on the
        // mirror's worst game is shorter, and the gap grows with lg N.
        for (int N : new int[] { 8, 64, 100, 1000, 65_536 })
            assertTrue(worstCaseByMirroring(N) < worstCaseWithPairs(N), "N = " + N);
    }

    @Test
    public void everyDoublingAddsTwoGuessesToThePairsAndOneToTheMirror() {
        // The write-up's table, and on past it to N = 2^20: the worst game over
        // every secret is 2 lg N for the pairs and lg N + 2 for the mirror.
        for (int N = 4, lgN = 2; N <= 1 << 20; N *= 2, lgN++) {
            assertEquals(2 * lgN, worstCaseWithPairs(N), "pairs, N = " + N);
            assertEquals(lgN + 2, worstCaseByMirroring(N), "mirror, N = " + N);
        }
    }

    /**
     * Plays one game with the pairs finder and checks the right number, a game
     * that ends on a hit, every guess inside the range, and no more than two
     * guesses per halving.
     */
    private static void assertPairsFindSecret(int lo, int hi, int secret) {
        RecordingOracle oracle = new RecordingOracle(lo, hi, secret);
        int found = Exercise_1_4_34.findSecretWithPairs(oracle);
        String context = "secret " + secret + " in [" + lo + ", " + hi + "], guesses " + oracle.guesses;

        assertEquals(secret, found, "wrong number: " + context);
        assertEquals(secret, lastOf(oracle.guesses), "did not end on a hit: " + context);

        for (int guess : oracle.guesses)
            assertTrue(lo <= guess && guess <= hi, "guess " + guess + " out of range: " + context);

        int size = hi - lo + 1;
        int bound = Math.max(1, 2 * halvings(size)); // a range of one still takes one guess
        assertTrue(oracle.guesses.size() <= bound, "too many guesses: " + context);
    }

    /** The most guesses any secret from 1 to 𝑁 costs the pairs finder. */
    private static int worstCaseWithPairs(int N) {
        int worst = 0;

        for (int secret = 1; secret <= N; secret++) {
            RecordingOracle oracle = new RecordingOracle(1, N, secret);
            Exercise_1_4_34.findSecretWithPairs(oracle);
            worst = Math.max(worst, oracle.guesses.size());
        }

        return worst;
    }

    /**
     * Plays one game with the mirroring finder and checks the right number, a
     * game that ends on a hit, no guess further outside the range than half its
     * size, and no more than one guess per halving plus two.
     */
    private static void assertMirrorFindsSecret(int lo, int hi, int secret) {
        RecordingOracle oracle = new RecordingOracle(lo, hi, secret);
        int found = Exercise_1_4_34.findSecretByMirroring(oracle);
        String context = "secret " + secret + " in [" + lo + ", " + hi + "], guesses " + oracle.guesses;

        assertEquals(secret, found, "wrong number: " + context);
        assertEquals(secret, lastOf(oracle.guesses), "did not end on a hit: " + context);

        int size = hi - lo + 1;
        int stray = size / 2 + 1;

        for (int guess : oracle.guesses)
            assertTrue(lo - stray <= guess && guess <= hi + stray, "guess " + guess + " strayed too far: " + context);

        assertTrue(oracle.guesses.size() <= halvings(size) + 2, "too many guesses: " + context);
    }

    /** The most guesses any secret from 1 to 𝑁 costs the mirroring finder. */
    private static int worstCaseByMirroring(int N) {
        int worst = 0;

        for (int secret = 1; secret <= N; secret++) {
            RecordingOracle oracle = new RecordingOracle(1, N, secret);
            Exercise_1_4_34.findSecretByMirroring(oracle);
            worst = Math.max(worst, oracle.guesses.size());
        }

        return worst;
    }

    private static int lastOf(List<Integer> guesses) {
        return guesses.get(guesses.size() - 1);
    }

    /** ⌈lg 𝑁⌉ — the number of halvings that take 𝑁 numbers down to one. */
    private static int halvings(int N) {
        int k = 0;

        for (long size = 1; size < N; size *= 2)
            k++;

        return k;
    }

    /**
     * An oracle that also writes down every guess it is asked about, in order. It
     * calls a halt after 200 guesses, far more than any game here needs, so that a
     * finder stuck in its loop fails its test instead of running out of memory.
     */
    private static class RecordingOracle extends Oracle {
        final List<Integer> guesses = new ArrayList<>();

        RecordingOracle(int lo, int hi, int secret) {
            super(lo, hi, secret);
        }

        @Override
        public Answer guessSecret(int guess) {
            if (guesses.size() == 200) {
                throw new IllegalStateException("Runaway game: 200 guesses without a hit, last guess " + guess);
            }

            guesses.add(guess);
            return super.guessSecret(guess);
        }
    }
}
