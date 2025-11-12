# Exercise 1.4.22

_Binary search with only addition and subtraction_. [Mihai Patrascu] Write a
program that, given an array of _N_ distinct `int` values in ascending order,
determines whether a given integer is in the array. You may use only additions
and subtractions and a constant amount of extra memory. The running time of your
program should be proportional to log _N_ in the worst case.

_Answer_: Instead of searching based on powers of two (binary search), use
Fibonacci numbers (which also grow exponentially). Maintain the current search
range to be the interval [*i*, *i* + *F<sub>k</sub>*] and keep _F<sub>k</sub>_
and _F<sub>k-1</sub>_ in two variables. At each step compute _F<sub>k-2</sub>_
via subtraction, check element _i_ + _F<sub>k-2</sub>_, and update the current
range to either [*i*, *i* + *F<sub>k-2</sub>*] or [*i* + *F<sub>k-2</sub>*, *i*
+ *F<sub>k-2</sub>* + *F<sub>k-1</sub>*].

---

## The problem

Ordinary binary search computes `mid = lo + (hi - lo) / 2` — a **division**.
This exercise bans it (along with multiplication and bit shifts): only `+`, `-`,
and O(1) extra memory, yet still logarithmic search. The book's Answer names the
trick — _Fibonacci search_ — and your work is to turn that two-sentence sketch
into correct code and to see why it satisfies every constraint.

The insight worth internalising: binary search doesn't fundamentally need
_halving_. It needs a family of interval sizes that (a) shrink by a constant
factor and (b) can be generated with allowed operations. Powers of two need
division to walk downward; Fibonacci numbers walk downward by subtraction alone:
F<sub>k-2</sub> = F<sub>k</sub> − F<sub>k-1</sub>.

### A trace to build intuition

```text
Fibonacci: 1, 1, 2, 3, 5, 8, 13, 21, ...

N = 20 → smallest F_k ≥ N is F_8 = 21, range [0, 21), keep (21, 13)
probe i + F_6 = 8:   too small → range [8, 21),  next pair (13, 8)
probe 8 + F_5 = 13:  too big   → range [8, 13),  next pair (8, 5)
...each step: one subtraction, one probe, one pair update
```

### Questions to guide your solution

1. The interval sizes go F<sub>k</sub> → F<sub>k-2</sub> or F<sub>k-1</sub>.
   What's the worst-case shrink factor per probe, and why does that still give
   O(log N) (with a slightly larger constant than lg N — how much larger, given
   the golden ratio φ ≈ 1.618)?
2. Setup: how do you find the starting F<sub>k</sub> ≥ N using only additions?
   Does that pre-loop break the O(log N) bound?
3. The probe index `i + F_{k-2}` can exceed `N - 1` when N isn't a Fibonacci
   number. How do you handle probes past the end without extra memory
   (conceptual padding with +∞)?
4. Exactly which variables do you keep? List them and confirm the count is
   constant — no array of Fibonacci numbers allowed.

### Practical notes

- **Termination:** the loop invariant should shrink the pair (F<sub>k</sub>,
  F<sub>k-1</sub>) down to the base case (1, 1) — check the last couple of
  iterations by hand; off-by-one at the tail is the classic bug here.
- **No overflow drama:** F<sub>46</sub> = 1,836,311,903 is the largest Fibonacci
  number that fits in an `int` — fine for any array you can index, but know
  where the ceiling is.
- **Testing:** compare against `Arrays.binarySearch` for membership on random
  sorted distinct arrays, probing every value present and absent values between
  all gaps; test N = 1, 2, and exact-Fibonacci sizes vs non-Fibonacci sizes.

<br />
<br />
