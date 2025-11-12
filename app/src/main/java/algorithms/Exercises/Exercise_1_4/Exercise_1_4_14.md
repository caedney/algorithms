# Exercise 1.4.14

_4-sum_. Develop an algorithm for the _4-sum_ problem.

---

## The problem

Given an array of `N` integers, count the number of quadruples that sum to zero.
The answer is a single number, but the space of candidate quadruples grows as
the fourth power of the input size — the exercise is asking for an algorithm
that does better than checking them all.

A quadruple is a set of four **distinct positions** in the array:

```text
count tuples (i, j, k, l) with i < j < k < l
such that a[i] + a[j] + a[k] + a[l] == 0
```

The ordering constraint `i < j < k < l` is what stops you from counting the same
four elements more than once (there are 4! = 24 orderings of any quadruple — we
want each counted exactly once), and it also guarantees the four positions are
distinct, even when the array contains duplicate _values_.

### A small example

```text
a = [ 8, -1, 2, -9, 4, 3 ]
      i0  i1 i2  i3 i4 i5
```

Checking combinations of four positions:

```text
(i0, i1, i2, i3)  →   8 + -1 + 2 + -9  =  0   ✓
(i0, i3, i4, i5)  →   8 + -9 + 4 + 3   =  6   ✗
(i1, i2, i3, i4)  →  -1 + 2 + -9 + 4   = -4   ✗
...
```

For an array of 6 elements there are "6 choose 4" = 15 quadruples to consider.
In general the search space is

```text
C(N, 4) = N(N-1)(N-2)(N-3) / 24   ~   N⁴ / 24
```

so the number of _candidate_ quadruples grows as the fourth power of the input
size. That is the heart of the exercise: the answer we want is one number (a
count), but the naive search space is enormous.

### The obvious baseline: brute force

Exactly as `ThreeSum` (Algorithm 1.4 in the book) does with three nested loops,
4-sum can be solved with four:

```java
public static int count(int[] a) {
    int n = a.length;
    int count = 0;

    for (int i = 0; i < n; i++)
        for (int j = i + 1; j < n; j++)
            for (int k = j + 1; k < n; k++)
                for (int l = k + 1; l < n; l++)
                    if (a[i] + a[j] + a[k] + a[l] == 0)
                        count++;

    return count;
}
```

This is correct, but its running time is **~N⁴/24 array accesses**. Under the
doubling hypothesis from Section 1.4, doubling `N` multiplies the running time
by **16**. If `N = 1,000` takes a second, `N = 8,000` takes over an hour — the
exercise is asking you to do better than this.

### Where this sits in the book

Section 1.4 develops a family of results this exercise builds on directly:

| Problem   | Brute force | Improved (book)              | Trick used                                   |
| --------- | ----------- | ---------------------------- | -------------------------------------------- |
| 2-sum     | O(N²)       | O(N log N) — `TwoSumFast`    | sort, then binary search for `-a[i]`         |
| 3-sum     | O(N³)       | O(N² log N) — `ThreeSumFast` | fix a pair, binary search for `-(a[i]+a[j])` |
| **4-sum** | O(N⁴)       | _this exercise_              | ?                                            |

The pattern to notice: each improved algorithm did **not** search the raw tuple
space. It reorganized the problem so that the innermost, most-executed step
became a fast lookup into pre-sorted data instead of a loop.

### Questions to guide your solution

1. Following the `ThreeSumFast` recipe literally — fix three indices, binary
   search for the fourth — what running time do you get? Is that the best you
   can do?
2. A quadruple `a[i] + a[j] + a[k] + a[l] == 0` can be regrouped as
   `(a[i] + a[j]) = -(a[k] + a[l])`. What object does that turn 4-sum into a
   search over, and how big is that collection?
3. If you go down that road, what could go wrong with counting? Think about (a)
   the same four positions being found more than once, and (b) "solutions" that
   reuse an index, like pairing `(i, j)` with `(j, l)`.
4. What is the trade-off you're making? Compare the _space_ your improved
   algorithm uses to the brute force's O(1).

---

## Working towards a solution

> Everything below this line is worked-through solution. Stop here if you want
> to solve it cold.

### 1. The literal extension, and its ceiling

Scale the `ThreeSumFast` recipe up one dimension: sort the array once, run three
nested loops over `i < j < k`, and binary search for `-(a[i] + a[j] + a[k])`.
That gives **O(N³ log N)**.

It is a real improvement — the binary search replaced one loop with a `log N`
factor, buying a factor of roughly `N / log N` — but it is still cubic-ish, and
doubling `N` still costs about 8×. This is the ceiling of the book's pattern:
sorting and binary searching can only ever collapse the _innermost_ loop, and
there are three left above it.

