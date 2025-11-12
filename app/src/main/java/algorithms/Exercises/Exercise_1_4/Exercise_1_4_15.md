# Exercise 1.4.15

_Faster 3-sum_. As a warmup, develop an implementation `TwoSumFaster` that uses
a _linear_ algorithm to count the pairs that sum to zero after the array is
sorted (instead of the binary-search-based linearithmic algorithm). Then apply a
similar idea to develop a quadratic algorithm for the 3-sum problem.

---

## The problem

Count the pairs (and then the triples) in an array that sum to zero, faster than
the book's binary-search-based algorithms: linear time for 2-sum once the array
is sorted, and quadratic time for 3-sum. The improvement comes from exploiting
sortedness more fully than one-lookup-at-a-time binary search does.

This is a two-part exercise, and the parts are deliberately ordered:

**Part 1 — `TwoSumFaster`.** Count the pairs that sum to zero:

```text
count pairs (i, j) with i < j
such that a[i] + a[j] == 0
```

The array is sorted first (you may treat the sort as given — the exercise
measures what happens _after_ sorting). The book's `TwoSumFast` already solves
this in linearithmic time $O(N \log N)$; your job is to do the counting pass in
linear time $O(N)$ — a single sweep, no binary searches.

**Part 2 — `ThreeSumFaster`.** Take whatever idea made Part 1 linear and lift it
one level up:

```text
count triples (i, j, k) with i < j < k
such that a[i] + a[j] + a[k] == 0
```

The book's best so far is `ThreeSumFast` at $O(N^2 \log N)$. The target here is
$O(N^2)$ — shaving the $\log N$ factor off, again by replacing binary search
with something that exploits sortedness more fully.

### Why the warmup matters

The exercise structure is a hint in itself. The book asks for the 2-sum version
first, and phrases Part 2 as "a similar idea" rather than a new one — so
whatever you discover in Part 1 is meant to be reusable, more or less wholesale,
in Part 2. That is worth knowing before you write Part 1, because it means the
shape of the routine matters as much as its running time. Get the warmup right,
and correct on awkward inputs, before touching 3-sum.

### What exactly are you counting?

Be precise about this before writing anything, because it decides whether an
implementation is _wrong_ or merely answering a different question.

- A **pair** is a pair of _indices_ $i < j$ with `a[i] + a[j] == 0`.
- A **triple** is a triple of _indices_ $i < j < k$ with
  `a[i] + a[j] + a[k] == 0`.

The index ordering is what stops the same combination being counted more than
once, and stops an element pairing with itself.

Note that the book's `TwoSumFast` and `ThreeSumFast` **quietly assume the values
are distinct**. Their `rank(...) > i` and `rank(...) > j` guards are
de-duplication tricks that only work under that assumption; as counts over a
multiset they give the wrong answer. So decide up front which you are
implementing:

1. correct counting of index tuples over a multiset — harder, and what a
   brute-force cross-check will hold you to; or
2. the book's distinct-keys assumption — easier, but then your test data has to
   respect it.

Also: sorting permutes the array, so the indices you end up counting are not the
caller's original indices. That is fine for a _count_ — the number of zero-sum
tuples is invariant under permutation — but satisfy yourself that this is true
rather than assuming it, and note that it rules out reporting actual positions.

### What does sortedness buy you?

`TwoSumFast` uses sortedness only to make _one lookup_ fast: for each `a[i]`,
binary search for `-a[i]` in $O(\log N)$. That treats each of the $N$ queries as
independent, paying $\log N$ every time — hence $O(N \log N)$.

But a zero-sum pair has a special shape in a sorted array. If `a[i] + a[j] == 0`
with `a[i] ≤ a[j]`, then:

```text
sorted:  [ negatives ...... 0s ...... positives ]
             ▲                            ▲
         a[i] lives here             a[j] lives here
```

