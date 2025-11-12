# Exercise 1.4.6

Give the order of growth (as a function of $N$) of the running times of each of
the following code fragments:

### $a$.

```java
int sum = 0;
for (int n = N; n > 0; n /= 2)
    for (int i = 0; i < n; i++)
        sum++;
```

### $b$.

```java
int sum = 0;
for (int i = 1; i < N; i *= 2)
    for (int j = 0; j < i; j++)
        sum++;
```

### $c$.

```java
int sum = 0;
for (int i = 1; i < N; i *= 2)
    for (int j = 0; j < N; j++)
        sum++;
```

<br>

---

## The problem

Three loop nests, three orders of growth. Each fragment hides its cost in the
loop bounds — halving, doubling, and multiplying — so plain "count the nesting
depth" intuition fails; the tool that cracks all three is the geometric series.

## Order of Growth

"Order of growth (as a function of $N$)" asks: as $N$ gets large, what _shape_
of function does the running time follow — $N$? $N^2$? $\lg N$? $N \lg N$?
Unlike tilde notation, the coefficient is dropped: code performing $\sim 2N$
operations has order of growth $N$. It places the code into a growth-hierarchy
class rather than giving an exact estimate.

Running time is driven by how many times the innermost operation executes —
everything else is loop bookkeeping. So the task reduces to **counting
executions of the innermost statement as a function of $N$**.

### Steps

1. **Trace the outer loop.** List the values its variable takes. Watch the
   update rule — `n /= 2` and `i *= 2` don't step by 1, they halve or double, so
   the loop runs $\approx \lg N$ times, not $N$ times.
2. **For each outer value, count inner iterations.** The inner bound may be
   constant ($N$) or depend on the outer variable ($n$ or $i$) — that dependence
   is the whole game.
3. **Add up the counts.** Expect a series like
   $N + \frac{N}{2} + \frac{N}{4} + \cdots$ or $1 + 2 + 4 + \cdots$ — write out
   enough terms to recognize the pattern.
4. **Simplify and classify.** Find what the total is proportional to; drop the
   coefficient and lower-order terms. That is the order of growth.

<br>

## Exercises

### $a$.

```java
int sum = 0;
for (int n = N; n > 0; n /= 2)
    for (int i = 0; i < n; i++)
        sum++;
```

The outer variable $n$ starts at $N$ and halves each time, and for each pass the
inner loop runs $n$ times:

- 1st pass: $n = N$ → inner runs $N$ times
- 2nd pass: $n = N/2$ → inner runs $N/2$ times
- 3rd pass: $n = N/4$ → inner runs $N/4$ times
- … continuing until $n$ reaches $1$

The total work is the sum of all the inner counts:

$$
N + \frac{N}{2} + \frac{N}{4} + \cdots + 1 \approx 2N
$$

The $\cdots$ means "the pattern continues in the same way" — there are
$\approx \lg N$ terms, so they cannot all be written out.

**Check** with $N = 16$:

$$
16 + 8 + 4 + 2 + 1 = 31 \approx 32 = 2 \times 16
$$

A halving series never quite reaches twice its first term, but gets arbitrarily
close — which is why the total is $\approx 2N$.

Drop the coefficient from $2N$.

**Order of growth:** $N$

<br>

---

### $b$.

```java
int sum = 0;
for (int i = 1; i < N; i *= 2)
    for (int j = 0; j < i; j++)
        sum++;
```

The outer variable $i$ starts at $1$ and doubles each time, and for each pass
the inner loop runs $i$ times:

- 1st pass: $i = 1$ → inner runs $1$ time
- 2nd pass: $i = 2$ → inner runs $2$ times
- 3rd pass: $i = 4$ → inner runs $4$ times
- … continuing while $i < N$

The total work is the sum of all the inner counts — the same geometric series as
the first exercise, reversed: growing up toward $N$ instead of halving down from
it, and dominated by its largest term:

$$
1 + 2 + 4 + \cdots = 2 \times \text{largest term} - 1,
\quad \text{which lies between } N \text{ and } 2N
$$

The largest term is the biggest power of $2$ below $N$ — between $N/2$ and $N$
depending on where $N$ sits — so the exact constant wobbles, but anything
between $N$ and $2N$ is proportional to $N$.

**Check** with $N = 16$:

$$
1 + 2 + 4 + 8 = 15 = 2 \times 8 - 1 \approx N
$$

whereas $N = 17$ _does_ allow the pass $i = 16$:

$$
1 + 2 + 4 + 8 + 16 = 31 = 2 \times 16 - 1 \approx 2N
$$

Either way the constant is discarded.

**Order of growth:** $N$

<br>

---

### $c$.

```java
int sum = 0;
for (int i = 1; i < N; i *= 2)
    for (int j = 0; j < N; j++)
        sum++;
```

