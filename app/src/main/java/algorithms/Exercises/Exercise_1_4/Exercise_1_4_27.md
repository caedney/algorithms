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
paying occasionally still averages out to $O(1)$.

The skeleton here recurs throughout the book: implement ADT X using instances of
ADT Y, then prove an amortized bound. Getting the accounting mindset right on
this one pays off every time the shape comes round again.

### The shape of the idea

Two stacks: an _in-stack_ that receives every `enqueue`, and an _out-stack_ that
serves every `dequeue`. Both start empty. Stacks are written bottom to top, like
an array that grows at its right-hand end, so the rightmost element is the one a
`pop` would return.

| Operation    | Primitive                              |  In-stack | Out-stack | Returns |
| ------------ | -------------------------------------- | --------: | --------: | ------: |
| `enqueue(1)` | in-stack `push(1)`                     |       $1$ |           |         |
| `enqueue(2)` | in-stack `push(2)`                     |    $1\ 2$ |           |         |
| `enqueue(3)` | in-stack `push(3)`                     | $1\ 2\ 3$ |           |         |
| `dequeue()`  | out-stack empty, so pour               | $1\ 2\ 3$ |           |         |
|              | in-stack `pop()` → out-stack `push(3)` |    $1\ 2$ |       $3$ |         |
|              | in-stack `pop()` → out-stack `push(2)` |       $1$ |    $3\ 2$ |         |
|              | in-stack `pop()` → out-stack `push(1)` |           | $3\ 2\ 1$ |         |
|              | out-stack `pop()`                      |           |    $3\ 2$ |     $1$ |
| `enqueue(4)` | in-stack `push(4)`                     |       $4$ |    $3\ 2$ |         |
| `dequeue()`  | out-stack `pop()`                      |       $4$ |       $3$ |     $2$ |
| `dequeue()`  | out-stack `pop()`                      |       $4$ |           |     $3$ |
| `dequeue()`  | out-stack empty, so pour               |       $4$ |           |         |
|              | in-stack `pop()` → out-stack `push(4)` |           |       $4$ |         |
|              | out-stack `pop()`                      |           |           |     $4$ |

The first `dequeue` needs $1$, which is at the _bottom_ of the in-stack. The
_pour_ — pop everything off the in-stack and push it onto the out-stack ($3$
pops and $3$ pushes here) — leaves the out-stack holding $3\ 2\ 1$ with $1$ on
top, so pops from the out-stack now come out in queue order: $1$, then $2$, then
$3$.

The `enqueue(4)` that arrives mid-way lands on the in-stack and stays there
while the out-stack still has elements to serve; only once the out-stack has
drained does the next `dequeue` pour again, and $4$ comes out last — exactly
where FIFO order puts it.

The expensive moment (the pour) is $O(n)$ — yet the claim is $O(1)$ amortized.
Why: each element is involved in at most a fixed number of stack operations
_over its entire lifetime_ (pushed in, poured once, popped out). Total stack ops
for any sequence of $M$ queue ops is $\le cM$; the pour's cost was _prepaid_ by
the enqueues that built the pile.

### Questions to guide your solution

1. When exactly should the pour happen — on every dequeue, or only when the
   out-stack is empty? One choice breaks correctness, the other is the
   algorithm; work out which and why (interleaved enqueues must not corrupt the
   order).
2. Count precisely: over a lifetime, how many pushes and pops does one element
   experience? So what is the constant in "constant amortized"?
3. State the amortized argument two ways: (a) aggregate — total ops across any
   $M$-operation sequence; (b) credit/potential — each enqueue deposits credits
   that the pour later spends. Which do you find more convincing?
4. Worst single operation: how bad can one dequeue be? Why is "amortized $O(1)$"
   compatible with that spike, and in what applications (hard real-time) would
   the spike matter anyway?

### When to pour

The out-stack, once poured into, holds its elements in queue order with the
oldest on top. Pouring onto it again would bury those elements under newer ones,
so the pour may only happen when the out-stack is empty.

**Claim.** Pouring only when the out-stack is empty preserves FIFO order;
pouring onto a non-empty out-stack does not.

_Proof._ Every element on the in-stack was enqueued after every element on the
out-stack, because the out-stack is only ever filled from the in-stack and the
in-stack is emptied by that pour. So at any moment the queue's logical order is
the out-stack top to bottom, followed by the in-stack bottom to top. Popping
from a non-empty out-stack returns its top, which is the oldest element present.
Pouring an empty out-stack reverses the in-stack, so the oldest in-stack element
— its bottom — lands on top, and pops again return the oldest first. Pouring
onto a non-empty out-stack instead pushes the in-stack's elements, all younger,
above the out-stack's older ones, so the next pop returns a younger element
ahead of an older one. $\blacksquare$

Take the trace's state after `enqueue(4)`: in-stack $4$, out-stack $2\ 3$. A
pour here would leave the out-stack as $4\ 2\ 3$ and the next `dequeue` would
return $4$ ahead of $2$ and $3$. Waiting until the out-stack drains returns $2$,
$3$, $4$. A newly enqueued element can sit on the in-stack for as long as older
ones are still being served, which is correct because it is younger than all of
them.

This settles question 1: pour only when the out-stack is empty.

### The lifetime of an element

Follow one element from `enqueue` to `dequeue`. It is pushed onto the in-stack,
popped off the in-stack and pushed onto the out-stack by a pour, then popped off
the out-stack. Four stack operations: push, pop, push, pop. It is poured at most
once, because the pour goes in one direction only — nothing is ever moved back
to the in-stack — and once on the out-stack the only thing that can happen to an
element is its final pop.

This settles question 2: the constant is $4$.

### The amortized bound

