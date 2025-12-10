<style>
  blockquote {
    padding: 1em 1.5em;
    margin-bottom: 1.5em;
  }

  blockquote p {
    margin: 0;
  }

  h1 {
    padding: 0.5em 0;
  }

  h2 {
    border-bottom: none;
    /* padding-bottom: 0.75em; */
  }

  h3 {}

  h4 {
    font-size: 1em;
  }

  hr {
    margin: 1.75em 0;
  }

  .box {
    padding: 1em 1.5em;
    margin-bottom: 1.5em;
    background: #000000;
    border-radius: 5px;
  }

  ul ul {
    margin-bottom: 1.25em;
  }
</style>

# Asymptotic Notation

**asymptote** | ˈasɪm(p)təʊt |  
_noun_  
&nbsp;&nbsp;&nbsp;&nbsp; a straight line that continually approaches a given
curve but does not meet it at any finite distance.

Asymptotic notations are mathematical tools used in algorithm analysis to
describe how functions behave as their input size grows. They help compare
algorithm efficiencies without getting bogged down in machine‐specific details.

---

## 1. Big-O Notation — $f(n) = \operatorname{O}(g(n))$

### Meaning

Upper bound. The function will **_not grow faster_** than $f(n)$. Used for
worst-case performance.

### Example

1. For the following expression:

   $$
   T(n) = 3n^2 + 2n + 1
   $$

   The terms:
   - $3n^2$ → quadratic term
   - $2n$ → linear term
   - $1$ → constant term

   We check whether the function will **_not grow faster_** than $f(n)$.
   - The dominating term is $3n^2$.
   - Big-O ignores constant factors and provides the upper bound.

   Therefore,

   $$
   T(n) = \operatorname{O}(n^2)
   $$

2. Let $f(n) = 3n^2 + 5n$ and $g(n) = n^2$

   Then:

   $$
   \frac{f(n)}{g(n)} = \frac{3n^2 + 5n}{n^2} = 3 + \frac{5}{n}
   $$

   As $n \to \infty$:
   - 3 stays 3
   - $\frac{5}{n}$ gets _smaller and smaller_ because the denominator grows

   Eventually:

   $$
   3 + \frac{5}{n} \approx 3
   $$

   Big-O asks: Is the ratio $\frac{f(n)}{g(n)}$ **_bounded above_** by a finite
   constant after some point?

   $$
   3 + \frac{5}{n} \le 4 \quad \text{whenever} \quad n \ge 5
   $$

   That means:
   - For large enough $n$, the ratio $\frac{f(n)}{g(n)}$ is bounded by the
     constant 4.
   - Therefor $f(n) = \operatorname{O}(g(n))$

### Exercises

#### Beginner

1. **Linear Growth**

   Determine whether

   $$
   2n + 3 = \operatorname{O}(n)
   $$

   as $n \to \infty$.

   Yes.

   By definition, $f(n) = \operatorname{O}(g(n))$ if there exist constants
   $C > 0$ and $n_0$ such that

   $$
   |f(n)| \le C|g(n)| \quad \text{for all} \quad n \ge n_0
   $$

   Here, let $f(n) = 2n + 3$ and $g(n) = n$. For $n \ge 3$,

   $$
   2n + 3 \le 3n
   $$

   Thus choosing $C = 3$ and $n_0 = 3$, we have

   $$
   2n + 3 \le Cn \quad \text{for all} \quad n \ge n_0
   $$

   Therefor,

   $$
   2n + 3 = \operatorname{O}(n) \quad \text{as} \quad n \to \infty
   $$

   In fact,

   $$
   2n + 3 = \operatorname{\Theta}(n)
   $$

2. **Quadratic vs. Linear**

   Decide if

   $$
   n^2 + n = \operatorname{O}(n)
   $$

   as $n \to \infty$.

3. **Dominant Term**

   Determine whether

   $$
   5n^3 + 100 = \operatorname{O}(n^3)
   $$

   as $n \to \infty$.

4. **Logarithmic Function**

   Check whether

   $$
   \ln(n) = \operatorname{O}(n)
   $$

   as $n \to \infty$.

5. **Powers of $n$**

   Decide if

   $$
   n^{1/2} = \operatorname{O}(n)
   $$

   as $n \to \infty$.

6. **Exponential vs. Polynomial**

   Determine whether

   $$
   n^5 = \operatorname{O}(2^n)
   $$

   as $n \to \infty$.