### 2. Regroup the equation

```text
a[i] + a[j]  ==  -(a[k] + a[l])
```

Structurally this is the whole exercise. A four-way condition has become a
statement about **two things being equal**. So instead of searching the raw
tuple space, build new data: the sum of every pair.

There are

```text
C(N, 2) = N(N-1) / 2   ~   N² / 2
```

such pairs, and the equation above says: _find two entries in that collection
whose values sum to zero._

That is **2-sum on a derived array** — a solved problem from earlier in this
same section. Note how different this move is from anything the book has shown
so far. `TwoSumFast` and `ThreeSumFast` both collapsed the innermost loop into a
binary search over the _same_ data. Here we build new data and run a solved
problem on it. This is **reduction**, not loop-collapsing.

### 3. The example, in pair sums

All 15 pair sums of `a = [8, -1, 2, -9, 4, 3]`:

```text
(0,1)  7     (1,2)   1     (2,3)  -7     (3,4)  -5
(0,2) 10     (1,3) -10     (2,4)   6     (3,5)  -6
(0,3) -1     (1,4)   3     (2,5)   5     (4,5)   7
(0,4) 12     (1,5)   2
(0,5) 11
```

Forget the original array and scan these 15 numbers for two entries summing to
zero. Six matches turn up:

```text
(0,1)  7  +  (2,3) -7
(0,2) 10  +  (1,3) -10
(0,3) -1  +  (1,2)  1
(2,3) -7  +  (4,5)  7
(2,4)  6  +  (3,5) -6
(2,5)  5  +  (3,4) -5
```

But there are only **two** zero-sum quadruples in the array. Group the matches
by the _set_ of indices involved and the discrepancy explains itself:

```text
{0,1,2,3}  found as (0,1)+(2,3),  (0,2)+(1,3),  (0,3)+(1,2)
{2,3,4,5}  found as (2,3)+(4,5),  (2,4)+(3,5),  (2,5)+(3,4)
```

Six matches, two quadruples. Every quadruple was discovered exactly three times.

### 4. Why exactly 3 — and why it never grows

Take any four indices `{w, x, y, z}`. The algorithm finds them by splitting them
into two pairs. Index `w` must be paired with _something_ — 3 candidates. Once
`w`'s partner is chosen, the remaining two indices are forced together. So **3 ×
1 = 3** splits, always.

This is a property of the number **4**, not of `N`. Whether the array holds 6
elements or 6 million, every zero-sum quadruple is discovered exactly three
times, so the scan overcounts by a constant factor of 3.

### 5. The index-reuse hazard

Consider a match between `(1,4)` and `(4,5)`. The values sum to zero, so the
scan reports it — but index 4 appears in both pairs. What it actually computed
is

```text
a[1] + a[4] + a[4] + a[5]
```

which double-counts `a[4]`. That is a triple with one element used twice, not a
quadruple, and it violates `i < j < k < l` (which demands four _strictly
increasing_, hence distinct, positions).

The sting: a naive scan **cannot see this**. Once the pair sums are built, `7`
and `-7` are just numbers — the sorted list has thrown away which indices
produced them, and a binary search over values has no way to know two entries
overlap.

So each entry must be a small **record** — the sum plus the two indices that
made it — sorted by sum. The disjointness check can only fire once a candidate
match is in hand: find a match, ask "are these four indices distinct?", count it
only if they are.

One more implementation detail: **binary search finds _an_ occurrence, not all
of them.** With `N²/2` sums, duplicate values are common — the example above
already has `7` twice, from `(0,1)` and `(4,5)`. If a pair sum is `-7` and five
entries equal `7`, all five are candidates and each needs its own overlap check.
So the inner step is really "locate the run of equal values, then walk it." This
is where Exercise 1.4.12 (common elements of two sorted arrays in linear time)
earns its keep: with the sums sorted, the `v` run and the `-v` run can be walked
together in one linear pass instead of binary searching each entry.

### 6. Fixing the count — order matters

Filter the bogus matches **first**, divide by 3 **last**.

The reason is not tidiness. Legitimate matches arrive in multiples of 3; bogus
overlapping ones arrive in whatever multiple the data produces. A total of
`6 legit + 4 bogus = 10` divided by 3 gives 3.33 — the right answer is no longer
recoverable. Filter down to 6 first, then divide, and you get 2.

The alternative is to dodge the ÷3 entirely by accepting matches only in
**canonical form** — e.g. require the first pair's smaller index to be less than
the second pair's smaller index — so each quadruple is discovered by exactly one
of its three splits. Arguably cleaner, since it never counts wrong in the first
place.

