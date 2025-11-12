# Exercise 1.4.20

_Bitonic search_. An array is _bitonic_ if it is comprised of an increasing
sequence of integers followed immediately by a decreasing sequence of integers.
Write a program that, given a bitonic array of $N$ distinct `int` values,
determines whether a given integer is in the array. Your program should use
$\sim 3 \lg N$ compares in the worst case.

---

## The problem

Search in an array that goes up and then comes down — sorted, but in two
opposite directions with an unknown split point. Plain binary search needs
monotone order, so it can't be applied to the whole array as-is. The budget of
$\sim 3 \lg N$ compares is practically an architecture diagram: three
logarithmic phases. Your job is to figure out what the three phases are and wire
them together.

### A small example

```text
a = [ 2, 7, 11, 25, 19, 12, 5, 1 ]
      └── increasing ──┘└─ decreasing ─┘
                   ▲
              peak = 25 at index 3
```

Is `12` in the array? Is `9`? Note `9` is greater than `a[0] = 2` and less than
the peak, yet absent — membership can't be decided from the extremes alone.

### Questions to guide your solution

1. If you knew the index of the _peak_ (the maximum), how would the problem
   decompose, and what would each piece cost?
2. You don't know the peak. Can you locate it with a binary-search-style loop?
   What local test at position `mid` (one compare against a neighbour) tells you
   which side of the peak you're on? You are looking for a _local maximum_ in an
   array that has exactly one.
3. Add up the pieces — does your total land at $\sim 3 \lg N$? Which phase could
   be skipped when the target is found early?
