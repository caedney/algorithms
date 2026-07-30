# Algebra Toolkit

Core rules for simplifying mathematical expressions: each entry gives the rule,
why it works, examples, and common pitfalls. These are universal — they apply
anywhere algebra applies. Where useful, an _application note_ shows the rule at
work in asymptotic analysis (tilde approximations), one place they all come
together.

---

## 1. Expanding Brackets (FOIL)

### Rule

$$
(a + b)(c + d) = ac + ad + bc + bd
$$

Multiply every term in the first bracket by every term in the second (**F**irst,
**O**uter, **I**nner, **L**ast), then collect like terms.

### Examples

$$
(x + 3)(x + 4) = x^2 + 4x + 3x + 12 = x^2 + 7x + 12
$$

$$
\left(1 + \frac{1}{n}\right)\left(1 + \frac{2}{n}\right)
= 1 + \frac{2}{n} + \frac{1}{n} + \frac{2}{n^2}
= 1 + \frac{3}{n} + \frac{2}{n^2}
$$

### Why it's useful

Expanding turns a product into a **plain sum of terms** — the form you need for
collecting like terms, comparing coefficients, or spotting dominant behavior.

### Caution

Copy brackets exactly — $\left(1 + \frac{2}{n}\right)$ and
$\left(\frac{1}{n} + 2\right)$ are **different expressions**. Term order within
a bracket doesn't matter ($1 + \frac{1}{n} = \frac{1}{n} + 1$), but which part
sits over the denominator does.

> **Application note (tilde):** expanding $(1 + 1/n)(1 + 2/n)$ exposes the
> dominant term $1$, giving $\sim 1$.

---

## 2. Factoring

### Rule

The reverse of expanding: pull a common factor out of a sum.

$$
ab + ac = a(b + c)
$$

Any factoring can be checked by expanding back out.

### Examples

$$
6x^2 + 9x = 3x(2x + 3)
$$

$$
n^2 + 1 = n^2\left(1 + \frac{1}{n^2}\right)
\qquad \text{check: } n^2 \cdot 1 + n^2 \cdot \tfrac{1}{n^2} = n^2 + 1 \; ✓
$$

### Why it's useful

Many rules apply only to **products** (log rules, cancellation in fractions).
Factoring converts a sum into a product so those rules become available. You can
factor out _anything_ nonzero — including a term bigger than the others, leaving
fractions behind (second example).

> **Application note (tilde):** factoring out the dominant term unlocks the log
> product rule: $\lg(n^2 + 1) = \lg n^2 + \lg(1 + 1/n^2)$, and the leftover
> factor tends to 1.

---

## 3. Splitting Fractions

### Rule

Dividing a sum by something = dividing each term separately.

$$
\frac{a + b}{c} = \frac{a}{c} + \frac{b}{c}
$$

Why: division is multiplication by $\frac{1}{c}$, and multiplication distributes
over addition: $(a + b) \cdot \frac{1}{c} = \frac{a}{c} + \frac{b}{c}$.

### Examples

$$
\frac{10 + 6}{2} = \frac{10}{2} + \frac{6}{2} = 5 + 3 = 8
$$

$$
\frac{2n^3 - 15n^2 + n}{2n^3}
= \frac{2n^3}{2n^3} - \frac{15n^2}{2n^3} + \frac{n}{2n^3}
= 1 - \frac{15}{2n} + \frac{1}{2n^2}
$$

(Anything divided by itself is 1; powers cancel:
$\frac{n^2}{n^3} = \frac{1}{n}$.)

### Why it's useful

Breaks one complicated fraction into simple pieces that can be evaluated,
cancelled, or limited independently.

### Caution

Only the **numerator** splits. The denominator does not:

$$
\frac{c}{a + b} \neq \frac{c}{a} + \frac{c}{b}
\qquad \text{e.g. } \frac{12}{2+4} = 2, \text{ but } \frac{12}{2} + \frac{12}{4} = 9
$$

> **Application note (tilde):** splitting $\frac{f(n)}{g(n)}$ term by term
> rewrites it as $1 + (\text{terms} \to 0)$, making the limit visible.

---

## 4. Logarithm Rules

A logarithm is an exponent: $\log_b x$ asks "$b$ to what power gives $x$?"
($\lg$ = log base 2, $\ln$ = base $e$, $\log$ usually base 10). The rules below
hold in **every** base — they are the exponent laws in reverse.