7. **Simplifying a Rational Function**

   Check whether

   $$
   \frac{3n^2 + 4n}{n^3} = \operatorname{O}(\frac{1}{n})
   $$

   as $n \to \infty$.

8. **Constant Function**

   Determine if

   $$
   7 = \operatorname{O}(1)
   $$

   as $n \to \infty$.

9. **Reciprocal Function**

   Determine whether

   $$
   \frac{1}{n} = \operatorname{O}(\frac{1}{\sqrt{n}})
   $$

   as $n \to \infty$.

10. **Polynomial Comparison**

    Check whether

    $$
    n^{2.5} = \operatorname{O}(n^3)
    $$

    as $n \to \infty$.

#### Intermediate

1. **Polynomial With Several Terms**

   Determine whether

   $$
   4n^4 + 3n^3 - 20n + 7 = \operatorname{O}(n^4)
   $$

   and find the **_tightest_** Big-O bound.

2. **Logarithmic Products**

   Check whether

   $$
   n(\ln n)^2 = \operatorname{O}(n^{1 + \epsilon})
   $$

   for any fixed $\epsilon > 0$.

3. **Exponential Comparison**

   Decide whether

   $$
   3^n = \operatorname{O}(2^{n + 5})
   $$

   as $n \to \infty$.

4. **Ratio Involving Factorials**

   Determine if

   $$
   \frac{n!}{2^n} = \operatorname{O}(n^n)
   $$

   as $n \to \infty$.

5. **Nested Logarithms**

   Check whether

   $$
   \ln(\ln n) = \operatorname{O}(\ln n)
   $$

   as $n \to \infty$.

6. **Rational Function With Different Growth Rates**

   Find the **_tightest_** Big-O bound of

   $$
   \frac{5n^3 + 2n}{n^2 + 7}
   $$

7. **Comparing Powers and Logs**

   Determine whether

   $$
   (\ln n)^{10} = \operatorname{O}(n^\epsilon)
   $$

   for every fixed $\epsilon > 0$.

8. **Trigonometric Expansion**

   Show whether

   $$
   \frac{\sin n}{n} = \operatorname{O}(1/n)
   $$

   as $n \to \infty$.

9. **Superpolynomial Function**

   Determine if

   $$
   n^{\sqrt{n}} = \operatorname{O}(e^n)
   $$

   as $n \to \infty$.

10. **Sum of Mixed Growth Terms**

    Given

    $$
    f(n) = n^2 + n \log n + 100 \sqrt{n}
    $$

    find the **_tightest_** Big-O classification of $f(n)$.

11. **Running Times**

    Given three functions that represent running times of three different
    algorithms:
    1. $f(n) = n \log n$
    2. $g(n) = 50n$
    3. $h(n) = n^2 + 100n$

    <br />

    a. Determine each function's Big-O class:
    1. $f(n) = n \log n \Rightarrow \operatorname{O}(n \log n)$
    2. $g(n) = 50n \Rightarrow \operatorname{O}(n)$
    3. $h(n) = n^2 + 100n \Rightarrow \operatorname{O}(n^2)$

    b. Compute each function’s value for the following inputs:
    - $n = 10$
    - $n = 100$
    1. Which function is smallest for $n = 10$?

       | Function            | $n=10$ | Method                                         |
       | ------------------- | ------ | ---------------------------------------------- |
       | $f(n) = n \log n$   | ~33.2  | $n \log n = 10 \cdot \log_2 10 \approx 33.219$ |
       | $g(n) = 50n$        | 500    | $50 \cdot 10 = 500$                            |
       | $h(n) = n^2 + 100n$ | 1100   | $ 10^2 + 100 \cdot 10 = 1100$                  |

       ✔ **Answer:** $f(n) = n \log n$

    <br />
    2. Which is smallest for $n = 100$?

       | Function            | $n=100$ | Method                                            |
       | ------------------- | ------- | ------------------------------------------------- |
       | $f(n) = n \log n$   | ~664.4  | $n \log n = 100 \cdot \log_2 100 \approx 664.385$ |
       | $g(n) = 50n$        | 5000    | $50 \cdot 100 = 5000$                             |
       | $h(n) = n^2 + 100n$ | 20000   | $ 100^2 + 100 \cdot 100 = 20000$                  |

       ✔ **Answer:** $f(n) = n \log n$

    <br />
    3. How do the rankings change as $n$ grows? (As $n$ becomes very large,
       which function eventually becomes smallest, middle, largest?)
       - $\operatorname{O}(n)$ grows slowest
       - $\operatorname{O}(n \log n)$ grows slower than quadratic
       - $\operatorname{O}(n^2)$ grows fastest

       The numeric rankings for small $n$ don’t always reflect the asymptotic
       ranking — but the asymptotic ranking always wins for huge $n$.

    <br />

    c. Order them by growth rate from fastest-growing to slowest-growing (as
    $n \to \infty$), order:
    - $\operatorname{O}(n^2)$ grows fastest - grows like a square → very large,
      very quickly
    - $\operatorname{O}(n \log n)$ grows next - grows faster than linear but
      much slower than quadratic
    - $\operatorname{O}(n)$ grows slowest - linear is the slowest because it
      only increases proportinal to $n$

