# Exercise 1.4.21

_Binary search on distinct values_. Develop an implementation of binary search
for `StaticSETofInts` (see page 98) where the running time of `contains()` is
guaranteed to be $\sim \lg R$, where $R$ is the number of different integers in
the array given as argument to the constructor.

---

## The problem

`StaticSETofInts` (the whitelist class from Section 1.1) answers `contains(key)`
with binary search over a sorted copy of the constructor's array. If the input
has lots of _duplicate_ values, the sorted array is longer than it needs to be:
$N$ entries but only $R$ distinct values, $R \le N$. Standard binary search
costs $\sim \lg N$; the exercise asks you to guarantee $\sim \lg R$ — the cost
should depend on the amount of _information_ in the set, not the amount of raw
input.

This is a preprocessing exercise, not a search exercise: `contains()` should
remain a bog-standard binary search. All the work happens once, in the
constructor.

### A small example

```text
constructor input (N = 12):
a = [ 5, 9, 5, 5, 2, 9, 5, 2, 5, 9, 2, 5 ]

distinct values (R = 3):  { 2, 5, 9 }
```

Binary search over 12 sorted entries costs $\sim \lg 12 \approx 3.6$ compares;
over 3 distinct entries, $\sim \lg 3 \approx 1.6$. Same answers to every
possible query.

### Questions to guide your solution

1. After the constructor sorts its copy, where do the duplicates sit? What
   single linear pass transforms the sorted array into what `contains()`
   actually needs?
2. Does the standard `rank()`/binary-search code need _any_ change once the
   constructor's output is right? (It shouldn't — that's the elegance.)
3. What is the constructor's total cost, and why is spending it acceptable under
   this class's usage model (build once, query many times — the whitelist
   scenario)?
4. Bonus: the deduplication idea assumes equality is cheap to detect in sorted
   order. Why do equal values always end up adjacent after sorting?

### Runs in a sorted array

**Claim.** After sorting, every occurrence of a given value is contiguous: if
`a[i] == a[k]` and $i < j < k$, then `a[j]` holds the same value.

_Proof._ Sorted order gives `a[i] <= a[j] <= a[k]`. The two ends are equal, so
the middle is squeezed: `a[j]` can be neither smaller than `a[i]` nor larger
than `a[k]`, and both are the same value $v$, so `a[j] == v`. $\blacksquare$

So sorting does more than order the values — it gathers each value into a single
unbroken _run_, and there are exactly $R$ runs. That turns "is this value new?"
from a lookup into a local test: `a[i]` is the first of its run precisely when
it differs from its left neighbour,

```java
i == 0 || a[i] != a[i - 1]
```

with `i == 0` covering the one position that has no left neighbour (and `||`
short-circuiting so `a[-1]` is never evaluated). Comparing with the _right_
neighbour also works — it picks out the last of each run instead — but then the
special case moves to `i == a.length - 1`, where it is easier to get wrong.

One left-to-right pass therefore flags exactly $R$ positions in $\sim N$
compares. Two ways to collect them:

- **Count, then copy.** Run the test once to learn $R$, allocate `new int[R]`,
  run it again writing each flagged element into the next free slot. Two passes,
  nothing overwritten.
- **Compact in place.** Keep a write cursor $r$ that starts at 0 and advances
  only when an element is flagged; write each flagged element to `a[r]`. Since
  $r$ advances no faster than the read index $i$, the invariant $r \le i$ holds
  throughout, so a write never lands on an element that has not yet been read.
  Afterwards the first $r$ slots hold the distinct values in order;
  `Arrays.copyOf(a, r)` trims the array to length $R$.

Either way the data is touched twice — once to find the runs, once more to count
or to copy — and either way the extra work is linear.

This settles guiding questions 1 and 4: duplicates sit in adjacent runs, and a
single neighbour-comparison pass reduces the sorted copy to its $R$ distinct
values. Question 2 follows immediately — `rank()` is handed a shorter sorted
array and needs no change at all.

### Where the cost lands

The exercise bounds `contains()`, not the constructor. The stock constructor
already pays $\sim N \lg N$ for the sort; a linear pass on top of that is
invisible by comparison, and it is paid once. Under the whitelist model (build
once, query many times) that is exactly where cost belongs. The only thing to
avoid is a reduction step that _dominates_ the sort — a quadratic one would
satisfy the letter of the exercise and still make the class worse. That answers
question 3.

### Practical notes

- **Don't mutate the caller's array** — `StaticSETofInts` already defensively
  copies before sorting; preserve that.
- **In-place dedup** with two indices (read cursor, write cursor) avoids a
  second allocation; alternatively count distinct first, then copy into a
  right-sized array so no capacity is wasted.
- **Edge cases:** empty array, all-equal array ($R = 1$), already-distinct array
  ($R = N$).
- **Testing:** for random arrays, check `contains()` agrees with a
  `HashSet`-based oracle for every value in a covering range; verify the
  internal array length equals $R$.

### The solution

