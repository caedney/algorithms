# Exercise 1.4.5

Give tilde approximations for the following quantities:

$a$. $\quad n + 1$  
$b$. $\quad 1 + 1/n$  
$c$. $\quad (1 + 1/n)(1 + 2/n)$  
$d$. $\quad 2n^3 - 15n^2 + n$  
$e$. $\quad \lg(2n) / \lg n$  
$f$. $\quad \lg(n^2 + 1) / \lg n$  
$g$. $\quad n^{100} / 2^n$

---

## The problem

A drill in tilde notation: for each quantity, keep the leading term and discard
everything that becomes negligible as $n$ grows. The point is fluency —
recognising on sight which term dominates and writing $f(n) \sim g(n)$ with the
simplest correct $g$.

## Tilde Notation — $f(n) \sim g(n)$

Tilde notation $(\sim)$ is a form of **_asymptotic notation_**, but it is
stronger and more precise than the common Big-O/Θ/Ω notations.

### Meaning

Consider what happens to the ratio $\frac{f(n)}{g(n)}$ in the limit as
$n \to \infty$.

$$
f(n) \sim g(n) \quad \Longleftrightarrow \quad \lim_{n \to \infty} \frac{f(n)}{g(n)} = 1.
$$

This says that $f(n) $ and $g(n)$ are asymptotically equal — they differ by a
factor that approaches 1.

- It does **not** refer to some vague or arbitrary notion of “large”.
- It does **not** specify how fast $n$ grows—only that it tends to infinity.
- It does **not** require the limit to be reached at any finite value of $n$; it
  describes **_asymptotic_** behavior only.

### Finding $g(n)$

To find $g(n)$, simplify first and choose the candidate second — never the other
way round:

1. **Simplify** $f(n)$ into a plain sum of terms (expand brackets, split
   fractions, apply log rules).
2. **Identify the leading term** — the term that dominates as $n \to \infty$,
   per the growth hierarchy:

$$
2^n \succ n^3 \succ n^2 \succ n \log n \succ n \succ \sqrt{n} \succ \log n \succ 1 \succ \tfrac{1}{\log n} \succ \tfrac{1}{n} \succ \tfrac{1}{n^2}
$$

3. **Keep its coefficient.** Unlike Big-O/Θ, tilde notation preserves the
   constant on the leading term: $2n^3 - 15n^2 + n \sim 2n^3$, **not**
   $\sim n^3$ (the ratio against $n^3$ tends to $2$, failing the definition).

The leading term (coefficient included) is $g(n)$; the limit
$\frac{f(n)}{g(n)} \to 1$ then verifies it. Note that if every non-constant term
shrinks to $0$, the leading term is the constant — e.g. $1 + 1/n \sim 1$.

### Example

Given the expressions: $f(n) = 3n + 5$ and $g(n) = n$:

- $f(n) = \operatorname{\Theta}(n)$ is true
- $f(n) \not\sim g(n)$ — because the ratio
  $\frac{f(n)}{g(n)} = 3 + \frac{5}{n} \to 3 \neq 1$

So tilde notation gives **_exact asymptotic equivalence_**, not just same order.

<br>

---

### $a$. $\quad n + 1$

$$
\lim_{n \to \infty} \frac{n+1}{n} = \lim_{n \to \infty} \left(1 + \frac{1}{n}\right) = 1
\quad \therefore \; n + 1 \sim n
$$

- $n = 10$: $\quad 1 + 1/10 = 1.1$
- $n = 1{,}000$: $\quad 1 + 1/1000 = 1.001$
- $n = 1{,}000{,}000$: $\quad 1 + 1/1000000 = 1.000001$

<br>

---

### $b$. $\quad 1 + 1/n$

$$
\lim_{n \to \infty} \frac{1 + 1/n}{1} = \lim_{n \to \infty} \left(1 + \frac{1}{n}\right) = 1
\quad \therefore \; 1 + 1/n \sim 1
$$

- $n = 10$: $\quad 1 + 1/10 = 1.1$
- $n = 1{,}000$: $\quad 1 + 1/1000 = 1.001$
- $n = 1{,}000{,}000$: $\quad 1 + 1/1000000 = 1.000001$

<br>

---

### $c$. $\quad (1 + 1/n)(1 + 2/n)$

$$
\lim_{n \to \infty} \frac{(1 + 1/n)(1 + 2/n)}{1} = \lim_{n \to \infty} \left(1 + \frac{3}{n} + \frac{2}{n^2}\right) = 1 \quad \therefore \; (1 + 1/n)(1 + 2/n) \sim 1
$$

- $n = 10$: $\quad 1 + 0.3 + 0.02 = 1.32$
- $n = 1{,}000$: $\quad 1 + 0.003 + 0.000002 = 1.003002$
- $n = 1{,}000{,}000$: $\quad 1 + 0.000003 + 2 \times 10^{-12} = 1.000003000002$

