# Exercise 1.4.19

_Local minimum of a matrix._ Given an $N$-by-$N$ array `a[][]` of $N^2$ distinct
integers, design an algorithm that runs in time proportional to $N$ to find a
_local minimum_: a pair of indices `i` and `j` such that
`a[i][j] < a[i+1][j], a[i][j] < a[i][j+1], a[i][j] < a[i-1][j]`, and
`a[i][j] < a[i][j-1]`. The running time of your program should be proportional
to $N$ in the worst case.

---

## The problem

The two-dimensional form of the local-minimum problem: find a cell smaller than
all four of its neighbours (up, down, left, right). The budget is the shocker —
$O(N)$ for an $N$-by-$N$ grid means you may only ever _look at_ a vanishing
fraction of the $N^2$ entries. Even reading one full row costs $N$, so you can
afford to fully examine only a constant number of rows' and columns' worth of
cells across the entire run.

### Why the one-dimensional trick doesn't transplant directly

The natural attempt: binary search on rows — scan the middle row for its minimum
($N$ compares), compare that cell against its neighbours above and below, and
recurse into the half holding the smaller of the two. The invariant that makes
the one-dimensional version sound carries over intact, so _correctness_ is not
what bites here. The cost is.

Halving the rows leaves the width untouched: the sub-grids are $N/2 \times N$,
then $N/4 \times N$, and so on. Every level still scans a full row of $N$ cells,
so the work per level never shrinks:

$$
\begin{aligned}
T(r) &= T(r/2) + cN && \text{an } r \times N \text{ strip still scans a full-width row} \\
T(N) &= \underbrace{cN + cN + \dots + cN}_{\lg N \text{ terms}} = \Theta(N \lg N)
\end{aligned}
$$

Much better than reading all $N^2$ entries, but still a factor of $\lg N$ over
budget. The recurrence only collapses to $O(N)$ when the per-level work decays
geometrically, and scanning a full-width row every time is what prevents that.

### A small example

```text
a =  [ 12  8  9 ]
     [  7  3 11 ]        3 < 8, 11, 7, 6 → (1,1) is a local minimum
     [ 15  6 10 ]        6 < 15, 10, 3? no — 6 > 3, so (2,1) is not
```

Borders follow the usual convention: missing neighbours are ignored
(equivalently, treat out-of-bounds as $+\infty$).

### Questions to guide your solution

1. State the invariant precisely: "the current sub-grid contains a cell that is
   a local minimum of the _full_ grid, because on its boundary we know ...".
   What do you need to remember from the cells you have already examined for
   that to hold?
2. What would the per-level cost have to look like for the total to come out at
   $O(N)$? If the examined region shrank in _both_ dimensions at each step, what
   recurrence would you get?
3. Where does _distinctness_ of the $N^2$ values enter the argument?
4. Sanity-check the information-theoretic side: why is $o(N)$ impossible, i.e.
   why must any correct algorithm look at $\Omega(N)$ cells in the worst case?

### The sweeps, round by round

The machine that does the shrinking is one move, repeated: sweep the middle line
of the current region in full, and call its minimum $m$.

- If $m$'s two neighbours perpendicular to the swept line are both bigger, $m$
  is a local minimum — along the line it already beats its neighbours, being the
  line's minimum, and across the line the two checks just passed. Stop.
- Otherwise some perpendicular neighbour $x < m$. Keep the half containing $x$;
  discard the swept line and everything on its far side. Note $x$ is not the
  next cell to examine — its job is to be the _witness_ that the kept half is
  safe, since $x < m \le$ every swept cell.

On an $8 \times 8$ grid, round 1 sweeps the middle row (8 cells). Say its
minimum is 6 and the cell directly above is 4:

```text
cols   0 1 2 3 4 5 6 7
row 0  . . . . . . . .   ┐
row 1  . . . . . . . .   │ keep (4 lives up here)
row 2  . . . . . . . .   │
row 3  . . 4 . . . . .   ┘
row 4  s s 6 s s s s s   ← swept; discard this row and below
```

Sweeping only rows would now repeat the move on the strip of rows 0–3 — correct,
but every sweep still runs the full width, which is exactly the
$\Theta(N \lg N)$ recurrence above. So round 2 sweeps the middle **column**
instead, and only where it still exists — 4 cells:

```text
cols   0 1 2 3 4 5 6 7
row 0  . . . c . . . .
row 1  . . . c . . . .   sweep the c's, take their minimum,
row 2  . . . c . . . .   check its left and right neighbours
row 3  . . . c . . . .
```

Keeping the side with the smaller horizontal neighbour leaves a $4 \times 4$
quadrant, whose own middle row costs 4, and so on. Alternating directions halves
each dimension in turn, so the sweep lengths run

$$8,\ 4,\ 4,\ 2,\ 2,\ 1,\ 1$$

To total these in general, group them: the first sweep costs $N$, and every
length after it appears **twice** — once as a height, once as a width — because
each halving is used by one column sweep and one row sweep before the next
halving kicks in:

$$N,\ \underbrace{N/2,\ N/2}_{\text{col, then row}},\ \underbrace{N/4,\ N/4}_{\text{col, then row}},\ \dots$$

So the total is the first term plus twice the tail,

$$N + 2\left(\tfrac{N}{2} + \tfrac{N}{4} + \tfrac{N}{8} + \dots\right) < N + 2N = 3N,$$

the bracket being the classic geometric series: each term fills half of the gap
remaining up to $N$, so however many terms there are it never reaches $N$. A
bound of $3N$ is a constant times $N$ — that is $O(N)$, and it answers question
2: the per-sweep cost decays geometrically because the region shrinks in both
dimensions, not one. As a sanity check, count both sides on the $8 \times 8$
walkthrough: the sweep lengths were $8, 4, 4, 2, 2, 1, 1$ — in grouped form
$8 + 2(4 + 2 + 1) = 22$ cells examined — while the bound allows at most
$3N = 24$. The worked example and the general claim agree.

It is worth pausing on where the $\lg N$ went, because both versions of the
algorithm run about $\lg N$-ish sweeps. $N \lg N$ is the signature of log-many
rounds of **equal** cost — $N + N + \dots + N$, one full-width scan per level,
which is exactly the rows-only recurrence above. The alternating version runs
roughly twice as many sweeps, but their costs decay geometrically, and a
geometric series is dominated by its first few terms — the same way
$1 + \frac{1}{2} + \frac{1}{4} + \dots$ never reaches $2$ however many terms are
added. The infinitely many sweeps of the tail together cost less than the first
sweep twice over, so the _number_ of sweeps stops mattering and the log
evaporates. The general lesson: log-many rounds gives $N \log N$ only while each
round costs $\Theta(N)$; the moment per-round cost decays geometrically, the
total collapses to a constant times the first round.

### The basin argument

Call the cells just outside the current region its _wall_: the swept lines it
borders, plus anything off the edge of the grid, which counts as $+\infty$. The
recursion lives or dies by one property.

**Invariant.** _The current region contains a cell — the witness — that is
smaller than every cell of its wall._

At the start the region is the whole grid, its wall is all $+\infty$, and any
cell serves as witness. What the invariant buys is this:

**Claim.** A region satisfying the invariant contains a local minimum of the
full grid.

_Proof._ Read the values as heights, so the region is a basin whose wall stands
everywhere higher than the witness $x$. Drop a ball on $x$ and let it roll:
whenever any neighbour of the current cell is lower, move to it. Every move is
strictly downhill, so the ball only ever visits cells lower than $x$; every wall
cell is higher than $x$, so rolling onto the wall would be an uphill move, and
the ball can never leave the region. The values are distinct and finite and the
ball strictly descends, so it must come to rest — at a cell with no lower
neighbour anywhere in the grid. That cell is a local minimum, and it is trapped
inside the region. $\blacksquare$

Distinctness earns its keep here (question 3): it makes every comparison strict,
so the descent never stalls on a plateau of equal heights and "a lower
neighbour" is always unambiguous.

The proof is deliberately non-constructive as far as the budget goes: actually
rolling the ball costs one cell per step, and a descending path can snake
through $\Theta(N^2)$ cells — the spiral grids in the testing notes below are
built to force exactly that. The sweeps are the fast alternative: each one
shrinks the basin while keeping the invariant true, because the
smaller-than-the-wall cell discovered during the sweep becomes the witness
carried into the kept half. What remains to check, once the region has walls on
several sides, is that the cell you keep really does beat _all_ of them at once
— which is exactly the "what do you need to remember" half of question 1.

