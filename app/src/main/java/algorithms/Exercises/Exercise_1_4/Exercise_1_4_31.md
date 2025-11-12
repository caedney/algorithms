# Exercise 1.4.31

_Deque with three stacks_. Implement a deque with three stacks so that each
deque operation takes a constant amortized number of stack operations.

---

## The problem

The capstone of the simulation ladder (1.4.27 → 1.4.29 → 1.4.30 → here): a full
deque again, but now from **three plain stacks** — no steque, so no
insert-at-bottom primitive to lean on. Everything the steque gave you for free
in 1.4.30 must now be manufactured, and the third stack is the manufacturing
floor.

The natural frame: two stacks face outward (left stack serves
`pushLeft`/`popLeft`, right stack serves `pushRight`/`popRight` — all O(1) worst
case), and the third stack is _scratch space_ used only during rebalancing, when
one outward stack empties while elements remain in the other.

### Why the third stack, exactly?

When the left stack empties, the leftmost surviving element is at the _bottom_
of the right stack. In 1.4.30 the steque's `enqueue` let you slide elements
underneath; with stacks only, reaching a bottom means pouring everything
somewhere — and one pour reverses order, two pours restore it (the 1.4.27
lesson). The design question: using pours through the scratch stack, how do you
split the right stack's n elements so that the bottom half ends up on the left
stack _in the correct orientation_ and the top half returns to the right stack,
also correctly oriented? Count your pours — each element moved costs a pop+push
per pour, and the sequence of pours is where solutions differ.

```text
right stack (top→bottom):  r₁ r₂ r₃ r₄ r₅ r₆     left stack: empty
goal after rebalance:      left: r₄ r₅ r₆ (r₆ on top? work it out!)
                           right: r₁ r₂ r₃ (r₁ on top)
tools: pour right→scratch (reverses), pour scratch→left (reverses again), ...
```

### Questions to guide your solution

1. Nail the orientation bookkeeping first, on paper: after each pour, write the
   top-to-bottom order of every stack. How many pours does your rebalance use,
   and does every element end up facing the right way?
2. Why _half_? Recycle the argument from 1.4.30: show that transferring all
   elements admits an adversary (alternating end-pops) with linear amortized
   cost, while the half-split maintains a balance invariant that makes the next
   rebalance far away.
3. Set up the potential function Φ = |size(left) − size(right)| (or your
   variant): how much potential does a rebalance consume, how much can a single
   push/pop add, and what constant amortized bound falls out?
4. During a rebalance you need to count elements (to find the half-point). Do
   you need the stacks to expose `size()`, or can you maintain counters outside?
   Does either choice smuggle in a fourth "container"?

### Practical notes

- Do 1.4.30 first if you haven't — this exercise is that one with the steque
  replaced by "you build it"; the analysis carries over, the mechanics get
  harder.
- **The scratch stack must be empty** outside rebalances — state it as part of
  the representation invariant and assert it.
- **Testing:** the alternating `popLeft`/`popRight` adversary is again the
  critical case; also randomized interleavings against `java.util.ArrayDeque`,
  plus an operation counter asserting total stack ops ≤ c · deque ops — then
  compare your measured c with the one your potential argument predicts.

<br />
<br />
