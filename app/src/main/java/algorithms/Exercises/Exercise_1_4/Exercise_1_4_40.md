# Exercise 1.4.40

_3-sum for random values_. Formulate and validate a hypothesis describing the
number of triples of _N_ random `int` values that sum to 0. If you are skilled
in mathematical analysis, develop an appropriate mathematical model for this
problem, where the values are uniformly distributed between -_M_ and _M_, where
_M_ is not small.

---

## The problem

Every 3-sum exercise so far asked how long _counting_ takes; this one asks about
the **answer itself**: for random inputs, how many zero-sum triples should you
_expect_? It's the chapter's scientific method (Section 1.4's
hypothesis–experiment loop) applied to a combinatorial quantity instead of a
running time — formulate a model, predict a formula in N and M, then design
experiments whose data confirms or refutes it.

### Building intuition for the model

A triple (a, b, c) sums to zero when c = −(a + b). With values uniform on the
2M+1 integers in [−M, M]:

- There are C(N, 3) ≈ N³/6 triples to test.
- For a _fixed_ pair (a, b), the third value must hit one exact number — and a
  uniform value hits a given target with probability 1/(2M+1), _provided the
  target is in range_. When is −(a+b) outside [−M, M], and how often does that
  happen for random a, b? (This is where "M is not small" and the shape of the
  sum-of-two-uniforms distribution — a triangle on [−2M, 2M] — enter.)

Chaining those gives a first-cut hypothesis of the form

```text
E[count] ≈ C(N,3) · P(a + b + c = 0) ≈ c · N³ / M     (for some constant c)
```

Your analysis task is the constant and its justification; the probability P is a
sum over the triangle distribution, and a clean route is: P(a+b+c=0) = Σₜ P(a+b
= −t)·P(c = t) — a convolution evaluated at 0, i.e. the probability that three
independent uniforms convolve to zero.

### Questions to guide your solution

1. Derive P(a + b = s) exactly for s in [−2M, 2M] (the triangle), then compute Σ
   P(a+b = −t)/(2M+1) over t in [−M, M]. What constant multiplies N³/M in the
   end? (Sanity target: the sum over the middle half of a triangle — expect
   something like 3/4 · 1/(2M+1)-ish mass — derive it properly.)
2. Independence caution: the C(N,3) triples share elements, so the count is a
   sum of _dependent_ indicators. Why is linearity of expectation immune to that
   — and what (variance, concentration) is _not_?
3. Experiment design: with the formula E ≈ cN³/M, two clean validations exist —
   double N at fixed M (expect 8×) and double M at fixed N (expect ½×). Run both
   sweeps with repetitions (1.4.39's harness) and compare fitted exponents.
4. Duplicates: with values in a bounded range, repeated values occur (birthday
   effect once N ≈ √M). Does your triple-counting convention (index triples
   i<j<k, as `ThreeSum` counts) versus value triples change the model? Which
   does the book's code measure?

### Practical notes

- Use `ThreeSum.count()` on arrays you generate with
  `StdRandom.uniform(-M, M+1)`; keep M large relative to N² if you want
  duplicate effects negligible, then deliberately violate that to see the model
  bend.
- Averages over many random arrays are essential — single-array counts have
  large relative spread at small E; report mean ± sd.
- Log–log plots of count vs N (slope 3) and count vs M (slope −1) make the
  hypothesis test visual and immediate.
- Overflow reminder: the _count_ fits comfortably, but a+b+c of extreme ints
  doesn't — the book's `ThreeSum` uses int sums; with M near 2³¹ that itself
  corrupts the experiment. Keep M modest or sum in `long`.

<br />
<br />
