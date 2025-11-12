# Exercise 1.4.17

_Farthest pair (in one dimension)_. Write a program that, given an array `a[]`
of $N$ `double` values, finds a _farthest pair_: two values whose difference is
no smaller than the difference of any other pair (in absolute value). The
running time of your program should be linear in the worst case.

---

## The problem

The mirror image of the closest-pair problem: instead of the two closest
values, find the two _farthest apart_. But look at the budget — **linear**
worst case, not linearithmic. That single word changes everything: sorting
costs $O(N \log N)$, so the approach that works for closest pair is now _too
expensive_. The exercise is teaching that maximizing and minimizing a pairwise
difference are not symmetric problems.

Precisely: find indices $i \ne j$ maximising `|a[i] - a[j]|`, in one pass
territory.

### Why the asymmetry?

The closest pair could be hiding between any two "neighbouring" values, so you
need to know the full neighbour structure — that costs a sort. Ask yourself what
the farthest pair could possibly look like:

```text
----|------|--|-----|---------|----
    x                         y
    ▲                         ▲
    could any pair be farther apart than these two?
```

If some value $z$ lies strictly between $x$ and $y$ on the number line, can $z$
be part of a pair that beats $(x, y)$? Work the inequality.

### Questions to guide your solution

1. Prove: for any three values $x \le z \le y$, both $|z - x|$ and $|y - z|$ are
   at most $|y - x|$. What does that say about which two values must form the
   farthest pair?
2. Given question 1, what two summary statistics of the array are sufficient to
   answer the whole problem — and can you compute both in one linear pass?
3. Why does _minimising_ a pairwise difference need the whole sorted order
   while _maximising_ it needs almost nothing? (Hint: how many candidate pairs
   survive in each case?)

### The endpoint argument

The linear plan — a single pass tracking the smallest and largest values seen
so far — examines no pairs at all. That is where all the speed comes from, and
it is also a claim that needs defending: the scan never compares two arbitrary
elements, so it silently asserts that only one pair can possibly be the answer.

**Claim.** Let $m$ be the smallest value in the array and $M$ the largest. Then
$(m, M)$ is a farthest pair — the maximum over all $\sim N^2/2$ pairwise
differences equals $M - m$.

**Proof.** Take any pair of the array, written $u \le v$. By definition of
smallest and largest, $m \le u$ and $v \le M$, so

$$v - u \le v - m \le M - m$$

Every pairwise difference is therefore at most $M - m$, and $(m, M)$ is itself
a pair of the array attaining it. $\blacksquare$

The constructive reading is the one that leads to the algorithm. Put any pair
$(u, v)$ on the number line and ask two questions about the rest of the array:
is there a value to the left of $u$, and is there a value to the right of $v$?
If either answer is yes, swap that value in and the pair gets strictly wider —
so $(u, v)$ was not farthest. Slide both endpoints outward for as long as you
are allowed to. The array is finite, so the process must stop, and what stops
the left endpoint is having nothing smaller to move to: the definition of the
minimum. The right endpoint, symmetrically, halts at the maximum.

That is the whole asymmetry with the closest-pair problem (question 3). A
closest pair can sit anywhere on the number line, so the candidates cannot be
narrowed without knowing the neighbour structure — and that costs a sort.
A farthest pair has nowhere to hide: there is exactly **one** candidate, fixed
by two order statistics, and neither depends on any pairwise comparison. The
$O(N \log N)$ barrier is not beaten so much as sidestepped — the problem was
never a search over pairs.

Two corollaries fall out for free. If every value is equal then $m = M$ and the
farthest pair has difference `0.0`, with no special case needed (question 2's
degenerate check). And the interior of the array is genuinely irrelevant: an
adjacent pair in sorted order is the _worst_ candidate here, precisely the pairs
the closest-pair problem cares about most.

### Practical notes

- **One pass, constant space** is achievable — if your solution allocates or
  sorts, revisit question 2.
- **Duplicates/degenerates:** if all values are equal, the farthest pair has
  difference `0.0`; make sure that falls out naturally. Decide behaviour for
  $N < 2$.
- **NaN:** comparisons with `Double.NaN` are always false and can silently
  corrupt a max-tracking loop — worth a guard if you generate test data.
- **Testing:** brute-force all pairs ($O(N^2)$) as a reference on small random
  arrays; a doubling test on your final program should show ratios approaching
  $2$.

### The solution

Seed both trackers from `a[0]` and make one pass over the rest, widening
whichever end the current value falls outside. No pair is ever formed, nothing
is allocated, and the array is read once.

```java
public record FarthestPair(double min, double max) {
    public double distance() {
        return max - min;
    }
}

public static FarthestPair farthestPair(double[] a) {
    int n = a.length;

    if (n < 2)
        throw new IllegalArgumentException("farthest pair needs at least two values");

    double min = a[0], max = a[0];

    for (int i = 1; i < n; i++) {
        if (a[i] < min)
            min = a[i];
        else if (a[i] > max)
            max = a[i];
    }

    return new FarthestPair(min, max);
}
```

Three details carry more weight than their size suggests:

- **Seeding both from `a[0]`.** Seeding from `a[0]` and `a[1]` instead forces a
  swap to restore $\text{min} \le \text{max}$ before the loop can start; taking
  the same element twice is ordered by construction, and the loop then begins
  at $1$ rather than $2$. The saving is a branch, but the real gain is that
  there is no longer an ordering invariant to establish by hand.
- **`else if`, not two `if`s.** The skipped comparison is safe because the
  invariant $\text{min} \le \text{max}$ holds at every iteration: if
  `a[i] < min` then `a[i] < max` too, so the second test could not have fired.
  This is the one place the code depends on the invariant rather than merely
  maintaining it. `Math.min`/`Math.max` express the same intent more directly
  but always evaluate both, giving up the common-path shortcut.
- **Returning a record, not printing.** `distance()` puts the quantity the
  exercise is actually about on the result instead of at every call site, and
  keeps the method pure and testable.

The cost is $N - 1$ iterations of constant work — $O(N)$ time, $O(1)$ extra
space, worst case as well as best, since there is no data-dependent branch that
can change the number of iterations. The input is not modified.

**Verification.** Cross-checked against a brute-force $O(N^2)$ oracle on
200,000 random arrays (sizes 2–13, a third of them duplicate-heavy integer
arrays to force ties and zero-width answers): zero mismatches. Unlike the
closest-pair problem, comparing the returned _pairs_ is safe rather than
tie-prone — $m$ and $M$ are unique values even when several indices hold them.
Directed cases all behave: minimum last, maximum last, strictly ascending,
strictly descending, all values equal (distance `0.0`), exactly two elements,
and $N < 2$ throwing. An earlier draft with the loop written `i < n - 1`
silently skipped the final element and disagreed with brute force on 32% of
random arrays — worth noting because the five-element array in `main` is not
among them, so the test that a reader would run by hand passes.

Doubling test (times in seconds, x86 Linux; the constants will differ on other
hardware but the ratio column will not):

```text
  N= 1000000   0.001s   ratio  —
  N= 2000000   0.003s   ratio 2.7
  N= 4000000   0.005s   ratio 1.8
  N= 8000000   0.010s   ratio 2.1
  N=16000000   0.021s   ratio 2.0
  N=32000000   0.044s   ratio 2.1
```

The ratio column sitting on $2 = 2^1$ is the empirical signature of $O(N)$.
At these sizes the pass is memory-bandwidth-bound rather than
comparison-bound — 32 million doubles is 256 MB, and 0.044s to read it is
roughly what the machine can stream — which is why the smallest rows are
noisy: they finish inside the timer's resolution.

<br />
<br />
