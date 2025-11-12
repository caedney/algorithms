# Exercise 1.4.23

_Binary search for a fraction_. Devise a method that uses a logarithmic number
of queries of the form _Is the number less than $x$?_ to find a rational number
$p/q$ such that $0 < p < q < N$. _Hint_: Two fractions with denominators less
than $N$ cannot differ by more than $1/N^2$.

---

## Correction

The hint is printed with its inequality reversed. Two _distinct_ fractions with
denominators less than $N$ cannot differ by _less_ than $1/N^2$ — they are
always further apart than that, never closer. As printed the hint says the
opposite, that any two such fractions are within $1/N^2$ of each other, which is
plainly false: $1/2$ and $1/3$ differ by $1/6$. Read the right way round it is
the key to the exercise, and it is derived below.

## The problem

A hidden rational number $p/q$ lives strictly between $0$ and $1$, with
denominator smaller than $N$. Your only tool is a comparison oracle: "is the
secret less than $x$?" for any $x$ you choose. Find the exact fraction in
$O(\log N)$ queries.

The twist versus ordinary binary search: the search space _looks_ continuous (an
interval of real numbers), but the answer is drawn from a **finite, structured
set** — the fractions with denominator $< N$. How many there are, and what that
says about the query budget, is worked out below. The hint is the bridge between
the continuous and discrete views: distinct candidates can't be closer than
$1/N^2$. For any two different candidates $\frac{a}{b}$ and $\frac{c}{d}$ (so
$b, d < N$),

$$
\frac{a}{b} \ne \frac{c}{d}, \; b, d < N \implies \left| \frac{a}{b} - \frac{c}{d} \right| = \frac{|ad - cb|}{bd} \ge \frac{1}{bd} > \frac{1}{N^2}
$$

The first step is ordinary subtraction of fractions over a common denominator.
The second holds because $|ad - cb|$ is a whole number and not zero (the
fractions differ), so it is at least $1$. The third because both denominators
are below $N$, so $bd < N^2$. With $\frac{3}{7}$ and $\frac{2}{7}$ at $N = 10$:
$\frac{7}{49} \ge \frac{1}{49} > \frac{1}{100}$. Throughout the rest of this
write-up $p/q$ means the secret and nothing else.

### How many candidates, and how many questions

The candidates, sorted, are a named object: the **Farey sequence of order $n$**
is every fraction in lowest terms between $0$ and $1$ with denominator at most
$n$, in increasing order. Order $4$ is

$$
\frac{0}{1}, \; \frac{1}{4}, \; \frac{1}{3}, \; \frac{1}{2}, \; \frac{2}{3}, \; \frac{3}{4}, \; \frac{1}{1}
$$

Our denominators are below $N$, so the candidates are the Farey sequence of
order $N - 1$ minus the two endpoints the exercise excludes. Counting them for
$N = 10$ takes two steps.

**Pairs.** A pair $(p, q)$ with $0 < p < q < 10$ is a choice of two different
numbers from $\{1, \ldots, 9\}$, the smaller being $p$. That is a binomial
coefficient:

$$
\binom{9}{2} = \frac{9 \cdot 8}{2} = 36
$$

(or, denominator by denominator, $1 + 2 + \cdots + 8 = 36$).

**Distinct values.** A pair repeats a value already counted exactly when $p$ and
$q$ share a factor — $\frac{2}{4}$ is $\frac{1}{2}$, $\frac{6}{9}$ is
$\frac{2}{3}$ — so the distinct values are the pairs with $\gcd(p, q) = 1$. For
each $q$ the number of such $p$ is **Euler's totient** $\varphi(q)$: how many of
$1, \ldots, q - 1$ share no factor with $q$.

You can count it directly. $8 = 2 \cdot 2 \cdot 2$ has the single prime $2$, so
"shares a factor with $8$" means "is even"; cross out the evens and four
survive:

$$
1, \; \cancel{2}, \; 3, \; \cancel{4}, \; 5, \; \cancel{6}, \; 7 \qquad \varphi(8) = 4
$$