<br>

---

### $d$. $\quad 2n^3 - 15n^2 + n$

$$
\lim_{n \to \infty} \frac{2n^3 - 15n^2 + n}{2n^3} = \lim_{n \to \infty} \left(1 - \frac{15}{2n} + \frac{1}{2n^2}\right) = 1 \quad \therefore \; 2n^3 - 15n^2 + n \sim 2n^3
$$

- $n = 10$: $\quad 1 - 0.75 + 0.005 = 0.255$
- $n = 1{,}000$: $\quad 1 - 0.0075 + 0.0000005 = 0.9925005$
- $n = 1{,}000{,}000$:
  $\quad 1 - 0.0000075 + 5 \times 10^{-13} = 0.9999925000005$

<br>

---

### $e$. $\quad \lg(2n) / \lg n$

$$
\begin{aligned}
\lim_{n \to \infty} \frac{\lg(2n)}{\lg n}
&= \lim_{n \to \infty} \left(\frac{\lg 2 + \lg n}{\lg n}\right) && \text{product rule: } \lg(ab) = \lg a + \lg b \\
&= \lim_{n \to \infty} \left(\frac{1 + \lg n}{\lg n}\right) && \lg 2 = 1 \\
&= \lim_{n \to \infty} \left(\frac{1}{\lg n} + \frac{\lg n}{\lg n}\right) && \text{split fraction} \\
&= \lim_{n \to \infty} \left(\frac{1}{\lg n} + 1\right) \\
&= 1 \quad \therefore \; \lg(2n)/\lg n \sim 1
\end{aligned}
$$

- $n = 10$: $0.30103 + 1 = 1.30103$
- $n = 1{,}000$: $0.10034 + 1 = 1.10034$
- $n = 1{,}000{,}000$: $0.05017 + 1 = 1.05017$

<br>

---

### $f$. $\quad \lg(n^2 + 1) / \lg n$

$$
\begin{aligned}
\lim_{n \to \infty} \frac{\lg(n^2 + 1)}{\lg n}
&= \lim_{n \to \infty} \frac{\lg\left(n^2 \left(1 + 1/n^2\right)\right)}{\lg n} && \text{factor: } n^2 + 1 = n^2(1 + 1/n^2) \\
&= \lim_{n \to \infty} \frac{\lg n^2 + \lg(1 + 1/n^2)}{\lg n} && \text{product rule: lg(ab) = lg a + lg b} \\
&= \lim_{n \to \infty} \frac{2\lg n + \lg(1 + 1/n^2)}{\lg n} && \text{power rule: } \lg(a^k) = k \lg a \\
&= \lim_{n \to \infty} \left(2 + \frac{\lg(1 + 1/n^2)}{\lg n}\right) && \text{split fraction} \\
&= 2 \quad \therefore \; \lg(n^2 + 1) / \lg n \sim 2
\end{aligned}
$$

Dividing the quantity by its approximation $g(n) = 2$, the ratio
$\frac{f(n)}{g(n)} \to 1$ as the definition requires:

- $n = 10$:
  $\frac{\lg(101)}{\lg 10} = \frac{6.6582115}{3.3219281} = 2.0043214, \quad \frac{2.0043214}{2} = 1.0021607$
- $n = 1{,}000$:
  $\frac{\lg(1{,}000{,}001)}{\lg 1{,}000} = \frac{19.9315700}{9.9657843} = 2.00000014, \quad \frac{2.00000014}{2} = 1.00000007$
- $n = 1{,}000{,}000$:
  $\frac{\lg(10^{12} + 1)}{\lg 10^6} = \frac{39.8631371}{19.9315686} \approx 2.0000000000001, \quad \frac{\approx 2}{2} \approx 1.00000000000004$

<br>

---

### $g$. $\quad n^{100} / 2^n$

$$
\begin{aligned}
\lg\left(\frac{n^{100}}{2^n}\right)
&= \lg(n^{100}) - \lg(2^n) && \text{quotient rule} \\
&= 100 \lg n - n && \text{power rule} \\
&\to -\infty && n \text{ grows faster than } \lg n \\[6pt]
&\therefore \; \lim_{n \to \infty} \frac{n^{100}}{2^n} = 0
\end{aligned}
$$

Since the quantity tends to $0$, no simpler tilde approximation exists:
$f \sim g$ requires $f/g \to 1$, which is impossible with $g = 0$, and the
expression is a single term with no lower-order parts to discard. Hence
$n^{100}/2^n \sim n^{100}/2^n$ (trivially), with the substantive result being
that the limit is $0$ — the exponential dominates the polynomial.

<br />
<br />
