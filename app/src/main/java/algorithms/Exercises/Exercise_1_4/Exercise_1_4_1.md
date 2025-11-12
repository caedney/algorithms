# Exercise 1.4.1

Show that the number of different triples that can be chosen from $N$ items is
precisely

$$
  N(N-1)(N-2)/6
$$

_Hint_: Use mathematical induction or a counting argument.

---

## The problem

A counting proof, not a program. The formula $N(N-1)(N-2)/6$ is exactly the
number of times `ThreeSum`'s `if` statement executes, so proving it is the first
step of the book's running-time analysis: connect the triple loop to a
closed-form count of the triples it visits, by induction or by a counting
argument.

## 1. Why we care: the ThreeSum algorithm

```java
for (int i = 0; i < N; i++)
    for (int j = i + 1; j < N; j++)
        for (int k = j + 1; k < N; k++)
            if (a[i] + a[j] + a[k] == 0)
                cnt++;
```

The running time of `ThreeSum` is driven by how many times the `if` executes.
The loop bounds force $i < j < k$, so the `if` runs exactly once per **triple**
of positions. Counting the triples counts the iterations — which is what this
exercise asks us to do.

## 2. Warm-up: pairs first

Before triples, understand the two-loop version:

```java
for (int i = 0; i < N; i++)
    for (int j = i + 1; j < N; j++)
        // body
```

List every possible $(i, j)$ pair with $i \ne j$ for $N = 5$: that's
$5 \times 4 = N(N-1)$ of them. But every pair has a mirror twin — $(1, 0)$ is
$(0, 1)$ swapped. Because $j$ always starts _above_ $i$, the loop keeps one twin
and skips the other, so it visits exactly half:

$$
  N(N-1)/2
$$

The $\div 2$ isn't done by the code — it's our bookkeeping for the mirrors the
loop skips.

### The Gauss trick: turning the sum into a formula

There's a second way to see the count. Counting the inner loop's runs per value
of $i$ (for $N = 5$): $i=0$ gives 4 runs, $i=1$ gives 3, $i=2$ gives 2, $i=3$
gives 1, $i=4$ gives 0. So the total is the sum

$$
  (N-1) + (N-2) + \dots + 2 + 1 + 0
$$

To turn that sum into a formula we use the **Gauss trick** (named after Gauss,
who supposedly used it as a schoolboy to sum 1 to 100 instantly): write the sum
twice — forwards and backwards — stack them, and add column by column.

Concrete demo with $N = 5$, so $S = 1 + 2 + 3 + 4$:

```
S = 1 + 2 + 3 + 4
S = 4 + 3 + 2 + 1
    -   -   -   -
    5   5   5   5
```

Every column adds to 5 — no coincidence: moving right, the top row goes up by 1
while the bottom goes down by 1, so the column total never changes. There are 4
columns, so both rows together give $2S = 4 \times 5 = 20$, hence $S = 10$.
Check: $1+2+3+4 = 10$ ✓

Now the same with letters, $S = 1 + 2 + \dots + (N-1)$:

$$
S = 1 + 2 + \dots + (N-1)
$$

$$
S = (N-1) + (N-2) + \dots + 1
$$

Each column adds to $N$ (first column $1 + (N-1) = N$, second $2 + (N-2) = N$,
...), and there are $N-1$ columns:

$$
  2S = N(N-1) \quad \Rightarrow \quad S = N(N-1)/2
$$

