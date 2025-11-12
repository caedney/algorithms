# Exercise 1.4.25

_Throwing two eggs from a building_. Consider the previous question, but now
suppose you only have two eggs, and your cost model is the number of throws.
Devise a strategy to determine _F_ such that the number of throws is at most
2√*N*, then find a way to reduce the cost to ~*c*√*F*. This is analogous to a
situation where search hits (egg intact) are much cheaper than misses (egg
broken).

---

## The problem

Same building, same hidden threshold F — but now the resource that made binary
search possible is gone. With only **two eggs**, you get exactly two "break"
events before you're blind: after the first egg breaks you must switch to a
strategy that can never break an egg except at the very answer, and after the
second breaks you'd better be done. Binary search breaks ~lg N eggs, so it's off
the table. The exercise asks how good search can be under an _asymmetric failure
budget_ — and the answer changes the complexity class: √N, not lg N.

### Why √N is the natural shape

Egg 1 can afford to probe in _jumps_ (each break is survivable once); egg 2 must
then _crawl_ linearly through the last uncertain gap. If egg 1 jumps in strides
of size s, the worst case is roughly

```text
throws ≈ N/s   (jumps by egg 1)  +  s   (crawl by egg 2)
```

Minimise `N/s + s` over s — where's the sweet spot, and what total does it give?
That's the 2√N part.

### Questions to guide your solution

1. Prove the trade-off above is tight for _fixed_ stride s: which F makes both
   terms bite simultaneously?
2. For the ~c√F refinement, fixed strides overspend when F is small (the same
   critique as Part 2 of 1.4.24). What if the stride _grows_ as you go — jump to
   positions 1, 3, 6, 10, 15, ... (gaps 1, 2, 3, 4, ...)? After the k-th jump
   you've spent k throws and stand near k²/2. How many throws to pass F, and how
   long is the crawl gap you leave behind?
3. The book frames it as "hits much cheaper than misses". Make that precise in
   your solution: how many _misses_ (breaks) does each strategy incur, and what
   does each miss cost you downstream?
4. Sanity check against 1.4.24: with unlimited eggs the answer was ~2 lg F; with
   two eggs it's ~c√F. Can you articulate _why_ restricting failures forces an
   exponential-to-polynomial degradation? (What information does a throw give
   you when you cannot afford it to break?)

### Practical notes

- **Simulate with a hard egg budget:** the harness should _throw an exception_
  (fittingly) if a third egg breaks — that catches subtle strategy bugs no
  averaging will.
- **Sweep every F** from 1 to N for small N and record worst-case throws; check
  the 2√N bound exactly and fit the constant c in c√F for the adaptive version.
- **Triangular-number bookkeeping** (positions 1, 3, 6, 10, ...) invites
  off-by-ones — pin down whether a jump position that breaks the egg leaves a
  crawl range that includes or excludes the previous jump position.

<br />
<br />
