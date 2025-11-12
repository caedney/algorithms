# Exercise 1.4.34

_Hot or cold_. Your goal is to guess a secret integer between 1 and _N_. You
repeatedly guess integers between 1 and _N_. After each guess you learn if your
guess equals the secret integer (and the game stops). Otherwise, you learn if
the guess is hotter (closer to) or colder (farther from the secret number than
your previous guess. Design an algorithm that finds the secret number in at most
~2 lg _N_ guesses. Then design an algorithm that finds the secret number in at
most ~1 lg _N_ guesses.

---

## The problem

Binary search with a degraded oracle. Ordinary guessing games tell you
_higher/lower_ — a direct comparison against the secret. Here you only learn
**hotter/colder**: whether your latest guess is closer to the secret than your
_previous_ guess. The feedback is relative to your own trajectory, not to the
number line. The exercise: first recover binary search's behaviour at a 2× guess
penalty (~2 lg N), then — much harder — eliminate the penalty (~1 lg N).

### What does one hot/cold answer actually tell you?

Guesses g₁ then g₂: "hotter" means |g₂ − s| < |g₁ − s|. Geometrically, the
secret s lies on g₂'s side of the **midpoint** m = (g₁+g₂)/2:

```text
   g₁            m             g₂
----|------------|-------------|----
    ← colder side| hotter side →         "hotter" ⇒ s > m (here)
```

So a _pair_ of guesses simulates one higher/lower comparison against a midpoint
of your choosing. That observation alone should hand you the ~2 lg N algorithm:
which two guesses do you make to "query" any midpoint you like, and how does the
interval shrink?

### Questions to guide your solution

1. Make the 2 lg N version precise: maintain an interval [lo, hi] known to
   contain s; which pair (g₁, g₂) tests the midpoint of the interval? Careful —
   the comparison is against the midpoint of _your two guesses_, not of the
   interval; place the guesses so those coincide.
2. Each pair halves the interval: confirm the count is ~2 lg N. Where might you
   save a guess when a guess scores an exact hit?
3. For ~1 lg N you must extract a full bit from (almost) _every single guess_ —
   meaning each new guess must be interpreted against the previous one, with no
   "setup" guess wasted. After learning "s > m", your next guess must
   simultaneously (a) be your next probe and (b) form a useful midpoint with the
   _current_ guess. Try to arrange guesses so consecutive midpoints track the
   binary-search midpoints. What goes wrong naively, and does allowing guesses
   _outside_ [1, N] (or thinking of the interval as folding/reflecting around
   known midpoints) help?
4. Information-theoretic floor: why can no strategy beat ~lg N guesses? So the
   target of Part 2 is optimal to within lower-order terms.

### Practical notes

- **Simulate the oracle** exactly as stated: it compares against your previous
  guess only, and equality ends the game. Sweep every secret s ∈ [1, N] for a
  range of N and assert the max guess count against both bounds — off-by-one
  drift between "guesses" and "comparisons" is very easy here.
- Ties in distance (|g₂ − s| = |g₁ − s|) need a convention — decide whether the
  oracle says "hotter" or "colder" for equidistant guesses and make the
  algorithm robust to either.
- Track your derivation with an explicit _known interval_ variable in the
  simulator and assert s ∈ interval after every answer — it catches reasoning
  bugs immediately.

<br />
<br />
