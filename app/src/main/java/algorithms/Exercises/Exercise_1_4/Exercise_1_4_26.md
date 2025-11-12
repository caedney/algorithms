# Exercise 1.4.26

_3-collinearity_. Suppose that you have an algorithm that takes as input _N_
distinct points in the plane and can return the number of triples that fall on
the same line. Show that you can use this algorithm to solve the 3-sum problem.
_Strong hint_: Use algebra to show that (_a_, *a*³), (_b_, *b*³), and (_c_,
*c*³) are collinear if and only if _a_ + _b_ + _c_ = 0.

---

## The problem

No code to write — this is a **reduction** exercise, and it's the chapter's
first taste of an idea that carries all the way to Chapter 6: relate two
problems so that an algorithm for one _is_ an algorithm for the other.
Concretely: given a black box that counts collinear triples among N points,
manufacture an input to it such that its output answers 3-sum.

The direction matters. You are showing 3-sum ≤ 3-collinearity ("3-sum reduces to
collinearity"): solve 3-sum _using_ the collinearity counter. The payoff cuts
both ways — an efficient collinearity algorithm would give an efficient 3-sum
algorithm, and contrapositively, if 3-sum is hard (the famous _3SUM-hardness_
conjecture — no strongly subquadratic algorithm is known), then counting
collinear triples is at least as hard. One reduction, an upper bound _and_ a
conditional lower bound.

### The construction

Map each input number x to the point (x, x³) — the inputs get placed on the
cubic curve y = x³:

```text
numbers:  { -2, 1, 3, ... }   →   points: (-2, -8), (1, 1), (3, 27), ...
```

The hint claims: three such points are collinear **iff** the three numbers sum
to zero. Then feeding the mapped points to the black box returns exactly the
3-sum count. Your task is the algebra.

### Questions to guide your solution

1. Write the collinearity condition for (a, a³), (b, b³), (c, c³) — equal slopes
   between two pairs, or the 3×3 determinant — and factor it. The identity
   you're hunting for: the slope between (a, a³) and (b, b³) is a² + ab + b².
   What does setting two such slopes equal give you after factoring out (b − c)
   or similar?
2. Distinctness: the black box requires distinct points, and factoring steps
   divide by differences like (a − b). Where do you use the assumption that the
   3-sum inputs are distinct numbers? What could go wrong with duplicates?
3. Direction check: does your argument prove _both_ implications of the iff
   (collinear ⇒ sums to zero, and sums to zero ⇒ collinear)? Reductions need the
   counts to match exactly, not one-sidedly.
4. Cost of the reduction itself: mapping N numbers to N points is O(N). Why does
   the reduction being cheap matter for the "hardness transfers" argument?

### Practical notes

- The determinant form is the cleanest to factor: collinear iff
  `(b−a)(c³−a³) − (c−a)(b³−a³) = 0`; expand using the difference-of-cubes
  identity `x³−y³ = (x−y)(x²+xy+y²)` and factor relentlessly — the target is a
  product with `(a+b+c)` as one factor and only nonzero factors beside it.
- **Numerical caution if you experiment:** cubes overflow `int` fast (already at
  |x| > 1290) — use `long` or exact rationals in any empirical check;
  collinearity via cross-product avoids division.
- Related book context: `ThreeSum` (page 173) and the discussion of reductions
  and lower bounds in Section 1.4 — worth rereading after finishing this one.

<br />
<br />