one element is $\le 0$ and sits toward the **left** end, its partner is $\ge 0$
and sits toward the **right** end, and the more negative the left element, the
larger its partner must be. The pairs are not scattered randomly — sortedness
imposes a _global geometry_ on where they can be. The linear algorithm comes
from exploiting that geometry across queries, instead of re-searching from
scratch each time.

### A small example to trace

```text
sorted a = [ -8, -5, -2, 0, 0, 0, 1, 3, 5, 8, 9, 10, 11 ]
              i0  i1  i2 i3 i4 i5 i6 i7 i8 i9 i10 i11 i12
```

$N = 13$. Counting index tuples, the answers are:

- **Zero-sum pairs: 5.** `(-8, 8)` and `(-5, 5)` give one each; the run of three
  zeros gives $\binom{3}{2} = 3$ more, since any two of them pair to zero.

  $$
  \binom{n}{k} = \frac{n!}{k!\,(n-k)!}
  \qquad\Rightarrow\qquad
  \binom{3}{2} = \frac{3!}{2!\,1!} = \frac{6}{2 \cdot 1} = \frac{6}{2} = 3
  $$

  $$
  \binom{3}{2} = \frac{3!}{2!\,1!}
  = \frac{3 \cdot 2 \cdot \cancel{1}}{2! \cdot \cancel{1}}
  = \frac{3 \cdot 2}{2!} = \frac{6}{2} = 3
  $$

- **Zero-sum triples: 9.** `(-8, 8, 0)` and `(-5, 5, 0)` each pick up a factor
  of 3 for the choice of zero, giving 6; `(0, 0, 0)` gives 1; and `(-8, 3, 5)`
  and `(-8, -2, 10)` give 2 more.

Keep those two numbers — they are your first sanity check. The run of three
zeros is deliberate: it is exactly the structure a naive implementation gets
wrong.

#### A partial trace

Suppose you begin by testing `-8` against the largest element:

```text
-8 + 11 =  3   > 0   →  11 can be discarded    still in play: -8 ... 10
-8 + 10 =  2   > 0   →  10 can be discarded    still in play: -8 ...  9
-8 +  9 =  1   > 0   →   9 can be discarded    still in play: -8 ...  8
-8 +  8 =  0         →  ?                      still in play: ?
...
```

Two things to take from that. First, discarding `11` is not a step taken _on the
way_ to reaching `8` — it is a permanent proof that `11` belongs to no zero-sum
pair at all, since its best available partner was the smallest remaining element
and even that overshot. Second, although the trace began by fixing the smallest
element, every move so far has come off the _top_ of the array. Whatever this
process is, it is not a traversal with a starting end.

Finish the trace by hand, all the way down, and see whether your final count of
zero-sum pairs comes to 5 — the answer established above.

### Where this sits in the book

| Version              | Approach                                      | Order of growth       |
| -------------------- | --------------------------------------------- | --------------------- |
| `TwoSum`             | brute-force double loop                       | $O(N^2)$              |
| `TwoSumFast`         | sort, then binary search for `-a[i]`          | $O(N \log N)$         |
| **`TwoSumFaster`**   | _this exercise_                               | $O(N)$ after the sort |
| `ThreeSum`           | brute-force triple loop                       | $O(N^3)$              |
| `ThreeSumFast`       | sort, then binary search for `-(a[i] + a[j])` | $O(N^2 \log N)$       |
| **`ThreeSumFaster`** | _this exercise_                               | $O(N^2)$              |

Note the pattern running through this section: the innermost operation is where
all the time goes, and every improvement works by making that innermost step
cheaper — from a loop, to a binary search, and now to something cheaper still.

### The baselines

The cubic version, which doubles as your correctness oracle:

```java
public static int count(int[] a) {
    int n = a.length;
    int count = 0;

    for (int i = 0; i < n; i++)
        for (int j = i + 1; j < n; j++)
            for (int k = j + 1; k < n; k++)
                if (a[i] + a[j] + a[k] == 0)
                    count++;

    return count;
}
```

And the binary-search version you are being asked to beat:

```java
public static int count(int[] a) {
    Arrays.sort(a);
    int n = a.length;
    int count = 0;

    for (int i = 0; i < n; i++)
        for (int j = i + 1; j < n; j++)
            if (BinarySearch.rank(-(a[i] + a[j]), a) > j)
                count++;

    return count;
}
```

### Questions to guide your solution

For the warmup:

1. Take the smallest element. Of all the partners available to it, which single
   one gives it the best possible shot at reaching zero? You know this from the
   ordering alone, without looking at any values.
2. Suppose that best shot still comes out negative. Complete the sentence: _"the
   smallest element cannot appear in any zero-sum pair, because **\_\_**."_
3. When you discard it, how many candidate pairs have you eliminated — one, or
   more? A single comparison ought to be buying you a great deal.
4. Now the mirror image: take the largest element. Which partner gives it its
   best shot at coming _down_ to zero, and what follows if it is still positive?
5. Questions 2 and 4 each retire one element. Look at the arithmetic you did in
   each — are those two different sums, or the same sum read two ways? Does the
   sign of that one sum tell you which of the two rules applies, or is there a
   choice to be made?
6. At any moment, can the set of elements still under consideration be described
   as a single contiguous region? If so, how many numbers do you need to track
   in order to describe it?
7. You start with $N$ elements in play and each step retires one. After $k$
   steps, how many remain? What is the stopping condition? Therefore how many
   steps at most — and how much work happens inside one step? Multiply the last
   two together and you have your order of growth.
8. Questions 2 and 4 cover the sum being negative and positive. There is a third
   case, and it is the only one that actually counts anything. What do you do
   then — and, critically, how much can you retire?

For the step up to 3-sum:

9. Fix one element. What must the other two now sum to? Is it still zero?
10. Look at the routine you wrote for the warmup. Does it _care_ that its target
    is zero, or would any target do? Does it care that it is scanning the whole
    array, or would any subrange do? If either answer is "it cares", that is a
    signature to loosen now rather than later.
11. If you fix `a[i]` and search only within `a[i+1..N-1]`, have you already
    guaranteed $i < j < k$? Do you still need any of the de-duplication guards
    that `ThreeSumFast` uses?
12. Count the cost: how many choices of fixed element, and what does each one
    cost you? Write out the resulting order of growth and confirm the
    $O(N \log N)$ sort disappears underneath it.

### The case that breaks a naive implementation

Once you think you have the warmup, hand-run it over:

```text
[ -2, -2, 0, 2, 2, 2 ]
```

Counting index pairs $i < j$, the correct answer is **6**. Most first attempts
return 1 or 2, because on finding an exact zero-sum they retire both ends and
move on — which silently assumes there was only one element at each end.

So: when you land on an exact zero-sum and there is a run of equal values at
_both_ ends of the region still in play, how many pairs does that single
discovery represent? And how much of the array can you retire in one go? The
answer involves the two run lengths, and there is a special case when the two
runs turn out to be the _same_ run — which is precisely what happens with the
three zeros in the example above.

### The match case in detail: runs, `L`, and `R`

The two sign cases retire one element each. The match case is different: finding
`a[lo] + a[hi] == 0` may represent _many_ pairs at once, because either value
may occur more than once. Trace it on the array that breaks naive
implementations:

```text
a = [ -2, -2, 0, 2, 2, 2 ]
       i0  i1 i2 i3 i4 i5
```

The sweep arrives at `lo = 0`, `hi = 5` with `a[0] + a[5] = -2 + 2 = 0` — a
match. First check whether `a[lo] == a[hi]`: here `-2 != 2`, so this is the
**two-distinct-runs** case.

`L` answers the question _"how many consecutive copies of `a[lo]` are there,
starting at `lo`?"_ Sortedness guarantees equal values are adjacent, so this is
a simple walk to the right:

```java
int L = 1;                          // a[lo] itself is the first copy
while (a[lo + L] == a[lo]) L++;
```

