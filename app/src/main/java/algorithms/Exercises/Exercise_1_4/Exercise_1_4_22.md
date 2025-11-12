# Exercise 1.4.22

_Binary search with only addition and subtraction_. [Mihai Patrascu] Write a
program that, given an array of $N$ distinct `int` values in ascending order,
determines whether a given integer is in the array. You may use only additions
and subtractions and a constant amount of extra memory. The running time of your
program should be proportional to $\log N$ in the worst case.

_Answer_: Instead of searching based on powers of two (binary search), use
Fibonacci numbers (which also grow exponentially). Maintain the current search
range to be the interval $[i, i + F_k]$ and keep $F_k$ and $F_{k-1}$ in two
variables. At each step compute $F_{k-2}$ via subtraction, check element
$i + F_{k-2}$, and update the current range to either $[i, i + F_{k-2}]$ or
$[i + F_{k-2}, i + F_{k-2} + F_{k-1}]$.

---

## The problem

Ordinary binary search computes `mid = lo + (hi - lo) / 2` — a **division**.
This exercise bans it (along with multiplication and bit shifts): only `+`, `-`,
and $O(1)$ extra memory, yet still logarithmic search. The book's Answer names
the trick — _Fibonacci search_ — and your work is to turn that two-sentence
sketch into correct code and to see why it satisfies every constraint.

The insight worth internalising: binary search doesn't fundamentally need
_halving_. It needs a family of interval sizes that (a) shrink by a constant
factor and (b) can be generated with allowed operations. Powers of two need
division to walk downward; Fibonacci numbers walk downward by subtraction alone:
$F_{k-2} = F_k - F_{k-1}$.

### A trace to build intuition

Fibonacci numbers: $1, 1, 2, 3, 5, 8, 13, 21, \dots$ For $N = 20$ the smallest
$F_k \ge N$ is $F_8 = 21$, so the search starts with the range $[0, 21)$ and the
pair $(21, 13)$.

| Range $[i, i + F_k)$ | Pair $(F_k, F_{k-1})$ | Probe $i + F_{k-2}$ | Outcome           |
| -------------------- | --------------------- | ------------------- | ----------------- |
| $[0, 21)$            | $(21, 13)$            | $0 + F_6 = 8$       | `a[8]` too small  |
| $[8, 21)$            | $(13, 8)$             | $8 + F_5 = 13$      | `a[13]` too big   |
| $[8, 13)$            | $(5, 3)$              | $8 + F_3 = 10$      | `a[10]` too small |
| $[10, 13)$           | $(3, 2)$              | $\dots$             | $\dots$           |

Each step is one subtraction, one probe, one pair update.

### Questions to guide your solution

1. The interval sizes go $F_k \to F_{k-2}$ or $F_{k-1}$. What's the worst-case
   shrink factor per probe, and why does that still give $O(\log N)$ (with a
   slightly larger constant than $\lg N$ — how much larger, given the golden
   ratio $\varphi \approx 1.618$)?
2. Setup: how do you find the starting $F_k \ge N$ using only additions? Does
   that pre-loop break the $O(\log N)$ bound?
3. The probe index $i + F_{k-2}$ can exceed $N - 1$ when $N$ isn't a Fibonacci
   number. How do you handle probes past the end without extra memory
   (conceptual padding with $+\infty$)?
4. Exactly which variables do you keep? List them and confirm the count is
   constant — no array of Fibonacci numbers allowed.

### The pair is the range

The state of the search is three `int`s: `i`, the left end of the range, and the
pair `fib1` $= F_k$, `fib2` $= F_{k-1}$. The range is $[i, i + F_k)$, so the
pair's first element _is_ the range's size, and the right end is never stored —
it is always `i + fib1`, and in fact the loop never needs to compute it. That
settles guiding question 4: three state variables plus a local for the probe
index, a count that has nothing to do with $N$.

