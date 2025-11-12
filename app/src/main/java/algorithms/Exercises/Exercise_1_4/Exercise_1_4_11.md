# Exercise 1.4.11

Add an instance method `howMany()` to `StaticSETofInts` (page 99) that finds the
number of occurrences of a given key in time proportional to $\log N$ in the
worst case.

---

## The problem

`StaticSETofInts` keeps a sorted array and answers `contains(key)` with binary
search. This exercise asks for a counting version: not _whether_ the key is
there, but _how many times_ it appears — and the count itself must be computed
in logarithmic time.

Say the sorted array is:

```
index:  0   1   2   3   4   5   6   7   8   9
value:  1   1   2   2   2   2   3   5   5   5
```

Then `howMany(2)` should return `4`, `howMany(3)` should return `1`, and
`howMany(4)` should return `0`. Because the array is sorted, all copies of a key
sit next to each other in one contiguous run — the whole problem is finding the
edges of that run.

## Why the constraint matters

"Time proportional to $\log N$ in the worst case" rules out the two tempting
shortcuts:

The first is to find any match with binary search and then walk outward in both
directions counting copies. If the whole array is one repeated value, the walk
is linear — $O(n)$, not $O(\log n)$.

The second is sneakier: count matches _during_ the search, incrementing a
counter each time `array[mid]` equals the key and continuing to one side. That
looks logarithmic, but it undercounts, because binary search skips elements by
design. On `{ 2, 2, 2, 2, 2 }` searching for `2`, the probes land on indices 2,
3, and 4 — three matches counted, two never visited, and the method reports `3`
instead of `5`.

## The logarithmic solution

Don't count matches at all — find the **boundaries** of the run and subtract.
Two binary searches do it: one finds the first index holding a value `>= key`
(the left edge), the other finds the first index holding a value `> key` (one
past the right edge). The difference between them is exactly the number of
occurrences:

```java
public int howMany(int key) {
    return upperBound(key) - lowerBound(key);
}

// first index i such that a[i] >= key (or a.length if none)
private int lowerBound(int key) {
    int lo = 0;
    int hi = a.length;

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;

        if (a[mid] < key)
            lo = mid + 1;
        else
            hi = mid;
    }

    return lo;
}

// first index i such that a[i] > key (or a.length if none)
private int upperBound(int key) {
    int lo = 0;
    int hi = a.length;

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;

        if (a[mid] <= key)
            lo = mid + 1;
        else
            hi = mid;
    }

    return lo;
}
```

The two helpers differ by a single character — `<` versus `<=` in the probe —
which is exactly the difference between stopping at the left edge of the run and
stopping just past its right edge.

Note the invariants differ from the book's `rank`: `hi` starts at `a.length`
rather than `a.length - 1`, the loop runs while `lo < hi`, and the "go left"
branch sets `hi = mid` rather than `mid - 1`, because `mid` itself might be the
boundary being searched for. Neither search stops early on a match — a match
just means "the edge is here or to the left."

## Why it's $O(\log N)$

Each helper halves the interval `[lo, hi)` on every iteration and never exits
early, so each makes $~\log N$ probes regardless of how many duplicates there
are — the whole method is at most $~2 \log N$ compares in the worst case. Absent
keys need no special case: both searches return the same insertion point, and
the difference is `0`. An empty array works for the same reason — both return
`0`.

## Testing it

The must-have case is a long run of one repeated value (like
`{ 2, 2, 2, 2, 2 }`), since that's exactly what breaks the count-during-search
shortcut. Beyond that: runs at the start, middle, and end of the array, absent
keys in a gap and beyond both ends, an empty array, and a randomized comparison
against a simple linear scan (a `for` loop counting matches) over arrays drawn
from a small value range so duplicates are guaranteed.

<br />
<br />