Or use the formula, which does the same crossing-out by fractions. Every second
number is a multiple of $2$, every third a multiple of $3$ — in general a share
$\frac{1}{r}$ of all numbers are multiples of a prime $r$, so removing them
keeps $\left(1 - \frac{1}{r}\right)$. Do that once for each distinct prime
dividing $q$:

$$
\varphi(q) = q \prod_{\text{prime } r \,\mid\, q} \left(1 - \frac{1}{r}\right)
$$

The $\prod$ is a product sign — "multiply together one bracket per prime" — so
the number of brackets depends on $q$:

- $q = 8$, primes $\{2\}$, one bracket:
  $8 \cdot \left(1 - \frac{1}{2}\right) = 8 \cdot \frac{1}{2} = 4$
- $q = 9$, primes $\{3\}$, one bracket:
  $9 \cdot \left(1 - \frac{1}{3}\right) = 9 \cdot \frac{2}{3} = 6$
- $q = 6$, primes $\{2, 3\}$, two brackets:
  $6 \cdot \left(1 - \frac{1}{2}\right)\left(1 - \frac{1}{3}\right) = 6 \cdot \frac{1}{2} \cdot \frac{2}{3} = 2$
- $q$ prime, one bracket: $q \cdot \left(1 - \frac{1}{q}\right) = q - 1$

(The prime is called $r$ rather than $p$ only because $p$ is the numerator in
this paragraph.)

| $q$ | $p$ with $\gcd(p, q) = 1$ | $\varphi(q)$ |
| --: | :------------------------ | -----------: |
| $2$ | $1$                       |          $1$ |
| $3$ | $1, 2$                    |          $2$ |
| $4$ | $1, 3$                    |          $2$ |
| $5$ | $1, 2, 3, 4$              |          $4$ |
| $6$ | $1, 5$                    |          $2$ |
| $7$ | $1, \ldots, 6$            |          $6$ |
| $8$ | $1, 3, 5, 7$              |          $4$ |
| $9$ | $1, 2, 4, 5, 7, 8$        |          $6$ |

Total $27$: the $36$ pairs less the $9$ reducible ones. So the exact count of
candidates for a given $n = N - 1$ is

$$
M = \sum_{q = 2}^{n} \varphi(q)
$$

and unlike $\binom{n}{2}$ for the pairs, **there is no closed form for this
sum**. $\varphi$ jumps around with each $q$'s prime factorisation
($\varphi(7) = 6$, $\varphi(8) = 4$, $\varphi(9) = 6$), so no formula takes $n$
and hands back $M$; it has to be built up $q$ by $q$. The options:

| you want         | method                                                         | cost               |
| :--------------- | :------------------------------------------------------------- | :----------------- |
| exact, small $n$ | count pairs with $\gcd(p, q) = 1$                              | $O(n^2)$           |
| exact, large $n$ | a sieve filling in $\varphi(1 \ldots n)$ in one pass, then sum | $O(n \log \log n)$ |
| an estimate      | $3n^2/\pi^2$                                                   | one multiplication |

None of this runs in the algorithm — it never builds the list or calls `gcd` —
it is only how the count is known, and for the exercise the estimate is all that
is needed: its job is to say "about $N^2$ candidates, so $\lg$ of that is about
$2 \lg N$". The estimate is the sequence's well-studied asymptotic size: rough
for small $n$, converging on exact as $n$ grows:

|       $N$ | $n = N - 1$ |  candidates | $3n^2/\pi^2$ |  ratio |
| --------: | ----------: | ----------: | -----------: | -----: |
|       $4$ |         $3$ |         $3$ |        $2.7$ | $1.10$ |
|      $10$ |         $9$ |        $27$ |       $24.6$ | $1.10$ |
|     $100$ |        $99$ |   $3{,}003$ |    $2{,}979$ | $1.01$ |
| $1{,}000$ |       $999$ | $303{,}791$ |  $303{,}356$ | $1.00$ |