<br />

---

## 2. Big-Omega Notation — $f(n) = \operatorname{\Omega}(g(n))$

### Meaning

Lower bound. The function will **_not grow slower_** than $f(n)$. Used for
best-case performance, or proving that no algorithm can do better than a certain
limit.

### Example

For the following expression:

$$
T(n) = 3n^2 + 2n + 1
$$

We check whether the function will **_not grow slower_** than $f(n)$.

- The dominating term is $3n^2$.
- Big-Omega ingnores contstant factors and provides the lower bound.

Therefore,

$$
T(n) = \Omega(n^2)
$$

### Exercises

#### Beginner

1. **Linear Function**

   Determine whether

   $$
   2n + 5 = \operatorname{\Omega}(n)
   $$

   as $n \to \infty$.

2. **Quadratic vs. Linear**

   Check whether

   $$
   n^2 + 3n = \operatorname{\Omega}(n)
   $$

   as $n \to \infty$.

3. **Dominant Term**

   Decide if

   $$
   5n^3 + 100 = \operatorname{\Omega}(n^3)
   $$

   as $n \to \infty$.

4. **Logarithmic Function**

   Determine whether

   $$
   \ln(n) = \operatorname{\Omega}(1)
   $$

   as $n \to \infty$.

5. **Comparing Linear and Sublinear**

   Check if

   $$
   n = \operatorname{\Omega}(\sqrt{n})
   $$

   as $n \to \infty$.

6. **Exponential vs. Polynomial**

   Decide whether

   $$
   2^n = \operatorname{\Omega}(n^5)
   $$

   as $n \to \infty$.

7. **Rational Function**

   Determine if

   $$
   \frac{3n^2 + 4n}{n^3} = \operatorname{\Omega}\bigg(\frac{1}{n^2}\bigg)
   $$

   as $n \to \infty$.

8. **Constant Function**

   Check whether

   $$
   7 = \operatorname{\Omega}(1)
   $$

   as $n \to \infty$.

9. **Reciprocal Function**

   Determine if

   $$
   \frac{1}{n} = \operatorname{\Omega}\bigg(\frac{1}{n}\bigg)
   $$

   as $n \to \infty$.

10. **Logarithm vs. Polynomial**

    Decide whether

    $$
    \log_2(n) = \operatorname{\Omega}(n^{\epsilon})
    $$

    for any fixed $\epsilon > 0$.

#### Intermediate

1. **Intermediate Big-Omega Exercises**

   Determine whether

   $$
   4n^4 +3n^3 - 20n + 7 = \operatorname{\Omega}(n^4)
   $$

   and find the **_tightest_** lower bound.

2. **Logarithmic Products**

   Check whether

   $$
   n(\ln n)^2 = \operatorname{\Omega}(n \ln n)
   $$

   as $n \to \infty$.

3. **Exponential Growth**

   Decide if

   $$
   3^n = \operatorname{\Omega}(2^n)
   $$

   as $n \to \infty$.

4. **Factorial vs. Exponential**

   Determine whether

   $$
   \frac{n!}{2^n} = \operatorname{\Omega}(n^{n/2})
   $$

   as $n \to \infty$.

5. **Nested Logarithms**

   Check if

   $$
   \ln(\ln n) = \operatorname{\Omega}(1)
   $$

   as $n \to \infty$.

6. **Rational Function with Dominant Term**

   Find the tightest Big-Omega bound for

   $$
   \frac{5n^3 + 2n}{n^2 + 7}
   $$

7. **Comparing Powers and Logs**

   Determine whether

   $$
   (\ln n)^{10} = \operatorname{\Omega}(\ln n)
   $$

   as $n \to \infty$.

