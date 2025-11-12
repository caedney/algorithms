# Exercise 1.4.18

_Local minimum of an array_. Write a program that, given an array `a[]` of $N$
distinct integers, finds a _local minimum_: an index `i` such that
`a[i-1] > a[i] < a[i+1]`. Your program should use $\sim 2 \lg N$ compares in the
worst case.

_Answer_: Examine the middle value `a[N/2]` and its two neighbors `a[N/2 - 1]`
and `a[N/2 + 1]`. If `a[N/2]` is a local minimum, stop; otherwise search in the
half with the smaller neighbor.

---

## The problem

Find _some_ index that is smaller than both of its neighbours — not the global
minimum, just any dip. The array is **not sorted**, yet the budget is
$\sim 2 \lg N$ compares: binary-search territory on completely unordered data.
That is the surprise of this exercise — binary search doesn't need sorted input,
it needs an _invariant that lets you discard half the array safely_. The book
gives the algorithm away in the Answer; your work is to understand _why_ it's
correct and to implement it without off-by-one bugs.

### A small example

```text
a = [ 9, 6, 3, 14, 5, 7, 4 ]
            ▲      ▲    (▲)
            3      5 and 4 (boundary?) are dips
```

`a[2] = 3` is a local minimum (6 > 3 < 14); so is `a[4] = 5` (14 > 5 < 7). Any
one of them is a valid answer — the algorithm is free to return whichever it
converges to.

### Why does discarding half work?

The Answer says: if `a[mid]` is not a local minimum, recurse into the half
containing the smaller neighbour. The claim you should prove before coding:

> If `a[mid-1] < a[mid]`, then the left half `a[0..mid-1]` **must** contain a
> local minimum.

Reason it through as a "descending walk" argument: entering a region behind a
value smaller than its boundary, the values can't descend forever (the region is
finite), so somewhere they must turn upward — and the turning point is a dip.
Distinctness matters here: with ties, "strictly smaller" arguments break down.
Also settle the boundary convention: what happens at `i = 0` or `i = N-1`, where
only one neighbour exists?

### The boundary convention

$+\infty$ at the ends is a definition, not data — you never allocate or store
it. It is shorthand for one sentence: _a comparison against a neighbour that
doesn't exist counts as satisfied_. "`a[i]` is smaller than both its neighbours"
is a conjunction of two clauses, each _vacuously true_ when the neighbour is off
the end:

```java
boolean isLocalMin(int[] a, int i) {
    boolean leftOk  = (i == 0)            || a[i - 1] > a[i];
    boolean rightOk = (i == a.length - 1) || a[i + 1] > a[i];
    return leftOk && rightOk;
}
```

Writing `a[-1]` and `a[N]` as $+\infty$ says that in one line instead of two
guards, so the array can be reasoned about uniformly without special-casing the
ends. It is a _wall_, not a wrap: the ends do not join up, and `a[-1]` is not
`a[N-1]`. In code, resist reaching for `Integer.MAX_VALUE` as a literal sentinel
— a real element could equal it, and the index guards above are the honest
version.

Descending arrays are the case that makes the convention feel load-bearing. Take
`a = [3, 2, 1]`:

| `i` | `a[i]` | left      | right     | local minimum? |
| --- | ------ | --------- | --------- | -------------- |
| 0   | 3      | wall ✓    | `2 > 3` ✗ | no             |
| 1   | 2      | `3 > 2` ✓ | `1 > 2` ✗ | no             |
| 2   | 1      | `2 > 1` ✓ | wall ✓    | yes            |

No interior index qualifies, and the array still has a local minimum — at the
right-hand edge. Under the stricter reading, where a local minimum must have two
real neighbours, `[3, 2, 1]` would have none at all and the exercise would be
unsatisfiable. That also settles the `(boundary?)` mark in the example above:
`a[6] = 4` is a local minimum, on exactly the same grounds.

**Claim.** Every non-empty array of distinct integers has at least one local
minimum.

_**Proof**._ Let `m` be the index of the smallest value. The values are
distinct, so every other element is strictly greater than `a[m]` — including
whichever neighbours `a[m]` has. If `m` is interior, both clauses hold. If `m`
is at an end, its one real neighbour is greater and the other clause is vacuous.
Either way `a[m]` is a local minimum. $\blacksquare$

The image to carry into the invariant: the edges of the array are not escape
hatches. You can walk downhill from anywhere, but not forever — either you hit a
dip in the middle or you run out of array, and running out of array _is_ a dip.
That is the same descending walk as above, with the walls supplying the stopping
condition at the ends. It is also half of question 1 below: at `lo = 0` and
`hi = N-1`, the values "just outside the window" are the walls.

Note what the claim does _not_ give you. It locates a local minimum only by
locating the global minimum, which costs $N - 1$ compares. Existence is settled
cheaply; finding one within $\sim 2 \lg N$ is the actual exercise.

### Questions to guide your solution