The constructor gains one step after the sort; the search is untouched.
`removeDuplicates` compacts in place and trims, so the empty array needs no
special case — the loop body never runs and `r` stays 0.

```java
public class StaticSETofInts {
    private int[] a;

    public StaticSETofInts(int[] keys) {
        a = keys.clone();
        Arrays.sort(a);
        a = removeDuplicates(a);
    }

    private static int[] removeDuplicates(int[] sorted) {
        int r = 0;

        for (int i = 0; i < sorted.length; i++)
            if (i == 0 || sorted[i] != sorted[i - 1])
                sorted[r++] = sorted[i];

        return Arrays.copyOf(sorted, r);
    }

    public boolean contains(int key) {
        return rank(key) != -1;
    }

    public int rank(int key) {
        int lo = 0;
        int hi = a.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (key < a[mid])
                hi = mid - 1;
            else if (key > a[mid])
                lo = mid + 1;
            else
                return mid;
        }

        return -1;
    }
}
```

Design decisions that matter:

- **Compare with the left neighbour, not the right.** The boundary then falls at
  `i == 0`, where the loop naturally starts, and the `||` short-circuit handles
  it without a separate statement. A first draft used the bound
  `i < a.length - 1` — a leftover from a right-looking version — and silently
  dropped the last element whenever the largest value appeared exactly once. It
  passed the `main` test array (the `9` appears three times there) and failed
  35% of random arrays against the oracle.
- **Compact in place rather than count-then-copy.** One loop instead of two, and
  $R$ falls out as the final value of `r` instead of being counted separately.
  The invariant $r \le i$ is what makes the overwrite safe.
- **Accept the empty array.** An earlier draft threw `IllegalArgumentException`;
  the original class handles `new int[0]` as an empty set that contains nothing,
  and matching that keeps the burden off every caller. With `r` starting at 0 it
  costs nothing.
- **Naming.** `removeDuplicates` mutates its argument, so it gets a verb that
  says so — a `get…` name would promise a side-effect-free query. The parameter
  is called `sorted` because that is the precondition the whole method rests on,
  and the signature is the best place to state it.
- **`rank()` is unchanged.** The search never learns that anything happened; it
  is simply given a shorter array.

Cost. The constructor is $\sim N \lg N$ for the sort, plus $\sim N$ for the
compaction pass and $\sim R$ for the trim — the sort dominates, so the order of
growth is unchanged from the original. `contains()` is a binary search over $R$
elements: $\sim \lg R$ compares in the worst case, as required.

**Verification.** Compiled and cross-checked against a `HashSet` oracle on
10,000 random arrays of length 0 to 20 drawn from a small value range, testing
every key in a covering range plus one either side: the internal array length
always equalled $R$ and every `contains()` answer agreed. Then two doubling
runs, counting compares per `contains()` over 200,000 random queries. With $R$
held fixed while $N$ doubles, the compare count does not move; with $R$
doubling, it rises by exactly one per row. Build time doubles per row as
$N \lg N$ predicts.

$R$ fixed at $1000$, $N$ doubling:

|        $N$ |    $R$ | build (ms) | ratio | compares/query |
| ---------: | -----: | ---------: | ----: | -------------: |
|   $131072$ | $1000$ |      $7.7$ |     — |         $9.64$ |
|   $262144$ | $1000$ |     $17.6$ | $2.3$ |         $9.65$ |
|   $524288$ | $1000$ |     $29.9$ | $1.7$ |         $9.65$ |
|  $1048576$ | $1000$ |     $54.8$ | $1.8$ |         $9.64$ |
|  $2097152$ | $1000$ |    $106.7$ | $1.9$ |         $9.65$ |
|  $4194304$ | $1000$ |    $203.4$ | $1.9$ |         $9.65$ |
|  $8388608$ | $1000$ |    $401.3$ | $2.0$ |         $9.65$ |
| $16777216$ | $1000$ |   $1023.8$ | $2.6$ |         $9.65$ |

$R = N/16$, both doubling:

|        $N$ |       $R$ | build (ms) | ratio | compares/query |
| ---------: | --------: | ---------: | ----: | -------------: |
|   $131072$ |    $8192$ |     $14.0$ |     — |        $12.67$ |
|   $262144$ |   $16384$ |     $24.9$ | $1.8$ |        $13.67$ |
|   $524288$ |   $32768$ |     $51.9$ | $2.1$ |        $14.67$ |
|  $1048576$ |   $65536$ |    $119.4$ | $2.3$ |        $15.67$ |
|  $2097152$ |  $131072$ |    $232.2$ | $1.9$ |        $16.67$ |
|  $4194304$ |  $262144$ |    $493.1$ | $2.1$ |        $17.66$ |
|  $8388608$ |  $524288$ |   $1014.6$ | $2.1$ |        $18.67$ |
| $16777216$ | $1048576$ |   $2114.2$ | $2.1$ |        $19.67$ |

The average sits a little under $\lg R$ because a hit ends the search early; the
per-doubling increment of one compare is the $\lg R$ signature.

<br />
<br />