The probe sits at $i + F_{k-2}$, and $F_{k-2}$ is one subtraction away:
`fib1 - fib2`. Each outcome discards one side of the probe and must leave the
pair describing what remains:

- **Key bigger than the probe** — discard the left part. The new range is
  $[i + F_{k-2},\ i + F_k)$, so `i` moves up to the probe and the pair steps
  down _once_, to $(F_{k-1}, F_{k-2})$.
- **Key smaller than the probe** — discard the right part. The new range is
  $[i,\ i + F_{k-2})$, so `i` stays where it is and the pair steps down _twice_,
  to $(F_{k-2}, F_{k-3})$.

**Claim.** After every step, `fib1` is the size of the current range.

_Proof._ It holds at the start, where the range is $[0, F_k)$. When the key is
bigger, the right end $i + F_k$ does not move and the left end advances by
$F_{k-2}$, so the new size is $F_k - F_{k-2} = F_{k-1}$ — the pair's new first
element. When the key is smaller, the left end does not move and the right end
comes back to $i + F_{k-2}$, so the new size is $F_{k-2}$ — again the pair's new
first element. $\blacksquare$

Both step-downs are the same two subtractions; only their order differs, and the
order is what decides how far the pair steps:

```java
// one step:  (F_k, F_{k-1}) -> (F_{k-1}, F_{k-2})
fib2 = fib1 - fib2;
fib1 = fib1 - fib2;

// two steps: (F_k, F_{k-1}) -> (F_{k-2}, F_{k-3})
fib1 = fib1 - fib2;
fib2 = fib2 - fib1;
```

From $(13, 8)$ the first gives $(8, 5)$ and the second $(5, 3)$; the
intermediate pair never exists, because the second subtraction uses the result
of the first. Neither needs a temporary, and both stay inside the allowed
operations. The trace above shows the two cases alternating: `i` moves on "too
small" and stays put on "too big".

### The starting pair, and why the search is still logarithmic

The pair is built upward by the mirror image of the step-down, from $(1, 1)$
until `fib1` reaches $N$:

```java
while (fib1 < n) {
    fib1 = fib1 + fib2; // F_{k+1}
    fib2 = fib1 - fib2; // F_k, recovered from the new fib1
}
```

Fibonacci numbers grow as $F_k \approx \varphi^k / \sqrt{5}$, so this takes
about $\log_\varphi N$ additions and ends holding exactly the $(F_k, F_{k-1})$
the search wants; there is nothing to memoise, because each number is produced
once and then replaced. That is guiding question 2: the pre-loop is logarithmic,
so it does not disturb the bound.

Question 1 has the same answer from the other side. The branch that shrinks
least is $F_k \to F_{k-1}$, a factor approaching $\varphi$ per probe, so the
worst case is about $\log_\varphi N = \lg N / \lg \varphi \approx 1.44 \lg N$
probes — some 44% more than binary search. The average is much closer to
$\lg N$: the measurements below put it within about 5%.

### The two edges

The book's sketch leaves two things unsaid. Both turned up by running a first
draft against a brute-force oracle rather than by inspection.

**Probing past the end.** The range starts as $[0, F_k)$ and $F_k$ is usually
larger than $N$, so a probe can land on an index that does not exist: for
$N = 20$ the range is $[0, 21)$, and a search for a key above the maximum walks
right until it probes index $20$. The fix is the "padding with $+\infty$" of
guiding question 3. An index past the end should behave like an element bigger
than every key — which is exactly what the "too big" branch already does — so
`probe >= n` becomes a second way into that branch:

```java
if (probe >= n || target < a[probe])
```

The `||` short-circuits, so the array is never read when the index is out of
range. This also covers the empty array with no separate guard: the first probe
is index $0$, $0 \ge 0$ holds, the pair steps to $(0, 1)$ and the loop ends. On
the draft that lacked this test, every non-Fibonacci array size could throw and
every Fibonacci size passed — when $N$ is itself a Fibonacci number the range is
exactly the array.