The size matters because of a counting argument from information theory. A
yes/no answer is one _bit_, and one bit can at best halve the set of
possibilities still open. So with $M$ candidates, after $k$ questions — $k$
being simply a count of how many have been asked — at least $M / 2^k$ remain,
and the secret is pinned down only when that is $1$. For $N = 10$, $M = 27$:

| questions asked $k$ | $27 / 2^k$ | still possible (at least) |
| ------------------: | ---------: | ------------------------: |
|                 $0$ |       $27$ |                      $27$ |
|                 $1$ |     $13.5$ |                      $14$ |
|                 $2$ |     $6.75$ |                       $7$ |
|                 $3$ |      $3.4$ |                       $4$ |
|                 $4$ |      $1.7$ |                       $2$ |
|                 $5$ |     $0.84$ |                       $1$ |

The last column first reaches $1$ at $k = 5$, so any method whatever needs at
least $5$ questions for $N = 10$; in general $k \ge \lg M$, and
$\lg 27 \approx 4.75$. "At least" because halving is the _best_ a question can
do: one that splits the candidates $20$ against $7$ rather than $14$ against
$13$ leaves more behind when the answer points at the $20$. The floor assumes
every question is as good as a question can be, which is why nothing can beat
it. (The same argument is what shows comparison sorting needs $\sim N \lg N$
compares.)

Now put $M \sim 3N^2/\pi^2$ through the three log rules, one at a time:

- **log of a product is the sum of the logs**:
  $\lg(3 \cdot N^2) = \lg 3 + \lg N^2$
- **log of a quotient is the difference of the logs**:
  $\lg \dfrac{3 N^2}{\pi^2} = \lg(3 N^2) - \lg \pi^2$
- **log of a power brings the exponent down as a coefficient**:
  $\lg N^2 = 2 \lg N$, and $\lg \pi^2 = 2 \lg \pi$

$$
\begin{aligned}
\lg M &= \lg \frac{3 N^2}{\pi^2} \\
      &= \lg(3 N^2) - \lg \pi^2 && \text{(quotient)} \\
      &= \lg 3 + \lg N^2 - \lg \pi^2 && \text{(product)} \\
      &= \lg 3 + 2 \lg N - 2 \lg \pi && \text{(power, twice)} \\
      &= 2 \lg N + \underbrace{(1.585 - 3.303)}_{\lg 3 \,-\, 2 \lg \pi} \\
      &= 2 \lg N - 1.72
\end{aligned}
$$

So the floor is $2 \lg N$ **minus a constant** of about $1.7$, and that is what
"$\lg M \sim 2 \lg N$" means: the tilde says the _ratio_ tends to $1$, not that
the two are equal. A fixed $1.7$ is a large share of $6.6$ and a negligible
share of $40$:

|             $N$ | $2 \lg N$ | $\lceil \lg M \rceil$ |  ratio |
| --------------: | --------: | --------------------: | -----: |
|            $10$ |     $6.6$ |                   $5$ | $0.72$ |
|       $1{,}000$ |    $19.9$ |                  $19$ | $0.91$ |
| $1{,}000{,}000$ |    $39.9$ |                  $39$ | $0.96$ |

(The table above counts with $n = N - 1$ while the formula is written with $N$;
the two differ by the same kind of vanishing constant.) This is why the exercise
asks for a logarithmic number of queries and why nothing could do better than
that order — at $N = 10$ the exact floor is $5$, and any method must ask at
least that many.

### What the hint buys you

If you can trap the secret in an interval of width at most $1/N^2$, **at most
one** candidate fraction survives inside it. So a two-phase plan suggests
itself: narrow first, then identify. The identification step is the interesting
part — given a tiny interval known to contain exactly one fraction with
denominator $< N$, how do you _name_ it?

### Comparing two fractions without dividing

Every query asks whether the secret $p/q$ is less than some $a/b$, and the
answer must be exact. Don't divide. Multiply both sides by $qb$ — positive,
since both denominators are — and the direction of the inequality is unchanged:

$$
\frac{p}{q} < \frac{a}{b} \iff pb < aq
$$

