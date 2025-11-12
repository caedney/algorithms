# Exercise 1.4.29

_Steque with two stacks_. Implement a steque with two stacks so that each steque
operation (see EXERCISE 1.3.32) takes a constant amortized number of stack
operations.

---

## The problem

A _steque_ (stack-ended queue) is a data type supporting three operations:
`push` (add to top), `pop` (remove from top), and `enqueue` (add to the
_bottom_). Build it from two stacks with **constant amortized** cost per
operation.

Structurally this sits right on top of the well-known construction of a queue
from two stacks: there, two stacks simulate pure FIFO; here the target ADT mixes
LIFO (`push`/`pop` at one end) with one FIFO-ish operation (`enqueue` at the
other end). The design question is how to assign the two ends of the steque to
the two stacks so that every operation is cheap at _its_ end, and the expensive
reconciliation (pouring one stack into the other) happens rarely enough to
amortize away.

### The shape to consider

Give each end of the steque its own stack. A _front-stack_ holds the top region,
with its top of stack at the steque's top; a _back-stack_ holds the bottom
region, with its top of stack at the steque's bottom. Then `push` is one push
onto the front-stack and `enqueue` is one push onto the back-stack — both $O(1)$
worst case. `pop` is one pop from the front-stack — but what when the
front-stack is empty?

