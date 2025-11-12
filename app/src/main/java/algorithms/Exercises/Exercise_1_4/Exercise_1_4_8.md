# Exercise 1.4.8

Write a program to determine the number pairs of values in an input file that
are equal. If your first try is quadratic, think again and use `Arrays.sort()`
to develop a linearithmic solution.

---

## The problem

You're given an input file of $N$ values and must produce one number: the count
of **pairs** $(i, j)$ with $i < j$ such that $a[i] = a[j]$ — every way of
picking two positions that hold equal values, not just adjacent ones. The
exercise's real point: the obvious all-pairs solution is quadratic, but sorting
first brings equal values together, and a single linear scan over the sorted
runs gives a linearithmic solution — the sort _is_ the algorithm.

## The quadratic first try

Check every pair directly with a double loop:

```java
for (int i = 0; i < N; i++)
    for (int j = i + 1; j < N; j++)
        if (a[i] == a[j])
            cnt++;
```

The number of pairs examined is

$$\binom{N}{2} = \frac{N(N-1)}{2} \sim \frac{N^2}{2}$$

so the running time grows as $\sim N^2$ — quadratic.

## The linearithmic insight

**Sorting brings equal values together.** After `Arrays.sort(a)`, all copies of
any value sit in one contiguous _run_. So instead of comparing everything
against everything, scan once and measure each run.

A run of length $L$ contains

$$\binom{L}{2} = \frac{L!}{2!\,(L-2)!} = \frac{L \cdot (L-1) \cdot \cancel{(L-2)!}}{  2! \cdot \cancel{(L-2)!}} = \frac{L(L-1)}{2}$$

equal pairs. E.g. `[7, 7, 7]` contributes $\binom{3}{2} = 3$ pairs — each of the
three sevens pairs with both others.

## The algorithm

```java
public static long count(int[] a) {
    Arrays.sort(a); // ~ N log N
    long cnt = 0;
    int i = 0;

    while (i < a.length) { // linear scan
        int j = i;

        while (j < a.length && a[j] == a[i])
            j++;  // find end of run

        long run = j - i;
        cnt += run * (run - 1) / 2; // C(run, 2) pairs
        i = j; // jump past the run
    }

    return cnt;
}
```

## Cost analysis

The total cost is the sort plus the scan:

$$\underbrace{\sim N \log N}_{\text{sort}} \;+\; \underbrace{\sim N}_{\text{scan}} \;\sim\; N \log N$$

The sort dominates, so the algorithm is **linearithmic**. For large $N$ this is
a dramatic win: at $N = 10^6$, roughly $N \log N \approx 2 \times 10^7$ steps
versus $N^2/2 = 5 \times 10^{11}$.

## Pitfalls

### 1. Adjacent-only counting is wrong

The most tempting bug in this exercise. After sorting, the natural one-liner is:

```java
for (int i = 1; i < N; i++)
    if (a[i] == a[i-1])
        cnt++;
```

This compares each element only to its immediate neighbor. For `[7, 7, 7]` it
counts 2 — the matches at positions $(0,1)$ and $(1,2)$. But the question asks
for _pairs of equal values_: every $(i, j)$ with $a[i] = a[j]$. For three sevens
that's $(0,1)$, $(0,2)$, and $(1,2)$ — 3 pairs. The adjacent version misses
$(0,2)$ because those elements never sit next to each other in the comparison.

For a run of length $L$:

- Adjacent counting gives $L - 1$
- Correct pair counting gives $\binom{L}{2} = \frac{L(L-1)}{2}$

These agree when $L = 2$ (both give 1) — which is exactly why the bug is sneaky:
test with data where no value appears more than twice and both versions produce
identical answers. The divergence only shows up with triplicates or more, and it
grows fast — a run of 10 gives 9 vs. 45.

What the adjacent version actually computes is the number of _duplicates_
(equivalently, $N$ minus the number of distinct values) — a legitimate quantity,
just not the one asked for. Hence the advice to sanity-check against the
brute-force double loop: it is unambiguous about what "pairs" means, so any
disagreement exposes the bug immediately.

### 2. Use `long` for the count

The pair count grows _quadratically_ in the run length, so it can blow past
`int` range even for modest files. A file of $10^5$ identical values contains

$$\binom{10^5}{2} = \frac{10^5 (10^5 - 1)}{2} \approx 5 \times 10^9$$

pairs — more than double the `int` maximum of
$2^{31} - 1 \approx 2.1 \times 10^9$. An `int` counter would silently overflow
and wrap to a garbage value (possibly negative), with no exception thrown.

The subtler trap is in the per-run arithmetic:

```java
cnt += run * (run - 1) / 2;
```

If `run` were declared `int`, the product `run * (run - 1)` is computed in `int`
arithmetic and can overflow _before_ the division by 2 or the assignment to a
`long` — overflow happens at the multiply, not the store. Declaring `run` as
`long` (as in the solution) forces the whole expression into `long` arithmetic.

Rule of thumb: when a count is $\binom{L}{2}$-shaped, assume it can exceed
$2 \times 10^9$ and use `long` throughout the calculation, not just for the
accumulator.

## Takeaway

The pattern — _sort, then exploit order with a linear pass_ — replaces a
quadratic all-pairs comparison with an $\sim N \log N$ algorithm. The same trick
powers 2-sum and 3-sum improvements later in the chapter.

<br />
<br />
