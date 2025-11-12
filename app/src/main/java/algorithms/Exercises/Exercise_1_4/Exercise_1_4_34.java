package algorithms.Exercises.Exercise_1_4;

/******************************************************************************
 * Exercise 1.4.34
 * 
 * <p>
 * <i>Hot or cold</i>. Your goal is to guess a secret integer between 1 and 𝑁.
 * You repeatedly guess integers between 1 and 𝑁. After each guess you learn if
 * your guess equals the secret integer (and the game stops). Otherwise, you
 * learn if the guess is hotter (closer to) or colder (farther from) the secret
 * number than your previous guess. Design an algorithm that finds the secret
 * number in at most ~2 lg 𝑁 guesses. Then design an algorithm that finds the
 * secret number in at most ~1 lg 𝑁 guesses.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_34
 * ./gradlew test --tests "Exercise_1_4_34Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_34 {
    enum Answer {
        HIT, HOTTER, COLDER, FIRST
    }

    static class Oracle {
        int lo;
        int hi;
        Integer previousGuess = null;
        int count;
        private final int secret;

        public Oracle(int lo, int hi, int secret) {
            if (lo > secret || hi < secret) {
                throw new IllegalArgumentException("The secret number must be between lo and hi.");
            }

            this.lo = lo;
            this.hi = hi;
            this.secret = secret;
        }

        public Answer guessSecret(int guess) {
            count++;

            if (guess == secret) {
                return Answer.HIT;
            }

            if (previousGuess == null) {
                previousGuess = guess;
                return Answer.FIRST;
            }

            if (Math.abs(guess - secret) < Math.abs(previousGuess - secret)) {
                previousGuess = guess;
                return Answer.HOTTER;
            } else {
                previousGuess = guess;
                return Answer.COLDER;
            }
        }
    }

    /**
     * Finds the secret with a pair of guesses per halving: the middle of the
     * interval, then the middle plus one. The answer to the second guess says which
     * half the secret is in; the answer to the first is ignored unless it is a hit.
     * Uses at most 2⌈lg 𝑁⌉ guesses.
     *
     * @param oracle the game to play; it holds the secret and the range
     * @return the secret
     */
    public static int findSecretWithPairs(Oracle oracle) {
        int lo = oracle.lo;
        int hi = oracle.hi;

        while (lo <= hi) {
            int midpoint = (lo + hi) / 2;
            Answer firstGuessAnswer = oracle.guessSecret(midpoint);

            if (firstGuessAnswer == Answer.HIT) {
                return midpoint;
            }

            Answer secondGuessAnswer = oracle.guessSecret(midpoint + 1);

            if (secondGuessAnswer == Answer.HIT) {
                return midpoint + 1;
            }

            if (secondGuessAnswer == Answer.HOTTER) {
                lo = midpoint + 1;
            } else {
                hi = midpoint;
            }
        }

        throw new IllegalStateException("The secret should have been found - the interval emptied without a hit");
    }

    /**
     * Finds the secret with one guess per halving. After a first guess at the
     * middle of the range, every guess is the mirror image of the previous guess
     * across the point being tested, so every answer halves the interval. Guesses
     * may fall outside the range. Uses about lg 𝑁 guesses.
     *
     * @param oracle the game to play; it holds the secret and the range
     * @return the secret
     */
    public static int findSecretByMirroring(Oracle oracle) {
        int lo = oracle.lo;
        int hi = oracle.hi;
        int firstGuess = (lo + hi) / 2;
        Answer firstGuessAnswer = oracle.guessSecret(firstGuess);
        int previousGuess = firstGuess;

        if (firstGuessAnswer == Answer.HIT) {
            return firstGuess;
        }

        while (lo <= hi) {
            int midpoint = (lo + hi) / 2;
            int guess = 2 * midpoint + 1 - previousGuess;

            if (lo == hi) {
                guess = lo;
            }

            Answer answer = oracle.guessSecret(guess);

            if (answer == Answer.HIT) {
                return guess;
            }

            boolean aboveAndHotter = guess > previousGuess && answer == Answer.HOTTER;
            boolean belowAndColder = guess < previousGuess && answer == Answer.COLDER;

            if (aboveAndHotter || belowAndColder) {
                lo = midpoint + 1;
            } else {
                hi = midpoint;
            }

            previousGuess = guess;
        }

        throw new IllegalStateException("The secret should have been found - the interval emptied without a hit");
    }

    /**
     * Plays the write-up's game, the secret 42 between 1 and 100, once with each
     * finder. Each game needs its own oracle, since an oracle remembers the guesses
     * of the game it has refereed.
     */
    public static void main(String[] args) {
        Oracle pairsGame = new Oracle(1, 100, 42);
        int foundWithPairs = findSecretWithPairs(pairsGame);
        System.out.println("Pairs: the secret is " + foundWithPairs + ", found in " + pairsGame.count + " guesses");

        Oracle mirrorGame = new Oracle(1, 100, 42);
        int foundByMirroring = findSecretByMirroring(mirrorGame);
        System.out.println(
                "Mirroring: the secret is " + foundByMirroring + ", found in " + mirrorGame.count + " guesses");
    }
}