**Claim.** Any sequence of $M$ queue operations performs at most $4M$ stack
operations.

_Proof._ (Aggregate.) Every stack operation acts on some element, and each
element accounts for at most $4$ over its lifetime. At most $M$ elements are
ever enqueued, so the total is at most $4M$. Dividing by $M$, the average cost
per queue operation is at most $4$, whatever the interleaving and however large
any single pour was. $\blacksquare$

The same fact can be stated as a budget. Let each stack operation cost one coin.
An `enqueue` pays $4$: one for its own push, and three left on the element as a
prepaid balance. The pour spends two of those coins per element it moves, and
the final pop spends the last. No operation is ever in debt: a `dequeue` pays
nothing out of its own pocket beyond what the elements it touches already carry.
So the amortized cost is $4$ per `enqueue` and $0$ per `dequeue`, and the bound
follows.

Nothing in the code changes — there are no coins. The scheme is bookkeeping for
the proof. What it predicts is observable, though: count every push and pop
across a long random workload and the total never exceeds $4$ times the number
of queue operations, which is the assertion in the testing note below.

This settles question 3. The aggregate argument needs the whole sequence in
view; the credit argument needs only an invariant about what each element
carries, which is why it scales to structures where the sequence is not known in
advance.

### The worst single operation

If $n$ elements are on the in-stack when a `dequeue` finds the out-stack empty,
the pour is $n$ pops and $n$ pushes, then the final pop: $2n + 1$ stack
operations. After $M$ enqueues and no dequeues, one `dequeue` costs $2M + 1$.
That is linear in the queue's size and it is the worst case.

It does not contradict the bound. The $2M$ operations in the pour were paid for,
two coins per element, by the enqueues that built the pile; the spike is that
prepaid total being spent at once. Amortized $O(1)$ bounds the total work over a
sequence, so it is a throughput guarantee: a billion queue operations cost about
four billion stack operations. It is not a latency guarantee for any single
call.

The distinction bites where each individual call has a deadline rather than the
run having a budget — a hard real-time controller, an audio callback, a frame
budget, a request with a timeout. There, the one `dequeue` that happens to
trigger the pour misses its deadline, and the small average is no consolation.
Those systems need a worst-case bound per operation, which this structure does
not give. The standard repair is to spread the pour out: move a constant number
of elements on every operation instead of all of them at once, so no single call
ever does more than a fixed amount of work.

This settles question 4.

### Practical notes

- Reuse your own `Stack` from Section 1.3 rather than `java.util` — the point is
  composing the book's ADTs; expose the queue as the book's `Queue` API
  (`enqueue`, `dequeue`, `isEmpty`, `size`).
- **Iteration order** (if you make it `Iterable`): the queue's logical order
  spans the out-stack top-to-bottom _then_ the in-stack bottom-to-top — an easy
  place to get it backwards.
- **Testing:** run randomized interleavings of enqueue/dequeue against
  `java.util.ArrayDeque` as an oracle; instrument the stacks with an operation
  counter and assert $\text{stackOps} \le 4 \cdot \text{queueOps}$ across many
  random workloads to _see_ the amortized constant.

### The solution

The queue is the book's `Queue` API over two of the book's `Stack`s. Every
`enqueue` is one push onto the in-stack; every `dequeue` is one pop from the
out-stack, preceded by a pour whenever the out-stack is empty.

```java
static class QueueWithTwoStacks<Item> {
    private Stack<Item> in;
    private Stack<Item> out;

    public QueueWithTwoStacks() {
        this(new Stack<>(), new Stack<>());
    }

    QueueWithTwoStacks(Stack<Item> in, Stack<Item> out) {
        this.in = in;
        this.out = out;
    }

    public void enqueue(Item item) {
        in.push(item);
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        if (out.isEmpty()) {
            while (!in.isEmpty()) {
                out.push(in.pop());
            }
        }

        return out.pop();
    }

    public boolean isEmpty() {
        return in.isEmpty() && out.isEmpty();
    }

    public int size() {
        return in.size() + out.size();
    }
}
```

- The pour is guarded by `out.isEmpty()`, not by whether the in-stack has
  anything — pouring onto a non-empty out-stack buries older elements under
  newer ones.
- The underflow check comes before the pour, so an empty queue throws the book's
  `"Queue underflow"` rather than letting the out-stack's own exception leak
  through.
- `isEmpty` and `size` combine the two stacks; an element is in exactly one of
  them at any time.
- The package-private constructor is a testing seam: it lets a test hand in
  stacks that count their own pushes and pops, so the amortized bound can be
  measured without an instrumentation field on the implementation.

**Cost.** Each element is pushed, popped, pushed and popped once over its
lifetime, so any sequence of $M$ queue operations performs at most $4M$ stack
operations: $O(1)$ amortized per queue operation. A single `dequeue` that finds
$n$ elements on the in-stack costs $2n + 1$, so the worst case per operation is
$O(n)$.

**Verification.** `Exercise_1_4_27Test` runs the trace above, the empty,
single-element, drained and large cases, and $2{,}000$ random interleavings of
up to $500$ operations against `java.util.ArrayDeque`, with the enqueue
probability drawn per run so some workloads pile up and others churn. A
`CountingStack` subclass of `Stack`, injected through the package-private
constructor, counts every push and pop: the trace's first `dequeue` costs
exactly $7$ stack operations, one `dequeue` after $10{,}000$ enqueues costs
exactly $20{,}001$, and across the random workloads the total never exceeds $4$
times the number of queue operations — the worst ratio observed was $2.97$. Both
wrong pour policies (pour on every `dequeue`, and pour whenever the in-stack is
non-empty) fail the trace test and the oracle test at the same step, returning
$4$ where $2$ was due.

<br />
<br />
