# Binomial coefficient

A binomial coefficient, written $\dbinom{n}{k}$ and read "$n$ choose $k$", is
**a single number**: the count of ways to pick $k$ items from $n$ items when
order doesn't matter.

$$
\binom{n}{k} = \frac{n!}{k!\,(n-k)!}
$$

### Example

$$
\binom{4}{2} = \frac{4!}{2!\,2!} = \frac{24}{2 \cdot 2} = \frac{24}{4} = 6
$$

<br />

Check by listing: the pairs from $\{a, b, c, d\}$ are
$ab,\ ac,\ ad,\ bc,\ bd,\ cd$ — six of them. Note $ab$ and $ba$ count as **one**
pair; the $k!$ in the denominator divides out the duplicate orderings.

> **Notation:** $\binom{n}{k}$ and $C(n, k)$ are the same object. The first is
> LaTeX/print notation, the second is the plain-text form.

<br />

## 2. Computing without big factorials: the product form

The definition asks for $n!$, but $n!$ explodes — $100!$ has 158 digits. You
never need it: the $(n-k)!$ in the denominator always cancels the tail of the
$n!$ upstairs. Watch with $\binom{6}{2}$:

$$
\binom{6}{2} = \frac{6!}{2!\,4!}
= \frac{6 \cdot 5 \cdot \cancel{4 \cdot 3 \cdot 2 \cdot 1}}{2! \cdot \cancel{4 \cdot 3 \cdot 2 \cdot 1}}
= \frac{6 \cdot 5}{2!} = \frac{30}{2} = 15
$$

What survives is the **product form**:

$$
\binom{n}{k} = \frac{\overbrace{n(n-1)(n-2)\cdots(n-k+1)}^{k \text{ factors, counting down from } n}}{k!}
$$

**The rule:** $k$ factors on top counting down from $n$, and $k!$ below. The
size of $n$ no longer matters — only $k$ decides how much work you do:

$$
\binom{100}{2} = \frac{100 \cdot 99}{2!} = 4950
\qquad\qquad
\binom{100}{3} = \frac{100 \cdot 99 \cdot 98}{3!} = 161700
$$

Pair it with the symmetry identity for large $k$: $\binom{100}{98}$ looks like
98 factors of work, but $\binom{100}{98} = \binom{100}{2} = 4950$ — two factors.
**Always choose the smaller side first.**

This form is also the one that appears naturally in algorithm analysis — it's
exactly where §7's formula comes from:

$$
\binom{N}{3} = \frac{N(N-1)(N-2)}{3!} = \frac{N(N-1)(N-2)}{6}
$$

and it's the same counting story as the brackets: $N$ choices, then $N-1$, then
$N-2$ (ordered pickings), divided by $3!$ to remove duplicate orders.

<br />

## 3. Two equations, two jobs

**The definition** computes _one coefficient_. Two numbers in, one number out.
It says nothing about $x$'s or $y$'s:

$$
\binom{n}{k} = \frac{n!}{k!\,(n-k)!}
$$

**The binomial theorem** is a statement about _expansions_ that **uses** those
coefficients:

$$
(x+y)^n = \sum_{k=0}^{n} \binom{n}{k} x^{n-k} y^k
$$

In code terms: the definition is a helper function `binom(n, k)` returning one
value; the theorem is `expand(n)`, which loops $k$ from $0$ to $n$ and _calls_
the helper for each term's coefficient.

The definition tells you **what the coefficient equals**; the theorem tells you
**where it shows up**. Everything below is about the second question.

<br />

## 4. The literal meaning of the name

A **binomial** is a two-term expression like $(x+y)$. The binomial coefficient
is literally _the coefficient that appears when you expand a binomial raised to
a power_.

Expand $(x+y)^2$ by multiplying every term in the first bracket by every term in
the second:

$$
\begin{aligned}
(x+y)^2 &= (x+y)(x+y) \\
&= x \cdot x + x \cdot y + y \cdot x + y \cdot y \\
&= x^2 + xy + yx + y^2 \\
&= x^2 + 2xy + y^2
\end{aligned}
$$

⚠️ Common trap: $(x+y)^2 \ne x^2 + y^2$. Check with $x=2, y=3$: $(2+3)^2 = 25$
but $4 + 9 = 13$. The missing $12$ is the cross term $2xy$.

One power up, reusing the result above:

$$
\begin{aligned}
(x+y)^3 &= (x+y)(x+y)^2 \\
&= (x+y)(x^2 + 2xy + y^2) \\
&= x^3 + 2x^2y + xy^2 + x^2y + 2xy^2 + y^3 \\
&= x^3 + 3x^2y + 3xy^2 + y^3
\end{aligned}
$$

The coefficients $1, 3, 3, 1$ are exactly
$\dbinom{3}{0}, \dbinom{3}{1}, \dbinom{3}{2}, \dbinom{3}{3}$. The next section
shows _why_.

<br />

## 5. Where the coefficients come from: brackets and choices

The brackets are the three copies of $(x+y)$:

$$
(x+y)^3 = \underbrace{(x+y)}_{\text{bracket 1}} \;
          \underbrace{(x+y)}_{\text{bracket 2}} \;
          \underbrace{(x+y)}_{\text{bracket 3}}
$$

Every product in the expansion is formed by taking **one letter from each
bracket**. Three brackets, two choices each: $2^3 = 8$ raw products —

$$
xxx,\quad xxy,\quad xyx,\quad yxx,\quad xyy,\quad yxy,\quad yyx,\quad yyy
$$

Group them by what they simplify to:

- $xxx \to x^3$ — 1 product
- $xxy,\ xyx,\ yxx \to x^2y$ — 3 products
- $xyy,\ yxy,\ yyx \to xy^2$ — 3 products
- $yyy \to y^3$ — 1 product

There's the $1, 3, 3, 1$. The coefficient of $x^2y$ is 3 because three raw
products collapse into it — and what distinguishes them is _which bracket
contributed the single $y$_. That's the "choose": pick 1 bracket out of 3 to be
the $y$-giver, $\dbinom{3}{1} = 3$.

### Why count $y$'s and not $x$'s?

Pure convention — either works, because of a symmetry fact:

$$
\binom{n}{k} = \binom{n}{n-k}
$$

This is not a new equation — it's an observation about the choose-numbers. Write
out both sides using the definition and the symmetry is visible in the general
form: the $k!$ and $(n-k)!$ in the denominator just swap places, and
multiplication doesn't care about order.

$$
\binom{n}{k} = \frac{n!}{k!\,(n-k)!}
\qquad\qquad
\binom{n}{n-k} = \frac{n!}{(n-k)!\,k!}
$$

Now watch it happen with $n = 3$, $k = 1$ (so $n-k = 2$) — compare each piece
with the general form above:

$$
\binom{3}{1} = \frac{3!}{1!\,2!} = \frac{6}{1 \cdot 2} = 3
\qquad\qquad
\binom{3}{2} = \frac{3!}{2!\,1!} = \frac{6}{2 \cdot 1} = 3
$$

Again with $n = 5$, $k = 2$ (so $n-k = 3$):

$$
\binom{5}{2} = \frac{5!}{2!\,3!} = \frac{120}{2 \cdot 6} = 10
\qquad\qquad
\binom{5}{3} = \frac{5!}{3!\,2!} = \frac{120}{6 \cdot 2} = 10
$$

In words: **choosing 2 things to take out of 5 is the same as choosing 3 things
to leave behind.** You have 5 friends and 2 concert tickets — "pick the 2 who
go" and "pick the 3 who stay home" are one decision, so both counts must be 10.

The same fact makes every row of Pascal's triangle read the same backwards:

```
1  3  3  1        ← C(3,0) C(3,1) C(3,2) C(3,3)
1  4  6  4  1
1  5 10 10  5  1
```

Applied to the brackets: computing the coefficient of $x^2y$ by counting "which
bracket gives the $y$" is $\dbinom{3}{1} = 3$; counting "which brackets give the
$x$'s" is $\dbinom{3}{2} = 3$. Same answer, because taking-1-for-$y$ _is_
leaving-2-for-$x$.

The theorem tracks $y$'s only because its terms are spelled $x^{n-k}y^k$ — the
index $k$ appears as the exponent on $y$, so $k$ literally means "number of
$y$'s". Had the convention been $x^k y^{n-k}$, then $k$ would count $x$'s and
everything would work identically in reverse order.

<br />

## 6. Applying the theorem: the Σ is a for-loop

The big $\Sigma$ isn't something you plug into — it's an instruction to
_repeat_. In code:

$$
(x+y)^n = \sum_{k=0}^{n} \binom{n}{k}\, x^{n-k}\, y^k
$$

```java
result = 0;
for (int k = 0; k <= n; k++)        // the Σ with its bounds: k=0 below, n above
    result += binom(n, k) * pow(x, n-k) * pow(y, k);   // the loop body
```

The pieces map one-to-one:

- $k=0$ _below_ the $\Sigma$ → the loop initializer
- $n$ _above_ the $\Sigma$ → the loop bound
- $\binom{n}{k}x^{n-k}y^k$ _after_ the $\Sigma$ → the loop body, evaluated once
  per $k$ and added up

"Applying the theorem" means unrolling the loop. Take $n = 3$ and execute each
iteration by hand:

$$
(x+y)^3 = \sum_{k=0}^{3} \binom{3}{k}\, x^{3-k}\, y^k
$$

| iteration | body $\binom{3}{k}x^{3-k}y^k$ | coefficient $\frac{3!}{k!\,(3-k)!}$ | evaluates to |
| --------- | ----------------------------- | ----------------------------------- | ------------ |
| $k=0$     | $\binom{3}{0}x^3y^0$          | $\frac{6}{1 \cdot 6} = 1$           | $x^3$        |
| $k=1$     | $\binom{3}{1}x^2y^1$          | $\frac{6}{1 \cdot 2} = 3$           | $3x^2y$      |
| $k=2$     | $\binom{3}{2}x^1y^2$          | $\frac{6}{2 \cdot 1} = 3$           | $3xy^2$      |
| $k=3$     | $\binom{3}{3}x^0y^3$          | $\frac{6}{6 \cdot 1} = 1$           | $y^3$        |

The $\Sigma$ says "add the rows":

$$
x^3 + 3x^2y + 3xy^2 + y^3
$$

That's the whole application — nothing else happens.

**What actually gets plugged in:** you supply $n$ (the power). Then $k$ isn't
yours to choose — it's the loop variable, taking every value from $0$ to $n$
automatically. $x$ and $y$ stay symbolic if you want the expansion, or you
substitute numbers if you want a value (like $x = y = 1$ in the sanity check —
the same loop run with numeric inputs).

**Wanting only one term?** Skip the loop and evaluate the body at the single
relevant iteration. Coefficient of $x^5y^3$ in $(x+y)^8$: that's $k = 3$, so
$\binom{8}{3} = 56$. One iteration, no sum.

<br />

## 7. Scaling up: expanding without multiplying

The goal is the same as it was for $(x+y)^2$ and $(x+y)^3$ — turn the compact
form into a sum of terms — but now with 8 brackets:

$$
(x+y)^8 = \underbrace{(x+y)(x+y)(x+y)(x+y)(x+y)(x+y)(x+y)(x+y)}_{8 \text{ brackets, each hands over an } x \text{ or a } y}
$$

Multiplying these out by hand means every raw product is now an **8-letter
string** — one letter from each bracket:

$$
xxxxxxxx,\quad xxxxxxxy,\quad xxxxxxyx,\quad \ldots,\quad yyyyyyyy
$$

Two choices per bracket, 8 brackets: $2^8 = 256$ strings to write down and
collect. For $(x+y)^3$ listing all 8 was fine; at 256 it's exhausting — that's
the problem this section solves.

But the _structure_ is unchanged. Every string still simplifies to $x^{8-k}y^k$
where $k$ is how many $y$'s it contains, so the skeleton of the answer is forced
before any multiplying — the same table as before, just with more rows:

| $k$ ($y$'s) | term     | coefficient    |
| ----------- | -------- | -------------- |
| 0           | $x^8$    | $\binom{8}{0}$ |
| 1           | $x^7y$   | $\binom{8}{1}$ |
| 2           | $x^6y^2$ | $\binom{8}{2}$ |
| ...         | ...      | ...            |
| 8           | $y^8$    | $\binom{8}{8}$ |

So the only real work is the 9 coefficients, and there are two fast ways to get
them.

**Pascal's triangle** — each entry is the sum of the two above it:

```
n=0:  1
n=1:  1 1
n=2:  1 2 1
n=3:  1 3 3 1
n=4:  1 4 6 4 1
n=5:  1 5 10 10 5 1
n=6:  1 6 15 20 15 6 1
n=7:  1 7 21 35 35 21 7 1
n=8:  1 8 28 56 70 56 28 8 1
```

**Stepping trick** — start at 1; each next coefficient is the previous times
$\frac{n-k}{k+1}$ (symmetric, so mirror after the middle):

$$
1 \xrightarrow{\times \frac{8}{1}} 8
  \xrightarrow{\times \frac{7}{2}} 28
  \xrightarrow{\times \frac{6}{3}} 56
  \xrightarrow{\times \frac{5}{4}} 70 \;\cdots
$$

Result:

$$
(x+y)^8 = x^8 + 8x^7y + 28x^6y^2 + 56x^5y^3 + 70x^4y^4
        + 56x^3y^5 + 28x^2y^6 + 8xy^7 + y^8
$$

Sanity checks: powers in every term sum to 8; coefficients are symmetric;
coefficients total $2^8 = 256$ (set $x = y = 1$).

<br />

## 8. Application: counting combinations directly

Triples from 4 letters:

$$
\binom{4}{3} = \frac{4!}{3!\,1!} = 4
\qquad \text{check: } abc,\ abd,\ acd,\ bcd \;✓
$$

Why the product form works: picking 3 of $N$ items _in order_ gives
$N(N-1)(N-2)$ pickings, but each unordered triple was counted $3! = 6$ times, so
divide:

$$
\binom{N}{3} = \frac{N(N-1)(N-2)}{6}
$$

<br />

## 9. Application: nested loop frequencies (TwoSum / ThreeSum)

```java
for (int i = 0; i < N; i++)
    for (int j = i+1; j < N; j++)      // j starts AFTER i
        for (int k = j+1; k < N; k++)  // k starts AFTER j
            ...                        // runs C(N,3) times
```

Each loop starts one past the previous index, so the body sees each combination
$i < j < k$ exactly once, in exactly one order. The body's frequency is
literally "how many ways to choose the indices from $N$":

- 2 nested loops → $\dbinom{N}{2} = \frac{N^2}{2} - \frac{N}{2}$ → order of
  growth $N^2$
- 3 nested loops → $\dbinom{N}{3} = \frac{N^3}{6} - \frac{N^2}{2} + \frac{N}{3}$
  → order of growth $N^3$

These expanded forms are the frequencies in Sedgewick's p.181 table.

<br />

## 10. Summary

One object, three costumes:

- an **algebraic coefficient** — expanding $(x+y)^n$
- a **counting number** — loop frequencies in TwoSum/ThreeSum
- a **probability weight** — $\dbinom{n}{k}$ ways to get $k$ heads in $n$ coin
  flips (the binomial distribution)

They're identical because all three reduce to _"how many ways can I choose $k$
things from $n$."_

<br />

## 11. Exercises

### Exercise 1: Single coefficients (definition)

Compute by hand, showing the factorial working:

**(a)** $\dbinom{6}{2}$

$$\dbinom{6}{2} = \dfrac{6!}{2!\,4!} = \dfrac{720}{2 \cdot 24} = \dfrac{720}{48} = 15$$

Via the product form — the $4!$ cancels the tail of $6!$, leaving $k=2$ factors
on top:

$$
\binom{6}{2} = \frac{6 \cdot 5 \cdot \cancel{4!}}{2! \cdot \cancel{4!}}
= \frac{6 \cdot 5}{2!} = \frac{30}{2} = 15
$$

Same number, but the second route never touches $720$ — and it's the only route
that survives when $n$ gets big.

**(b)** $\dbinom{7}{5}$ — _before computing: can you rewrite it as an easier
choose using the symmetry identity?_

**(c)** $\dbinom{5}{0}$ — _which convention does this rely on?_

**(d)** In $(x+y)^{10}$, what is the coefficient of $x^7y^3$? No expansion
allowed — identify $k$, evaluate one term.

### Exercise 2: Apply the theorem (unroll the loop)

Expand $(x+y)^4$ using the binomial theorem only — no bracket multiplying. Build
the iteration table:

$$
(x+y)^4 = \sum_{k=0}^{4} \binom{4}{k}\, x^{4-k}\, y^k
$$

| iteration | body $\binom{4}{k}x^{4-k}y^k$ | coefficient $\frac{4!}{k!\,(4-k)!}$ | evaluates to |
| --------- | ----------------------------- | ----------------------------------- | ------------ |
| $k=0$     |                               |                                     |              |
| $k=1$     |                               |                                     |              |
| $k=2$     |                               |                                     |              |
| $k=3$     |                               |                                     |              |
| $k=4$     |                               |                                     |              |

Then run all three sanity checks on your result:

1. Exponents in every term sum to 4
2. Coefficients are symmetric
3. Coefficients total $2^4$ (set $x = y = 1$)

Finally, verify your coefficients against row $n=4$ of Pascal's triangle, built
by hand from row $n=3$: `1 3 3 1`.

### Exercise 3: Loop frequencies (the Sedgewick connection)

Consider **FourSum** — count quadruples that sum to zero:

```java
public static int count(int[] a) {
    int N = a.length;
    int cnt = 0;

    for (int i = 0; i < N; i++)
        for (int j = i+1; j < N; j++)
            for (int k = j+1; k < N; k++)
                for (int l = k+1; l < N; l++)
                    if ((long) a[i] + a[j] + a[k] + a[l] == 0)
                        cnt++;
    return cnt;
}
```

**(a)** How many times does the `if` statement execute, as a binomial
coefficient? (No algebra needed — just say which one and why.)

**(b)** Write that coefficient in product form
$\left(\text{like } \binom{N}{3} = \frac{N(N-1)(N-2)}{6}\right)$ — where does
the denominator come from?

**(c)** Without fully expanding, what is the leading term (the $\sim$ tilde
approximation) and the order of growth?

**(d)** Sanity check with $N = 5$: your formula from (b) should give the same
answer as listing — how many quadruples can you choose from $\{a, b, c, d, e\}$?
(Hint: symmetry makes this a very easy choose.)

### Stretch goal (optional)

Use the theorem with a substitution — no expansion by hand — to explain why the
_alternating_ sum of any Pascal row is zero:

$$
1 - 4 + 6 - 4 + 1 = 0
$$

_Hint: what values of $x$ and $y$ turn $\binom{n}{k}x^{n-k}y^k$ into
$\pm\binom{n}{k}$?_
