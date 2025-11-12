# Exercise 1.4.23

_Binary search for a fraction_. Devise a method that uses a logarithmic number
of queries of the form _Is the number less than x?_ to find a rational number
_p_/_q_ such that 0 < _p_ < _q_ < _N_. _Hint_: Two fractions with denominators
less than _N_ cannot differ by more than 1/*N*².

---

## The problem

A hidden rational number p/q lives strictly between 0 and 1, with denominator
smaller than N. Your only tool is a comparison oracle: "is the secret less than
x?" for any x you choose. Find the exact fraction in O(log N) queries.

The twist versus ordinary binary search: the search space _looks_ continuous (an
interval of real numbers), but the answer is drawn from a **finite, structured
set** — the fractions with denominator < N (the Farey fractions of order N−1).
There are ~3N²/π² of them, so information theory says ~2 lg N queries are
necessary. The hint is the bridge between the continuous and discrete views:
distinct candidates can't be closer than 1/N², i.e.

```text
p/q ≠ r/s, with q, s < N  →  |p/q − r/s| = |ps − qr| / qs ≥ 1 / qs > 1/N²
```

(the numerator |ps − qr| is a positive integer, hence at least 1).

### What the hint buys you

If you can trap the secret in an interval of width at most 1/N², **at most one**
candidate fraction survives inside it. So a two-phase plan suggests itself:
narrow first, then identify. The identification step is the interesting part —
given a tiny interval known to contain exactly one fraction with denominator <
N, how do you _name_ it?

### Questions to guide your solution

1. Phase 1: how many halvings of (0, 1) does it take to reach width ≤ 1/(2N²)?
   Express the query count in terms of lg N.
2. Phase 2: with the interval pinned down, one approach tries each denominator q
   = 1..N−1 and asks which numerator p could land inside — but that's O(N)
   arithmetic (no queries, though!). Does the exercise's budget constrain
   _queries_ or _all_ computation? Both readings are defensible; can you solve
   the stronger one?
3. For the stronger version, look up (or rediscover) the **Stern–Brocot tree**:
   every rational in lowest terms sits in a binary search tree of mediants. What
   does one oracle query correspond to in that tree, and why might a naive walk
   cost more than O(log N) steps (think 1/N)? What fixes it?
4. Where exactly does the argument need `0 < p < q` (proper fraction, nonzero) —
   what would break if p/q could equal 0 or 1?

### Practical notes

- **Exact arithmetic:** compare using cross-multiplication (`a·d < b·c`) on
  `long`s rather than floating point — doubles cannot represent the candidates
  exactly and the whole point is exactness near 1/N² gaps.
- **Simulate the oracle** with a hidden fraction and a comparison counter;
  assert the count stays within your claimed bound across all hidden fractions
  for small N (exhaustive: every p/q with q < N).
- **Watch for overflow:** cross-multiplying values up to N² needs headroom —
  `long` is fine for any reasonable N, but note the bound.

<br />
<br />
