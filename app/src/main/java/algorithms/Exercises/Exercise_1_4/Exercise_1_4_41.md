# Exercise 1.4.41

_Running times_. Estimate the amount of time it would take to run `TwoSumFast`,
`TwoSum`, `ThreeSumFast` and `ThreeSum` on your computer to solve the problems
for a fie of 1 million numbers. Use `DoublingRatio` to do so.

---

## The problem

Extrapolation — the payoff skill of the whole doubling-test method. You will
_not_ run `ThreeSum` on a million numbers (spoiler: you'd be waiting years);
you'll measure it at sizes you can afford, confirm its growth law, and
**predict** the million-number time arithmetically. The four programs make the
exercise a table: two growth laws you've verified (quadratic, cubic) and two
linearithmic-ish ones, all scaled to the same target N = 10⁶.

### The method

For each program: run `DoublingRatio`-style timings until the ratio stabilises
(call it R, and T(N₀) the largest comfortable measurement). Then the model T(2N)
= R·T(N) extrapolates in doublings:

```text
T(10⁶) ≈ T(N₀) · R^(lg(10⁶/N₀))

e.g. ThreeSum: N₀ = 8000 takes t seconds, R ≈ 8
     10⁶/8000 = 125 ≈ 2^6.97 → multiply t by 8^6.97 ≈ 2×10⁶
```

Each program has its own R and its own largest-affordable N₀ — part of the
exercise is noticing that the _slow_ programs are the ones you must extrapolate
furthest, compounding any error in R.

### Questions to guide your solution

1. What are the expected ratios — 8 for `ThreeSum`, 4 for `TwoSum` and
   `ThreeSumFast` (~N² and ~N² lg-free? careful: `ThreeSumFast` is N² lg N —
   what does its _measured_ ratio look like, slightly above 4?), and just above
   2 for `TwoSumFast` (N lg N)? For the linearithmic case the ratio never quite
   settles — how do you extrapolate a slowly-drifting ratio honestly?
2. Error propagation: if R is off by 5% and you extrapolate 7 doublings, how far
   off is the prediction? Which of the four estimates is least trustworthy, and
   does the 1.4.39 multi-trial harness help?
3. Present results in human units. A million numbers: `TwoSumFast` in well under
   a second, `ThreeSum` in _years_ — convert and sanity-check each against the
   book's own table (page 191 tells you what to expect qualitatively).
4. Optional verification: `TwoSumFast` and `TwoSum` _can_ actually be run at 10⁶
   (quadratic ≈ minutes). Run them and score your predictions — nothing teaches
   error bars faster.

### Practical notes

- The book's `1Mints.txt` is the natural input for the verification runs; for
  the doubling measurements generate random ints in the same style.
- Choose N₀ per program so the largest timed run is seconds-to-a-minute: perhaps
  10⁶ for `TwoSumFast`, ~64k for `TwoSum`, ~16k for `ThreeSumFast`, ~4–8k for
  `ThreeSum`.
- Report the whole chain — N₀, T(N₀), R, doublings, prediction — not just the
  final number; the chain is the answer ("a fie of 1 million numbers" is the
  text's typo for _file_).

<br />
<br />
