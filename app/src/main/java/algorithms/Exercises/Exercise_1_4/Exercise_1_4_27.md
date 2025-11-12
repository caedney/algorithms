# Exercise 1.4.27

_Queue with two stacks_. Implement a queue with two stacks so that each queue
operation takes a constant amortized number of stack operations. _Hint_: If you
push elements onto a stack and then pop them all, they appear in reverse order.
If you repeat this process, they're now back in order.

---

## The problem

Build FIFO behaviour out of two LIFO primitives — and meet a precise performance
contract: **constant amortized** stack operations per queue operation. A stack
reverses order; the hint points out that two reversals cancel. The design
question is _when_ to pay for the reversal, and the analysis question is why
paying occasionally still averages out to O(1).

This is the first of a run of structural exercises (1.4.27–1.4.31) that all
share one skeleton: implement ADT X using instances of ADT Y, then prove an
amortized bound. Getting the accounting mindset right here pays off four times
over.

### The shape of the idea

```text
enqueue 1, 2, 3:        in-stack: [3 2 1⟩     out-stack: ⟨empty
dequeue?  need 1 —  it's at the BOTTOM of in-stack
pour in→out (3 pops + 3 pushes):
                        in-stack: empty       out-stack: [1 2 3⟩
now pops from out-stack come in queue order: 1, then 2, then 3
```

The expensive moment (the pour) is O(n) — yet the claim is O(1) amortized. Why:
each element is involved in at most a fixed number of stack operations _over its
entire lifetime_ (pushed in, poured once, popped out). Total stack ops for any
sequence of M queue ops is ≤ cM; the pour's cost was _prepaid_ by the enqueues
that built the pile.

### Questions to guide your solution

1. When exactly should the pour happen — on every dequeue, or only when the
   out-stack is empty? One choice breaks correctness, the other is the
   algorithm; work out which and why (interleaved enqueues must not corrupt the
   order).
2. Count precisely: over a lifetime, how many pushes and pops does one element
   experience? So what is the constant in "constant amortized"?
3. State the amortized argument two ways: (a) aggregate — total ops across any
   M-operation sequence; (b) credit/potential — each enqueue deposits credits
   that the pour later spends. Which do you find more convincing?
4. Worst single operation: how bad can one dequeue be? Why is "amortized O(1)"
   compatible with that spike, and in what applications (hard real-time) would
   the spike matter anyway?

### Practical notes

- Reuse your own `Stack` from Section 1.3 rather than `java.util` — the point is
  composing the book's ADTs; expose the queue as the book's `Queue` API
  (`enqueue`, `dequeue`, `isEmpty`, `size`).
- **Iteration order** (if you make it `Iterable`): the queue's logical order
  spans the out-stack top-to-bottom _then_ the in-stack bottom-to-top — an easy
  place to get it backwards.
- **Testing:** run randomized interleavings of enqueue/dequeue against
  `java.util.ArrayDeque` as an oracle; instrument the stacks with an operation
  counter and assert `stackOps ≤ c · queueOps` across many random workloads to
  _see_ the amortized constant.

<br />
<br />
