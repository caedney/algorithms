# Exercise 1.4.31

_Deque with three stacks_. Implement a deque with three stacks so that each
deque operation takes a constant amortized number of stack operations.

---

## The problem

A full deque again, but now from **three plain stacks** — no steque, so no
insert-at-bottom primitive to lean on. Everything a steque would give you for
free must now be manufactured, and the third stack is the manufacturing floor.

The natural frame: two stacks face outward (the left stack serves
`pushLeft`/`popLeft`, the right stack serves `pushRight`/`popRight` — all $O(1)$
worst case), and the third stack is _scratch space_ used only during
rebalancing, when one outward stack empties while elements remain in the other.

### Why the third stack, exactly?

When the left stack empties, the leftmost surviving element is at the _bottom_
of the right stack. With a steque in hand, its `enqueue` lets you slide elements
underneath; with stacks only, reaching a bottom means pouring everything
somewhere — and one pour reverses order, two pours restore it — the same
reversal a queue built from two stacks turns to its advantage. The design
question: using pours through the scratch stack, how do you split the right
stack's $n$ elements so that the bottom half ends up on the left stack _in the
correct orientation_ and the top half returns to the right stack, also correctly
oriented? Count your pours — each element moved costs a pop and a push per pour,
and the sequence of pours is where solutions differ.

Suppose the left stack is empty and the right stack holds, bottom to top,
$1\ 2\ 3\ 4\ 5\ 6$ — like an array that grows at its right-hand end — so the
deque reads $1\ 2\ 3\ 4\ 5\ 6$ from left to right. After the rebalance the left
stack should hold $1$, $2$ and $3$ and the right stack $4$, $5$ and $6$, each in
whatever order keeps the deque reading the same way. Which order that is, for
each stack, is the first thing to work out. The tools: a pour from the right
stack to the scratch stack reverses, a pour from the scratch stack to the left
stack reverses again, and so on.

### Questions to guide your solution

1. Nail the orientation bookkeeping first, on paper: after each pour, write the
   bottom-to-top order of every stack. How many pours does your rebalance use,
   and does every element end up facing the right way?
2. Why _half_? Recycle the argument from the deque built from a stack and a
   steque: show that transferring all elements admits an adversary (alternating
   end-pops) with linear amortized cost, while the half-split maintains a
   balance invariant that makes the next rebalance far away.
3. Set up the potential function
   $\Phi = \lvert \text{size}(\text{left}) - \text{size}(\text{right}) \rvert$
   (or your variant): how much potential does a rebalance consume, how much can
   a single push or pop add, and what constant amortized bound falls out?
4. During a rebalance you need to count elements (to find the half-point). Do
   you need the stacks to expose `size()`, or can you maintain counters outside?
   Does either choice smuggle in a fourth "container"?

### Three pours

Pouring is the only way to reach the bottom of a stack, and every pour reverses
what it moves. The left stack wants the leftmost half with the leftmost element
on top — the _opposite_ of how that half sits in the right stack — so it needs
an odd number of pours; the right stack wants its half back the way it was, so
that half needs an even number. Four pour steps do both, and no element makes
more than three of them: pour the whole right stack onto the scratch stack, pour
the top half of the scratch stack back onto the right stack, pour that onto the
left stack, then pour the rest of the scratch stack onto the right stack. The
halves travel together for the first pour and part company at the second.

Take the left stack empty and the right stack holding $1\ 2\ 3\ 4\ 5\ 6$ bottom
to top, like an array that grows at its right-hand end, so that the deque reads
$1\ 2\ 3\ 4\ 5\ 6$ from left to right, and `popLeft`, which wants $1$:

| Operation   | Primitive                         |      Left |            Scratch |              Right | Returns |
| ----------- | --------------------------------- | --------: | -----------------: | -----------------: | ------: |
| `popLeft()` | left empty, so rebalance          |           |                    | $1\ 2\ 3\ 4\ 5\ 6$ |         |
|             | right `pop()` → scratch `push(6)` |           |                $6$ |    $1\ 2\ 3\ 4\ 5$ |         |
|             | right `pop()` → scratch `push(5)` |           |             $6\ 5$ |       $1\ 2\ 3\ 4$ |         |
|             | right `pop()` → scratch `push(4)` |           |          $6\ 5\ 4$ |          $1\ 2\ 3$ |         |
|             | right `pop()` → scratch `push(3)` |           |       $6\ 5\ 4\ 3$ |             $1\ 2$ |         |
|             | right `pop()` → scratch `push(2)` |           |    $6\ 5\ 4\ 3\ 2$ |                $1$ |         |
|             | right `pop()` → scratch `push(1)` |           | $6\ 5\ 4\ 3\ 2\ 1$ |                    |         |
|             | scratch `pop()` → right `push(1)` |           |    $6\ 5\ 4\ 3\ 2$ |                $1$ |         |
|             | scratch `pop()` → right `push(2)` |           |       $6\ 5\ 4\ 3$ |             $1\ 2$ |         |
|             | scratch `pop()` → right `push(3)` |           |          $6\ 5\ 4$ |          $1\ 2\ 3$ |         |
|             | right `pop()` → left `push(3)`    |       $3$ |          $6\ 5\ 4$ |             $1\ 2$ |         |
|             | right `pop()` → left `push(2)`    |    $3\ 2$ |          $6\ 5\ 4$ |                $1$ |         |
|             | right `pop()` → left `push(1)`    | $3\ 2\ 1$ |          $6\ 5\ 4$ |                    |         |
|             | scratch `pop()` → right `push(4)` | $3\ 2\ 1$ |             $6\ 5$ |                $4$ |         |
|             | scratch `pop()` → right `push(5)` | $3\ 2\ 1$ |                $6$ |             $4\ 5$ |         |
|             | scratch `pop()` → right `push(6)` | $3\ 2\ 1$ |                    |          $4\ 5\ 6$ |         |
|             | left `pop()`                      |    $3\ 2$ |                    |          $4\ 5\ 6$ |     $1$ |

After the first pour the scratch stack reads $6\ 5\ 4\ 3\ 2\ 1$: the leftmost
half is on top, already in the order $3\ 2\ 1$ that the left stack wants — but
one more pour would flip it to $1\ 2\ 3$ with $3$ on top. Routing it through the
now-empty right stack adds the extra reversal, and the right stack is free to
act as the waypoint precisely because the first pour emptied it. The other half,
$6\ 5\ 4$, has been reversed once and needs reversing once more, which the
single pour back to the right stack does.

Every element moved by a pour costs a `pop` and a `push`. All $n$ elements make
the first pour ($2n$), the right half makes one more ($n$), the left half makes
two more ($2n$): $5n$ primitives for the rebalance, $30$ here, plus the one
`pop` that `popLeft` does itself. Without an insert-at-bottom primitive the
third pour is unavoidable; it is the price of having only stacks, and the third
stack is what pays it.

This settles question 1: three pours, $5n$ primitives, every element facing the
right way. The `popRight` mirror image — left stack full, right stack empty — is
the same procedure with the roles of the two outward stacks swapped.

### Why a full pour sloshes

Moving _everything_ is cheaper than the split. Pouring the whole right stack
straight onto the left stack is a single pour: $1\ 2\ 3\ 4\ 5\ 6$ bottom to top
becomes $6\ 5\ 4\ 3\ 2\ 1$, with $1$ on top — the orientation the left stack
wants, because swapping which stack holds the deque swaps "bottom is leftmost"
for "top is leftmost" for free. That is $2n$ primitives against the split's
$5n$. The case against it is not what one rebalance costs but how often it
happens.

Pour everything and the right stack is empty, so the very next `popRight` wants
the element at the _bottom_ of the left stack and pours everything back.
Alternating ends forces a full pour on every call:

| Operation    | Primitive                      |         Left | Scratch |        Right | Returns |
| ------------ | ------------------------------ | -----------: | ------: | -----------: | ------: |
| `popLeft()`  | left empty, so pour            |              |         | $1\ 2\ 3\ 4$ |         |
|              | right `pop()` → left `push(4)` |          $4$ |         |    $1\ 2\ 3$ |         |
|              | right `pop()` → left `push(3)` |       $4\ 3$ |         |       $1\ 2$ |         |
|              | right `pop()` → left `push(2)` |    $4\ 3\ 2$ |         |          $1$ |         |
|              | right `pop()` → left `push(1)` | $4\ 3\ 2\ 1$ |         |              |         |
|              | left `pop()`                   |    $4\ 3\ 2$ |         |              |     $1$ |
| `popRight()` | right empty, so pour           |    $4\ 3\ 2$ |         |              |         |
|              | left `pop()` → right `push(2)` |       $4\ 3$ |         |          $2$ |         |
|              | left `pop()` → right `push(3)` |          $4$ |         |       $2\ 3$ |         |
|              | left `pop()` → right `push(4)` |              |         |    $2\ 3\ 4$ |         |
|              | right `pop()`                  |              |         |       $2\ 3$ |     $4$ |

Each pop costs about $2n$ primitives, and the deque shrinks by one on every pop,
so a run of $n$ alternating pops pours $n$, then $n - 1$, then $n - 2$ elements,
and so on. The table counts rather than lists, carrying the trace above on until
the deque is empty:

| Operation                    | Left | Right | Primitives | Running total |
| ---------------------------- | ---: | ----: | ---------: | ------------: |
| `popLeft()` — pour $4$, pop  |  $3$ |   $0$ |        $9$ |           $9$ |
| `popRight()` — pour $3$, pop |  $0$ |   $2$ |        $7$ |          $16$ |
| `popLeft()` — pour $2$, pop  |  $1$ |   $0$ |        $5$ |          $21$ |
| `popRight()` — pour $1$, pop |  $0$ |   $0$ |        $3$ |          $24$ |

Each row is $2$ primitives per element poured plus $1$ for the pop itself. In
general the pours move $n + (n - 1) + \dots + 1 = n(n + 1)/2$ elements:

$$
2 \cdot \frac{n(n + 1)}{2} + n = n^2 + 2n
$$

About $n^2$ for $n$ pops, so about $n$ per pop: linear per operation, not
constant.

The split avoids this by leaving the two outward stacks _balanced_. After it,
each holds half the deque, and a rebalance only happens when one of them is
empty — so at least $n/2$ operations must pass before the next one, however the
caller chooses ends (popping from one side only empties a stack soonest, after
$n/2$ pops; alternating takes about $n$). Rebalances can never happen back to
back, which is exactly what the full pour allowed.

Now the two numbers sit side by side. A rebalance is a bill of $5n$ primitives
that arrives all at once, on one unlucky pop. Share it evenly among the
operations between this rebalance and the next, of which there are at least
$n/2$:

$$
\frac{5n}{n/2} = 5n \cdot \frac{2}{n} = \frac{5\cancel{n} \cdot 2}{\cancel{n}} = 10
$$

The $n$ cancels, so each operation's share of the rebalance is a constant,
however large the deque. Add the one primitive every operation does for itself,
and a first, loose bound falls out:

$$
\text{amortized cost per operation} \le 10 + 1 = 11
$$

With the full pour the $n$ does not cancel. A $2n$ bill may be shared among as
few as one operation:

$$
\frac{2n}{1} = 2n
$$

This settles question 2. The $11$ is loose — it charges every rebalance at the
size of the deque when the _previous_ one happened — and the potential argument
below tightens it.

### The amortized bound

The bill-splitting argument made formal. A _potential function_ $\Phi$ is a
number computed from the current state, chosen to rise a little on every cheap
operation and fall a lot on an expensive one; an operation's amortized cost is
its real cost plus the change in $\Phi$. Here $\Phi$ measures how much trouble
is stored up: the imbalance between the two outward stacks, scaled by the price
of moving one element through a rebalance,

$$
\Phi = 5 \cdot \lvert \text{size}(\text{left}) - \text{size}(\text{right}) \rvert
$$

When the two stacks are equal, $\Phi = 0$ and no rebalance is near; when one
holds everything, $\Phi = 5n$ and the next pop may force one.