The outer variable $i$ starts at $1$ and doubles each time, and for each pass
the inner loop runs $N$ times — its bound doesn't depend on $i$ at all:

- 1st pass: $i = 1$ → inner runs $N$ times
- 2nd pass: $i = 2$ → inner runs $N$ times
- 3rd pass: $i = 4$ → inner runs $N$ times
- … continuing while $i < N$

The number of passes is how many times $1$ can double before reaching $N$ —
that's $\lg N$ (up to rounding). Since every pass contributes the same $N$,
there's no series to sum this time; the total is just a product:

$$
\underbrace{N + N + \cdots + N}_{\lg N \text{ passes}} = N \lg N
$$

**Check** with $N = 16$: passes are $i = 1, 2, 4, 8$ — that's $4 = \lg 16$
passes of $16$ each:

$$
\underbrace{4}_{\lg N} \times \underbrace{16}_{N} = 64 = 16 \times 4 = N \lg N
$$

**Order of growth:** $N \lg N$

<br>

---

<br>

## Geometric Series in Loop Analysis

A **geometric series** is a sum where each term is a fixed multiple of the
previous one (here, that multiple is $2$ or $\tfrac{1}{2}$):

$$
\underbrace{N + \frac{N}{2} + \frac{N}{4} + \cdots + 1}_{\approx \, 2N}
\qquad \text{or} \qquad
\underbrace{1 + 2 + 4 + \cdots + \frac{N}{2}}_{\approx \, N}
$$

(assuming $N$ is a power of 2 — both are geometric, both proportional to $N$)

### The key fact

A doubling/halving series is **dominated by its largest term**: the whole sum is
always less than twice it.

$$
\text{sum} \approx 2 \times \text{largest term}
$$

Why: fold the series up from the small end — $1 + 2 = 3 < 4$, then
$3 + 4 = 7 < 8$, then $7 + 8 = 15 < 16$ — the running total never catches up to
the next term. All the smaller terms together are worth less than one more copy
of the largest.

### Consequence for loops

When a loop variable halves (`n /= 2`) or doubles (`i *= 2`) and the inner work
is proportional to it, the total work is $\approx 2 \times$ the largest pass —
so the **order of growth equals the largest term**, and the factor 2 is
discarded with the coefficient:

- Fragment 1: $N + \tfrac{N}{2} + \tfrac{N}{4} + \cdots + 1 \approx 2N$ → order
  of growth $N$
- Fragment 2: $1 + 2 + 4 + \cdots + \tfrac{N}{2} \approx N$ → order of growth
  $N$

Direction doesn't matter: halving down from $N$ and doubling up toward $N$ give
the same kind of series (fragment 2 is fragment 1 reversed, minus its largest
term — a coefficient-level difference only). Either way the order of growth is
$N$.

### Contrast: arithmetic series

If the terms instead step by a constant (inner bound depends on a counter that
increments by 1), the largest term does _not_ dominate:

$$
1 + 2 + 3 + \cdots + N = \frac{N(N+1)}{2} \approx \frac{N^2}{2}
\quad \to \quad \text{order of growth } N^2
$$

**Check** with $N = 8$:

$$
1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 = 36
$$

Formula: $\frac{N(N+1)}{2} = \frac{8 \times 9}{2} = 36$ ✓ — and the
approximation $\frac{N^2}{2} = \frac{64}{2} = 32$ is close, with the gap ($36$
vs $32$) being the lower-order $\frac{N}{2}$ term that order-of-growth analysis
discards.

**Where the lower-order term comes from** — expand the exact formula; it splits
into two terms:

$$
\frac{N(N+1)}{2} = \frac{N^2 + N}{2} = \frac{N^2}{2} + \frac{N}{2}
$$

($N(N+1) = N^2 + N$ by distributing, then split the fraction.)

So the exact count is $\frac{N^2}{2}$ _plus_ $\frac{N}{2}$. The approximation
keeps only the dominant $\frac{N^2}{2}$ and discards the $\frac{N}{2}$ — which
is precisely the gap in the check:

$$
36 - 32 = 4 = \frac{8}{2} = \frac{N}{2} \; ✓
$$

This is the same "keep the leading term" move as tilde notation:
$\frac{N^2}{2} + \frac{N}{2} \sim \frac{N^2}{2}$, because the ratio
$1 + \frac{1}{N} \to 1$. The discarded gap grows in absolute terms (it's
$N/2$!), but shrinks _relative to the total_ — which is all asymptotic analysis
cares about.

### Rule of thumb

- **Multiplicative loop** (`i *= 2`, `n /= 2`) → geometric series → sum ≈
  largest term → order of growth = largest term
- **Additive loop** (`i++`) → arithmetic series → sum ≈ largest term × number of
  terms ÷ 2 → order of growth = one power higher

<br />
<br />