### 7. Pricing it

| Step                                     | Work                               | Cost        |
| ---------------------------------------- | ---------------------------------- | ----------- |
| 1. Build the pair-sum records            | `N²/2` records, constant work each | O(N²)       |
| 2. Sort by sum                           | mergesort on `M = N²/2` items      | O(N² log N) |
| 3. Scan for matches + disjointness check | linear merge over the records      | O(N²) †     |
| 4. Filter, then divide by 3              | —                                  | O(1)        |

The sort dominates. Substituting `M = N²/2` into `M log M`:

```text
M log M  =  (N²/2) · log(N²/2)
         =  (N²/2) · (2 log N − 1)
         =  N² log N − N²/2
         ~  N² log N
```

The `2` and the `1/2` cancel, and `N² log N` outgrows `N²`, so the subtracted
term drops.

**Total: O(N² log N) time, O(N²) space.**

|                    | Brute force | This algorithm |
| ------------------ | ----------- | -------------- |
| Time               | ~N⁴/24      | ~N² log N      |
| Space              | O(1)        | O(N²)          |
| Doubling `N` costs | 16×         | ~4×            |

### † The honest caveat

Step 3 is **output-sensitive**. Feed the algorithm an array of all zeros and
_every_ quadruple sums to zero — the answer itself is `C(N,4) ~ N⁴/24`, so no
algorithm can enumerate them faster than N⁴. The `O(N² log N)` claim assumes
matches are sparse, which holds for the random-ish data used in testing but is
not universal. `ThreeSumFast` in the book carries the same unspoken assumption.

### The trade-off you are actually buying

Space went from O(1) to `N²/2` records, and this is not a theoretical concern —
it is what actually stops the program. Run the doubling test up to `N = 16,000`
and it does not get slow, it dies:

```text
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
```

At `N = 16,000` there are `C(16000, 2) = 127,992,000` pairs. Held as objects —
16 bytes of header, an 8-byte sum, two 4-byte indices, plus the 4-byte array
reference — that is about 36 bytes each:

| `N`    | pairs         | as objects (~36 B) | packed in a `long` (8 B) |
| ------ | ------------- | ------------------ | ------------------------ |
| 4,000  | 8.0 million   | 0.29 GB            | 0.06 GB                  |
| 8,000  | 32.0 million  | 1.15 GB            | 0.26 GB                  |
| 16,000 | 128.0 million | 4.61 GB            | 1.02 GB                  |
| 32,000 | 512.0 million | 18.4 GB            | 4.10 GB                  |

A stock JVM caps the heap at a quarter of physical RAM, so the object version
runs out somewhere around `N = 8,000`–`10,000` on a normal laptop.

Two responses, and only one of them is interesting:

- **Raise the heap.** `-Xmx4g` moves the wall. It does not remove it, and each
  doubling of `N` quadruples the requirement.
- **Remove the objects.** Each record fits in a single `long`: the pair sum in
  the high 33 bits, the pair `i * n + j` in the low 31. Shifting the sum left
  preserves ordering, so packed values sort by sum under `Arrays.sort(long[])`,
  which is an in-place primitive sort — no aux array, no pointer chasing. 8
  bytes rather than 36, and a constant-factor win only: still O(N²).

So the algorithm becomes _time_-feasible at input sizes where it is already
_memory_-infeasible. The binding constraint moved from the clock to the heap —
the real lesson of the exercise, and the kind of thing an asymptotic table
quietly hides.

### Practical notes

- **Overflow:** four `int` values summed together can exceed
  `Integer.MAX_VALUE`. Accumulate in a `long` (cast the first operand:
  `(long) a[i] + a[j] + ...`). The pair sums themselves are safe in an `int`,
  but storing them as `long` costs little and removes the worry.
- **Testing:** the brute force is slow but obviously correct — keep it as a
  reference and cross-check the improved algorithm against it on many small
  random arrays. Include arrays with heavy duplicates, which is where the
  overlap check and the ÷3 both get exercised.
- **Array sizing:** `N(N-1)` overflows an `int` above `N = 65,536`, which would
  silently size the pair array wrongly. Compute the pair count in `long`
  arithmetic and cast down.
- **Timing:** validate the order-of-growth claim experimentally with
  `DoublingRatio` (Section 1.4): the ratio between successive runs tells you the
  exponent. Expect a ratio a little above 4, not 16 — anything higher near the
  top end is garbage collection, not the algorithm. Bound the loop rather than
  letting it double forever, since this one ends in an `OutOfMemoryError` rather
  than in a long wait.

<br />
<br />