8. \*\*Trigonometric Function

   Check whether

   $$
   \frac{\sin n}{n} = \operatorname{\Omega}\bigg(\frac{1}{n}\bigg)
   $$

   as $n \to \infty$.

9. **Superpolynomial Function**

   Determine if

   $$
   n^{\sqrt{n}} = \operatorname{\Omega}(n^k)
   $$

   for any fixed $k > 0$.

10. **Sum of Mixed Growth Terms**

    Given

    $$
    f(n) = n^2 + n \log n + 100\sqrt{n}
    $$

    find the **_tightest_** Big-Omega classification of $f(n)$.

<br />

---

## 3. Big-Theta Notation — $f(n) = \operatorname{\Theta}(g(n))$

### Meaning

Tight bound. The function grows **_exactly_** on the order of $f(n)$. Used for
when the upper and lower bounds match.

### Example

For the following expression:

$$
T(n) = 3n^2 + 2n + 1
$$

We check whether the function grows **_exactly_** on the order of $f(n)$.

- The dominating term is $3n^2$.
- Since lower-order terms and constants don’t affect tight asymptotic bounds,
  the function grows exactly like $n^2$.

Therefore,

$$
T(n) = \Theta(n^2)
$$

### Exercises

#### Beginner

1. **Linear Function**

   Determine whether

   $$
   2n + 5 = \operatorname{\Theta}(n)
   $$

   as $n \to \infty$.

2. **Quadratic Function**

   Check whether

   $$
   n^2 + 3n = \operatorname{\Theta}(n^2)
   $$

   as $n \to \infty$.

3. **Constant Function**

   Decide if

   $$
   7 = \operatorname{\Theta}(1)
   $$

   as $n \to \infty$.

4. **Logarithmic Function**

   Check whether

   $$
   \ln(n) = \operatorname{\Theta}(\ln n)
   $$

   as $n \to \infty$.

5. **Sublinear Function**

   Determine if

   $$
   \sqrt{n} + 5 = \operatorname{\Theta}(\sqrt{n})
   $$

   as $n \to \infty$.

6. **Reciprocal Function**

   Check whether

   $$
   \frac{1}{n+2} = \operatorname{\Theta}\left(\frac{1}{n}\right)
   $$

   as $n \to \infty$.

7. **Polynomial with Leading Term**

   Decide if

   $$
   3n^3 + 2n^2 + 5 = \operatorname{\Theta}(n^3)
   $$

   as $n \to \infty$.

8. **Linear vs. Linear with Coefficient**

   Check whether

   $$
   5n - 100 = \operatorname{\Theta}(n)
   $$

   as $n \to \infty$.

9. **Logarithms with Base Change**

   Determine if

   $$
   \log_2(n) = \operatorname{\Theta}(\log_{10} n)
   $$

   as $n \to \infty$.

10. **Small Polynomial**

    Check whether

    $$
    n + n^{1/2} = \operatorname{\Theta}(n)
    $$

    as $n \to \infty$.

#### Intermediate

1. **Polynomial with Multiple Terms**

   Determine whether

   $$
   4n^4 + 3n^3 - 20n + 7 = \operatorname{\Theta}(n^4)
   $$

   and find the tightest bound.

2. **Product of Polynomial and Log**

   Check whether

   $$
   n (\ln n)^2 = \operatorname{\Theta}(n \ln^2 n)
   $$

   as $n \to \infty$.

3. **Comparing Exponentials**

   Decide if

   $$
   3^n = \operatorname{\Theta}(2^n)
   $$

   as $n \to \infty$.

4. **Factorial vs. Exponential**

   Determine whether

   $$
   \frac{n!}{2^n} = \operatorname{\Theta}\left(\frac{n!}{2^n}\right)
   $$

   as $n \to \infty$.

5. **Nested Logarithms**

   Check if

   $$
   \ln(\ln n) = \operatorname{\Theta}(\ln \ln n)
   $$

   as $n \to \infty$.

6. **Rational Function**

   Find the tightest (\operatorname{\Theta}) bound for

   $$
   \frac{5n^3 + 2n}{n^2 + 7}
   $$

7. **Powers of Logarithms**

   Determine whether

   $$
   (\ln n)^{10} = \operatorname{\Theta}((\ln n)^{10})
   $$

   as $n \to \infty$.

8. **Trigonometric Term**

   Check whether

   $$
   \frac{\sin n}{n} = \operatorname{\Theta}\left(\frac{1}{n}\right)
   $$

   as $n \to \infty$.