**The tail.** The identity $F_{k-2} = F_k - F_{k-1}$ stops describing the
sequence at the bottom. A "too big" from $(2, 1)$ leaves the pair at $(1, 0)$:
the range has one element, but the probe offset `fib1 - fib2` is $1$, which
points outside it. Left alone the pair continues $(1, -1), (2, -3), \dots$ and
the probe marches off the end. The smallest failing case is `a = [2, 10]` with
key `2`: index $1$ is probed twice and index $0$ never, so a key that is present
is reported absent. The fix is a clamp — never let `fib2` fall below $1$ in
either step-down:

```java
fib2 = fib2 - fib1 < 1 ? 1 : fib2 - fib1; // two-step branch
fib2 = fib1 - fib2 < 1 ? 1 : fib1 - fib2; // one-step branch
```

The clamp bites in exactly two places: the "too big" step from $(2, 1)$, which
now lands on $(1, 1)$ instead of $(1, 0)$, and the "too small" step from
$(1, 1)$, which lands on $(0, 1)$ instead of $(1, 0)$. With the pair at $(1, 1)$
the range is the single element at $i$ and the probe is $i$ itself, so the last
candidate is always examined, and from there both outcomes reach $(0, 1)$, where
`fib1` is zero and the loop exits. That is the termination argument the first
practical note asks for.

### Practical notes

- **Termination:** the loop invariant should shrink the pair $(F_k, F_{k-1})$
  down to the base case $(1, 1)$ — check the last couple of iterations by hand;
  off-by-one at the tail is the classic bug here.
- **No overflow drama:** $F_{46} = 1{,}836{,}311{,}903$ is the largest Fibonacci
  number that fits in an `int`. Java does allow arrays longer than that (up to
  about $2^{31}$ elements), and the setup loop would overflow on one — but such
  an array is over 7 GB of `int`s, so the ceiling is theoretical. Know where it
  is all the same.
- **Testing:** compare against `Arrays.binarySearch` for membership on random
  sorted distinct arrays, probing every value present and absent values between
  all gaps; test $N = 1$, $N = 2$, and exact-Fibonacci sizes vs non-Fibonacci
  sizes.

### The solution

Two loops over the same pair of variables: the first walks the Fibonacci
sequence up to $N$ by addition, the second walks it back down by subtraction
while the probe homes in. The method returns the index of the key, or $-1$ — a
stronger contract than the yes/no the exercise asks for, and free, since the
probe that finds the key _is_ its index.

```java
public static int findTarget(int[] a, int target) {
    int n = a.length;
    int fib1 = 1;
    int fib2 = 1;

    while (fib1 < n) {
        fib1 = fib1 + fib2;
        fib2 = fib1 - fib2;
    }

    int i = 0;

    while (i < i + fib1) {
        int probe = i + fib1 - fib2;

        if (probe >= n || target < a[probe]) {
            fib1 = fib1 - fib2;
            fib2 = fib2 - fib1 < 1 ? 1 : fib2 - fib1;
        } else if (target > a[probe]) {
            fib2 = fib1 - fib2 < 1 ? 1 : fib1 - fib2;
            fib1 = fib1 - fib2;
            i = probe;
        } else {
            return probe;
        }
    }

    return -1;
}
```

Design decisions that matter:

- **Range as start and size, not `lo` and `hi`.** The right end is `i + fib1`
  and is never stored, so there is no second boundary to keep in step with the
  pair. The loop condition `i < i + fib1` is "while the range is non-empty",
  written in those terms; `fib1 > 0` says the same thing more directly.
- **Swap by subtraction.** The step-up and both step-downs are two assignments
  in which the second subtracts the first's result, so none needs a temporary
  and the whole algorithm is additions, subtractions and comparisons — the
  constraint is met by construction, not by avoidance.
