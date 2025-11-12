# Exercise 1.4.19

_Local minimum of a matrix._ Given an _N_-by-_N_ array `a[]` of *N*² distinct
integers, design an algorithm that runs in time proportional to _N_ to find a
_local minimum_: a pair of indices `i` and `j` such that
`a[i][j] < a[i+1][j], a[i][j] < a[i][j+1], a[i][j] < a[i-1][j]`, and
`a[i][j] < a[i][j-1]`. The running time of your program should be proportional
to _N_ in the worst case.

---

## The problem

The 2-D sequel to 1.4.18: find a cell smaller than all four of its neighbours
(up, down, left, right). The budget is the shocker — **O(N)** for an N-by-N grid
means you may only ever _look at_ a vanishing fraction of the N² entries. Even
reading one full row costs N, so you can afford to fully examine only a constant
number of rows/columns' worth of cells across the entire run.

### Why the 1-D trick doesn't transplant directly

The natural attempt: binary search on rows — find the minimum of the middle row
(N compares), check its vertical neighbours, recurse into the half with the
smaller neighbour. The recurrence is

```text
T(N) = T(N/2) + O(N)  →  O(N)   ✓ time is fine
```

but the _correctness_ argument is where it bites: after you discard half the
rows, the local minimum you eventually find in the sub-grid must be a local
minimum of the **original** grid — its neighbours in the discarded half were
never checked. The 1-D proof leaned on an invariant about the window boundary;
here the boundary is an entire row. What exactly must you carry along as the
invariant to make discarding sound?

### A small example

```text
a =  [ 12  8  9 ]
     [  7  3 11 ]        3 < 8, 11, 7, 15 → (1,1) is a local minimum
     [ 15  6 10 ]        6 < 15, 10, 3? no — 6 > 3, so (2,1) is not
```

Borders follow the same convention as 1-D: missing neighbours are ignored (or
treat out-of-bounds as +∞).

### Questions to guide your solution

1. State the invariant precisely: "the current sub-grid contains a cell that is
   a local minimum of the _full_ grid, because on its boundary we know ...".
   What value do you need to remember from the rows you've already examined?
2. If you recurse on rows only, the sub-problems are N/2 × N, then N/4 × N, ...
   — the width never shrinks. Does the O(N) total survive? Would alternating
   between rows and columns change the recurrence?
3. Where does _distinctness_ of the N² values enter the argument?
4. Sanity-check the information-theoretic side: why is o(N) impossible, i.e. why
   must any correct algorithm look at Ω(N) cells in the worst case?

### Practical notes

- **Checker first:** write the O(1) verifier (compare a cell against ≤4
  neighbours) and a brute-force finder for small grids before attempting the
  fast version.
- **Testing:** random permutations of `0..N²-1` reshaped into the grid guarantee
  distinctness; also test grids where the minimum path spirals, and 1×1, 1×N,
  2×2 degenerates.
- **Count cell inspections**, not wall-clock time, to validate O(N) — instrument
  the accesses and run a doubling test on the count.

<br />
<br />