Tracing: `a[1] == -2` → `L = 2`; `a[2] == 0` → stop. $L = 2$. `R` is the mirror
image, walking left from `hi`: `a[4] == 2` → `R = 2`; `a[3] == 2` → `R = 3`;
`a[2] == 0` → stop. $R = 3$.

What the match has actually discovered is not a pair but a **grid**:

```text
        a[3]=2   a[4]=2   a[5]=2
a[0]=-2   ✓        ✓        ✓
a[1]=-2   ✓        ✓        ✓
```

Every copy of `-2` pairs with every copy of `2`, each ✓ a distinct index pair
$(i, j)$ with $i < j$, and there are $L \times R = 2 \times 3 = 6$ of them. One
comparison banked all six:

```java
count += (long) L * R;
```

Then both runs are retired whole: `lo += L` (0 → 2) and `hi -= R` (5 → 2). Every
pair involving any member of either run is already in the count, so letting `lo`
stop partway through the run would count pairs twice. The region left in play is
the single `0` at `i2`, which has no possible partner; `lo < hi` fails and the
sweep ends on **6** — the correct answer for this array, produced entirely by
this one branch.

Note that the naive match handling — `count++; lo++; hi--` — is exactly this
code in the special case $L = 1, R = 1$. The runs version is not a different
idea, just the honest generalisation of it.

#### The same-run case

The grid logic assumed the two runs are distinct. When `a[lo] == a[hi]` at a
match, both values must be zero (if $x + x = 0$ then $x = 0$), and since the
array is sorted, _everything_ between `lo` and `hi` is zero too: the two runs
have merged into one run of length $m = hi - lo + 1$. Here $L \times R$ would
double-count (and pair elements with themselves); the correct count is the
number of ways to choose 2 indices from the run:

$$\binom{m}{2} = \frac{m(m-1)}{2}$$

and since the run is the entire remaining region, the sweep is finished:

```java
long m = hi - lo + 1;
count += m * (m - 1) / 2;
break;                              // nothing left in play
```

#### Does the walking break linearity?

The `while` loops look like extra loops inside the loop. They aren't a problem:
each element of the array is walked over at most once across the whole sweep,
because `lo += L` and `hi -= R` jump past everything just examined and the
pointers never revisit ground. The total work is still $O(N)$.

### Practical notes

- **The sort is allowed.** Sorting costs $O(N \log N)$, which appears to break
  the "linear" claim for Part 1 — but the exercise explicitly scopes the linear
  requirement to the counting _after_ the array is sorted. For Part 2 the
  $O(N \log N)$ sort is absorbed by the $O(N^2)$ total anyway.
- **Overflow.** `a[i] + a[j]` is the sum of two `int`s that may sit near the
  extremes of the range, and `a[i] + a[j] + a[k]` all the more so. Form the sum
  as a `long` before comparing it to zero.
- **Do not literally remove anything.** If "discard this element" means building
  a new array or shifting elements down, that step costs linear time on its own,
  and doing it $N$ times lands you right back at quadratic. The elimination has
  to be $O(1)$. What is the cheapest possible way to express "this element is
  out of play"?
- **Test against brute force, with the right data.** Random `int`s over the full
  range almost never collide, so a test suite built on them will happily pass an
  implementation that is broken on duplicates. Generate values in a narrow range
  — say $-3$ to $3$ with $N$ around 20 — so that repeats are unavoidable, and
  cross-check against the cubic reference over a few thousand trials. Use the
  book's brute-force `TwoSum` / `ThreeSum` as the oracle, **not** `TwoSumFast` /
  `ThreeSumFast`, which are only correct under the distinct-keys assumption.
- **Order of work.** Write the brute-force oracle first, then the generator,
  then `TwoSumFaster`. Get the pair count agreeing before going near triples: a
  subtly wrong two-sum sweep produces a 3-sum that is wrong in a way that is
  genuinely unpleasant to localise.
