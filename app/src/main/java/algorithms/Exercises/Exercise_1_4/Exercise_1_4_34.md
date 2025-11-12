# Exercise 1.4.34

_Hot or cold_. Your goal is to guess a secret integer between $1$ and $N$. You
repeatedly guess integers between $1$ and $N$. After each guess you learn if
your guess equals the secret integer (and the game stops). Otherwise, you learn
if the guess is hotter (closer to) or colder (farther from) the secret number
than your previous guess. Design an algorithm that finds the secret number in at
most $\sim 2 \lg N$ guesses. Then design an algorithm that finds the secret
number in at most $\sim 1 \lg N$ guesses.

---

## The problem

Binary search with a degraded oracle. Ordinary guessing games tell you
_higher/lower_ — a direct comparison against the secret. Here you only learn
**hotter/colder**: whether your latest guess is closer to the secret than your
_previous_ guess. The feedback is relative to your own trajectory, not to the
number line. The exercise: first recover binary search's behaviour at a 2× guess
penalty ($\sim 2 \lg N$), then — much harder — eliminate it ($\sim 1 \lg N$).

### What does one hot/cold answer actually tell you?

Guesses $g_1$ then $g_2$: "hotter" means $|g_2 - s| < |g_1 - s|$. Geometrically,
the secret $s$ lies on $g_2$'s side of the **midpoint** $m = (g_1 + g_2)/2$:

```text
   g₁             m             g₂
----|-------------|-------------|----
    ← colder side | hotter side →         "hotter" ⇒ s > m (here)
```

So a _pair_ of guesses simulates one higher/lower comparison against a midpoint
of your choosing. That observation alone should hand you the $\sim 2 \lg N$
algorithm: which two guesses do you make to "query" any midpoint you like, and
how does the interval shrink?

### Questions to guide your solution

1. Make the $2 \lg N$ version precise: maintain an interval $[lo, hi]$ known to
   contain $s$; which pair $(g_1, g_2)$ tests the midpoint of the interval?
   Careful — the comparison is against the midpoint of _your two guesses_, not
   of the interval; place the guesses so those coincide.
2. Each pair halves the interval: confirm the count is $\sim 2 \lg N$. Where
   might you save a guess when a guess scores an exact hit?
3. For $\sim 1 \lg N$ you must extract a full bit from (almost) _every single
   guess_ — meaning each new guess must be interpreted against the previous one,
   with no "setup" guess wasted. After learning "$s > m$", your next guess must
   simultaneously (a) be your next probe and (b) form a useful midpoint with the
   _current_ guess. Try to arrange guesses so consecutive midpoints track the
   binary-search midpoints. What goes wrong naively, and does allowing guesses
   _outside_ $[1, N]$ (or thinking of the interval as folding/reflecting around
   known midpoints) help?
4. Information-theoretic floor: why can no strategy beat $\sim \lg N$ guesses?
   So the target of Part 2 is optimal to within lower-order terms.

### Avoiding ties

One answer places $s$ on one side of the midpoint $(g_1 + g_2)/2$ of the two
guesses. If that midpoint is an integer the secret can sit exactly on it,
equidistant from both guesses, and the oracle has to break a tie. The guesses
are yours to choose, so choose them so the midpoint is never an integer: make
$g_1 + g_2$ odd. The midpoint then falls at a half, "colder" means
$s \le
\lfloor m \rfloor$, "hotter" means $s \ge \lceil m \rceil$, and the two
outcomes partition the integers with nothing left on the fence.

The simplest such pair is two adjacent guesses, $g_2 = g_1 + 1$. The midpoint is
$g_1 + \tfrac12$: colder means $s \le g_1$, hotter means $s \ge g_2$. Place
$g_1$ at the centre of the interval and one pair is one step of binary search.

### Part 1: a pair per halving

Keep $[lo, hi]$, the interval known to contain $s$. It starts as $[1, N]$ and is
the algorithm's own bookkeeping — the oracle never mentions it. Each round
guesses $g_1 = \lfloor (lo + hi)/2 \rfloor$ then $g_2 = g_1 + 1$; colder shrinks
the interval to $[lo, g_1]$, hotter to $[g_2, hi]$.