Each numerator is multiplied by the _other_ fraction's denominator, and the two
products are compared. The same move decides $=$ and $>$. Two consequences for
this exercise: the test uses integer arithmetic only, so there is no rounding to
worry about even when candidates sit $1/N^2$ apart; and the products reach about
$2N^3$ (a numerator below $N$ times a query denominator up to $2N^2$), so do the
arithmetic in `long`.

### Midpoint versus mediant

Two different ways to get a fraction that lies between $\frac{a}{b}$ and
$\frac{c}{d}$, and it is easy to reach for the wrong one.

The **midpoint** is the average, halfway between the two:

$$
\frac{1}{2}\left(\frac{a}{b} + \frac{c}{d}\right) = \frac{ad + cb}{2bd}
$$

When the denominators are equal this collapses to $\frac{a + c}{2b}$ — note the
$2$. Halving an interval, as in ordinary binary search, means asking about the
midpoint.

The **mediant** adds numerators and denominators separately:

$$
\frac{a}{b} \oplus \frac{c}{d} = \frac{a + c}{b + d}
$$

It always lands strictly between the two fractions, but not halfway. For
$\frac{1}{2}$ and $\frac{1}{1}$:

|          |                               value |
| :------- | ----------------------------------: |
| midpoint |     $\frac{1 + 2}{4} = \frac{3}{4}$ |
| mediant  | $\frac{1 + 1}{2 + 1} = \frac{2}{3}$ |

The two coincide only when the denominators are equal, which is why a
same-denominator trace can look like "add the numerators" and mislead. Phase 1
wants the midpoint; the mediant is the operation behind the Stern–Brocot tree
named in the guiding questions, and it comes into its own later.

### Questions to guide your solution

1. Phase 1: how many halvings of $(0, 1)$ does it take to reach width
   $\le 1/(2N^2)$? Express the query count in terms of $\lg N$.
2. Phase 2: with the interval pinned down, one approach tries each denominator
   $q = 1, \ldots, N - 1$ and asks which numerator $p$ could land inside — but
   that's $O(N)$ arithmetic (no queries, though!). Does the exercise's budget
   constrain _queries_ or _all_ computation? Both readings are defensible; can
   you solve the stronger one?
3. For the stronger version, look up (or rediscover) the **Stern–Brocot tree**:
   every rational in lowest terms sits in a binary search tree of mediants. What
   does one oracle query correspond to in that tree, and why might a naive walk
   cost more than $O(\log N)$ steps (think $1/N$)? What fixes it?
4. Where exactly does the argument need $0 < p < q$ (proper fraction, nonzero) —
   what would break if $p/q$ could equal $0$ or $1$?

### Phase 1: narrowing the interval

The finder never holds a guess. It holds an **interval** known to contain the
secret, and shrinks it. Both ends are kept over one shared denominator, so the
state is three integers — `lo`, `hi` and `den` — meaning

$$
\frac{lo}{den} \le \text{secret} < \frac{hi}{den}, \qquad hi - lo = 1
$$

The interval is half-open: the lower end is included, the upper end is not. That
comes straight from the oracle — "is it less than $x$?" answering _no_ means the
secret is $x$ **or above**, so $x$ becomes an inclusive lower bound.

Start with $\frac{0}{1} \le \text{secret} < \frac{1}{1}$, which is exactly
$0 < p < q$ rewritten. One step asks about the midpoint
$\frac{lo + hi}{2 \cdot den}$ and keeps the half that answered:

- _yes_ (secret is below the midpoint): $lo \leftarrow 2 \cdot lo$,
  $hi \leftarrow lo + hi$
- _no_ (secret is at or above it): $lo \leftarrow lo + hi$,
  $hi \leftarrow 2 \cdot hi$

and then $den \leftarrow 2 \cdot den$, so both bounds are once more over the
shared denominator with $hi - lo = 1$. The width is always exactly $1/den$.

**When to stop.** Distinct candidates are more than $1/N^2$ apart, so once the
width is $1/N^2$ or less the interval can hold at most one of them — and it
holds at least one, because the secret never left. So the loop runs while
$den < N^2$. Since $den$ doubles from $1$, that is $\lceil 2 \lg N \rceil$
queries, and every secret takes the same number: Phase 1 is a fixed-length loop.

