# Exercise 1.4.24

_Throwing eggs from a building_. Suppose that you have an $N$-story building and
plenty of eggs. Suppose also that an egg is broken if it is thrown off floor $F$
or higher, and intact otherwise. First, devise a strategy to determine the value
of $F$ such that the number of broken eggs is $\sim \lg N$ when using
$\sim \lg N$ throws, then find a way to reduce the cost to $\sim 2 \lg F$.

---

## The problem

A search problem in costume. The building's floors $1 \ldots N$ are an array;
"does the egg break from floor $x$?" is exactly the query "is $x \ge F$?" — a
monotone yes/no predicate (once it breaks, everything above breaks too). Finding
$F$ is finding the boundary in a sorted 0/1 sequence.

Part 1 ($\sim \lg N$ throws, $\sim \lg N$ broken eggs) should feel familiar.
Part 2 is where the exercise earns its place in this chapter: a bound of
$\sim 2 \lg F$ depends on the _answer_, not on the input size $N$. When $F$ is
small — say the egg is fragile and breaks from floor 3 of a
$1{,}000{,}000$-story building — you should find it in a handful of throws,
never spending anything close to $\lg N$. Output-sensitive search.

### Reframing

```text
floors:   1  2  3  ...  F-1  F  F+1  ...  N
outcome:  ok ok ok ...  ok   ✗   ✗   ...  ✗
                          ▲
              find the first ✗ (unbounded budget on eggs)
```

### Questions to guide your solution

1. Part 1: which classic algorithm answers this immediately? Why does its
   broken-egg count equal (roughly) its throw count in the worst case — what
   fraction of probes land at-or-above $F$?
2. Part 2: you must not spend $\lg N$ when $F$ is tiny — so the first phase
   can't start by probing $N/2$. What probing pattern reaches the vicinity of
   $F$ using only $\sim \lg F$ throws? (Think about how you'd search an
   _infinite_ sorted array for the first ✗.)
3. Once phase 1 brackets $F$ inside an interval, how wide is that interval in
   terms of $F$, and what does finishing inside it cost? Add the phases: does
   the total come to $\sim 2 \lg F$?
4. Check the model: which of your throws break an egg, and does Part 2 also keep
   the _broken_ count logarithmic in $F$? (Here eggs are plentiful; the variant
   where the supply is limited is a different problem.)

### Part 1: binary search

The floors are a sorted sequence of outcomes — survive, survive, …, break, break
— and $F$ is the boundary. Binary search finds it: throw from the middle floor;
if the egg breaks, $F$ is at or below that floor, otherwise it is above. Either
way the candidate range halves, so $\sim \lg N$ throws suffice.

The exercise also asks for the number of _broken_ eggs, which is a different
count: a throw breaks an egg exactly when its floor is at or above $F$, so the
broken count is the number of probes that land at or above the threshold.

**Claim.** In the worst case, binary search breaks $\sim \lg N$ eggs.

_Proof._ Binary search makes $\sim \lg N$ throws whatever $F$ is, so the broken
count is at most that. It is reached when $F = 1$: every floor is then at or
above the threshold, so every throw breaks an egg. $\blacksquare$

### Part 2: doubling, then binary search

Binary search cannot meet that bound because its first throw commits it. With
$N = 1{,}000{,}000$ and $F = 3$ the budget is $2 \lg 3 \approx 3.2$ throws, but
a first throw from floor $500{,}000$ leaves a $500{,}000$-floor range to search,
about $19$ more throws. Whatever the strategy is, it has to start near the
bottom.

Starting at the bottom and stepping up one floor at a time costs $F$ throws, not
$\lg F$. The fix is to double the floor instead of incrementing it.

**Phase 1 — doubling.** Throw from floors $1, 2, 4, 8, \ldots$, that is
$2^0, 2^1, 2^2, 2^3, \ldots$, and stop at the first break. Every throw before
the last survives, so this phase breaks exactly one egg. If the first break is
at $2^k$, then floor $2^{k-1}$ survived and floor $2^k$ broke, so $F$ lies in
the bracket $(2^{k-1}, 2^k]$. Phase 1 makes $k + 1$ throws.

**Phase 2 — binary search in the bracket.** The bracket holds
$2^k - 2^{k-1} = 2^{k-1}$ floors. Binary search over $m$ candidates halves the
range each throw and so needs $\lg m$ throws to reach a single floor; with
$m = 2^{k-1}$ that is $\lg 2^{k-1} = k - 1$ throws.

Trace for $F = 8$ in a $100$-story building, starting with the whole building as
the candidate range:

| Phase | Throw from | Outcome | Candidates for $F$ |
| ----: | ---------: | :------ | -----------------: |
|   $1$ |        $1$ | survive |     $2 \ldots 100$ |
|   $1$ |        $2$ | survive |     $3 \ldots 100$ |
|   $1$ |        $4$ | survive |     $5 \ldots 100$ |
|   $1$ |        $8$ | break   |       $5 \ldots 8$ |
|   $2$ |        $6$ | survive |       $7 \ldots 8$ |
|   $2$ |        $7$ | survive |                $8$ |

Six throws, one broken egg, and $2 \lg 8 = 6$.

**Claim.** The strategy finds $F$ in $\sim 2 \lg F$ throws, breaking at most
$\sim \lg F$ eggs.

_Proof._ Phase 1 ends at the first $k$ with $2^k \ge F$, so
$2^{k-1} < F \le 2^k$, which gives $k - 1 < \lg F \le k$, i.e.
$k = \lceil \lg F \rceil$. Phase 1 makes $k + 1$ throws and phase 2 makes
$k - 1$, a total of $2k = 2 \lceil \lg F \rceil \sim 2 \lg F$. Phase 1 breaks
one egg; phase 2 breaks at most one per throw, $k - 1$ at most; so at most
$k = \lceil \lg F \rceil$ eggs break. $\blacksquare$

Neither count mentions $N$. That is the whole point: a fragile egg in a tall
building is found quickly because the doubling never travels further than it has
to.

Two edges to handle in the code. If the very first throw, from floor $1$,
breaks, then $F = 1$ and there is no bracket to search — one throw, one egg. And
the doubling can overshoot the building: when $2^k$ would exceed $N$, throw from
$N$ instead and take the bracket as $(2^{k-1}, N]$; the analysis is unchanged
because that bracket is no wider than $(2^{k-1}, 2^k]$.

This settles the guiding questions:

1. Binary search; the broken count equals the number of probes at or above $F$,
   and for $F = 1$ that is all of them.
2. Doubling: $1, 2, 4, 8, \ldots$ until a break, which is how one searches an
   infinite sorted sequence for a boundary.
3. The bracket is $(2^{k-1}, 2^k]$, of width $2^{k-1} < F$; finishing inside it
   costs $k - 1 \approx \lg F$ throws, and the two phases add to $\sim 2 \lg F$.
4. Phase 1 breaks exactly one egg and phase 2 at most $\lg F$, so the broken
   count is logarithmic in $F$ too.

### Practical notes

- **Simulate, don't build:** implement the "building" as a hidden threshold with
  a throw counter; that makes worst-case counting exact and lets you sweep every
  $F$ from $1$ to $N$.
- **Off-by-one discipline:** define precisely whether $F$ is the first breaking
  floor and keep `[lo, hi)` conventions consistent — boundary-finding is 90%
  interval bookkeeping.
- **Validate the bound empirically:** for each $F$, assert that throws
  $\le 2 \lg F + c$ for a small constant $c$; plot worst-case throws against $F$
  (log x-axis) and check the slope.

<br />
<br />