Three facts, each checked with numbers. First, an ordinary operation moves
$\Phi$ by exactly $5$: with sizes $(3, 5)$, $\Phi = 10$; `pushLeft` gives
$(4, 5)$ and $\Phi = 5$, `pushRight` gives $(3, 6)$ and $\Phi = 15$. One stack's
size changes by one, so the difference changes by one. Second, a rebalance of
$m$ elements starts from sizes $(0, m)$, so $\Phi = 5m$, and ends with the
halves equal or differing by one, so $\Phi$ falls by at least $5(m - 1)$. Third
— the fact the bill-splitting glossed over — the rebalance's real cost is $5m$
where $m$ is the size _at that moment_, and that is exactly $\Phi / 5$ just
before it. Three `popLeft`s from $(3, 3)$ give $(0, 3)$, and the fourth
rebalances $3$ elements at a cost of $15$, not $30$.

Now account for each kind of operation:

$$
\begin{aligned}
\text{ordinary:} \quad & 1 + 5 = 6 \\
\text{rebalance:} \quad & (5m + 1) - 5(m - 1) \le 6
\end{aligned}
$$

Every deque operation has amortized cost at most $6$ stack operations. The two
lines agree because both are measuring one element of imbalance, which $\Phi$
prices at $5$: the ordinary operation tips the balance by one element, and an
odd split leaves one element of imbalance behind, so it releases $5$ less than
it spent. For even $m$ the rebalance line is $1$. Informally: $\Phi$ was at most
$5$ after the last rebalance and rose by at most $5$ per operation, so about $m$
operations have passed since; the $5m$ bill shared among them is $5$ each, plus
the $1$ each does for itself.

This settles question 3. The loose bound of $11$ charged every rebalance at the
size of the deque when the previous one happened; the sequence that brings the
next rebalance soonest — $n/2$ pops from one side — also halves the deque, so
that rebalance costs $5n/2$, not $5n$, and spread over the $n/2$ operations that
is $5$ again.

### Finding the half-point

The rebalance needs one number, how many elements to peel off as the left half,
and that needs $n$. The book's `Stack` exposes `size()`, so the simplest answer
is to ask for it, as a deque built from a stack and a steque does.

Held to the strict stack API — `push`, `pop`, `isEmpty` and nothing else — the
count has to live elsewhere. One route is a pair of `int` fields in the deque,
one per outward stack, adjusted on every push and pop; it works, but it is
bookkeeping that every operation must keep honest. The other falls out of the
rebalance itself: the first pour moves every element, one at a time, until the
stack it drains is empty. Count the iterations and the total _is_ $n$ when the
loop ends — a local variable, no state between operations, and no extra cost
because the pour was touching every element anyway. The second pour then moves
half of that count.

Neither route smuggles in a fourth container. A local count adds no state at
all; two `int` fields are constant extra state, not a stack. The one thing the
pour-count cannot do is answer the deque's own `size()` and `isEmpty()`, which
need either the stacks' `size()` or those fields. Since the stack provides it,
`size()` is the consistent choice here; the pour-count is the fallback for a
stack that does not.

This settles question 4.

### Practical notes

- This is the deque built from a stack and a steque with the steque replaced by
  "you build it"; the analysis carries over, the mechanics get harder.
- **The scratch stack must be empty** outside rebalances — state it as part of
  the representation invariant and assert it.
- **Testing:** the alternating `popLeft`/`popRight` adversary is again the
  critical case; also randomized interleavings against `java.util.ArrayDeque`,
  plus an operation counter asserting
  $\text{stackOps} \le c \cdot \text{dequeOps}$ — then compare your measured $c$
  with the one your potential argument predicts.

### The solution

Two outward stacks and a scratch stack, with one `rebalance` serving both ends:
the full outward stack is `from`, the empty one is `to`, and the four pours are
the same whichever way round they are. A lone element is poured straight across,
since a split of one element would send it back where it came from.