9. **Superpolynomial Function**

   Determine if

   $$
   n^{\sqrt{n}} = \operatorname{\Theta}(n^{\sqrt{n}})
   $$

   as $n \to \infty$.

10. **Sum of Mixed Growth Terms**

    Given

    $$
    f(n) = n^2 + n\log n + 100\sqrt{n},
    $$

    find the tightest $\operatorname{\Theta}$ classification of $f(n)$.

<br />

---

## 4. Little-o Notation — $f(n) = \operatorname{o}(g(n))$

### Meaning

Strict upper bound. The function grows **_strictly slower_** than $f(n)$. This
implies the ratio $g(n)/f(n) \to 0$.

### Example

For the following expression:

$$
T(n) = 3n^2 + 2n + 1
$$

We check whether the function grows **_strictly slower_** than $f(n)$.

- The dominating term is $3n^2$
- A function is $\operatorname{o}(n^2)$ only if it grows **_aymptotically
  smaller_** than $n^2$
- But $T(n)$ grows exactly on the order of $n^2$, not strictly smaller.

Therefore,

$$
T(n) \neq \operatorname{o}(n^2)
$$

What we _can_ say is:

$$
T(n) = \operatorname{o}(n^{2+\varepsilon}) \quad \text{for any} \quad  \varepsilon > 0.
$$

✔ **Final Outcome:** $T(n)$ is **_not_** little-o of $n^2$, but it **_is_**
little-o of $n^{2+\varepsilon}$ for any positive $\varepsilon$.

### Exercises

#### Beginner

1. **Linear vs. Quadratic**

   Determine whether

   $$
   n = \operatorname{o}(n^2)
   $$

   as $n \to \infty$.

2. **Constant vs. Linear**

   Check if

   $$
   7 = \operatorname{o}(n)
   $$

   as $n \to \infty$.

3. **Logarithm vs. Linear**

   Determine whether

   $$
   \ln n = \operatorname{o}(n)
   $$

   as $n \to \infty$.

4. **Square Root vs. Linear**

   Check whether

   $$
   \sqrt{n} = \operatorname{o}(n)
   $$

   as $n \to \infty$.

5. **Polynomial Comparison**

   Determine if

   $$
   n^2 = \operatorname{o}(n^3)
   $$

   as $n \to \infty$.

6. **Reciprocal Function**

   Check whether

   $$
   \frac{1}{n} = \operatorname{o}(1)
   $$

   as $n \to \infty$.

7. **Logarithms vs. Polynomial**

   Determine whether

   $$
   \ln n = \operatorname{o}(n^{1/2})
   $$

   as $n \to \infty$.

8. **Linear vs. Linear with Coefficient**

   Check if

   $$
   n = \operatorname{o}(5n)
   $$

   as $n \to \infty$.

9. **Small Polynomial vs. Larger Polynomial**

   Determine whether

   $$
   n + n^{1/2} = \operatorname{o}(n^2)
   $$

   as $n \to \infty$.

10. **Logarithms vs. Polynomial**

    Check if

    $$
    (\ln n)^2 = \operatorname{o}(n)
    $$

    as $n \to \infty$.

#### Intermediate

1. **Polynomial vs. Higher Polynomial**

   Determine whether

   $$
   n^4 + 3n^3 - 20n + 7 = \operatorname{o}(n^5)
   $$

   as $n \to \infty$.

2. **Logarithmic Product vs. Polynomial**

   Check whether

   $$
   n (\ln n)^2 = \operatorname{o}(n^{1.1})
   $$

   as $n \to \infty$.

3. **Comparing Exponentials**

   Determine if

   $$
   2^n = \operatorname{o}(3^n)
   $$

   as $n \to \infty$.

4. **Factorial vs. Exponential**

   Check whether

   $$
   n! = \operatorname{o}(n^n)
   $$

   as $n \to \infty$.

5. **Nested Logarithms vs. Logarithms**

   Determine if

   $$
   \ln(\ln n) = \operatorname{o}(\ln n)
   $$

   as $n \to \infty$.

6. **Rational Function**

   Check whether

   $$
   \frac{5n^3 + 2n}{n^4 + 7} = \operatorname{o}(1)
   $$

   as $n \to \infty$.

7. **Powers of Logarithms vs. Polynomials**

   Determine whether

   $$
   (\ln n)^{10} = \operatorname{o}(n^\epsilon)
   $$

   for any fixed (\epsilon > 0).

