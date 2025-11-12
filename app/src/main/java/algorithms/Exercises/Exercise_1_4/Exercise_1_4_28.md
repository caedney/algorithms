# Exercise 1.4.28

_Stack with a queue_. Implement a stack with a single queue so that each stack
operations takes a linear number of queue operations. _Hint_: To delete an item,
get all of the elements on the queue one at a time, and put them at the end,
except for the last one which you should delete and return. (This solution is
admittedly very inefficient.)

---

## The problem

The converse of 1.4.27, with the constraints tightened and the performance bar
deliberately lowered: **one** queue only, and linear cost per operation is
accepted — the book even apologises for it. The point isn't speed; it's
understanding _why_ this direction is so much worse. A queue preserves order; a
stack needs the _newest_ element first; with a single FIFO pipe, the only way to
reach the far end is to cycle everything through.

### The rotation trick

A queue lets you remove from the front and insert at the back. Chaining those is
a _rotation_ — the queue is a ring you can spin:

```text
queue (front → back):  [ 1 2 3 4 ]        want pop() = 4 (last pushed)
rotate 3 times (dequeue+enqueue):
    [ 2 3 4 1 ] → [ 3 4 1 2 ] → [ 4 1 2 3 ]
now dequeue() = 4  ✓   and the remainder [1 2 3] is intact and in order
```

That's the hint, mechanised: N−1 rotations + 1 removal per pop.

### Questions to guide your solution

1. Two designs are possible: expensive `pop()` (rotate on delete, as hinted) or
   expensive `push()` (rotate after every insert so the newest sits at the
   front, making `pop()` trivial). Implement one, sketch the other. When would
   each be preferable (many pushes vs many pops)?
2. Where does `peek()` fit — can you support it without a full extra rotation in
   your chosen design?
3. Count queue operations exactly for a workload of M pushes and M pops in each
   design. Both are O(N) per operation — but the constants and _which_ operation
   pays differ.
4. The deeper question the book is nudging at: 1.4.27 got FIFO from two LIFOs at
   O(1) amortized, yet LIFO from one FIFO seems doomed to Ω(N) per op with one
   queue. What asymmetry between the two directions is responsible? (What can a
   second container "remember" that a single queue cannot?)

### Practical notes

- Use the book's `Queue` from Section 1.3 as the only storage — no auxiliary
  arrays or lists; a `size` counter is fair game (or use `queue.size()`).
- **Empty-stack behaviour:** match the book's convention (throw
  `NoSuchElementException` on `pop()` of an empty stack).
- **Testing:** randomized interleavings against `java.util.ArrayDeque` (used as
  a stack) as the oracle; also instrument queue-op counts and confirm the
  linear-per-op shape empirically with a doubling test on workload size.

<br />
<br />