When `pop` finds the front-stack empty, the top of the steque is buried at the
_bottom_ of the back-stack — exactly the situation a two-stack queue faces on
`dequeue`. Decide what a pour does to orderings (the back-stack holds the
steque's bottom on top; pouring reverses) and convince yourself the poured items
land in correct pop order.

Here is the design on a short sequence. Stacks are written bottom to top, like
an array that grows at its right-hand end, so the rightmost element is the one a
pop would return; the front-stack's top is the steque's top and the back-stack's
top is the steque's bottom.

| Operation    | Primitive                                  | Front-stack | Back-stack | Returns |
| ------------ | ------------------------------------------ | ----------: | ---------: | ------: |
| `enqueue(1)` | back-stack `push(1)`                       |             |        $1$ |         |
| `enqueue(2)` | back-stack `push(2)`                       |             |     $1\ 2$ |         |
| `push(3)`    | front-stack `push(3)`                      |         $3$ |     $1\ 2$ |         |
| `pop()`      | front-stack `pop()`                        |             |     $1\ 2$ |     $3$ |
| `pop()`      | front-stack empty, so pour                 |             |     $1\ 2$ |         |
|              | back-stack `pop()` → front-stack `push(2)` |         $2$ |        $1$ |         |
|              | back-stack `pop()` → front-stack `push(1)` |      $2\ 1$ |            |         |
|              | front-stack `pop()`                        |         $2$ |            |     $1$ |
| `enqueue(4)` | back-stack `push(4)`                       |         $2$ |        $4$ |         |
| `push(5)`    | front-stack `push(5)`                      |      $2\ 5$ |        $4$ |         |
| `pop()`      | front-stack `pop()`                        |         $2$ |        $4$ |     $5$ |
| `pop()`      | front-stack `pop()`                        |             |        $4$ |     $2$ |
| `pop()`      | front-stack empty, so pour                 |             |        $4$ |         |
|              | back-stack `pop()` → front-stack `push(4)` |         $4$ |            |         |
|              | front-stack `pop()`                        |             |            |     $4$ |

The first `pop` is served straight from the front-stack. The second finds it
empty: the steque's top is $1$, enqueued first and therefore at the _bottom_ of
the back-stack, and the pour brings it to the top of the front-stack with $2$
beneath it. The `enqueue(4)` that arrives mid-way lands on the back-stack and
stays there while the front-stack still has elements to serve; it comes out
last, after $5$ and $2$, which is where the steque's order puts it.

### Questions to guide your solution

1. Write down the representation invariant precisely: reading the steque
   top-to-bottom = front-stack top-to-bottom followed by back-stack
   **bottom-to-top**. Check every operation preserves it, including the pour.
2. Lifetime accounting: how many stack pushes/pops can a single element
   experience end-to-end (arrival by `push` vs by `enqueue` differ!)? What
   constant does that give?
3. Does the credit argument for the two-stack queue transfer verbatim, or does
   `enqueue` need to deposit a different number of credits than `push`? Where is
   a poured element's second move paid from?
4. Adversarial check: is there an operation sequence that forces a pour _every
   other operation_? Why or why not — what must be true of the back-stack's size
   for a pour to be expensive, and who paid for those elements?

### The representation invariant

Each stack has a top, and so does the steque, and they are not all the same end.
The steque's top is whatever `pop` returns next. The front-stack's top is the
steque's top; the back-stack's top is the steque's _bottom_. That gives the rule
for reading the steque off the two stacks.

**Claim.** At every moment, the steque read top to bottom is the front-stack
read top to bottom, followed by the back-stack read bottom to top.

_Proof._ Each operation is checked as a snapshot: assume the rule holds before
it, and look at the stacks exactly as they stand after it.

A `push` adds an item to the front-stack's top, which is the first thing the
rule reads, so the new item is at the steque's top — where `push` puts it. A
`pop` that finds the front-stack non-empty removes the front-stack's top, which
the rule read first, so it removes the steque's top — what `pop` should return.
The rest of the reading is untouched in both cases.

An `enqueue` adds an item to the back-stack's top. The rule reads the back-stack
bottom to top, so its top is read _last_, and the new item lands at the steque's
bottom — where `enqueue` puts it.

The pour happens only when `pop` finds the front-stack empty, so just before it
the rule says the whole steque is the back-stack read bottom to top. The pour
pops every item off the back-stack and pushes it onto the front-stack, which
reverses their order: the back-stack's bottom becomes the front-stack's top.
Afterwards the back-stack is empty and the rule reads the steque as the
front-stack top to bottom — the same sequence as before. The `pop` that follows
then takes the front-stack's top, which is the steque's top. $\blacksquare$

Take the back-stack holding $1$, $2$, $3$, $4$, enqueued in that order with
nothing pushed. Drawn upright:

```text
            front-stack    back-stack
top    →                       4
                               3
                               2
bottom →                       1
```

Read bottom to top it gives $1\ 2\ 3\ 4$, which is the steque top to bottom: the
next `pop` returns $1$, and $4$, the last enqueued, sits at the steque's bottom
while being on the back-stack's top.

A `pop` now finds the front-stack empty and pours. $4$ is popped first and lands
at the bottom of the front-stack; $1$ is popped last and lands on top:

```text
            front-stack    back-stack
top    →        1
                2
                3
bottom →        4
```

Read top to bottom the front-stack gives $1\ 2\ 3\ 4$ — the steque unchanged,
with $1$ on top ready to be popped.

This settles question 1.

### The lifetime of an element

Follow one element from the operation that adds it to the `pop` that returns it.
How many stack operations it experiences depends on which way it came in.

An element added by `push` goes onto the front-stack and is later popped off it:
two stack operations, push and pop. It is never poured, because the pour moves
items in one direction only, from the back-stack to the front-stack, and this
element was on the front-stack from the start.

An element added by `enqueue` goes onto the back-stack. To reach the steque's
top it must be poured — popped off the back-stack and pushed onto the
front-stack — and is then popped off the front-stack: four stack operations,
push, pop, push, pop. It is poured at most once, for the same one-way reason:
once on the front-stack, nothing ever moves it back.

**Claim.** Any sequence of $M$ steque operations performs at most $4M$ stack
operations.

_Proof._ Every stack operation acts on some element, and each element accounts
for at most $4$ over its lifetime — $2$ if it arrived by `push`, $4$ if by
`enqueue`. At most $M$ elements are ever added, so the total is at most $4M$.
The worst case is a workload in which every element arrives by `enqueue`; any
mix with pushes costs less. $\blacksquare$

The constant is $4$, the same as for a queue built from two stacks.

This settles question 2.

### The credit argument

The same bound can be stated as a budget. Let each stack operation cost one
coin. Each steque operation pays a fixed number of coins up front, spends what
its own stack operations need immediately, and leaves the rest on the element as
a prepaid balance. The rule is that nothing may go into debt: every later stack
operation on an element must be covered by coins already on it.

A `push` pays $2$: one for its own push onto the front-stack, and one left on
the element for its final pop.

An `enqueue` pays $4$: one for its own push onto the back-stack, two left for
the pour — the pop off the back-stack and the push onto the front-stack — and
one left for its final pop.

A `pop` pays $0$. If it finds the front-stack non-empty, its one stack operation
is the final pop of the element on top, which that element already carries a
coin for. If it finds the front-stack empty, the pour it sets off may move many
elements, but every one of them arrived by `enqueue` and carries the two coins
for its own move; then the final pop is covered as before. The `pop` spends
nothing of its own, however large the pour.

So the argument for a queue built from two stacks does not carry over verbatim:
there every element is deposited the same way, while here the two arrival routes
deposit different amounts — $2$ for `push`, $4$ for `enqueue`. A poured
element's second move, the push onto the front-stack, is paid from the coins its
`enqueue` left behind. Summed over any sequence of $M$ operations, the amortized
costs give at most $4M$ stack operations, agreeing with the lifetime count.

Nothing in the code changes — there are no coins. The scheme is bookkeeping for
the proof.

This settles question 3.

### The adversarial check

An adversary can force a pour on every other operation. Start empty and repeat
`enqueue`, `pop`: each `enqueue` puts its item on the back-stack, so each `pop`
finds the front-stack empty and pours.

```text
            front-stack    back-stack
top    →                       1
```

Each of those pours moves a single element, though. Every `enqueue`/`pop` pair
costs exactly $4$ stack operations — push onto the back-stack, pop off it, push
onto the front-stack, pop off it. The `enqueue` does the first of those, the
`pop` the other three.

To compare that with the bound, count steque operations the way $M$ does: the
`enqueue` is one and the `pop` is another, so a pair is two steque operations
and $M$ operations make $M/2$ pairs. The total is then

$$
\frac{M}{2} \cdot 4 = 2M,
$$

an average of $2$ stack operations per steque operation, well inside the $4M$
bound. Reaching $4M$ would need every steque operation to cost $4$ on average;
here the `enqueue` costs $1$ and the `pop` costs $3$.

For a single pour to be expensive the back-stack must hold many elements, and
each of those got there by an `enqueue` that already left two coins on it to pay
for its move. So pours can be frequent only if they are small, and a large pour
must be preceded by as many prepaid enqueues as it moves. The adversary cannot
have both, and the total stays within $4M$ whatever the sequence.

In fact no adversary gets close to $4M$. The lifetime count charges every
element $4$ whether or not it is ever popped, but a popped element needs two
steque operations — its add and its pop — to earn that charge, and an element
never popped is charged at most $3$.

**Claim.** Any sequence of $M$ steque operations performs fewer than $3M$ stack
operations, and the ratio can be made as close to $3$ as desired.

_Proof._ Let the sequence contain $a$ adds (pushes and enqueues) and $p$ pops,
so $a + p = M$. Each add is one stack operation. Each pop is one stack operation
plus whatever its pour moves, and a pour moves each enqueued element at most
once at a cost of $2$, so all the pours together cost at most $2a$. The total is
therefore at most

$$
a + p + 2a = 3a + p.
$$

With $p = 0$ the total is $a = M$. With $p \ge 1$,

$$
3a + p < 3a + 3p = 3(a + p) = 3M.
$$

For the ratio, take $n$ enqueues followed by one `pop`. The pour moves all $n$
elements, so the cost is $n + 2n + 1 = 3n + 1$ for $n + 1$ operations, and

$$
\frac{3n + 1}{n + 1} \to 3 \quad \text{as } n \to \infty.
$$

$\blacksquare$

So the worst a random adversary can achieve is a ratio just under $3$, and the
$4M$ bound is loose by a constant: it pays each element's final pop in advance,
even for elements that never leave.

This settles question 4.

### Practical notes

- Get the two-stack queue working first and diff the designs — the delta is
  small and seeing it isolates what `enqueue` really adds.
- API: `push()`, `pop()`, `enqueue()`, plus `isEmpty()`/`size()`; keep generics
  and use the book's `Stack`.
- **Testing:** oracle with a `java.util.ArrayDeque`
  (`addFirst`/`removeFirst`/`addLast`) under long random interleavings; count
  stack ops and assert $\text{stackOps} \le c \cdot \text{stequeOps}$, then find
  empirically the worst $c$ a random adversary achieves — compare with your
  analysis.

### The solution

The steque is the book's `Stack` API twice over: a front-stack whose top is the
steque's top and a back-stack whose top is the steque's bottom. Every `push` is
one push onto the front-stack, every `enqueue` one push onto the back-stack, and
every `pop` one pop from the front-stack, preceded by a pour whenever the
front-stack is empty.

```java
static class StequeWithTwoStacks<Item> {
    private Stack<Item> front;
    private Stack<Item> back;

    public StequeWithTwoStacks() {
        this(new Stack<>(), new Stack<>());
    }

    StequeWithTwoStacks(Stack<Item> front, Stack<Item> back) {
        this.front = front;
        this.back = back;
    }

    public void push(Item item) {
        front.push(item);
    }

    public Item pop() {
        if (isEmpty())
            throw new NoSuchElementException("Steque underflow");

        if (front.isEmpty()) {
            while (!back.isEmpty()) {
                front.push(back.pop());
            }
        }

        return front.pop();
    }

    public void enqueue(Item item) {
        back.push(item);
    }

    public boolean isEmpty() {
        return front.isEmpty() && back.isEmpty();
    }

    public int size() {
        return front.size() + back.size();
    }
}
```

- The pour is guarded by `front.isEmpty()`, not by whether the back-stack has
  anything — pouring onto a non-empty front-stack buries the steque's top under
  elements that belong beneath it.
- The underflow check comes before the pour, so an empty steque throws
  `"Steque underflow"` rather than letting the front-stack's own exception leak
  through.
- `isEmpty` and `size` combine the two stacks; an element is in exactly one of
  them at any time.
- The package-private constructor is a testing seam: it lets a test hand in
  stacks that count their own pushes and pops, so the amortized bound can be
  measured without an instrumentation field on the implementation.

**Cost.** A pushed element is pushed and popped once, an enqueued element is
pushed, popped, pushed and popped once, so any sequence of $M$ steque operations
performs at most $4M$ stack operations: $O(1)$ amortized per steque operation. A
single `pop` that finds $n$ elements on the back-stack costs $2n + 1$, so the
worst case per operation is $O(n)$.

**Verification.** `Exercise_1_4_29Test` runs the trace above, the empty,
single-element (pushed and enqueued), drained, interleaved and large cases, and
$2{,}000$ random interleavings of up to $500$ operations against
`java.util.ArrayDeque`, with the mix of `push`, `enqueue` and `pop` drawn per
run so some workloads pile up and others churn. A `CountingStack` subclass of
`Stack`, injected through the package-private constructor, counts every push and
pop: a pushed element costs exactly $2$ and an enqueued one exactly $4$; the
trace's two pops cost $1$ and then $5$; one `pop` after $10{,}000$ enqueues
costs exactly $20{,}001$; the alternating `enqueue`/`pop` adversary costs
exactly $4$ per pair; and across the random workloads the total never exceeds
$4$ times the number of steque operations — the worst ratio observed was $2.94$,
just under the $3$ that the adversarial check shows no sequence can reach. Both
wrong pour policies (pour on every `pop`, and pour whenever the back-stack is
non-empty) fail the same four tests, returning $1$ where the trace expects $3$;
sending `enqueue` to the front-stack fails ten.

<br />
<br />