8. **Reciprocal vs. Sublinear**

   Check if

   $$
   \frac{1}{n} = \operatorname{o}(n^{-1/2})
   $$

   as $n \to \infty$.

9. **Superpolynomial Growth**

   Determine whether

   $$
   n^{\sqrt{n}} = \operatorname{o}(2^n)
   $$

   as $n \to \infty$.

10. **Sum of Mixed Growth Terms**

    Given

    $$
    f(n) = n \log n + 100 \sqrt{n},
    $$

    check whether

    $$
    f(n) = \operatorname{o}(n^{1.5})
    $$

    as $n \to \infty$.

11. **Logarithmic**

    Is this expression true or false?

    $$
    n \log n = \operatorname{o}(n^{1.1})
    $$

    **Step 1: Recall the definition of little-o**  
    A function $f(n) = \operatorname{o}(g(n))$ if:

    $$
    \lim_{n \to \infty} \frac{f(n)}{g(n)} = 0
    $$

    **Step 2: Plug in $f(n) = n \log n$ and $g(n) = n^{1.1}$**

    $$
    \frac{f(n)}{g(n)} = \frac{n \log n}{n^{1.1}} = \log n \cdot \frac{n^1}{n^{1.1}}  = \log n \cdot n^{-0.1} = \log n \cdot \frac{1}{n^{0.1}} = \frac{\log n}{n^{0.1}}
    $$

    **Step 3: Take the limit as $n \to \infty$**

    $$
    \lim_{n \to \infty} \frac{\log n}{n^{0.1}}
    $$
    - $\log n$ grows **_very slowly_** compared to any positive power of $n$
      ($n^{0.1}$ here).
    - So the limit $\to 0$

    Therefore,

    $$
    n \log n = \operatorname{o}(n^{1.1}) \text{ is TRUE.}
    $$

    **_Note_**: For **little-o**, check if $f(n)/g(n) \to 0$

    ✔ **_Answer:_** TRUE

<br />

---

## 5. Little-omega Notation — $f(n) = \operatorname{\omega}(g(n))$

### Meaning

Strict lower bound. The function grows **_strictly faster_** than $f(n)$. This
implies the ratio $g(n)/f(n) \to \infty$.

### Example

For the following expression:

$$
T(n) = 3n^2 + 2n + 1
$$

We check whether the function grows **_strictly faster_** than $f(n)$.

- The dominating term is $3n^2$
- A function is $\operatorname{\omega}(n^2)$ only if it grows **_asymptotically
  faster_** than $n^2$
- But $T(n)$ grows exactly on the order of $n^2$, not strictly faster

Therefore,

$$
T(n) \neq \operatorname{\omega}(n^2)
$$

What we can say is:

$$
T(n) = \operatorname{\omega}(n^{2-\varepsilon}) \quad \text{for any} \quad \varepsilon > 0.
$$

✔ **Final Outcome:** $T(n)$ is **_not_** little-omega of $n^2$, but it **_is_**
little-o of $n^{2-\varepsilon}$ for any positive $\varepsilon$.

### Exercises

Here is a full set of **little-omega ((\omega)) exercises**, split into **10
beginner** and **10 intermediate** problems, formatted in the same style as your
earlier sets.

#### Beginner

1. **Linear vs. Constant**

   Determine whether

   $$
   n = \operatorname{\omega}(1)
   $$

   as $n \to \infty$.

2. **Quadratic vs. Linear**

   Check whether

   $$
   n^2 = \operatorname{\omega}(n)
   $$

   as $n \to \infty$.

3. **Linear vs. Logarithm**

   Determine whether

   $$
   n = \operatorname{\omega}(\ln n)
   $$

   as $n \to \infty$.

4. **Polynomial vs. Smaller Polynomial**

   Check if

   $$
   n^3 = \operatorname{\omega}(n^{2.5})
   $$

   as $n \to \infty$.

5. **Linear vs. Square Root**

   Determine whether

   $$
   n = \operatorname{\omega}(\sqrt{n})
   $$

   as $n \to \infty$.

6. **Logarithm vs. Constant**

   Check if

   $$
   \ln n = \operatorname{\omega}(1)
   $$

   as $n \to \infty$.

7. **Square Root vs. Logarithm**

   Determine whether

   $$
   \sqrt{n} = \operatorname{\omega}(\ln n)
   $$

   as $n \to \infty$.