4. (Harder, from the book's web exercises) Can you decide membership in
   $\sim 2 \lg N$ compares, _without_ ever locating the peak exactly? What can
   you learn from comparing `a[mid]` to the target alone?

### Finding the peak

The three phases are: locate the peak, then binary-search the ascending half
`a[0..peak]` for the target, then binary-search the descending half
`a[peak..n-1]`. Each phase is a binary search, so each costs $\sim \lg N$
compares, and the total is $\sim 3 \lg N$. This section is about the first phase
— the one that isn't a textbook binary search, because it never looks at the
target at all.

**Claim.** Let $p$ be the index of the peak. For any index $m$ with
$0 \le m < N - 1$: if `a[m] < a[m + 1]` then $p > m$; otherwise $p \le m$.

_Proof._ A bitonic array is strictly increasing up to $p$ and strictly
decreasing after it. If $m < p$ then both $m$ and $m + 1$ lie in the increasing
run (or $m + 1 = p$), so `a[m] < a[m + 1]`. If $m \ge p$ then both lie in the
decreasing run (or $m = p$), so `a[m] > a[m + 1]`. The values are distinct, so
`a[m] < a[m + 1]` and `a[m] > a[m + 1]` are the only two outcomes, and each
identifies a side. $\blacksquare$

Distinctness is doing real work here: with a repeated value, `a[m] == a[m + 1]`
would tell you nothing about which side of the peak you were on.

**What binary search actually needs.** Textbook binary search compares `a[mid]`
to a key, and it works because the array is sorted. But sortedness is only one
way of supplying what the algorithm really depends on: a yes/no question about
the index that is `true` up to some point and `false` from there on — a
predicate that flips _exactly once_. The claim above says `a[i] < a[i + 1]` is
such a predicate on a bitonic array:

```text
a  =  2   7  11  25  19  12   5   1
      T   T   T   F   F   F   F
```

Finding the peak is binary search for the first `false`. Same halving loop, but
the thing compared is the array's shape rather than a value you supplied — which
is why the target never appears in this phase.

**The constructive reading.** One compare against the right-hand neighbour tells
you which slope `mid` sits on, and nothing else about the array is needed — the
loop carries only `lo` and `hi`, exactly like ordinary binary search. But the
two outcomes are not symmetric. On the ascending slope, `mid` is definitely not
the peak, so the window can move _past_ it: `lo = mid + 1`. On the descending
slope, `mid` might _be_ the peak, so it has to stay in the window: `hi = mid`.

```java
int lo = 0;
int hi = a.length - 1;

while (lo < hi) {
    int mid = lo + (hi - lo) / 2;

    if (a[mid] < a[mid + 1]) {
        lo = mid + 1;
    } else {
        hi = mid;
    }
}

return lo;
```

The asymmetry is a property of the test, not of the direction chosen — compare
against the left-hand neighbour instead and you get the mirror image, with
`hi = mid - 1` on one branch and `lo = mid` on the other.

Two facts follow from `lo < hi` and floor division, and both are needed:

- `mid < hi`, so `hi = mid` strictly shrinks the window and the loop cannot
  stall. (Rounding `mid` _up_ would break this — that branch would spin
  forever.)
- `mid + 1 <= hi <= n - 1`, so the neighbour read is always in bounds.

On exit `lo == hi`, and the peak is the single index left. When $N = 1$ the loop
body never runs and `a[1]` is never read.

**A trace** on the example array `a = [2, 7, 11, 25, 19, 12, 5, 1]`:

```text
 lo  hi  mid  a[mid]  a[mid+1]  test        action
  0   7    3      25        19  25 < 19 ✗   hi = 3
  0   3    1       7        11   7 < 11 ✓   lo = 2
  2   3    2      11        25  11 < 25 ✓   lo = 3
  3   3    —       —         —  lo == hi    exit
```

Three compares for $N = 8$, and $\lg 8 = 3$. The first iteration lands on the
peak and _keeps_ it (`hi = mid`); discarding `mid` there would have lost the
answer. The window halves on every iteration at one compare each, so the phase
costs $\sim \lg N$.

This settles guiding questions 1 and 2: the problem decomposes into a peak
search followed by one ascending and one descending binary search, each
$\sim \lg N$; and the peak is found with a single neighbour compare per
iteration.

### Practical notes

- **Descending binary search** is the same algorithm with the comparison flipped
  — write it once, parameterise the direction, and test it separately; sign
  errors here are the classic bug.
- **Edge shapes:** purely increasing (peak at the end), purely decreasing (peak
  at the start), N = 1, N = 2 — make sure the peak-finding loop terminates on
  all of them.
- **Distinctness** is what makes the neighbour test at the peak unambiguous;
  note where you rely on it.
- **Testing:** generate bitonic arrays by sorting random distinct values,
  splitting at a random point, and reversing the tail; probe with present
  values, absent values inside the range, and values outside the range.

### The solution

Three binary searches in sequence. `findPeak` is the neighbour-compare loop
derived above; the two half-searches are ordinary binary search with the
comparison sense flipped for the descending side; `findTarget` wires them
together and short-circuits when the ascending half already has the answer. It
returns the index rather than the boolean the book asks for — a free upgrade
that also made the test's linear-scan oracle trivial to write.

```java
public static int findPeak(int[] a) {
    int lo = 0;
    int hi = a.length - 1;

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;

        if (a[mid] < a[mid + 1]) {
            lo = mid + 1;
        } else {
            hi = mid;
        }
    }

    return lo;
}

public static int ascendingSearch(int[] a, int target, int lo, int hi) {
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;

        if (target > a[mid]) {
            lo = mid + 1;
        } else if (target < a[mid]) {
            hi = mid - 1;
        } else {
            return mid;
        }
    }

    return -1;
}

public static int descendingSearch(int[] a, int target, int lo, int hi) {
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;

        if (target < a[mid]) {
            lo = mid + 1;
        } else if (target > a[mid]) {
            hi = mid - 1;
        } else {
            return mid;
        }
    }

    return -1;
}

public static int findTarget(int[] a, int target) {
    if (a.length == 0) {
        return -1;
    }

    int peak = findPeak(a);
    int asc = ascendingSearch(a, target, 0, peak);

    if (asc > -1) {
        return asc;
    }

    return descendingSearch(a, target, peak + 1, a.length - 1);
}
```

- **The two search loops use `lo <= hi` and can discard `mid` on both branches**
  — `a[mid]` has already been compared to the target, so it's never the answer.
  The peak loop is the odd one out: it uses `lo < hi` and keeps `mid` on one
  branch, because there `mid` might be the thing being searched for.
- **Ascending and descending are separate methods**, differing only in which way
  `lo` and `hi` move when `target != a[mid]`. A single method with a direction
  flag is equally valid; two copies were kept for readability, at the cost of
  the sign error having two places to hide. The method names must match the half
  they are called on — an early version had them swapped, with the call sites
  swapped to compensate, which works and reads wrongly.
- **`a[peak]` belongs to the ascending search**, so the descending one starts at
  `peak + 1`. Either half could own the peak; what matters is that exactly one
  does.
- **The empty array is guarded in `findTarget`.** Without it, `findPeak` returns
  0 on an empty array (its loop never runs) and the ascending search then reads
  `a[0]`. The exercise supplies $N \ge 1$ values, so this is belt-and-braces,
  but it costs one line.

**Cost.** Counting one compare per loop iteration (the book's convention — a
three-way comparison of `target` against `a[mid]` counts as one compare, as it
does for `BinarySearch.rank()`), each phase runs at most $\lceil \lg N \rceil$
iterations on a window of at most $N$ elements, so the worst case is
$\sim 3 \lg N$. The measured worst case sits at $3 \lg N - 1$: the halves are
each strictly smaller than $N$, so between them they save a compare on the
bound.

**Verification.** Compiled and checked against a linear-scan oracle on random
bitonic arrays of every size from 1 to 40 — 400 arrays per size, probing every
target from one below the minimum to one above the maximum, 3.2 million probes
in all — plus the purely increasing, purely decreasing, one- and two-element
shapes. Zero mismatches. `Exercise_1_4_20Test` pins the same cases down in
JUnit, including the empty-array guard.

The compare count was measured by instrumenting the three loops and taking the
worst case over eight peak positions (both ends, the quarter points, and the
middle) and every target in range. Doubling $N$ adds exactly 3 compares — one
per phase — which is the $3 \lg N$ signature:

|     $N$ | worst compares | $3 \lg N$ | $+3$ per doubling |
| ------: | -------------: | --------: | ----------------: |
|     $8$ |            $8$ |       $9$ |                 — |
|    $16$ |           $11$ |      $12$ |               $3$ |
|    $32$ |           $14$ |      $15$ |               $3$ |
|    $64$ |           $17$ |      $18$ |               $3$ |
|   $128$ |           $20$ |      $21$ |               $3$ |
|   $256$ |           $23$ |      $24$ |               $3$ |
|   $512$ |           $26$ |      $27$ |               $3$ |
|  $1024$ |           $29$ |      $30$ |               $3$ |
|  $2048$ |           $32$ |      $33$ |               $3$ |
|  $4096$ |           $35$ |      $36$ |               $3$ |
|  $8192$ |           $38$ |      $39$ |               $3$ |
| $16384$ |           $41$ |      $42$ |               $3$ |
| $32768$ |           $44$ |      $45$ |               $3$ |
| $65536$ |           $47$ |      $48$ |               $3$ |

<br />
<br />