For $N = 10$ and secret $\frac{3}{7} \approx 0.4286$, $den$ must reach $128$:

| query |              asks "less than…?" | answer |                                interval after |
| ----: | ------------------------------: | :----- | --------------------------------------------: |
|   $1$ |             $\frac{1}{2} = 0.5$ | yes    |       $\left[\frac{0}{2}, \frac{1}{2}\right)$ |
|   $2$ |            $\frac{1}{4} = 0.25$ | no     |       $\left[\frac{1}{4}, \frac{2}{4}\right)$ |
|   $3$ |           $\frac{3}{8} = 0.375$ | no     |       $\left[\frac{3}{8}, \frac{4}{8}\right)$ |
|   $4$ |         $\frac{7}{16} = 0.4375$ | yes    |     $\left[\frac{6}{16}, \frac{7}{16}\right)$ |
|   $5$ |  $\frac{13}{32} \approx 0.4063$ | no     |   $\left[\frac{13}{32}, \frac{14}{32}\right)$ |
|   $6$ |  $\frac{27}{64} \approx 0.4219$ | no     |   $\left[\frac{27}{64}, \frac{28}{64}\right)$ |
|   $7$ | $\frac{55}{128} \approx 0.4297$ | yes    | $\left[\frac{54}{128}, \frac{55}{128}\right)$ |

Seven queries — $\lceil 2 \lg 10 \rceil = \lceil 6.64 \rceil = 7$ — and the
secret is trapped in an interval of width $\frac{1}{128} < \frac{1}{100}$.

This settles the first guiding question, and slightly improves on it: a width of
$1/N^2$ is already enough, because the gap bound is strict. It also answers the
fourth. The starting interval $[0, 1)$ is exactly the set of legal secrets
because $0 < p < q$: a secret of $1$ would never be trapped — every answer would
be _no_, $lo$ would climb toward $1$, and $1$ itself is never inside a half-open
interval ending at $1$ — and a secret of $0$ would break the lowest-terms
property of Phase 2 below, since $\frac{0}{2}$ would be accepted before
$\frac{0}{1}$ is ever considered.

### Phase 2: naming the candidate

The interval $\left[\frac{lo}{den}, \frac{hi}{den}\right)$ contains exactly one
fraction with denominator below $N$, but the bounds are dyadic ($54/128$), not
the answer ($3/7$). Identifying it costs no queries at all.

Fix the denominator. For a given $q$, the candidates $\frac{p}{q}$ are spaced
$\frac{1}{q}$ apart, and the interval is far narrower than that, so **at most
one numerator** can land inside. It has to be the smallest $p$ that clears the
lower bound:

$$
\frac{p}{q} \ge \frac{lo}{den} \iff p \ge \frac{lo \cdot q}{den} \iff p = \left\lceil \frac{lo \cdot q}{den} \right\rceil
$$

and it is a genuine hit only if that $p$ also stays under the upper bound:
$\frac{p}{q} < \frac{hi}{den}$, i.e. $p \cdot den < hi \cdot q$ by
cross-multiplication. Try $q = 2, 3, \ldots, N - 1$ and return on the first hit.

The ceiling in integer arithmetic is the idiom
$\lceil a / b \rceil = (a + b - 1) / b$ for positive integers: Java's `/` rounds
down, and adding $b - 1$ first pushes any non-zero remainder over to the next
whole number while leaving an exact multiple where it is.

For the trace above, $lo = 54$, $hi = 55$, $den = 128$:

| $q$ | $p = \lceil 54 q / 128 \rceil$ | $p \cdot 128 < 55 q$? |
| --: | -----------------------------: | :-------------------- |
| $2$ |                            $1$ | $128 < 110$ — no      |
| $3$ |                            $2$ | $256 < 165$ — no      |
| $4$ |                            $2$ | $256 < 220$ — no      |
| $5$ |                            $3$ | $384 < 275$ — no      |
| $6$ |                            $3$ | $384 < 330$ — no      |
| $7$ |                            $3$ | $384 < 385$ — **yes** |
| $8$ |                            $4$ | $512 < 440$ — no      |
| $9$ |                            $4$ | $512 < 495$ — no      |