- **Doubling tests.** On `1Kints.txt`, `2Kints.txt`, `4Kints.txt`, `8Kints.txt`,
  the new 3-sum should settle towards a ratio of 4. Do not expect the timings
  alone to separate $O(N^2)$ from `ThreeSumFast`'s $O(N^2 \log N)$ cleanly — the
  latter's ratio is $4 \cdot \frac{\log 2N}{\log N}$, which at these sizes is
  only a few percent above 4. The convincing evidence is not the ratio but the
  reach: a correct implementation should let you run a couple of file sizes past
  the point where `ThreeSumFast` became impractical.

---

## The solution

Everything above converges on one routine: an inward two-pointer sweep over a
sorted region, counting the pairs that sum to a given target. `TwoSumFaster` is
that sweep with the target hardwired to zero over the whole array;
`ThreeSumFaster` is the same sweep called $N$ times with a shifting target and a
shrinking range.

### `TwoSumFaster`

```java
public static long count(int[] a) {
    Arrays.sort(a);
    int lo = 0;
    int hi = a.length - 1;
    long count = 0;

    while (lo < hi) {
        long sum = (long) a[lo] + a[hi];

        if (sum < 0) {
            lo++;
        } else if (sum > 0) {
            hi--;
        } else if (a[lo] == a[hi]) {
            // Equal ends means the whole remaining region is one run of zeros:
            // count all pairs within it, and nothing is left in play.
            long m = hi - lo + 1;
            count += m * (m - 1) / 2;
            break;
        } else {
            // Two distinct runs: measure each, bank the grid, retire both.
            int L = 1;
            int R = 1;

            while (a[lo + L] == a[lo])
                L++;

            while (a[hi - R] == a[hi])
                R++;

            count += (long) L * R;
            lo += L;
            hi -= R;
        }
    }

    return count;
}
```

The correctness argument, in one paragraph: `lo` and `hi` bound the region still
in play, and the invariant is that every uncounted zero-sum pair lies within it.
If the sum overshoots zero, `a[hi]` was paired with the smallest element
available to it and still overshot, so `a[hi]` belongs to no remaining pair and
is retired; the mirror argument retires `a[lo]` on an undershoot. On a match,
the runs at each end are measured and every cross pair is banked at once
($L \times R$, or $\binom{m}{2}$ when the two runs are one). Every comparison
retires at least one element permanently, so the sweep is $O(N)$; each element
is walked over at most once, so the inner run-measuring loops do not change
that. The sums are formed in `long` before comparison because
`Integer.MIN_VALUE + Integer.MIN_VALUE` wraps to exactly $0$ in `int` arithmetic
— an overflow that manufactures phantom pairs.

### `ThreeSumFaster`

The lift to 3-sum is the substitution the guiding questions pointed at: the two
literal zeros in the comparisons were the target all along. Parameterise them,
and fix each element in turn:

```java
public static long count(int[] a) {
    Arrays.sort(a);
    long count = 0;

    for (int i = 0; i < a.length - 2; i++)
        count += countPairs(a, -(long) a[i], i + 1, a.length - 1);

    return count;
}

private static long countPairs(int[] a, long target, int lo, int hi) {
    long count = 0;

    while (lo < hi) {
        long sum = (long) a[lo] + a[hi];

        if (sum < target) {
            lo++;
        } else if (sum > target) {
            hi--;
        } else if (a[lo] == a[hi]) {
            // Equal ends means the whole remaining region is one run of
            // identical values: count all pairs within it, done.
            long m = hi - lo + 1;
            count += m * (m - 1) / 2;
            break;
        } else {
            // Two distinct runs: measure each, bank the grid, retire both.
            int L = 1;
            int R = 1;

            while (a[lo + L] == a[lo])
                L++;

            while (a[hi - R] == a[hi])
                R++;

            count += (long) L * R;
            lo += L;
            hi -= R;
        }
    }

    return count;
}
```

The design decisions that matter:

- **The target.** Fixing `a[i]` turns `a[i] + a[j] + a[k] == 0` into
  `a[j] + a[k] == -a[i]`. The negation is `-(long) a[i]` — widen _then_ negate,
  because `-Integer.MIN_VALUE` overflows in `int`.
