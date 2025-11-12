# Exercise 1.4.29

_Steque with two stacks_. Implement a steque with two stacks so that each steque
operation (see EXERCISE 1.3.32) takes a constant amortized number of stack
operations.

---

## The problem

A _steque_ (from 1.3.32) is a stack-ended queue supporting three operations:
`push` (add to top), `pop` (remove from top), and `enqueue` (add to the
_bottom_). Build it from two stacks with **constant amortized** cost per
operation.

Structurally this sits right on top of 1.4.27: there, two stacks simulated pure
FIFO; here the target ADT mixes LIFO (`push`/`pop` at one end) with one FIFO-ish
operation (`enqueue` at the other end). The design question is how to assign the
two ends of the steque to the two stacks so that every operation is cheap at
_its_ end, and the expensive reconciliation (a pour, as in 1.4.27) happens
rarely enough to amortize away.

### The shape to consider

```text
steque order (top → bottom):   t  ...  ...  b

front-stack: holds the top region,  top of stack = steque top
back-stack:  holds the bottom region, top of stack = steque bottom

push   → push on front-stack          O(1) worst case
enqueue→ push on back-stack           O(1) worst case
pop    → pop front-stack ... but what when the front-stack is empty?
```

When `pop` finds the front-stack empty, the top of the steque is buried at the
_bottom_ of the back-stack — the 1.4.27 situation exactly. Decide what the pour
does to orderings (the back-stack holds items bottom-first; pouring reverses)
and convince yourself the poured items land in correct pop order.

### Questions to guide your solution

1. Write down the representation invariant precisely: reading the steque
   top-to-bottom = front-stack top-to-bottom followed by back-stack
   **bottom-to-top**. Check every operation preserves it, including the pour.
2. Lifetime accounting: how many stack pushes/pops can a single element
   experience end-to-end (arrival by `push` vs by `enqueue` differ!)? What
   constant does that give?
3. Does the credit argument from 1.4.27 transfer verbatim, or does `enqueue`
   need to deposit a different number of credits than `push`? Where is a poured
   element's second move paid from?
4. Adversarial check: is there an operation sequence that forces a pour _every
   other operation_? Why or why not — what must be true of the back-stack's size
   for a pour to be expensive, and who paid for those elements?

### Practical notes

- Get 1.4.27 working first and diff the designs — the delta is small and seeing
  it isolates what `enqueue` really adds.
- API per 1.3.32: `push()`, `pop()`, `enqueue()`, plus `isEmpty()`/`size()`;
  keep generics and use the book's `Stack`.
- **Testing:** oracle with a `LinkedList` (addFirst/removeFirst/addLast) under
  long random interleavings; count stack ops and assert `≤ c ·` steque ops, then
  find empirically the worst c a random adversary achieves — compare with your
  analysis.

<br />
<br />