Exactly one row passes, as the hint guarantees, and it names both $q$ and $p$.

Two properties fall out of the loop's shape. The result is **in lowest terms**
without any reduction step: if $\frac{6}{8}$ fitted, $\frac{3}{4}$ would have
fitted first, because $q$ runs upward. And the two bounds are tested in the
**same half-open sense** Phase 1 established them in — ceiling for the inclusive
lower bound, strict $<$ for the exclusive upper — so a secret sitting exactly on
$lo/den$ is still found.

This is the first reading of the second guiding question: the exercise's budget
is stated in _queries_, and Phase 2 uses none, so the two phases together answer
the exercise as set — $\lceil 2 \lg N \rceil$ queries, $O(N)$ arithmetic. The
stronger version, $O(\log N)$ arithmetic as well, is the third guiding question
and is still open here; the mediant is the tool for it.

### Practical notes

- **Exact arithmetic:** compare fractions by cross-multiplication as above,
  never with `double` — doubles cannot represent the candidates exactly and the
  whole point is exactness near $1/N^2$ gaps.
- **Simulate the oracle** as a class holding a hidden fraction and a comparison
  counter; assert the count stays within your claimed bound across all hidden
  fractions for small $N$ (exhaustive: every $p/q$ with $q < N$).
- **Watch for overflow:** the query denominators climb to $N^2$ and the
  cross-products to $\sim 2N^3$ — `int` gives out around $N \approx 1000$,
  `long` is good to $N \approx 10^6$.

### The solution

An `Oracle` holds the target as a `Fraction` alongside `denominatorBound` — the
book's $N$, named for what it bounds — and counts the questions put to it; the
finder reads the bound from it and otherwise only ever asks `isTargetLessThan`.
Target, query and answer are all `Fraction`s, so the call reads as the question
it is — _is the target less than the midpoint?_ — and the comparison inside is
the cross-multiplication from the notes with the names on it:
`target.p * x.q < x.p * target.q`. Phase 1 halves a shared-denominator interval
until its width is at most $1/N^2$; Phase 2 walks the denominators and returns
the one candidate that fits.

```java
static class Fraction {
    long p;
    long q;

    public Fraction(long p, long q) {
        this.p = p;
        this.q = q;
    }

    public String toString() {
        return p + "/" + q;
    }
}

static class Oracle {
    Fraction target;
    int denominatorBound;
    int count;

    public Oracle(int denominatorBound, Fraction target) {
        if (0 >= target.p || target.p >= target.q || target.q >= denominatorBound) {
            throw new IllegalArgumentException("Need 0 < p < q < N");
        }

        this.target = target;
        this.denominatorBound = denominatorBound;
    }

    public boolean isTargetLessThan(Fraction x) {
        count++;
        return target.p * x.q < x.p * target.q;
    }
}

public static Fraction findFraction(Oracle oracle) {
    long lo = 0;
    long hi = 1;
    long denominator = 1;
    int N = oracle.denominatorBound;
    long limit = (long) N * N;

    while (denominator < limit) {
        Fraction midpoint = new Fraction(lo + hi, 2 * denominator);

        if (oracle.isTargetLessThan(midpoint)) {
            lo = 2 * lo;
            hi = midpoint.p;
        } else {
            lo = midpoint.p;
            hi = 2 * hi;
        }

        denominator *= 2;
    }

    for (int q = 2; q < N; q++) {
        long p = (lo * q + denominator - 1) / denominator;

        if (p * denominator < hi * q) {
            return new Fraction(p, q);
        }
    }

    throw new IllegalStateException("No candidate in interval - Phase 1 invariant broken");
}
```

The decisions that matter:

- **The finder never looks inside the oracle.** It receives an `Oracle`, reads
  the public rule `denominatorBound` from it (as the local `N`, so the code
  matches the maths), and otherwise only asks `isTargetLessThan`; the
  constructor is the one place the rule $0 < p < q < N$ is enforced, with a real
  exception rather than an `assert` (which is off unless the JVM runs with
  `-ea`). Nothing stops `findFraction` reading `oracle.p` — the class is nested,
  so even `private` would not — and the rule stays a stated convention rather
  than a compiler-enforced one.
- **Cross-multiplication throughout**, never `double`. `Fraction` fields are
  `long`, so `isTargetLessThan` multiplies in `long` without a cast.
- **`long` for the fractions and working variables.** The Phase 1 denominator is
  the first power of two at or above $N^2$, and the products reach $\sim 2N^3$;
  `int` overflows around $N \approx 1000$ and the failure is silent — an
  overflowed `limit` or `denominator` ends the loop early and Phase 2 names the
  wrong fraction. `long` moves the ceiling to $N \approx 10^6$ without a guard,
  which is as far as the exercise will ever need. The `(long) N * N` cast exists
  because both operands there are `int`.
- **Half-open on both sides of the code.** Phase 1's _no_ answer makes the lower
  bound inclusive; Phase 2 mirrors that with a ceiling for `lo` and a strict `<`
  for `hi`.
- **Ascending $q$ gives lowest terms for free**, so no `gcd` is needed on the
  way out.
- **Throw, don't return `null`**, if Phase 2 finds nothing: it cannot happen
  while Phase 1 is correct, so it should be loud when it does.

**Cost.** Phase 1 asks exactly $\lceil 2 \lg N \rceil$ questions regardless of
the target. Phase 2 asks none and does at most $N - 2$ constant-time checks.
Total: $\sim 2 \lg N$ queries, $O(N)$ arithmetic. Against the
information-theoretic floor of $\lceil \lg M \rceil$ for $M$ candidates — $5$,
$12$, $19$ at $N = 10, 100, 1000$ — the $7$, $14$, $20$ queries here are within
a couple, so the budget is the right order even if not the exact minimum.

**Verification.** Checked against every legal target for every $N$ from $3$ to
$300$ — $4{,}455{,}100$ runs — plus $N = 1{,}000{,}000$ with target
$\frac{314159}{999983}$: every answer equal in value to the target, every answer
a legal fraction in lowest terms, no run exceeding $\lceil 2 \lg N \rceil$
queries ($40$ at $N = 10^6$). `Exercise_1_4_23Test` carries the trace, the
lowest-terms cases, the extremes, an exhaustive sweep to $N = 40$, a fixed-seed
random sweep to $N = 500$, the exact query count at eight sizes, and the
large-$N$ case.

One draft went wrong in an instructive way: it returned `new Fraction(lo, hi)`
after Phase 1 — for $\frac{3}{7}$ that is $\frac{54}{55}$, the two numerators
with the shared denominator dropped. It had trapped the target perfectly and
then reported the trap instead of the prisoner. The tests were then run against
that draft and three others to confirm they bite: the $\frac{54}{55}$ draft
fails $7$ of $9$, floor instead of ceiling in Phase 2 fails $6$, stopping at
$den \ge N$ instead of $N^2$ fails $6$, and `int` throughout passes every small
case and fails only the two large-$N$ tests. One mutant survives: $\le$ in place
of $<$ on the upper bound passes all nine — and rightly so, since $hi/den$ can
never itself be a candidate (it would sit within $1/N^2$ of the target), so the
two comparisons are indistinguishable. The strict form is kept because it is
what the half-open interval means.

Doubling $N$ adds two queries, as $\lceil 2 \lg N \rceil$ predicts:

|       $N$ | queries | increment |
| --------: | ------: | --------: |
|      $16$ |     $8$ |         — |
|      $32$ |    $10$ |       $2$ |
|      $64$ |    $12$ |       $2$ |
|     $128$ |    $14$ |       $2$ |
|     $256$ |    $16$ |       $2$ |
|     $512$ |    $18$ |       $2$ |
| $1{,}024$ |    $20$ |       $2$ |

<br />
<br />