- **The range `i+1 .. N-1`** is the whole de-duplication story. Each index
  triple is counted exactly once, by the iteration that fixes its smallest
  index; no element pairs with itself; the `rank > j` guards `ThreeSumFast`
  needed (and which break on duplicates) have no counterpart here. Repeated
  values in `a[i]` need no special handling — equal fixed elements sweep
  different subranges, so they count different triples.
- **Sort once, outside the loop.** The sweep itself must not sort, or the total
  becomes $N^2 \log N$ and the exercise is lost.
- **The equal-ends case generalises quietly.** With a nonzero target the run at
  both ends is not zeros but the value $x$ with $2x = \text{target}$; the
  squeeze argument (sorted, equal ends ⇒ everything between is equal) and the
  $\binom{m}{2}$ count survive unchanged.

Cost: iteration $i$ sweeps $N - 1 - i$ elements, so the total is
$\sum_{i} (N - 1 - i) \sim N^2/2$, with the single $O(N \log N)$ sort absorbed
beneath it. `TwoSumFaster` is the special case `countPairs(a, 0, 0, N-1)` after
a sort — kept as a self-contained class in the book's style, at the price of a
little duplication.

### Verification

- **Worked examples.** `[ -8, -5, -2, 0, 0, 0, 1, 3, 5, 8, 9, 10, 11 ]`: 5
  pairs, 9 triples. `[-2, -2, 0, 2, 2, 2]`: 6 pairs, 6 triples. Both match the
  hand counts, exercising the grid, the same-run binomial, and the
  nonzero-target match.
- **Brute force.** 20,000 random duplicate-heavy arrays (values in $[-3, 3]$,
  $N \le 20$) against the cubic oracle: zero mismatches, for both the pair sweep
  and the triple count.
- **The book's data.** On `1Mints.txt`, `TwoSumFaster` counts **249,838**
  zero-sum pairs — confirmed by an independent value-tally
  ($\sum_{v>0} c_v \, c_{-v}$, plus $\binom{c_0}{2}$). The file turns out to
  contain 1,000,000 _distinct_ values and no zeros, which is why `TwoSumFast`
  agrees on it: the book's data is manufactured to honour the book's
  distinct-keys assumption. On data sampled _with_ replacement from the same
  range, `TwoSumFast` undercounts by tens of thousands while the sweep stays
  correct.

### Measured order of growth

Doubling test (`OrderOfGrowth ThreeSumFaster`, times in seconds, Apple Silicon):

```text
   16000     0.4   4.0
   32000     1.4   4.0
   64000     5.8   4.0
  128000    23.5   4.1
  256000    94.6   4.0
```

The ratio column sitting on $4 = 2^2$ is the empirical signature of $O(N^2)$ —
and $N = 256{,}000$ is far beyond where the cubic `ThreeSum` (ratio 8, minutes
at $N = 16{,}000$) stopped being usable. Rows below $N \approx 16{,}000$ are
omitted: their times are too small for the clock and the JIT is still warming
up.

Two instrument-reading lessons the timings taught along the way:

- **Scale the value range with $N$.** With the harness's range fixed at
  $\pm 10^6$, arrays beyond $\sim 2 \times 10^6$ elements are flooded with
  duplicates,
  which changes the cost being measured — `TwoSumFast`'s binary searches start
  terminating early on runs of equal keys and its doubling ratio sinks to 2
  rather than drifting above it. The input distribution is part of the
  experiment.
- **A one-off ratio dip is the JIT, not the algorithm.** A doubling table that
  shows a single anomalous ratio (e.g. one 1.9 in a column of 8.0s for
  `ThreeSum`), with the rows on either side each internally consistent but on
  constants $\sim 4\times$ apart, is the just-in-time compiler replacing the
  loop with a better-optimised version mid-run: the exponent is preserved, only
  the constant drops. Timing the same $N$ twice in one JVM makes the seam
  visible.

<br />
<br />