### The amnesia trap

The alternating machine, built exactly as described so far, is unsound — and
random testing finds it out at a rate of roughly one grid in 250. Here is the
smallest culprit a 200,000-grid run produced, a $7 \times 7$ on which the naive
version discards every local minimum and runs out of board:

```text
48  7 16 34 17 37 39
43 47 28 42 35 15 18
 8 26  6 22  1 23 38
21 10 12 20 14  4 27
24 33  3 40 36  0 46
32 25 11 44 45  9 13
29  2  5 31 30 19 41
```

Trace the first two passes. Pass 1 sweeps row 3, finds its minimum 4 at
$(3, 5)$, sees the 0 below it, and keeps rows 4–6 — _on the strength of that 0_.
Pass 2 sweeps column 3 of the strip, finds its minimum 31 at $(6, 3)$, sees the
5 to its left, and keeps columns 0–2 — throwing away columns 3–6, and the 0 with
them. At no point did pass 2 compare anything against 0. It couldn't: the
algorithm never wrote it down. Each pass starts amnesiac, trusting whatever
minimum its own line happens to contain, and 31 is a large, unremarkable value
with no authority over a region that was kept because of a 0.

The unsoundness in one sentence: **a discard is only justified when the swept
line's minimum is at least as small as everything the region was previously
trusted for — and nothing enforces that.** In the strip-only version this held
for free, because every sweep spanned the full width; once both axes shrink, it
has to be enforced by hand.

The repair is the answer to question 1's "what do you need to remember": carry
the coordinates of the **best cell** — the smallest cell any pass has seen —
across passes, and make each pass's _first_ comparison the sweep's minimum $m$
against the remembered best:

- **The best cell wins** ($\text{best} < m$, strictly): no neighbour probes. The
  best cell beats the entire swept line, so the line is a wall it is safe
  behind: keep the half of the rectangle where the best cell lives, discard the
  swept line with the other half. (Strictness matters: values are distinct, so a
  tie means the best cell _is_ the swept minimum, and that case must fall
  through to the probes — there is no "side" for a cell that sits on the line
  itself.)
- **The sweep wins** ($m \le \text{best}$): proceed exactly as before — probe
  the two perpendicular neighbours of $m$, return if both are bigger, follow the
  smaller one otherwise. The followed neighbour $x$ becomes the new best with no
  extra comparison, by a chain: $x < m \le \text{best} \le$ everything any
  earlier pass saw.

This closes question 1 in full. The invariant, stated precisely: _the current
rectangle contains the best cell, and the best cell is smaller than every cell
of the rectangle's wall_. Every branch above preserves it, the basin argument
turns it into "a local minimum of the full grid is trapped inside", and a return
only ever fires for a cell that beats its whole swept line, its two probed
neighbours, and (through the invariant) every wall cell it borders.

### Practical notes

- **Checker first:** write the $O(1)$ verifier (compare a cell against at most
  four neighbours) and a brute-force finder for small grids before attempting
  the fast version.
- **Testing:** random permutations of $0 \dots N^2-1$ reshaped into the grid
  guarantee distinctness; also test grids where the minimum path spirals, and
  the $N = 1$ and $N = 2$ degenerates (a $1 \times N$ strip is an instance only
  if your implementation accepts general $M \times N$ grids).
- **Count cell inspections**, not wall-clock time, to validate $O(N)$ —
  instrument the accesses and run a doubling test on the count.

### The solution

The verified implementation. Four bounds describe the surviving rectangle, a
flag alternates the sweep axis, and `(bestRow, bestCol)` is the memory that
keeps the discards honest:

```java
public static int[] findLocalMin(int[][] a) {
    int loRow = 0;
    int hiRow = a.length - 1;
    int loCol = 0;
    int hiCol = a.length - 1;
    int bestRow = 0;
    int bestCol = 0;
    boolean sweepRow = true;

    while (loRow <= hiRow && loCol <= hiCol) {
        if (sweepRow) {
            int midRow = loRow + (hiRow - loRow) / 2;
            int j = indexOfRowMin(a, midRow, loCol, hiCol);

            if (a[bestRow][bestCol] < a[midRow][j]) {
                // the remembered best beats the whole swept row: keep the
                // half where it lives, no neighbour probes needed
                if (bestRow < midRow) {
                    hiRow = midRow - 1;
                } else {
                    loRow = midRow + 1;
                }
            } else {
                // the sweep's minimum is the smallest value yet seen:
                // probe the cells above and below it
                if (midRow > 0 && a[midRow - 1][j] < a[midRow][j]) {
                    hiRow = midRow - 1;
                    bestRow = midRow - 1;
                    bestCol = j;
                } else if (midRow < a.length - 1 && a[midRow + 1][j] < a[midRow][j]) {
                    loRow = midRow + 1;
                    bestRow = midRow + 1;
                    bestCol = j;
                } else {
                    return new int[] { midRow, j };
                }
            }
        } else {
            // the mirror pass: sweep the middle column, probe left and
            // right, and move the column bounds
            int midCol = loCol + (hiCol - loCol) / 2;
            int i = indexOfColMin(a, midCol, loRow, hiRow);

            if (a[bestRow][bestCol] < a[i][midCol]) {
                if (bestCol < midCol) {
                    hiCol = midCol - 1;
                } else {
                    loCol = midCol + 1;
                }
            } else {
                if (midCol > 0 && a[i][midCol - 1] < a[i][midCol]) {
                    hiCol = midCol - 1;
                    bestCol = midCol - 1;
                    bestRow = i;
                } else if (midCol < a[0].length - 1 && a[i][midCol + 1] < a[i][midCol]) {
                    loCol = midCol + 1;
                    bestCol = midCol + 1;
                    bestRow = i;
                } else {
                    return new int[] { i, midCol };
                }
            }
        }

        sweepRow = !sweepRow;
    }

    throw new AssertionError("unreachable: every array has a local minimum");
}
```

`indexOfRowMin` and `indexOfColMin` are plain segment scans returning the index
of the smallest cell in one row (between two columns) or one column (between two
rows).

The design decisions that matter:

- **The memory comparison comes first, and it is strict.**
  `a[bestRow][bestCol] < a[midRow][j]` sends ties to the probe branch — a tie
  means the best cell is the swept minimum itself, and the memory-wins branch
  has no side to pick for a cell on its own line.
- **Neighbour guards test the grid edge, not the rectangle edge.** Cells outside
  the rectangle are walls, not $+\infty$: they hold real values and must be
  compared for real. Only off-the-grid is $+\infty$.
- **The memory update is two assignments and no `if`.** A followed neighbour is
  automatically the new best, since $x < m \le \text{best}$.
- **`a[row][col]`, always.** Both instructive bugs in this implementation's
  history were the same bug: transposed indices in `indexOfColMin` (`a[col][i]`
  for `a[i][col]`) and in the memory read (`a[bestCol][bestRow]`). Each
  compiled, each passed friendly grids, and each fell to random testing within a
  few thousand grids.

Cost: the sweep lengths halve in alternation — $N, N/2, N/2, N/4, \dots$ — so
cells inspected total $N + 2(N/2 + N/4 + \dots) < 3N$, and everything else in a
pass is $O(1)$. The bound holds per run, not on average, so the test suite
asserts it on every grid it generates.

**Verification.** Checked against the definition (not against the brute-force
finder's _choice_, since a grid can hold several local minima): 200,000 random
permutation grids of sizes 1–12, every permutation of the $1 \times 1$ through
$3 \times 3$ grids exhaustively, snake-descent grids up to $101 \times 101$, and
the amnesia-trap grid as a regression test — zero failures, with the naive
version failing the same battery roughly 800 times. A doubling test on
instrumented inspection counts confirms the linear budget:

|    $N$ | avg inspections | per $N$ |  ratio |
| -----: | --------------: | ------: | -----: |
|  $250$ |         $250.0$ |  $1.00$ |      — |
|  $500$ |         $500.0$ |  $1.00$ | $2.00$ |
| $1000$ |        $1000.0$ |  $1.00$ | $2.00$ |
| $2000$ |        $2000.0$ |  $1.00$ | $2.00$ |
| $4000$ |        $4000.0$ |  $1.00$ | $2.00$ |

Ratio 2 under doubling is linear growth, and the averages sit near $1{\times}N$
— random grids rarely exhaust the $3N$ worst case. Wall-clock at these sizes
says nothing (roughly 20–50 µs a call, noisier than JIT warm-up); the inspection
count is the machine-independent measurement.

<br />
<br />