- **Only "too small" moves `i`.** Discarding the left part moves the left end;
  discarding the right part only shrinks the size. Getting this backwards is the
  mistake the trace table exists to prevent.
- **`probe >= n` folded into "too big".** One extra test, evaluated first, and
  the array is never touched out of bounds. It removes the need for an `n == 0`
  guard, which a first draft had and a later one dropped.
- **The clamp.** `< 1 ? 1 :` on `fib2` in both branches keeps the pair off the
  negative tail of the sequence. The alternative is to run the loop while
  `fib1 > 1` and test the single remaining candidate afterwards; that works too,
  but needs the extra statement after the loop.

Cost. The setup loop is $\sim \log_\varphi N$ additions. The search makes at
most $\sim \log_\varphi N \approx 1.44 \lg N$ probes, each costing one array
read, one or two comparisons and two or three subtractions. Extra memory is
three `int`s and the probe local — constant. Both loops are $O(\log N)$, as
required.

**Verification.** Compiled and cross-checked against a linear-scan oracle on
20,000 random sorted distinct arrays of length 0 to 59, querying every key from
one below the minimum to one above the maximum — 1.9 million queries, all in
agreement, no exceptions. The two drafts on the way both passed inspection and
failed the oracle: the first threw on 69% of queries (probes past the end), and
the one that fixed only the tail still threw on every non-Fibonacci size. Then a
doubling run on arrays of even numbers from $N = 2^{10}$ to $2^{24}$, 200,000
random keys per row (roughly half present), counting probes per query:

|        $N$ | $\lg N$ | $\log_\varphi N$ | probes/query | increment | worst |
| ---------: | ------: | ---------------: | -----------: | --------: | ----: |
|     $1024$ |    $10$ |          $14.40$ |      $10.73$ |         — |  $15$ |
|     $2048$ |    $11$ |          $15.84$ |      $11.55$ |    $0.82$ |  $16$ |
|     $4096$ |    $12$ |          $17.28$ |      $12.49$ |    $0.94$ |  $17$ |
|     $8192$ |    $13$ |          $18.73$ |      $13.69$ |    $1.20$ |  $19$ |
|    $16384$ |    $14$ |          $20.17$ |      $14.58$ |    $0.89$ |  $20$ |
|    $32768$ |    $15$ |          $21.61$ |      $15.81$ |    $1.23$ |  $22$ |
|    $65536$ |    $16$ |          $23.05$ |      $16.69$ |    $0.88$ |  $23$ |
|   $131072$ |    $17$ |          $24.49$ |      $17.97$ |    $1.28$ |  $25$ |
|   $262144$ |    $18$ |          $25.93$ |      $18.81$ |    $0.85$ |  $26$ |
|   $524288$ |    $19$ |          $27.37$ |      $20.13$ |    $1.32$ |  $27$ |
|  $1048576$ |    $20$ |          $28.81$ |      $20.95$ |    $0.82$ |  $29$ |
|  $2097152$ |    $21$ |          $30.25$ |      $21.87$ |    $0.92$ |  $30$ |
|  $4194304$ |    $22$ |          $31.69$ |      $23.07$ |    $1.21$ |  $31$ |
|  $8388608$ |    $23$ |          $33.13$ |      $23.97$ |    $0.90$ |  $32$ |
| $16777216$ |    $24$ |          $34.57$ |      $25.21$ |    $1.24$ |  $34$ |

The worst case tracks $\log_\varphi N$ almost exactly — that is the $1.44$ of
guiding question 1 made visible. The average grows by about one probe per
doubling, landing near $1.05 \lg N$: Fibonacci search pays its 44% only in the
worst case, and on typical keys costs about 5% more than binary search. The
increments alternate high and low because a doubling of $N$ does not align with
the Fibonacci sizes; from $N = 2^{13}$ upward, two doublings together add $2.1$
probes almost exactly.

<br />
<br />