### Product rule

$$
\lg(ab) = \lg a + \lg b
\qquad \text{because } 2^x \cdot 2^y = 2^{x+y}
$$

### Quotient rule

$$
\lg\left(\frac{a}{b}\right) = \lg a - \lg b
$$

### Power rule

$$
\lg(a^k) = k \lg a
\qquad \text{e.g. } \lg n^2 = 2 \lg n
$$

### Anchor values (base 2)

| $x$    | $\lg x$ |
| ------ | ------- |
| $1/4$  | $-2$    |
| $1/2$  | $-1$    |
| $1$    | $0$     |
| $2$    | $1$     |
| $4$    | $2$     |
| $1024$ | $10$    |

$\lg 0$ is **undefined** ($\lg x \to -\infty$ as $x \to 0^+$). Each doubling of
$x$ adds 1 to $\lg x$. In any base, $\log 1 = 0$ and $\log(\text{base}) = 1$.

### Change of base

Needed when evaluating or comparing logarithms whose bases are not directly
available or convenient (e.g. on a calculator):

$$
\log_b x = \frac{\log_k x}{\log_k b}
$$

For $\lg$ specifically:

$$
\lg x = \frac{\ln x}{\ln 2} = \frac{\log_{10} x}{\log_{10} 2}
$$

**Check:**
$\lg 8 = \frac{\log_{10} 8}{\log_{10} 2} \approx \frac{0.9031}{0.3010} = 3$ ✓

### Caution

There is **no rule for the log of a sum**: $\lg(a + b) \neq \lg a + \lg b$. Two
ways to handle $\lg(a+b)$:

- **Numerically:** compute the sum first, then take the log:
  $\lg(3+5) = \lg 8 = 3$.
- **Symbolically:** factor the sum into a product first (§2), then use the
  product rule: $\lg(a+b) = \lg a + \lg\left(1 + \tfrac{b}{a}\right)$.

**Summary:** logs turn products into sums and powers into multiples — they do
nothing for sums.

---

## 5. Limit Facts

### Notation

"$\to$" means "approaches / tends to". $f(n) \to L$ as $n \to \infty$ means
$f(n)$ gets arbitrarily close to $L$ — it need not ever equal $L$ at any finite
$n$.

### Shrinking terms

$$
\frac{c}{n} \to 0, \qquad \frac{c}{n^2} \to 0, \qquad \frac{c}{\lg n} \to 0
\qquad \text{as } n \to \infty \text{ (any constant } c\text{)}
$$

Anything with the variable **only in the denominator** dies off as that variable
grows.

### Limits pass through arithmetic

If $f \to a$ and $h \to b$, then:

$$
f + h \to a + b, \qquad f \cdot h \to a \cdot b, \qquad \frac{f}{h} \to \frac{a}{b} \;\; (b \neq 0)
$$

This lets you evaluate limits piece by piece:
$(1 + 1/n)(1 + 2/n) \to 1 \cdot 1 = 1$.

---

## 6. Growth Hierarchy

When terms compete as $n \to \infty$, the higher one on this ladder dominates:

$$
2^n \succ n^3 \succ n^2 \succ n \lg n \succ n \succ \sqrt{n} \succ \lg n \succ 1 \succ \frac{1}{\lg n} \succ \frac{1}{n} \succ \frac{1}{n^2}
$$

Everything **right of $1$** shrinks to zero; everything **left of $1$** grows
without bound. Used across asymptotic analysis (Big-O, Θ, tilde), limit
evaluation, and algorithm comparison.

---

## Application: Tilde Approximations

One workflow where every section above gets used. $f(n) \sim g(n)$ means
$\lim_{n \to \infty} \frac{f(n)}{g(n)} = 1$.

1. **Simplify** $f(n)$ into a plain sum — expand brackets (§1), factor sums
   inside logs (§2), split fractions (§3), apply log rules (§4).
2. **Identify the leading term** via the growth hierarchy (§6), **keeping its
   coefficient** — $2n^3 - 15n^2 + n \sim 2n^3$, not $n^3$. If every
   non-constant term shrinks to 0, the leading term is the constant:
   $1 + 1/n \sim 1$.
3. **Verify**: show $\frac{f(n)}{g(n)} \to 1$ using the limit facts (§5).