```java
public static class DequeWithThreeStacks<Item> {
    private Stack<Item> left;
    private Stack<Item> scratch;
    private Stack<Item> right;

    public DequeWithThreeStacks() {
        this(new Stack<>(), new Stack<>(), new Stack<>());
    }

    DequeWithThreeStacks(Stack<Item> left, Stack<Item> scratch, Stack<Item> right) {
        this.left = left;
        this.scratch = scratch;
        this.right = right;
    }

    private void rebalance(Stack<Item> from, Stack<Item> to) {
        if (from.size() == 1)
            pour(from, to);
        else {
            pour(from, scratch);
            split(from, to);
        }
    }

    private void pour(Stack<Item> from, Stack<Item> to) {
        while (!from.isEmpty()) {
            to.push(from.pop());
        }
    }

    private void split(Stack<Item> from, Stack<Item> to) {
        int movedTop = scratch.size() / 2;
        int movedBottom = scratch.size() - movedTop;

        for (int i = 0; i < movedTop; i++) {
            from.push(scratch.pop());
        }

        for (int i = 0; i < movedTop; i++) {
            to.push(from.pop());
        }

        for (int i = 0; i < movedBottom; i++) {
            from.push(scratch.pop());
        }
    }

    public void pushLeft(Item item) {
        left.push(item);
    }

    public Item popLeft() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        if (left.isEmpty()) {
            rebalance(right, left);
        }

        return left.pop();
    }

    public void pushRight(Item item) {
        right.push(item);
    }

    public Item popRight() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        if (right.isEmpty()) {
            rebalance(left, right);
        }

        return right.pop();
    }

    public boolean isEmpty() {
        return left.isEmpty() && right.isEmpty();
    }

    public int size() {
        return left.size() + right.size();
    }

    public String toString() {
        return left.toString() + " " + right.toString();
    }
}
```

The decisions that matter:

- **One `rebalance`, parameterised by direction.** All three containers are
  stacks, so the procedure for `popLeft` (`from = right`, `to = left`) and for
  `popRight` (`from = left`, `to = right`) is the same code. The scratch stack
  is a field because it is the same stack in both cases.
- **`split` reads the scratch stack's size once, before the loops.** The scratch
  stack holds everything at that point, and both halves are computed from it up
  front because the sizes change inside the loops. The top half, `movedTop`,
  makes two pours (back to `from`, then on to `to`); the bottom half,
  `movedBottom`, makes one.
- **The one-element guard lives in `rebalance`, not `split`.** With a single
  element `movedTop` is $0$, so `split` would pour it straight back onto `from`
  and `to.pop()` would throw. A lone element is instead poured across in one
  step, $2$ primitives, the cheapest rebalance there is.
- **The scratch stack is empty between operations.** `rebalance` drains it
  completely, and nothing else touches it; the random test asserts this after
  every operation through the package-private constructor.
- **Underflow is the deque's own.** Both pops guard with `isEmpty()` and throw
  `"Deque underflow"` rather than letting a stack's `"Stack underflow"` leak.

Cost: a push is one primitive; a pop is one primitive plus, when its stack is
empty, a rebalance of $5m$ for the $m$ elements on the other side ($2$ for one
element). Amortized at most $6$ primitives per deque operation, by the potential
argument above.

**Verification.** `Exercise_1_4_31Test` (20 tests) injects a `CountingStack`
into all three positions through the package-private constructor and asserts the
exact costs: $6$ primitives for the easy-case sequence, with the scratch stack
never touched; $31$ for the `popLeft` trace of six elements and the same for its
mirror `popRight`; $3$ for a one-element rebalance; and the worst sequence of
eight `pushLeft`s then eight `popRight`s costing $41, 1, 1, 1, 21, 1, 11, 3$ for
the pops, $88$ in all. The same sequence at $n = 8, 64, 1024, 65536$ comes to
$5.50, 5.94, 5.996, 6.000$ primitives per operation — strictly between $5$ and
$6$, climbing to the bound from below, so the bound of $6$ is tight. $2{,}000$
random interleavings against `java.util.ArrayDeque` agree on every return value,
size and emptiness, with the scratch stack empty after every operation, and stay
under $6$ primitives per operation (the worst seen is about $4.5$). Mutants:
dropping the one-element guard fails $11$ tests, skipping the waypoint so the
top half makes only two pours fails $10$, pouring everything instead of
splitting fails $4$, and moving all but one element fails $4$. One mutant
survives — giving the _other_ side the extra element on an odd split,
`(size + 1) / 2` — because it is an equivalent design: same cost, same bound,
the balance off by one the other way.

<br />
<br />