1. Write out the invariant your loop maintains ("the subarray `[lo..hi]` always
   contains a local minimum because ..."). What must be true of the values just
   _outside_ the current window?
2. Each iteration costs how many compares (checking `a[mid]` against both
   neighbours)? Multiply by the number of halvings — do you land on
   $\sim 2 \lg N$?
3. Where exactly does the argument use the fact that the integers are
   _distinct_?

### Practical notes

- **Edge cases:** `N = 1` (index 0 is trivially a local min under the one-sided
  convention), `N = 2`, and strictly monotone arrays (the min is at an end).
- **Testing:** the checker is trivial — verify the returned index against its
  neighbours. Randomised testing with distinct values (shuffle `0..N-1`) is
  ideal.
- **Read the compare count off the loop** rather than instrumenting it. Count
  the comparisons on one iteration, multiply by the number of halvings. A
  counter means changing the algorithm in order to measure it, and timing cannot
  resolve a $\lg N$ cost at all — the whole search finishes in tens of compares,
  below the clock's resolution and swamped by the cost of building the array.

### The solution

The loop keeps a window `[lo..hi]` guaranteed to contain a local minimum, probes
its midpoint, and discards the half that cannot help. Three branches, taken in
order: if the left neighbour descends, go left; otherwise if the right neighbour
descends, go right; otherwise `mid` is itself a dip.

```java
public static boolean isLocalMin(int[] a, int i) {
    boolean leftOk = (i == 0) || a[i - 1] > a[i];
    boolean rightOk = (i == a.length - 1) || a[i + 1] > a[i];

    return leftOk && rightOk;
}

public static int findLocalMin(int[] a) {
    int lo = 0;
    int hi = a.length - 1;

    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;

        if (mid > 0 && a[mid - 1] < a[mid])
            hi = mid - 1;
        else if (mid < a.length - 1 && a[mid + 1] < a[mid])
            lo = mid + 1;
        else
            return mid;
    }

    throw new AssertionError("unreachable: every array has a local minimum");
}
```

The decisions that matter:

- **The new window excludes `mid`** — `hi = mid - 1`, not `hi = mid`. Keeping
  `mid` is not merely wasteful: the window then stops shrinking once `hi - lo`
  reaches 1, `lo = mid` becomes a no-op, and the loop spins forever. Excluding it
  also restores the wall invariant, since `a[mid] > a[mid-1]` makes `a[mid]` the
  new right wall.
- **The order of the three branches is the whole cost argument.** Each
  comparison is acted on before the next one is made, so a probe costs one
  compare when the left neighbour descends and two otherwise — never three.
  Calling `isLocalMin(a, mid)` first and then choosing a direction re-derives
  what those comparisons already settled, and pays a third for it.
- **The `mid > 0` and `mid < a.length - 1` guards are the wall convention in
  code.** A neighbour that does not exist cannot descend, so the guard
  short-circuits to false and the branch is skipped — exactly what comparing
  against $+\infty$ would do.
- **`isLocalMin` is no longer called by the algorithm**, but it is kept as the
  predicate the tests assert against. A local minimum is not unique, so a test
  must check the returned index against the definition rather than against an
  expected index; `{2, 3, 1}` has local minima at both 0 and 2, and either is a
  correct answer.
- **The book's own Answer costs more than the exercise asks for.** "Search in
  the half with the smaller neighbour" needs the two neighbours compared to each
  other, which the two comparisons above do not settle when `a[mid]` is a peak.
  Following it costs $\sim 3 \lg N$; going to _a_ descending side rather than the
  _smaller_ one is what buys the stated bound. Either is sound, because the
  descending-walk argument needs only that the side you enter descends.

**Cost.** Each iteration discards `mid` and one side, so the window is at most
halved: at most $\lg N$ halvings, plus a final probe on a window of size 1. At
most 2 compares per probe gives $\sim 2 \lg N$.

**Verification.** Checked against the definition over every permutation of
`0..N-1` for $N \le 10$ — 4,037,913 arrays — plus randomised shuffles up to
$N = 10^6$ and the monotone, V and inverted-V shapes. Every returned index
satisfies `isLocalMin`, and no run failed to terminate. Worst-case compares on a
strictly descending array, the shape that forces the maximum number of halvings:

```text
        N    2 lg N   compares   difference
     1024        20         21            -
     2048        22         23           +2
     4096        24         25           +2
     8192        26         27           +2
    16384        28         29           +2
    32768        30         31           +2
    65536        32         33           +2
```

Read the difference column, not a ratio. Doubling $N$ adds one iteration, so a
$\lg N$ algorithm gains a constant rather than multiplying by one; a constant $c$
there means $\sim c \lg N$ compares. The same table run against the book's
"smaller neighbour" rule reads +3.

Two bugs found along the way, both worth remembering:

- `hi = mid` in place of `hi = mid - 1` hung on 448 of the 40,320 eight-element
  permutations — 1.1% — while returning correctly on the `main` test array.
- Reading `a[mid - 1]` without the `mid > 0` guard threw
  `ArrayIndexOutOfBoundsException` on `{2, 1}`.

### Where distinctness earns its keep

Question 3, settled. Distinctness is what makes "not smaller" mean "strictly
greater", and the algorithm concludes from silence: when neither branch fires —
neither neighbour is smaller than `a[mid]` — it declares `a[mid]` a local
minimum. With ties that inference is wrong. On `{7, 7, 7, 7}` the search
terminates and returns index 1, yet `isLocalMin(a, 1)` is false, because
`a[0] > a[1]` fails on `7 > 7` — a quietly wrong answer rather than a crash.

The deeper failure is that ties kill the guarantee itself, not just the search:
in `{7, 7, 7, 7}` no index has both neighbours strictly greater, so a local
minimum in the strict sense does not exist and no algorithm could find one. The
existence claim's proof needs "every other element is strictly greater than the
minimum", and the descending walk needs every downhill step to make strict
progress — both are exactly the distinctness assumption.

<br />
<br />
