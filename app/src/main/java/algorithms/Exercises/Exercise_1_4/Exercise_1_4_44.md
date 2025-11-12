# Exercise 1.4.44

_Birthday problem_. Write a program that takes an integer _N_ from the command
line and uses `StRandom.uniform()` to generate a random sequence of integers
between 0 and _N_ - 1. Run experiments to validate the hypothesis that the
number of integers generated before the first repeated value is found is
~√π*N*/2.

---

## The problem

The classic birthday paradox, recast for validation: draw uniform values from
{0, ..., N−1} and count how many draws happen before the first duplicate. The
claimed law is that this count grows like **√(πN/2)** ≈ 1.2533·√N — square-root
growth, the same surprising scale that makes 23 people enough for a shared
birthday among 365 days. Your job is an experiment sharp enough to distinguish
√(πN/2) from, say, plain √N (constants matter: it's a ~1.25 vs 1.0 coefficient
fight) — which forces good experimental habits: many trials, careful averaging,
and a log–log fit.

### The mechanics

One trial: generate values with `StdRandom.uniform(N)` (the text's "StRandom" is
a typo), remembering what you've seen, stopping at the first repeat; record the
count. The "have I seen this?" structure is your choice — a `boolean[N]` is the
simple exact tool here. Average the count over T independent trials for each N,
then sweep N over doublings.

```text
N = 365:  theory ≈ √(π·365/2) ≈ 23.9
N = 10⁶:  theory ≈ 1253.3
double N → expect the average to grow by √2 ≈ 1.414
```

### Questions to guide your solution

1. Pin the statistic: "number of integers generated before the first repeated
   value" — does the count include the colliding draw or not? The ~ law absorbs
   ±1 either way, but your validation is cleaner if the convention is explicit
   (the classical expectation including the collision is √(πN/2) + 2/3 + o(1) —
   the +2/3 is visible at small N!).
2. Experiment design: the trial count T controls the error bars — the
   distribution of the stopping time has standard deviation on the same √N scale
   as the mean, so the standard error of your average is ~√N/√T. How large must
   T be to resolve the 1.2533 coefficient to within, say, 1%?
3. Fit exponent and constant separately: on a log–log plot of average vs N, the
   slope should be 0.5; then divide averages by √N and check convergence to
   √(π/2) ≈ 1.2533. Doing it in two stages tells you _which_ part of the
   hypothesis fails if something's off.
4. Where does √(πN/2) come from? Sketch the derivation: P(first k draws all
   distinct) = Π(1 − i/N) ≈ exp(−k²/2N), so the stopping time's survival
   function is a Gaussian tail; summing survival probabilities gives the
   expectation as a Gaussian integral — the source of the π. Even if you skip
   rigor, connect exp(−k²/2N) to the ½ and π in the answer.

### Practical notes

- Reset the seen-structure between trials without reallocating (or accept the
  allocation — but then N·T array-clears dominate runtime at large N; a
  generation-stamp trick avoids it).
- Runtime per trial is ~√N draws, so even N = 10⁸ with thousands of trials is
  cheap — this experiment can afford large N and tight error bars; exploit that.
- Report mean, standard deviation, and trial count per N; the histogram of
  stopping times for one large N is worth plotting once — the Rayleigh-like
  shape _is_ the theory made visible.

<br />
<br />