The trace below plays $N = 100$ with the secret $s = 42$.

| Interval   | $g_1$ | $g_2$ | Answer | Interval after |
| ---------- | ----- | ----- | ------ | -------------- |
| $[1, 100]$ | $50$  | $51$  | colder | $[1, 50]$      |
| $[1, 50]$  | $25$  | $26$  | hotter | $[26, 50]$     |
| $[26, 50]$ | $38$  | $39$  | hotter | $[39, 50]$     |
| $[39, 50]$ | $44$  | $45$  | colder | $[39, 44]$     |
| $[39, 44]$ | $41$  | $42$  | hit    | —              |

Each pair halves the interval, so at most $\lceil \lg N \rceil$ pairs are
needed, and at two guesses a pair that is $\sim 2 \lg N$. The game never has to
work the secret out without guessing it: an interval of two numbers is itself
the next pair, so one of its two guesses hits. The one guess that can be saved
is the second of a pair, which is not made when the first hits. This settles
questions 1 and 2.

### Part 2: mirror the previous guess

The setup guess of each pair is the waste. When $25$ followed $51$ the oracle
compared the two and answered, and the answer was ignored. But the oracle
compares _every_ guess against the one before it, so every guess after the first
comes with a comparison for free — provided the guess is chosen relative to the
previous one rather than to the interval alone.

The previous guess $p$ is fixed. The next guess $g$ decides where the dividing
line $(p + g)/2$ falls, and to put it at the half-midpoint $m + \tfrac12$ of the
current interval, where $m = \lfloor (lo + hi)/2 \rfloor$, $g$ is forced:

$$
\begin{aligned}
\frac{p + g}{2} &= m + \tfrac12 \\
g &= 2m + 1 - p
\end{aligned}
$$

That is the mirror image of $p$ across the point being tested. The guess itself
need not be anywhere near the interval; only the midpoint it makes with $p$
matters. The half is only on paper: $m$, $p$ and $g$ are all whole numbers, and
the line tested is the gap between $m$ and $m + 1$.

Colder puts $s$ on $p$'s side of the line, hotter on $g$'s side. With the pairs
the second guess was always the higher of the two, so hotter always meant the
upper half. The mirror lands sometimes above the previous guess and sometimes
below it, so the answer alone no longer names a half; it takes the answer and
the side together.

| Guess against previous | Answer | Secret is in              |
| ---------------------- | ------ | ------------------------- |
| above                  | hotter | upper half, $[m + 1, hi]$ |
| below                  | colder | upper half, $[m + 1, hi]$ |
| above                  | colder | lower half, $[lo, m]$     |
| below                  | hotter | lower half, $[lo, m]$     |

The upper half is the two rows where the facts agree: above and hotter, or below
and colder. Everything else is the lower half.

The trace below plays $N = 100$ with $s = 42$ again: a first guess of $50$ at
the centre, then every guess the mirror of the one before.

| Interval   | Previous $p$ | Line tested | Guess $g$ | Answer | Interval after |
| ---------- | ------------ | ----------- | --------- | ------ | -------------- |
| $[1, 100]$ | $50$         | $50.5$      | $51$      | colder | $[1, 50]$      |
| $[1, 50]$  | $51$         | $25.5$      | $0$       | colder | $[26, 50]$     |
| $[26, 50]$ | $0$          | $38.5$      | $77$      | hotter | $[39, 50]$     |
| $[39, 50]$ | $77$         | $44.5$      | $12$      | hotter | $[39, 44]$     |
| $[39, 44]$ | $12$         | $41.5$      | $71$      | hotter | $[42, 44]$     |
| $[42, 44]$ | $71$         | $43.5$      | $16$      | hotter | $[42, 43]$     |
| $[42, 43]$ | $16$         | $42.5$      | $69$      | colder | $[42, 42]$     |
| $[42, 42]$ | $69$         | —           | $42$      | hit    | —              |

