# Exercise 1.4.42

_Problem sizes_. Estimate the size of the largest value of _P_ for which you can
run `TwoSumFast`, `TwoSum`, `ThreeSumFast`, and `ThreeSum` on your computer to
solve the problems for a file of 2*ᵖ* thousand numbers. Use `DoublingRatio` to
do so.

---

## The problem

The inverse of 1.4.41. There you fixed the input size and predicted the time;
here you fix a time budget ("can run" — pick something concrete, say an hour,
and state it) and predict the largest input each program can digest: N = 2^P
thousand numbers. Same doubling data, opposite direction of the arithmetic — and
a more vivid punchline, because the answer exposes just how differently the four
growth laws convert _hardware patience into problem size_.

### The method

From the doubling measurements (stable ratio R, anchor time T(N₀)), each
doubling of N multiplies time by R. With budget T_max:

```text
affordable doublings  d = log_R ( T_max / T(N₀) )
largest N ≈ N₀ · 2^d      →  express as 2^P thousand, extract P
```

The punchline to anticipate: an extra order of magnitude of budget buys
`TwoSumFast` almost an order of magnitude of N, but buys `ThreeSum` only
10^(1/3) ≈ 2.15× — the cube root. Frame your answers around that.

### Questions to guide your solution

1. Define "can run" precisely before computing — an hour? a day? P shifts by
   only ~1–2 per order of magnitude of budget for the slow programs (why? relate
   ΔP to log R), so the estimates are surprisingly robust to the choice. Show
   that robustness explicitly.
2. For `ThreeSumFast` (order N² lg N) and `TwoSumFast` (N lg N), the ratio
   drifts rather than settles. Does treating them as clean powers (4 and 2) bias
   P up or down, and by roughly how much over many doublings?
3. Memory is the silent second constraint: 2^P thousand ints occupy 4·2^P KB,
   and the JVM heap has limits — at what P does `TwoSumFast` stop being
   time-limited and start being memory-limited on your machine? ("Can run"
   includes fitting.)
4. Cross-check with 1.4.41: your predicted T(10⁶) figures and your (T_max, P)
   answers must be mutually consistent — 10⁶ numbers is P ≈ 10. Do the two
   exercises agree about which programs handle it?

### Practical notes

- Reuse the exact anchors (N₀, T(N₀), R) from 1.4.41 — this exercise should cost
  you arithmetic, not new machine-hours.
- Present a table: program | growth | R | T_max = 1 min | 1 hour | 1 day → P for
  each budget; the column-to-column deltas _are_ the lesson (constant for each
  program, and equal to lg of the budget ratio over lg R).
- Verify the cheapest claim: actually run each program at its predicted 1-minute
  P and time it — a factor-of-2 miss is fine, a factor-of-30 miss means R or the
  anchor was wrong.

<br />
<br />
