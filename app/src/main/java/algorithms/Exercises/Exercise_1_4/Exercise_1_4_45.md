# Exercise 1.4.45

_Coupon collector problem_. Generating random integers as in the previous
exercise, run experiments to validate the hypothesis that the number of integers
generated before all possible values are generated is ~*N*H<sub>_N_</sub>.

---

## The problem

The birthday problem's companion: same random stream of values uniform on {0,
..., N−1}, opposite stopping rule. Instead of stopping at the first _repeat_
(which happens after ~√N draws), keep drawing until you've seen **every** value
at least once. The claim: that takes ~N·H<sub>N</sub> draws, where H<sub>N</sub>
= 1 + 1/2 + 1/3 + ... + 1/N is the harmonic number — so roughly N ln N + 0.5772N
(Euler's constant γ sneaking in). Together, 1.4.44 and this exercise bracket the
two natural "coverage" questions about random sampling, and both answers — √N
and N ln N — are quantities this chapter has taught you to recognise on sight.

### The mechanics

One trial: draw with `StdRandom.uniform(N)`, track which values have appeared
and how many distinct ones so far, stop when the distinct count hits N; record
total draws. A `boolean[N]` plus a counter is all it takes:

```text
N = 10:    H₁₀ ≈ 2.929   →  theory ≈ 29.3 draws
N = 1000:  H₁₀₀₀ ≈ 7.485 →  theory ≈ 7485 draws
```

Intuition for the law: when k values remain unseen, each draw hits a new one
with probability k/N, so the wait for the next new value is ~N/k draws; summing
N/k for k = N down to 1 gives N·H<sub>N</sub>. That one-line argument is also
your best guide to _where the time goes_ — the last few coupons cost the most.

### Questions to guide your solution

1. Validation against ~N·H<sub>N</sub> is more delicate than a power law: on a
   log–log plot, N log N looks _almost_ like slope-1. Design the test to
   discriminate: divide measured averages by N — the quotient should track
   H<sub>N</sub> ≈ ln N + γ, i.e. be _linear in ln N_. Fit that line; its slope
   should be 1 and intercept ≈ 0.5772.
2. Compute H<sub>N</sub> exactly (a loop) rather than via ln N + γ for the
   comparison at small N — at N = 10 the approximation error is visible. At what
   N do the two agree to within your error bars?
3. Variance: the coupon collector's stopping time has standard deviation
   ~(π/√6)·N ≈ 1.28N — _linear_ in N, so relative spread shrinks like 1/ln N:
   slowly! How many trials do you need at each N for meaningful averages,
   compared with 1.4.44's needs?
4. Instrument the _phases_: record how many draws were spent collecting the
   first half of the values vs the last single value. Does the data match the
   N/k waiting-time picture (last coupon alone costs ~N draws on average)?

### Practical notes

- Runtime per trial is ~N ln N draws — still cheap up to N = 10⁶ or so with
  plenty of trials; reuse the trial harness and the reset-without-reallocate
  trick from 1.4.44.
- Guard the degenerate cases N = 1 (one draw suffices) and confirm your
  convention for "number generated before all values are generated" includes the
  final completing draw (the ~ hypothesis absorbs the ±1; your plots are cleaner
  with a fixed convention).
- Plot mean draws/N against ln N — a straight line with slope 1 and intercept γ
  is a genuinely satisfying picture to end Section 1.4's exercises on, with both
  constants of the chapter's analysis (leading coefficient and lower-order term)
  visible in one graph.

<br />
<br />