8. **Polynomial vs. Constant**

   Determine whether

   $$
   n^{1.1} = \operatorname{\omega}(1)
   $$

   as $n \to \infty$.

9. **Logarithmic Power vs. Logarithm**

   Check whether

   $$
   (\ln n)^2 = \operatorname{\omega}(\ln n)
   $$

   as $n \to \infty$.

10. **Linear vs. Linear With Smaller Coefficient**

    Determine whether

    $$
    n = \operatorname{\omega}(0.1, n)
    $$

    as $n \to \infty$.

#### Intermediate

1. **Polynomial vs. Lower Polynomial**

   Determine whether

   $$
   n^4 + 3n^3 = \operatorname{\omega}(n^3)
   $$

   as $n \to \infty$.

2. **Logarithmic Products**

   Check whether

   $$
   n (\ln n)^2 = \operatorname{\omega}(n \ln n)
   $$

   as $n \to \infty$.

3. **Comparing Exponentials**

   Determine if

   $$
   3^n = \operatorname{\omega}(2^n)
   $$

   as $n \to \infty$.

4. **Factorial vs. Exponential**

   Check whether

   $$
   n! = \operatorname{\omega}(2^n)
   $$

   as $n \to \infty$.

5. **Logarithmic Tower**

   Determine whether

   $$
   \ln n = \operatorname{\omega}(\ln \ln n)
   $$

   as $n \to \infty$.

6. **Rational Function**

   For

   $$
   f(n) = \frac{5n^3 + 2n}{n^2 + 7}
   $$

   determine whether

   $$
   f(n) = \operatorname{\omega}(n)
   $$

   as $n \to \infty$.

7. **Powers of Logarithms vs. Powers of n**

   Determine whether

   $$
   (\ln n)^{10} = \operatorname{\omega}((\ln n)^3)
   $$

   as $n \to \infty$.

8. **Trigonometric Ratio**

   Check whether

   $$
   \frac{1}{n} = \operatorname{\omega}\left(\frac{1}{n \ln n}\right)
   $$

   as $n \to \infty$.

9. **Superpolynomial vs. Polynomial**

   Determine whether

   $$
   n^{\sqrt{n}} = \operatorname{\omega}(n^k)
   $$

   for **_any_** fixed $k > 0$.

10. **Sum of Mixed Terms**

    Given

    $$
    f(n) = n^2 + n\log n + 100\sqrt{n},
    $$

    determine whether

    $$
    f(n) = \operatorname{\omega}(n \log n)
    $$

    as $n \to \infty$.

11. **Logarithmic**

    Is this expression true or false?

    $$
    n^2 = \operatorname{\omega}(n \log n)
    $$

    **Step 1: Recall the definition of little-omega**  
    A function $f(n) = \operatorname{\omega}(g(n))$ if:

    $$
    \lim_{n \to \infty} \frac{f(n)}{g(n)} = \infty
    $$

    **Step 2: Plug in $f(n) = n^2$ and $g(n) = n \log n$**

    $$
    \frac{f(n)}{g(n)} = \frac{n^2}{n \log n} = \frac{n}{\log n}
    $$

    **Step 3: Take the limit as $n \to \infty$**

    $$
    \lim_{n \to \infty} \frac{n}{\log n} = \infty
    $$
    - Because $n$ grows much faster than $\log n$.

    Therefore,

    $$
    n^2 = \operatorname{\omega}(n \log n) \text{ is TRUE.}
    $$

    **_Note_**: For **little-omega**, check if $f(n)/g(n) \to \infty$

    ✔ **_Answer:_** TRUE

<br />

---

## 6. Tilde Notation — $f(n) \sim g(n)$

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

### Exercises

#### Beginner

1. **Linear Functions**

   Show whether the following is true:

   $$
   2n + 5 \sim 2n
   $$

   as $n \to \infty$.

2. **Leading-Term Identification**

   Determine whether

   $$
   n^2 + 3n \sim n^2
   $$

   as $n \to \infty$.

3. **Constant vs. Vanishing Term**

   Decide if

   $$
   1 + \frac{1}{n} \sim 1
   $$

   as $n \to \infty$.

4. **Comparing Square Roots**

   Check whether

   $$
   \sqrt{n^2 + n} \sim n
   $$

   as $n \to \infty$.

5. **Logarithmic Growth**

   Determine if

   $$
   \ln(2n) \sim\ln n
   $$

   as $n \to \infty$.