Same answer as the mirror-twin argument — two views of one fact. The Gauss trick
reappears implicitly whenever a "triangle-shaped" loop is analyzed (insertion
sort, selection sort, ThreeSum's inner structure).

## 3. The proof: counting argument for triples

### Claim

The number of different triples that can be chosen from $N$ items is
$N(N−1)(N−2)/6$.

### Proof

First count **ordered** selections of 3 different items:

1. $N$ choices
2. $N−1$ choices (can't reuse the 1st)
3. $N−2$ choices (can't reuse the first two)

Total: $N(N−1)(N−2)$ ordered selections.

But a _triple_ is unordered — { A, B, C } is the same triple as { C, A, B }.
Each triple gets counted once for every order it can be picked in, and three
items can be ordered $3 \times 2 \times 1 = 6$ ways:

{ A, B, C } { A, C, B } { B, A, C } { B, C, A } { C, A, B } { C, B, A }

So the list of $N(N−1)(N−2)$ ordered selections counts every triple exactly 6
times. The number of distinct triples is therefore

$$
  N(N−1)(N−2) / 6
$$

### Check with small cases

**N = 3, items { A, B, C }** — formula gives $3 \cdot 2 \cdot 1/6 = 1$, and by
inspection there's only one triple: { A, B, C }. (All 6 orderings above are that
_same_ triple: 6 ordered pickings ÷ 6 orders = 1.)

**N = 4, items { A, B, C, D }** — formula gives $4 \cdot 3 \cdot 2/6 = 4$, and
listing confirms it:

{ A, B, C } { A, B, D } { A, C, D } { B, C, D }

(Equivalently: choosing 3 out of 4 = choosing which 1 to leave out — 4 ways.)

## 4. Connecting the proof back to the code

The algorithm does **not** count each combination 6 times and divide — it visits
each triple exactly once. The combination at positions $(2, 5, 7)$ happens only
as $(i=2, j=5, k=7)$, never as $(5, 2, 7)$, because $j$ must be above $i$ and
$k$ above $j$.

The divide-by-6 lives in the math, not the code:

- All ordered arrangements of 3 positions: $N(N−1)(N−2)$.
- The loop visits only the "sorted" one out of every 6 arrangements.
- So iterations $= N(N−1)(N−2)/6$.

Same story as pairs, one level up: pairs skip 1 of every 2 arrangements
($\div 2$); triples skip 5 of every 6 ($\div 6$).

(A naive version where all three loops run from 0 to N _would_ touch each
combination 6 times — plus wasteful cases where indices repeat. The $i+1$/$j+1$
pattern is precisely the trick that avoids all that.)

## 5. From exact count to $\sim N^3/6$

### Step 1: Multiply out $N(N−1)(N−2)$

Take it two pieces at a time. First the last two brackets, using "each term
times each term" (First, Outer, Inner, Last — FOIL):

- $N \times N = N^2$
- $N \times (−2) = −2N$
- $(−1) \times N = −N$
- $(−1) \times (−2) = +2$

Add those up:

$$
  N^2 − 2N − N + 2 = N^2 − 3N + 2
$$

Now multiply the whole thing by the $N$ out front — $N$ times each term:

- $N \times N^2 = N^3$
- $N \times (−3N) = −3N^2$
- $N \times 2 = 2N$

So:

$$
  N(N−1)(N−2) = N^3 − 3N^2 + 2N
$$

Sanity check with $N = 5$:

- Original: $5 \times 4 \times 3 = 60$
- Expanded: $125 − 75 + 10 = 60$ ✓

### Step 2: Divide by 6

Divide each term of the expansion by 6:

- $N^3/6$
- $−3N^2/6 = −N^2/2$
- $2N/6 = N/3$

Giving:

$$
  N^3/6 − N^2/2 + N/3
$$

Check with $N = 5$ again:

- $60/6 = 10$
- $125/6 − 75/6 + 10/6 = 20.833 − 12.5 + 1.667 = 10$ ✓

### Step 3: Drop the small terms

Now the $\sim N^3/6$ claim is direct: as $N$ gets big, $N^3/6$ dwarfs the other
two terms. At $N = 1{,}000$, $N^3/6 \approx 166{,}700{,}000$ while $N^2/2$ is
only $500{,}000$ — a rounding error by comparison. So we keep the big term and
say the count grows like $N^3/6$.

That's the definition of Sedgewick's tilde notation: $f(N) \sim g(N)$ means
$f(N)/g(N) \to 1$ as $N \to \infty$.

## 6. Generalizing: FourSum and "N choose k"

Four nested loops, each starting one above the previous:

```java
for (int i = 0; i < N; i++)
    for (int j = i+1; j < N; j++)
        for (int k = j+1; k < N; k++)
            for (int l = k+1; l < N; l++)
                if (a[i] + a[j] + a[k] + a[l] == 0) count++;
```

The `if` runs once per group of 4 positions with $i < j < k < l$. Ordered
pickings: $N(N−1)(N−2)(N−3)$. Each group of 4 can be ordered
$4 \times 3 \times 2 \times 1 = 24$ ways, and the loop visits only the sorted
one — so:

$$
  N(N−1)(N−2)(N−3)/24 \; \sim \; N^4/24
$$

The divisor is always $k!$ ("k factorial") for $k$ nested loops of this shape —
2 for pairs, 6 for triples, 24 for quadruples, 120 for quintuples. This whole
family is the binomial coefficient "N choose k":

$$
  C(N, k) = N(N−1)(N−2)\cdots(N−k+1) / k!
$$

This is the _iteration count_ of the inner statement. The running time is
proportional to it (order of growth $N^4$), but the actual seconds depend on the
machine and the cost of the loop body — which is why the book keeps those two
ideas separate.

Think of C(N, k) like a function:

```java
static long C(long n, long k) {
    // multiply k factors counting down from n,
    // then divide by k!
}
```

Two inputs (how many items you have, how big a group you're picking), one output
(how many distinct groups exist).

- C(4, 3) = 4
- C(5, 2) = 10
- C(1000, 3) = 166,167,000

The right side of the equation

$$
  C(N, k) = N(N−1)⋯(N−k+1) / k!
$$

is then just the function body — the implementation. And like any function,
there's more than one valid implementation: the same value can be computed as
$N!/(k!(N−k)!)$ (the textbook definition) or via Pascal's triangle. Different
code, same outputs.

So the full picture: "binomial coefficient" is what the output value is called,
$C(N, k)$ or $\binom{N}{k}$ is the function you call to get it, and the
product-over-factorial formula is one way to compute it. Your ThreeSum analysis
was essentially evaluating $C(N, 3)$ by hand.

## 7. Final thoughts

### The doubling test

The practical payoff of $\sim N^3/6$: double $N$ and the running time multiplies
by $2^3 = 8$. Sedgewick's `DoublingRatio` experiment runs `ThreeSum` at $N$,
$2N$, $4N \dots$ and checks the ratio settles near 8 — an empirical check that
the math matches reality.

### The count vs the constant

Order of growth ($N^3$) tells you how time scales; the $/6$ and the
machine-dependent cost per iteration only affect the absolute time. That's why
analyses usually stop at "cubic."

### `ThreeSum` can be beaten

The triple loop is the brute-force solution. The book later shows
`ThreeSumFast`: sort the array ($N \log N$), then for each pair binary-search
for the value completing a zero-sum triple — $N^2 \log N$ overall. A two-pointer
technique gets to $\sim N^2$. Analysis motivates better algorithms: once you
know _why_ it's slow (that $N^3/6$ count), you look for ways to avoid
enumerating every triple.

### The math generalizes

This exercise essentially derives binomial coefficients — $C(N, k)$ shows up all
over algorithm analysis. And the sum trick $1+2+\dots+(N−1) = N(N−1)/2$ is the
single most reused identity in the field: it's the analysis of insertion sort,
selection sort, and every "triangle-shaped" double loop.

<br />
<br />
