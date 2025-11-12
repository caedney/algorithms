# Exercise 1.4.28

_Stack with a queue_. Implement a stack with a single queue so that each stack
operation takes a linear number of queue operations. _Hint_: To delete an item,
get all of the elements on the queue one at a time, and put them at the end,
except for the last one which you should delete and return. (This solution is
admittedly very inefficient.)

---

## The problem

Build LIFO behaviour out of a single FIFO primitive, with the performance bar
deliberately lowered: **one** queue only, and linear cost per operation is
accepted — the book even apologises for it. The point isn't speed; it's
understanding _why_ this direction is so much harder than building a queue out
of stacks. A queue preserves order; a stack needs the _newest_ element first;
with a single FIFO pipe, the only way to reach the far end is to cycle
everything through.

### The rotation trick

A queue lets you remove from the front and insert at the back. Chaining those is
a _rotation_ — the queue is a ring you can spin — and a rotation is the only way
to move an element from one end of a queue to the other. The book's hint spins
the queue on delete, to bring the newest element to the front just before
removing it. The same spin can be done on insert instead, so that the newest
element is _already_ at the front whenever anyone asks for it; that is the
design built here.

The queue is written front to back, so the leftmost element is the one a
`dequeue` would return. Start from a queue holding $3\ 2\ 1$, where $3$ was
pushed last, and `push(4)`:

| Operation | Primitive    |        Queue | Returns |
| --------- | ------------ | -----------: | ------: |
| `push(4)` | `enqueue(4)` | $3\ 2\ 1\ 4$ |         |
|           | `dequeue()`  |    $2\ 1\ 4$ |         |
|           | `enqueue(3)` | $2\ 1\ 4\ 3$ |         |
|           | `dequeue()`  |    $1\ 4\ 3$ |         |
|           | `enqueue(2)` | $1\ 4\ 3\ 2$ |         |
|           | `dequeue()`  |    $4\ 3\ 2$ |         |
|           | `enqueue(1)` | $4\ 3\ 2\ 1$ |         |
| `pop()`   | `dequeue()`  |    $3\ 2\ 1$ |     $4$ |

The enqueue puts $4$ at the back; three rotations carry $3$, $2$ and $1$ round
behind it, and the queue reads $4\ 3\ 2\ 1$ — the stack, top to bottom. `pop()`
is then a single `dequeue()`, and what remains is still the stack, top to
bottom. That is the hint's rotation, moved from `pop` to `push`: $N - 1$
rotations plus one enqueue per `push()`.

### Questions to guide your solution

1. Two designs are possible: expensive `pop()` (rotate on delete, as hinted) or
   expensive `push()` (rotate after every insert so the newest sits at the
   front, making `pop()` trivial). Implement one, sketch the other. When would
   each be preferable (many pushes vs many pops)?
2. Where does `peek()` fit — can you support it without a full extra rotation in
   your chosen design?
3. Count queue operations exactly for a workload of $M$ pushes followed by $M$
   pops in each design. Both are $O(N)$ per operation — but the constants and
   _which_ operation pays differ.
4. The deeper question the book is nudging at: a queue can be built from two
   stacks at $O(1)$ amortized per operation, yet a stack from one queue seems
   doomed to $\Omega(N)$ per operation. What asymmetry between the two
   directions is responsible? (What can a second container "remember" that a
   single queue cannot?)

### Which operation pays

Rotating on `push()` maintains one invariant: the queue, read front to back,
_is_ the stack, read top to bottom. Every other operation then has nothing to do
— `pop()` is one `dequeue()`, `peek()` is one `peek()`, and `push` is the only
operation that does any work.

**Claim.** After `enqueue(item)` followed by $\text{size} - 1$ rotations, the
queue reads `item` followed by the previous contents in their previous order.

_Proof._ Let the queue hold $a_1\ a_2 \cdots a_n$ before the push. After the
enqueue it holds $a_1\ a_2 \cdots a_n\ x$, with $n + 1$ elements. A rotation
moves the front element to the back and leaves the relative order of the others
unchanged, so after $k$ rotations the queue holds
$a_{k+1} \cdots a_n\ x\ a_1 \cdots a_k$. With $k = n = \text{size} - 1$ that is
$x\ a_1 \cdots a_n$. $\blacksquare$

The claim is the invariant restated for one push: if the queue was the stack top
to bottom before, it is again afterwards, with `item` on top. The empty stack is
the base case — one element, zero rotations.

**Cost.** A push onto a stack of $n$ elements is one enqueue plus $n$ rotations
of two queue operations each: $2n + 1$. A pop or a peek is one queue operation.
Under the hinted design the costs swap ends: a push is one enqueue, a pop on $n$
elements is $n - 1$ rotations plus the final dequeue, $2n - 1$, and a peek has
to do a full cycle — bring the newest to the front, look, then rotate once more
to restore the order — unless the top is remembered separately.

Which to prefer depends on which operation can afford to be slow. Every element
is pushed once but not every element is popped, so a stack that is filled and
then abandoned does no rotation at all under the hinted design and all of it
under this one. Conversely a stack that is peeked at often wants the newest
element at the front, where `peek()` is free. And where one operation has a
latency budget and the other does not — a push that must return quickly, say —
the design that puts the rotation on the other side wins regardless of the
counts.

This settles questions 1 and 2.

### Counting the workload

