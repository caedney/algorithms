# Exercise 1.4.38

_Naive 3-sum implementation._ Run experiments to evaluate the following im.
plementation of the inner loop of `ThreeSum`:

```java
for (int i = 0; i < N; i++)
    for (int j = 0; j < N; j++)
        for (int k = 0; k < N; k++)
            if (i < j && j < k)
                if (a[i] + a[j] + a[k] == 0)
                    cnt++;
```

Do so by developing a version of `DoublingTest` that computes the ratio of the
running times of this program and `ThreeSum`.

---

## The problem

Two programs, same asymptotic order of growth, very different clocks. The book's
`ThreeSum` iterates `i < j < k` directly, examining N(N−1)(N−2)/6 ≈ N³/6
triples. The naive version above sweeps the full N³ cube of index combinations
and _filters_ with `if (i < j && j < k)` — same answer, same Θ(N³), but it
visits every triple 6 times' worth of index space (all orderings plus repeats)
and rejects most of it. This exercise makes a point the doubling test alone
can't: **order of growth is not the whole story — constants are real,
measurable, and sometimes a factor of six.**

### What to build

A modified `DoublingTest` that, for each N (doubling), times _both_ programs on
the same input array and prints the ratio naive/standard:

```text
N      T_naive   T_standard   ratio
250      ...        ...         ?
500      ...        ...         ?     ← does the ratio settle? at what value?
```

### Questions to guide your solution

1. Predict the ratio before running. Counting loop iterations says 6 (N³ vs
   N³/6). But the naive inner body is _cheaper_ for most iterations (the
   `i < j && j < k` test short-circuits before touching the array) — argue
   whether the measured ratio should land at 6, below it, or above it, and what
   it depends on (cost of a compare vs an array access vs a branch).
2. Each program alone should show a doubling ratio → 8 (cubic). Verify both do —
   that's the control confirming the two differ only in constant, not in order
   of growth.
3. The compiler/JIT could in principle hoist `a[i]`, reorder tests, or optimise
   the filter. Does the measured ratio drift as N grows (cache effects on the 6×
   larger index walk)? Report ratio vs N, not one number.
4. A middle variant is instructive: keep the full cube but test `i < j && j < k`
   _in the loop bounds_ of j and k instead (i.e. reconstruct the book's version
   step by step). Which restructuring buys the most?

### Practical notes

- Use the same input array for both timings at each N, and alternate which
  program runs first between trials — JIT warmup otherwise systematically
  favours whichever runs second.
- Naive at N is ~6× standard: budget runtimes so the largest N stays tolerable
  (if standard takes 10 s, naive takes a minute).
- Print the counts from both programs and assert equality — a benchmark
  comparing two programs that don't agree is measuring nothing.
- The stray "im. plementation" and doubled `</p>` are the source text's own
  typos — the code is as printed.

<br />
<br />