6. **Small-Angle Approximation**

   Show whether

   $$
   \sin x \sim x
   $$

   as $n \to 0$.

7. **Rational Function Simplification**

   Find a simple function $g(n)$ such that

   $$
   \frac{3n^2 + 1}{n^2} \sim g(n)
   $$

   as $n \to \infty$.

8. **Exponential Expansion**

   Determine whether

   $$
   e^n - e^{n-1} \sim e^n
   $$

   as $n \to \infty$.

9. **Comparing Rates of Growth**

   Check whether

   $$
   \frac{n}{n + 1} \sim 1
   $$

   as $n \to \infty$.

10. **Reciprocal Comparison**

    Decide if

    $$
    \frac{1}{n + 2} \sim \frac{1}{n}
    $$

    as $n \to \infty$.

#### Intermediate

1. **Basic Asymptotic Equivalence**

   Show that

   $$
   (1+ \frac{1}{n})^n \sim e\ (1 - \frac{1}{2n})
   $$

   as $n \to \infty$.

2. **Using Logarithmic Expansions**

   Prove or disprove that

   $$
   \ln(n + 1) \sim \ln n
   $$

   as $n \to \infty$.

   (_Hint: use the expansion_ $\ln(1 + x) = x - x^2/2 + \operatorname{O}(x^3)$)

3. **Ratio Test for Equivalence**

   Determine whether the following holds:

   $$
   \sqrt{n^2 + 3n} \sim n + \frac{3}{2}
   $$

   as $n \to \infty$.

4. **Asymptotics of Differences**

   Find a simple $g(n)$ such that

   $$
   \sqrt{n + 1} - \sqrt{n} \sim g(n)
   $$

   as $n \to \infty$.

5. **Stirling-type Comparison**

   Using Stirling's approximation, determine whether

   $$
   \frac{n!}{\sqrt{2 \pi n}(n/e)^n} \sim 1
   $$

   as $n \to \infty$.

6. **Polynomial + Lower-Order Term**

   Determine whether

   $$
   (n^3 + 5n^2 + 100)^{1/3} \sim n
   $$

   as $n \to \infty$, and if not, find a more precise asymptotic form.

7. **Asymptotics of Series Remainder**

   let

   $$
   H_n = \sum_{k=1}^n \frac{1}{k}
   $$

   be the harmonic numbers.  
   Show that

   $$
   H_n - \ln n \sim \gamma
   $$

   where $\gamma$ is the Euler–Mascheroni constant.

8. **Trigonmetric Asymptotics**

   Show that for small $x$,

   $$
   1 - \cos x \sim \frac{x^2}{2}
   $$

9. **Exponential vs Polynomial**

   Determine whether the following equivalence is true:

   $$
   e^{1/n} - 1 \sim \frac{1}{n}
   $$

10. **Equivalent Form of a Rational Function**

    Find a simple function $g(n)$ such that

    $$
    \frac{n^2 + 3n + 2}{n^2 - 5n + 4} \sim g(n)
    $$

<br />

---

## Summary

| Name         | Notation                             | Meaning                    | Constants | Ratio as $n \to \infty$                                      |
| ------------ | ------------------------------------ | -------------------------- | --------- | ------------------------------------------------------------ |
| Big-O        | $f(n) = \operatorname{O}(g(n))$      | Does not grow faster than  | No        | $\displaystyle \frac{f(n)}{g(n)} \le C$ eventually           |
| Big-Omega    | $f(n) = \operatorname{\Omega}(g(n))$ | Does not grow slower than  | No        | $\displaystyle \frac{f(n)}{g(n)} \ge c$ eventually           |
| Big-Theta    | $f(n) = \operatorname{\Theta}(g(n))$ | Same asymptotic growth     | No        | $\displaystyle \frac{f(n)}{g(n)} \to$ a positive constant    |
| Little-o     | $f(n) = \operatorname{o}(g(n))$      | Grows strictly slower than | No        | $\displaystyle \lim_{n\to\infty} \frac{f(n)}{g(n)} = 0$      |
| Little-omega | $f(n) = \operatorname{\omega}(g(n))$ | Grows strictly faster than | No        | $\displaystyle \lim_{n\to\infty} \frac{f(n)}{g(n)} = \infty$ |
| Tilde        | $f(n) \sim g(n)$                     | Asymptotically equal       | Yes       | $\displaystyle \lim_{n\to\infty} \frac{f(n)}{g(n)} = 1$      |

<br />
