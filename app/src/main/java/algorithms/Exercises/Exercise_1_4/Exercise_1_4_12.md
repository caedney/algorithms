# Exercise 1.4.12

Write a program that, given two sorted arrays of $N$ `int` values, prints all
elements that appear in both arrays, in sorted order. The running time of your
program should be proportional to $N$ in the worst case.

---

## The problem

Given two arrays that are each already sorted, print their intersection — the
values common to both — and do it in a single linear pass. Say the inputs are:

```
a:  1   3   5   7
b:  3   4   7   9
```

Then the program should print `3` and `7`, in that order. The output must come
out sorted, and the whole computation must finish in time proportional to $N$
even in the worst case.

## Why the constraint matters

"Time proportional to $N$ in the worst case" rules out the two tempting
shortcuts:

The first is the nested loop: for each element of `a`, scan all of `b` looking
for a match. That ignores the sortedness entirely and costs $O(N^2)$.

The second is the `Whitelist` approach from Section 1.1: for each element of
`a`, binary search for it in `b`. That's better — $O(N \log N)$ — but it still
misses the bound. Binary search treats each lookup as independent, restarting
from the middle of `b` every time, and that restarting is where the log factor
comes from. The exercise is deliberately asking for something binary search
can't deliver, to force a different idea.

## The linear solution

The idea is the merge step of mergesort, repurposed. Put one index at the start
of each array and compare the two values they point at:

If `a[i] < b[j]`, then `a[i]` cannot appear anywhere in `b` — everything left in
`b` is at least `b[j]`, which is already larger. So `a[i]` is permanently
eliminated; advance `i`. If `a[i] > b[j]`, the same logic mirrored eliminates
`b[j]`; advance `j`. If they're equal, that value is in both arrays — print it
and advance both. When either array runs out, nothing further can match, so
stop.

```java
public static void printIntersection(int[] a, int[] b) {
    int i = 0;
    int j = 0;

    while (i < a.length && j < b.length) {
        if (a[i] < b[j])
            i++;
        else if (a[i] > b[j])
            j++;
        else {
            StdOut.println(a[i]);
            i++;
            j++;
        }
    }
}
```

The output falls out in sorted order for free: both inputs are sorted, and the
indices only ever move forward, so each printed value is at least as large as
the one before it.

A trace on the example above:

```
1 vs 3  → 1 < 3, drop 1
3 vs 3  → match — print 3, advance both
5 vs 4  → 5 > 4, drop 4
5 vs 7  → 5 < 7, drop 5
7 vs 7  → match — print 7, advance both
loop ends (a exhausted)
```

## Why it's $O(N)$

Every iteration of the loop advances `i`, `j`, or both — no comparison is ever
wasted, and no index ever moves backward. Since `i` can advance at most $N$
times and `j` can advance at most $N$ times, the loop runs at most $2N$ times
before one array is exhausted. Each iteration does constant work, so the whole
program is linear in the worst case.

The key insight is in the first branch: sortedness lets a single comparison
_permanently eliminate_ an element from consideration. That's what buys linear
time. Binary search also exploits sortedness, but only within one lookup — it
forgets everything between lookups. The two-pointer walk remembers, which is
exactly the log factor's worth of difference.

## Duplicates

The code above prints a value once per matched _pair_: on `a = { 3, 3 }` and
`b = { 3, 3 }` it prints `3` twice. If the intended reading is set intersection
— each common value printed once — skip past repeats after a match:

```java
else {
    StdOut.println(a[i]);
    i++;
    j++;

    while (i < a.length && a[i] == a[i - 1])
        i++;
    while (j < b.length && b[j] == b[j - 1])
        j++;
}
```

Either reading of the problem is defensible — just pick one deliberately and say
which in a comment.

## Testing it

The must-have cases are the boundary ones: no elements in common (disjoint
ranges, and interleaved values that never match), one array empty, both arrays
empty, and arrays that are entirely identical. Then the duplicate cases, since
that's where the two readings of the problem diverge: repeated values in one
array only, and repeated values in both. Beyond that, a randomized comparison
against a simple quadratic nested-loop intersection over arrays drawn from a
small value range (so matches and duplicates are guaranteed) gives good
confidence, and a large input — say two arrays of a few million identical values
— makes any accidental quadratic behavior obvious by wall-clock alone.

<br />
<br />
