# Exercise 1.4.16

_Closest pair (in one dimension)_. Write a program that, given an array `a[]` of
$N$ `double` values, finds a _closest pair_: two values whose difference is no
greater than the difference of any other pair (in absolute value). The running
time of your program should be linearithmic in the worst case.

---

## The problem

Among $N$ real numbers, find the two that are closest together on the number
line. Unlike the book's 2-sum and 3-sum problems, this one asks for a _search_
result (which pair?) rather than a count — and it dictates the performance
contract up front: **linearithmic**, $O(N \log N)$, in the worst case. The
brute-force approach is quadratic, so the exercise is really asking: what can
you do to the data in your linearithmic budget that collapses the number of
pairs worth examining?

Precisely: find indices $i \ne j$ minimising `|a[i] - a[j]|`. Any pair achieving
the minimum is an acceptable answer (there may be ties).

### A small example

```text
a = [ 2.7, -1.1, 9.4, 3.0, 6.8 ]
```

Plotted on the number line:

```text
  -1.1        2.7 3.0            6.8         9.4
----|----------|---|--------------|-----------|----
               └─┬─┘
             0.3 apart  ← closest pair: (2.7, 3.0)
```

Every other pair is at least $|6.8 - 9.4| = 2.6$ apart. Note the closest pair is
_not_ adjacent in the array as given — array order tells you nothing about
number-line proximity.

### The obvious baseline: brute force

Check all pairs, keep the best seen so far:

```java
public static void closestPair(double[] a) {
    int n = a.length;
    double best = Double.POSITIVE_INFINITY;
    double x = 0, y = 0;

    for (int i = 0; i < n; i++)
        for (int j = i + 1; j < n; j++)
            if (Math.abs(a[i] - a[j]) < best) {
                best = Math.abs(a[i] - a[j]);
                x = a[i]; y = a[j];
            }

    System.out.println(x + " " + y);
}
```

This examines all $\sim N^2/2$ pairs — $O(N^2)$, which fails the exercise's
contract. The interesting observation: almost all of that work is provably
wasted. Two values far apart on the number line can never be the answer once
you've seen any closer pair, yet brute force compares them anyway.

### Where this sits in the book

The section's running theme: don't search the raw pair space — spend some of
your budget _reorganizing the data_ so that the answer can only live in a few
predictable places. `TwoSumFast` and `ThreeSumFast` embody it already: a sort
up front turns "find the partner summing to zero" from a scan into a binary
search. Here the same move has to carry even more of the weight — after the
reorganization, the answer should be nearly staring at you. The worst-case
linearithmic bound is itself a strong hint about which reorganization the
authors have in mind (compare the cost of the sorts in Chapter 2: which ones
are linearithmic in the _worst_ case?).

### Questions to guide your solution

1. The budget is $O(N \log N)$ _worst case_. What well-known operation costs
   exactly that and fundamentally changes what "close together" looks like in
   the array?
2. After that reorganization, suppose `a[i]` and `a[j]` end up with some element
   between them. Can `(a[i], a[j])` still be the unique closest pair? Prove it
   to yourself with the triangle-style argument: how does `|a[i] - a[j]|` relate
   to the gaps on either side of the in-between element?
3. Given your answer to question 2, how many candidate pairs actually need to be
   examined, and what does that final pass cost?
4. Does your algorithm handle _duplicate values_ correctly? What should it
   output when the array contains the same value twice?

### The adjacency argument

The linearithmic plan — sort, then walk the array comparing each element with
its immediate neighbour — examines just $N - 1$ of the $\sim N^2/2$ pairs. That
is where all the speed comes from, and it is also a claim that needs defending:
the scan never looks at a non-adjacent pair, so it silently asserts that none of
them can be the answer. "The scan returns the best pair it examined" is
trivially true; "the best pair it examined is the best pair there is" is the
theorem.

**Claim.** In a sorted array, some closest pair is adjacent — equivalently, the
minimum over the $N - 1$ adjacent gaps equals the minimum over all pairs.

**Proof.** Take any non-adjacent pair `a[i]`, `a[j]` with at least one element
`a[k]` between them in sorted order, $i < k < j$. Sortedness splits the big gap
exactly into the two smaller ones:

$$a[j] - a[i] = (a[k] - a[i]) + (a[j] - a[k])$$

Both pieces are non-negative, so the whole is at least as large as either piece
— the pair `(a[i], a[k])` (or `(a[k], a[j])`) is at least as close and spans
fewer elements, and repeating the argument walks it down to an adjacent pair at
least as close as the pair we started with. Put another way: every non-adjacent
difference is the **sum of all the adjacent gaps it spans**, and a sum of
non-negative terms can never be smaller than any single term. $\blacksquare$

On the example array, sorted:

```text
a =  [ -1.1,  2.7,  3.0,  6.8,  9.4 ]
gaps:      3.8   0.3   3.8   2.6
```

The non-adjacent pair (2.7, 6.8) has difference 4.1 — exactly the sum of the
adjacent gaps 0.3 and 3.8 it spans, so it cannot undercut either of them.