Nine guesses against $\lg 100 \approx 6.6$: one setup guess with no previous
guess to be compared against, $\lceil \lg 100 \rceil = 7$ halvings, and one
final guess to hit the number the interval has pinned down. In general
$\lg N +
2$, which is $\sim 1 \lg N$. This settles the first half of question 3:
consecutive midpoints track binary search's exactly.

### How far the mirror strays

The guess of $0$ in the trace lies outside $[1, 100]$. The oracle does not mind:
it measures $|0 - s|$ against $|51 - s|$ and reports which is smaller, and that
still says which side of $25.5$ the secret is on. So out-of-range guesses are
allowed here, and the only question is whether the mirror can wander off without
limit.

It cannot. By construction $g$ is the reflection of $p$ across the line being
tested, so $g$ is exactly as far from that line as $p$ was. After the answer the
interval halves and the next line to test is the centre of the surviving half,
which is a quarter of the current interval away from the line just tested — $25$
for $[1, 100]$, then about $12$, then $6$, each move half the one before. A
guess can therefore be at most a quarter-interval further from the next line
than its predecessor was from the last one, and over the whole game those moves
add up to

$$
\frac{N}{4} + \frac{N}{8} + \frac{N}{16} + \dots = \frac{N}{2}.
$$

Every guess is within about $N/2$ of a line inside $[1, N]$, so no guess leaves
roughly $[-N/2, 3N/2]$. The guesses in the trace bounce between the low teens
and the seventies while the interval closes in on $42$, never drifting further
out. This settles the rest of question 3: allowing guesses outside $[1, N]$ is
what makes the mirror rule work, and it costs nothing.

### Why nothing beats $\lg N$

Set the mirror rule aside and take any strategy at all. Record a game as a
transcript of the oracle's answers in order — `colder, hotter, hotter, colder`.
The strategy is fixed, so the transcript decides which number it ends up
guessing: two secrets that produce the same transcript make it guess the same
number, and it is wrong for one of them. For the strategy to find every one of
the $N$ secrets there must be at least $N$ different transcripts.

Each entry is one of two words, so there are $2$ transcripts of length $1$, $4$
of length $2$, $8$ of length $3$ — each extra answer doubles the count — and
$2^k$ of length $k$. For $N = 100$, $2^6 = 64$ is too few and $2^7 = 128$ is
enough, so every strategy needs at least $7 = \lceil \lg 100 \rceil$ answers. In
general $2^k \ge N$ means $k \ge \lg N$: there are not enough distinct answer
sequences to go round with fewer. The mirror rule uses $\lg N + 2$, so Part 2 is
optimal up to a constant. This settles question 4.

### Practical notes

- **Simulate the oracle** exactly as stated: it compares against your previous
  guess only, and equality ends the game. Sweep every secret $s \in [1, N]$ for
  a range of $N$ and assert the max guess count against both bounds — off-by-one
  drift between "guesses" and "comparisons" is very easy here.
- Ties in distance ($|g_2 - s| = |g_1 - s|$) need a convention — decide whether
  the oracle says "hotter" or "colder" for equidistant guesses and make the
  algorithm robust to either.
- Track your derivation with an explicit _known interval_ variable in the
  simulator and assert $s \in$ interval after every answer — it catches
  reasoning bugs immediately.

### The solution

An `Oracle` holds the secret and the range, remembers the previous guess, and
counts the guesses put to it. `guessSecret` answers with one of four words:
`HIT`, `HOTTER`, `COLDER`, or `FIRST` for the opening guess, which has nothing
to be compared against. Each finder receives the oracle, reads the range from
it, and otherwise only ever calls `guessSecret`. `findSecretWithPairs` is Part 1
and `findSecretByMirroring` is Part 2.

```java
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
```

The decisions that matter:

- **The oracle has memory, and "no guess yet" is not a number.** `previousGuess`
  is an `Integer` that starts as `null`. A draft used $0$ to mean no previous
  guess, but $0$ is a real guess of the mirror — the third in the trace — and
  after it the oracle would have answered `FIRST` again. The first-guess case
  also has to remember the guess before it returns; a draft that did not
  answered `FIRST` for ever.