Take $M$ pushes followed by $M$ pops. Under rotate-on-`push()` the $i$-th push
lands on $i - 1$ elements and costs $2(i - 1) + 1$ queue operations:

$$
\sum_{i=1}^{M} (2i - 1) = M^2
$$

and the $M$ pops cost one each, for $M^2 + M$ in total. Under rotate-on-`pop()`
the pushes cost $M$ and the $i$-th pop finds $M - i + 1$ elements and costs
$2(M - i + 1) - 1$:

$$
\sum_{n=1}^{M} (2n - 1) = M^2
$$

again, for the same $M^2 + M$. On this workload the two designs do identical
work; only the operation that pays differs. They part company when pushes and
pops interleave, because each design charges by the size of the stack at _its_
expensive moment — rotate-on-`push()` by the size when an element arrives,
rotate-on-`pop()` by the size when it leaves — and for the elements never popped
at all, which cost the hinted design nothing.

Both are $\Theta(N)$ per operation in the worst case, exactly the linear bound
the exercise asks for, and $\Theta(M^2)$ for the workload. There is no amortized
saving to be had: the cost is not an occasional spike that cheaper operations
prepay, it is paid on every push.

This settles question 3.

### What a second container remembers

A queue built from two stacks reaches $O(1)$ amortized because its expensive
step, the pour, _changes the arrangement_: the elements come out of the pour in
the order the queue needs, and they stay in that order until they are served.
Each element is reversed once in its lifetime, and the work of reversing it is
banked in the second stack, which holds the reversed order on the queue's
behalf. The pour is a spike, but it is prepaid.

A rotation banks nothing. Cycling a queue through a full turn leaves it holding
the same elements in the same order, so the work done by one rotation is not
available to the next — every push (or every pop, under the hinted design) does
its $\Theta(N)$ cycling from scratch, and the structure afterwards is no better
prepared than it was before. One queue has one order, and a stack needs the end
of that order that the queue hides; with nowhere to keep a second order, the
only way to reach the hidden end is to spin the whole thing past the visible
one.

That is the asymmetry: two containers can hold the elements in two orders at
once, and one can act as the other's memory; a single queue has a single order
and no memory beyond it.

This settles question 4.

### Practical notes

- Use the book's `Queue` from Section 1.3 as the only storage — no auxiliary
  arrays or lists; a `size` counter is fair game (or use `queue.size()`).
- **Empty-stack behaviour:** match the book's convention (throw
  `NoSuchElementException` on `pop()` of an empty stack).
- **Testing:** randomized interleavings against `java.util.ArrayDeque` (used as
  a stack) as the oracle; also instrument queue-op counts and confirm the
  linear-per-op shape empirically with a doubling test on workload size.

### The solution

The stack is the book's `Stack` API over a single instance of the book's
`Queue`, rotating on `push` so that the queue, front to back, is always the
stack, top to bottom. `pop`, `peek`, `isEmpty` and `size` each delegate to the
one queue operation of the same shape.

```java
static class StackWithAQueue<Item> {
    private Queue<Item> queue;

    public StackWithAQueue() {
        this(new Queue<>());
    }

    StackWithAQueue(Queue<Item> queue) {
        this.queue = queue;
    }

    public void push(Item item) {
        queue.enqueue(item);

        for (int i = 0; i < queue.size() - 1; i++) {
            queue.enqueue(queue.dequeue());
        }
    }

    public Item pop() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");

        return queue.dequeue();
    }

    public Item peek() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");

        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public String toString() {
        return queue.toString();
    }
}
```

- The loop bound is `size() - 1`, read after the enqueue: `size()` rotations
  would bring the queue back to where it started, and `size() - 2` would leave
  the new element one place short of the front. Re-reading `size()` on each
  iteration is safe because a rotation dequeues one element and enqueues one, so
  the size never changes inside the loop.
- The underflow guards come before the queue call, so an empty stack throws the
  stack's own `"Stack underflow"` rather than letting the queue's
  `"Queue underflow"` leak through the abstraction.
- The package-private constructor is a testing seam: it lets a test hand in a
  queue that counts its own operations, so the costs can be measured without an
  instrumentation field on the implementation.

**Cost.** A push onto $n$ elements costs $2n + 1$ queue operations; a pop or a
peek costs $1$. Every operation is $O(N)$ in the number of queue operations, as
the exercise requires, and $N$ pushes cost exactly $N^2$.

**Verification.** `Exercise_1_4_28Test` runs the trace above, the empty,
single-element, drained, interleaved and large ($5{,}000$ pushes, about $25$
million queue operations) cases, and $2{,}000$ random interleavings of up to
$500$ pushes, pops and peeks against `java.util.ArrayDeque`, with the push
probability drawn per run so some workloads pile up and others churn. A
`CountingQueue` subclass of `Queue`, injected through the package-private
constructor, counts every enqueue, dequeue and peek: the trace's four pushes
cost exactly $1 + 3 + 5 + 7 = 16$ queue operations, a pop and a peek cost one
each, and $1{,}000$ pushes cost exactly $1{,}000{,}000$. Three mutants of the
loop bound — rotating `size()` times, `size() - 2` times, and not at all — each
fail the same seven of the thirteen tests, starting with the trace, whose queue
no longer reads $4\ 3\ 2\ 1$ after the four pushes.

<br />
<br />