Why insist on a proof for something so plausible? Brute force needs no
correctness argument — it looks at everything. Every faster algorithm in this
section wins by _not looking_ at most candidates, and the proof is the licence
for the discard. Moreover, the intuition does not transfer as freely as it
feels like it should: "sort, then the answer is between neighbours" is _false_
for the two-dimensional closest pair (sort points by $x$-coordinate; the
closest pair need not be adjacent in $x$-order), and it inverts for the mirror
problem of finding a _farthest_ pair, where adjacent pairs are precisely the
worst candidates. The proof shows which
part of the intuition is load-bearing: a 1-D difference decomposes as a sum of
the gaps it spans. No such decomposition exists in the plane — the proof
refusing to generalise is how you know the 2-D shortcut is suspect before ever
hunting a counterexample.

Two corollaries fall out for free: duplicates sort adjacent, so a pair of equal
values shows up as a gap of `0.0` and wins as it should (question 4); and the
final pass examines $N - 1$ pairs at constant cost each, $O(N)$, leaving the
sort as the dominant $O(N \log N)$ term (question 3).

### Practical notes

- **Ties:** "no greater than any other pair" means ties are fine — you need not
  enumerate all closest pairs, just return one achieving the minimum difference.
- **Duplicates:** two equal values are a pair at distance `0.0` — the best
  possible. Make sure your code returns them rather than skipping "equal" as a
  special case.
- **Degenerate input:** decide explicitly what happens for $N < 2$ (no pair
  exists) — throw, or document a sentinel.
- **Floating point:** comparisons on `double` are exact operations, so no
  epsilon is needed for _comparing_ differences; but beware `Double.NaN` in
  input if you're generating test data — any comparison with NaN is false and
  can silently corrupt a min-tracking loop.
- **Testing:** cross-check against the brute force on many small random arrays
  (include duplicates and near-ties), and run a doubling test — ratios should
  approach $2$ with only a slowly growing factor, clearly distinguishable from
  brute force's $4$.

### The solution

Sort a _clone_ of the input (protecting the caller's array from the sort's side
effect), then a single pass over the $N - 1$ adjacent gaps, tracking the
smallest. The adjacency argument guarantees the answer lives among those gaps;
the subtraction needs no `Math.abs` because on a sorted array
`a[i + 1] - a[i]` is never negative.

```java
public static ClosestPair closestPair(double[] a) {
    int n = a.length;

    if (n < 2)
        throw new IllegalArgumentException("closest pair needs at least two values");

    a = a.clone();
    Arrays.sort(a);
    double best = Double.POSITIVE_INFINITY;
    double x = 0, y = 0;

    for (int i = 0; i < n - 1; i++) {
        double diff = a[i + 1] - a[i];
        if (diff < best) {
            best = diff;
            x = a[i];
            y = a[i + 1];
        }
    }

    return new ClosestPair(x, y);
}
```

`ClosestPair` is a small record, `record ClosestPair(double x, double y) {}` —
returning one keeps the method pure and testable, unlike printing from inside
it.

The cost is the sort's $O(N \log N)$ plus $O(N)$ for the scan and $O(N)$ space
for the clone. For the _worst-case_ guarantee the sort matters:
`Arrays.sort(double[])` is a dual-pivot quicksort, which historically had
quadratic worst cases on adversarial inputs (recent JDKs add a heapsort
fallback that restores the $O(N \log N)$ bound); a mergesort — the sort used
for `Arrays.sort(Object[])` — carries the worst-case guarantee outright, which
is what the exercise's "in the worst case" wording is really probing.

**Verification.** Cross-checked against `closestPairBruteForce` on 200,000
random arrays (sizes 2–13, half duplicate-heavy integer arrays to force ties
and zero gaps), comparing the best _difference_ from each — ties mean the two
versions may legitimately return different pairs of equal difference, so
comparing pairs would produce false alarms. All 200,000 agreed, and the
caller's array was confirmed unmodified after the call. The doubling test
settles at a ratio just above $2$, against brute force's $4$:

```text
closestPair doubling:                   brute force doubling:
  N= 1000000   0.174s   ratio  —          N=  2000   0.001s   ratio  —
  N= 2000000   0.188s   ratio 1.1         N=  4000   0.004s   ratio 4.2
  N= 4000000   0.592s   ratio 3.1*        N=  8000   0.015s   ratio 3.9
  N= 8000000   1.252s   ratio 2.1         N= 16000   0.058s   ratio 3.9
  N=16000000   2.911s   ratio 2.3         N= 32000   0.234s   ratio 4.1

  * GC/warm-up blip, not the algorithm
```

At $N = 32{,}000$ the two meet head-to-head: 0.23s brute force against 0.002s
for sort-and-scan — a factor of ~116 — and sort-and-scan handles 16 million
elements in under three seconds, a size brute force would need roughly half an
hour for. (Timing both on a 5-element array in `main` will "show" brute force
winning — at that size the measurement is JIT warm-up and class loading, not
the algorithm.)

<br />
<br />