- **Distance, not direction.** `Math.abs` on both sides, so it does not matter
  which side of the secret a guess is on. Hotter means strictly closer and a tie
  is colder. No answer a finder acts on can be a tie, since the two guesses it
  compares always have an odd sum.
- **`FIRST` is for the oracle's honesty.** The finders never test for it: each
  knows which of its guesses is the first, and ignores that answer unless it is
  a hit.
- **Check for a hit before guessing again.** The pairs finder looks at the first
  answer before making the second guess. A draft made both guesses and then
  looked, and for $N = 100$ charged $64$ of the $100$ games a guess made after
  the game was over.
- **The half never appears.** `midpoint` is an ordinary integer middle, the line
  tested is the gap above it, and the mirror `2 * midpoint + 1 - previousGuess`
  is whole numbers throughout.
- **Both rows of the upper half.** `aboveAndHotter || belowAndColder`. Either
  one alone loses the secret on the second row of the trace, where $0$ is below
  $51$ and colder.
- **Every game ends on a hit.** Both loops run while `lo <= hi`, so the last
  number is guessed, not inferred. In the pairs finder that needs no special
  case — when one number is left the middle is that number — and in the mirror
  it is the one place the guess is the number itself and not its reflection. The
  exception after each loop cannot be reached while the oracle tells the truth.
- **The finder never looks inside the oracle.** `Oracle` is nested, so `private`
  on `secret` would not stop a finder reading it; the rule is a stated
  convention, and the guess counts would give a peeking finder away.
- **`int` sets the ceiling.** `2 * midpoint` in the mirror overflows above about
  $1.07$ billion; a billion is fine.

**Cost.** The pairs finder uses at most $2\lceil \lg N \rceil$ guesses, and
exactly $2 \lg N$ in its worst game when $N$ is a power of two. The mirror uses
at most $\lceil \lg N \rceil + 2$: one setup guess with nothing to be compared
against, $\lceil \lg N \rceil$ halvings at one guess each, and one last guess at
the number left. Against the floor of $\lceil \lg N \rceil$ that is two over.

**Verification.** `Exercise_1_4_34Test` has $33$ tests: $7$ for the oracle, $11$
for the pairs, $14$ for the mirror, and one that measures both as $N$ doubles.
Both finders are checked guess for guess against the traces above, on every
secret for every $N$ from $1$ to $300$, on ranges that do not start at $1$, at
$N = 1{,}000{,}000{,}000$, and on $2{,}000$ random games with a fixed seed;
every game must return the secret, end on a hit and stay inside its guess
budget. The pairs must keep every guess in range. The mirror must stray no
further than half the range, and in practice strays about a sixth: its guesses
span $-16$ to $116$ for $N = 100$ and $-166{,}666$ to $1{,}166{,}666$ for
$N =
1{,}000{,}000$.

The tests were then run against nineteen broken versions, among them every draft
bug above, and each one fails: the both-guesses-first draft fails $9$, `FIRST`
not remembering the guess $25$, $0$ as no previous guess $9$, no absolute value
$21$, either row of the upper half alone $10$ and $12$, the mirror without its
$+1$ $13$, and a loop that stops at one number and returns it unguessed $11$.

`main` plays the trace's game, the secret $42$ between $1$ and $100$, with each
finder: $10$ guesses for the pairs and $9$ for the mirror. The table is the
worst game over every secret as $N$ doubles. Doubling $N$ adds two guesses to
the pairs and one to the mirror, and the tests check every doubling up to
$N =
2^{20}$, where the counts are $40$ and $22$:

|       $N$ | pairs | increment | mirror | increment |
| --------: | ----: | --------: | -----: | --------: |
|      $16$ |   $8$ |         — |    $6$ |         — |
|      $32$ |  $10$ |       $2$ |    $7$ |       $1$ |
|      $64$ |  $12$ |       $2$ |    $8$ |       $1$ |
|     $128$ |  $14$ |       $2$ |    $9$ |       $1$ |
|     $256$ |  $16$ |       $2$ |   $10$ |       $1$ |
|     $512$ |  $18$ |       $2$ |   $11$ |       $1$ |
| $1{,}024$ |  $20$ |       $2$ |   $12$ |       $1$ |

<br />
<br />
