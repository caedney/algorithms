# Exercise 1.4.26

_3-collinearity_. Suppose that you have an algorithm that takes as input $N$
distinct points in the plane and can return the number of triples that fall on
the same line. Show that you can use this algorithm to solve the 3-sum problem.
_Strong hint_: Use algebra to show that $(a, a^3)$, $(b, b^3)$, and $(c, c^3)$
are collinear if and only if $a + b + c = 0$.

---

## The problem

No code to write — this is a **reduction** exercise, and it's the chapter's
first taste of an idea that carries all the way to Chapter 6: relate two
problems so that an algorithm for one _is_ an algorithm for the other.
Concretely: given a black box that counts collinear triples among $N$ points,
manufacture an input to it such that its output answers 3-sum.

The direction matters. You are showing $\text{3-sum} \le \text{3-collinearity}$
("3-sum reduces to collinearity"): solve 3-sum _using_ the collinearity counter.
The payoff cuts both ways — an efficient collinearity algorithm would give an
efficient 3-sum algorithm, and contrapositively, if 3-sum is hard (the famous
_3SUM-hardness_ conjecture — no strongly subquadratic algorithm is known), then
counting collinear triples is at least as hard. One reduction, an upper bound
_and_ a conditional lower bound.

### The construction

Map each input number $x$ to the point $(x, x^3)$ — the inputs get placed on the
cubic curve $y = x^3$:

$$
\{-2, 1, 3, \dots\} \;\longmapsto\; (-2, -8),\ (1, 1),\ (3, 27),\ \dots
$$

The hint claims: three such points are collinear **iff** the three numbers sum
to zero. Then feeding the mapped points to the black box returns exactly the
3-sum count. Your task is the algebra.

### Questions to guide your solution

1. Write the collinearity condition for $(a, a^3)$, $(b, b^3)$, $(c, c^3)$ —
   equal slopes between two pairs, or the $3 \times 3$ determinant — and factor
   it. The identity you're hunting for: the slope between $(a, a^3)$ and
   $(b, b^3)$ is $a^2 + ab + b^2$. What does setting two such slopes equal give
   you after factoring out $(b - c)$ or similar?
2. Distinctness: the black box requires distinct points, and factoring steps
   divide by differences like $(a - b)$. Where do you use the assumption that
   the 3-sum inputs are distinct numbers? What could go wrong with duplicates?
3. Direction check: does your argument prove _both_ implications of the iff
   (collinear $\Rightarrow$ sums to zero, and sums to zero $\Rightarrow$
   collinear)? Reductions need the counts to match exactly, not one-sidedly.
4. Cost of the reduction itself: mapping $N$ numbers to $N$ points is $O(N)$.
   Why does the reduction being cheap matter for the "hardness transfers"
   argument?

### The slope argument

The tool is the slope of a line through two points. Slope is a rate — how far
the line rises for each one unit it moves across — so it is the rise divided by
the run. Between $(x_1, y_1)$ and $(x_2, y_2)$:

$$
\text{slope} = \frac{\text{rise}}{\text{run}} = \frac{y_2 - y_1}{x_2 - x_1}
$$

The division is what makes slopes comparable. Through $(1, 1)$ and $(3, 27)$ the
line goes up $26$ over an across of $2$; through $(1, 1)$ and $(2, 14)$ it goes
up $13$ over an across of $1$. Those are the same steepness, and dividing
normalises both to $13$ per unit across:

$$
\frac{27 - 1}{3 - 1} = \frac{26}{2} = 13
\qquad
\frac{14 - 1}{2 - 1} = \frac{13}{1} = 13
$$

Two lines through a shared point are the same line iff they have the same slope,
which is the test the proof uses.

**Claim.** For distinct numbers $a$, $b$, $c$, the points $(a, a^3)$, $(b, b^3)$
and $(c, c^3)$ are collinear iff $a + b + c = 0$.

_Proof._ Three points are collinear exactly when the line through the first two
has the same slope as the line through the first and third. The slope between
$(a, a^3)$ and $(b, b^3)$ is rise over run,

$$
\frac{b^3 - a^3}{b - a} = \frac{(b - a)(a^2 + ab + b^2)}{b - a} = a^2 + ab + b^2,
$$

by the difference-of-cubes identity $x^3 - y^3 = (x - y)(x^2 + xy + y^2)$;
cancelling $(b - a)$ is legal because $a \ne b$. The same calculation with $c$
in place of $b$ gives the slope between $(a, a^3)$ and $(c, c^3)$ as
$a^2 + ac + c^2$. The points are collinear iff the two slopes are equal. Cancel
the $a^2$ that appears on both sides, move everything to the left, and factor:

$$
\begin{aligned}
a^2 + ab + b^2 &= a^2 + ac + c^2 \\
ab + b^2 - ac - c^2 &= 0 \\
(ab - ac) + (b^2 - c^2) &= 0 \\
a(b - c) + (b - c)(b + c) &= 0 \\
(b - c)(a + b + c) &= 0
\end{aligned}
$$

So the points are collinear iff $(b - c)(a + b + c) = 0$. A product is zero iff
at least one factor is, and $b - c \ne 0$ because $b \ne c$, so the condition is
exactly $a + b + c = 0$. $\blacksquare$

Both directions fall out of the one equation. Read forwards: collinear means the
product is zero, and since $b - c \ne 0$ that forces $a + b + c = 0$. Read
backwards: if $a + b + c = 0$ then the product is $(b - c) \cdot 0 = 0$ whatever
$b - c$ is, so the slopes agree and the points are collinear. Only the forward
direction leans on distinctness.

A concrete instance: $a = 1$, $b = 2$, $c = -3$ sum to zero, and the two slopes
are $1 + 2 + 4 = 7$ and $1 - 3 + 9 = 7$ — the points $(1, 1)$, $(2, 8)$,
$(-3, -27)$ lie on the line of slope $7$ through $(1, 1)$.

This settles the first three guiding questions. Question 1: the condition,
factored, is $(b - c)(a + b + c) = 0$. Question 2: distinctness is used twice —
to cancel $(b - a)$ from the slope fraction, and to dismiss the $(b - c)$ factor
at the end. With a repeated number two of the mapped points coincide, the black
box's precondition is violated, and the $(b - c)$ factor vanishes so the
condition no longer says anything about the sum. Question 3: yes — the same
equation read in each direction, so the black box's count is exactly the 3-sum
count, neither over nor under.

### Practical notes

- The determinant form is the cleanest to factor: collinear iff
  $(b - a)(c^3 - a^3) - (c - a)(b^3 - a^3) = 0$; expand using the
  difference-of-cubes identity $x^3 - y^3 = (x - y)(x^2 + xy + y^2)$ and factor
  relentlessly — the target is a product with $(a + b + c)$ as one factor and
  only nonzero factors beside it.
- **Numerical caution if you experiment:** cubes overflow `int` fast (already at
  $|x| > 1290$) — use `long` or exact rationals in any empirical check;
  collinearity via cross-product avoids division.
- Related book context: `ThreeSum` (page 173) and the discussion of lower bounds
  in Section 1.4 and of reductions in Section 6.5 — worth rereading after
  finishing this one.

### The solution

There is no program to write; the solution is the reduction itself. Given $N$
distinct numbers:

1. Map each number $x$ to the point $(x, x^3)$. Distinct numbers give distinct
   $x$-coordinates, so the $N$ points are distinct and the black box's
   precondition is met.
2. Hand the $N$ points to the collinearity counter.
3. Return its count.

By the slope argument, three of the points are collinear iff the three numbers
sum to zero, so the count that comes back is exactly the number of 3-sum triples
— the reduction is correct.

**Cost.** Step 1 is one cube and one point per number, step 3 is a single
return: the reduction's own work is $O(N)$. If the collinearity counter runs in
time $T(N)$, the whole 3-sum solver runs in

$$
O(N) + T(N).
$$

**Hardness transfers.** Two costs combine there, and the reduction's is the
smaller, which is what gives the argument its force. Read one way: any
improvement to collinearity counting is an improvement to 3-sum, since a counter
with $T(N) = O(N^{1.5})$, say, gives $O(N) + O(N^{1.5}) = O(N^{1.5})$ for 3-sum.
Read the other way: if, as conjectured, 3-sum has no strongly subquadratic
algorithm, then neither does collinearity counting — a fast counter would hand
3-sum a speed it is believed not to have. One reduction yields both an upper
bound on 3-sum and a conditional lower bound on collinearity.

The $O(N)$ is essential. Had the mapping cost $O(N^2)$, the solver would cost
$O(N^2) + T(N)$, and a fast counter would be swamped by the glue: 3-sum would
still come out quadratic, which was already known, and nothing would transfer in
either direction. A reduction carries a bound across only when its own cost sits
below that bound.

<br />
<br />
