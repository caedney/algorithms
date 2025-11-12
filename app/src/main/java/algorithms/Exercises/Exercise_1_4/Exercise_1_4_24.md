# Exercise 1.4.24

_Throwing eggs from a building_. Suppose that you have an _N_-story building and
plenty of eggs. Suppose also that an egg is broken if it is thrown off floor _F_
or higher, and intact otherwise. First, devise a strategy to determine the value
of _F_ such that the number of broken eggs is ~lg _N_ when using ~lg _N_ throws,
then find a way to reduce the cost to ~2 lg _F_.

---

## The problem

A search problem in costume. The building's floors 1..N are an array; "does the
egg break from floor x?" is exactly the query "is x ≥ F?" — a monotone yes/no
predicate (once it breaks, everything above breaks too). Finding F is finding
the boundary in a sorted 0/1 sequence.

Part 1 (~lg N throws, ~lg N broken eggs) should feel familiar. Part 2 is where
the exercise earns its place in this chapter: a bound of **~2 lg F** depends on
the _answer_, not on the input size N. When F is small — say the egg is fragile
and breaks from floor 3 of a 1,000,000-story building — you should find it in a
handful of throws, never spending anything close to lg N. Output-sensitive
search.

### Reframing

```text
floors:   1  2  3  ...  F-1  F  F+1  ...  N
outcome:  ok ok ok ...  ok   ✗   ✗   ...  ✗
                          ▲
              find the first ✗ (unbounded budget on eggs)
```

### Questions to guide your solution

1. Part 1: which classic algorithm answers this immediately? Why does its
   broken-egg count equal (roughly) its throw count in the worst case — what
   fraction of probes land at-or-above F?
2. Part 2: you must not spend lg N when F is tiny — so the first phase can't
   start by probing N/2. What probing pattern reaches the vicinity of F using
   only ~lg F throws? (Think about how you'd search an _infinite_ sorted array
   for the first ✗.)
3. Once phase 1 brackets F inside an interval, how wide is that interval in
   terms of F, and what does finishing inside it cost? Add the phases: does the
   total come to ~2 lg F?
4. Check the model: which of your throws break an egg, and does Part 2 also keep
   the _broken_ count logarithmic in F? (Here eggs are plentiful — 1.4.25 will
   take them away.)

### Practical notes

- **Simulate, don't build:** implement the "building" as a hidden threshold with
  a throw counter; that makes worst-case counting exact and lets you sweep every
  F from 1 to N.
- **Off-by-one discipline:** define precisely whether F is the first breaking
  floor and keep [lo, hi) conventions consistent — boundary-finding is 90%
  interval bookkeeping.
- **Validate the bound empirically:** for each F, assert throws ≤ 2 lg F + c for
  a small constant c; plot worst-case throws against F (log x-axis) and check
  the slope.

<br />
<br />
