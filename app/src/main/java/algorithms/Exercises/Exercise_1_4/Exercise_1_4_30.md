# Exercise 1.4.30

_Deque with a stack and a steque_. Implement a deque with a stack and a steque
(see EXERCISE 1.3.32) so that each deque operation takes a constant amortized
number of stack and steque operations.

---

## The problem

A _deque_ supports insertion and removal at **both** ends (`pushLeft`,
`popLeft`, `pushRight`, `popRight` — the book's deque API). Your building blocks
are one stack (one flexible end) and one steque (flexible top _plus_
insert-at-bottom, the book's stack-ended queue). Each deque operation must cost
a constant amortized number of stack and steque operations.

Inventory the primitive capabilities against the four required operations — this
is an exercise in matching interfaces:

| Container | Insert at top | Remove from top | Insert at bottom | Remove from bottom |
| --------- | :-----------: | :-------------: | :--------------: | :----------------: |
| stack     |       ✓       |        ✓        |        ✗         |         ✗          |
| steque    |       ✓       |        ✓        |        ✓         |         ✗          |

The deque needs insert and remove at both ends. Neither component can _remove
from its bottom_ — so each component must be oriented with its removable end
facing outward, and the crunch comes when one side empties while the other still
holds elements. Rebalancing is where all the thinking lives.

### The easy case, traced

While both containers are non-empty, every deque operation is a single primitive
on one of them. The stack and the steque are both written bottom to top, like an
array that grows at its right-hand end, so in each column the rightmost element
is the one a `pop` would return and the steque's `enqueue` would insert at the
left. Start empty:

| Operation      | Primitive        |  Stack | Steque | Returns |
| -------------- | ---------------- | -----: | -----: | ------: |
| `pushLeft(1)`  | stack `push(1)`  |    $1$ |        |         |
| `pushLeft(2)`  | stack `push(2)`  | $1\ 2$ |        |         |
| `pushRight(3)` | steque `push(3)` | $1\ 2$ |    $3$ |         |
| `pushRight(4)` | steque `push(4)` | $1\ 2$ | $3\ 4$ |         |
| `popLeft()`    | stack `pop()`    |    $1$ | $3\ 4$ |     $2$ |
| `popRight()`   | steque `pop()`   |    $1$ |    $3$ |     $4$ |

The steque's `enqueue` never appears: inserting at the steque's bottom puts an
element at the _seam_ between the two containers, not at either end of the
deque.

### The crunch, concretely

Say the stack guards the left end and the steque the right. `popLeft` on an
empty stack means the leftmost element is at the _bottom_ of the steque —
unreachable directly. You must transfer elements across. A full pour reverses
order (stack semantics); the steque's `enqueue` (insert-at-bottom) is the extra
tool a plain stack doesn't have. Which transfer, of _how many_ elements,
restores the invariant while keeping the amortized bound? (Transferring
everything each time is tempting but examine what an alternating
`popLeft`/`popRight` adversary does to it.)

### Questions to guide your solution

1. Where does each element live? Write down the rule that says, for any contents
   of the stack and the steque, what the deque reads left to right. Then check
   that each of the four operations keeps the rule true.
2. When one container is empty and its end is asked for, elements have to be
   moved across from the other one. If you move all of them, a caller
   alternating `popLeft` and `popRight` can make you move them all back on every
   call — why? If you move only half, how many cheap operations must the caller
   make before it can force another move? What is true of the two sizes just
   after a move, and how does that pay for the move?
3. The elements you want to move are at the _bottom_ of the steque, but `pop`
   only reaches its top. Using the stack as scratch space, what sequence of
   `pop`, `push` and `enqueue` leaves the bottom half in the steque and puts the
   top half on the stack, each in the right order?
4. Add it up: how many primitives does a deque operation cost, amortized, and
   which sequence of calls is the most expensive one?

### Why a full pour sloshes

**Claim.** While both containers are non-empty, the deque read left to right is
the stack read top to bottom followed by the steque read bottom to top, and each
of the four deque operations preserves this with a single primitive.

_Proof._ `pushLeft` and `popLeft` push and pop the stack's top, which is the
deque's left end. `pushRight` and `popRight` push and pop the steque's top,
which is the deque's right end. Neither touches the seam between the two
containers, so the reading is unchanged except at the end operated on.
$\blacksquare$

The invariant breaks down as a guide only when one side is empty. Take the stack
empty and the steque holding $1\ 2\ 3\ 4$ bottom to top: the deque reads
$1\ 2\ 3\ 4$ and `popLeft` wants $1$, which sits at the steque's bottom, the one
end no primitive can remove from. The obvious repair is a _pour_: pop every
element off the steque and push it on the stack. Popping reverses the order, so
the stack ends up holding $4\ 3\ 2\ 1$ bottom to top with $1$ on top, exactly
where `popLeft` wants it. The pour is correct — the reading after it is the same
as before — and it never needs `enqueue`.

Its cost is the problem. Pouring everything leaves the steque empty, so the very
next `popRight` wants the element at the _bottom_ of the stack and has to pour
everything back. Alternating ends therefore forces a pour on every call:

| Operation    | Primitive                        |        Stack |       Steque | Returns |
| ------------ | -------------------------------- | -----------: | -----------: | ------: |
| `popLeft()`  | stack empty, so pour             |              | $1\ 2\ 3\ 4$ |         |
|              | steque `pop()` → stack `push(4)` |          $4$ |    $1\ 2\ 3$ |         |
|              | steque `pop()` → stack `push(3)` |       $4\ 3$ |       $1\ 2$ |         |
|              | steque `pop()` → stack `push(2)` |    $4\ 3\ 2$ |          $1$ |         |
|              | steque `pop()` → stack `push(1)` | $4\ 3\ 2\ 1$ |              |         |
|              | stack `pop()`                    |    $4\ 3\ 2$ |              |     $1$ |
| `popRight()` | steque empty, so pour            |    $4\ 3\ 2$ |              |         |
|              | stack `pop()` → steque `push(2)` |       $4\ 3$ |          $2$ |         |
|              | stack `pop()` → steque `push(3)` |          $4$ |       $2\ 3$ |         |
|              | stack `pop()` → steque `push(4)` |              |    $2\ 3\ 4$ |         |
|              | steque `pop()`                   |              |       $2\ 3$ |     $4$ |
| `popLeft()`  | stack empty, so pour             |              |       $2\ 3$ |         |
|              | steque `pop()` → stack `push(3)` |          $3$ |          $2$ |         |
|              | steque `pop()` → stack `push(2)` |       $3\ 2$ |              |         |
|              | stack `pop()`                    |          $3$ |              |     $2$ |

Every operation costs a number of primitives proportional to the elements
present. No amortization can rescue that, because the work is never banked: the
pile just sloshes from one container to the other and back.

This is new. A queue built from two stacks, or a steque built from two stacks,
removes from one end only, so an element flows one way — in on one side, poured
across once, out on the other — and is poured at most once in its lifetime. A
deque removes from both ends, so a pour in one direction can be undone by demand
from the other, and the once-in-a-lifetime accounting is gone.

The repair is to leave something behind. If a pour moves only _half_ of the
non-empty side, both containers hold about $n/2$ elements afterwards, and no
sequence of calls can trigger another pour until one of them has been emptied by
$n/2$ single-primitive pops. An $O(n)$ pour then buys $n/2$ cheap operations
before the next one can happen, which is the shape an amortized bound needs. How
to move only half, when the elements to move are at the steque's bottom and
`pop` only reaches its top, is the next question.

This settles question 1, and the first part of question 2: moving all $n$
elements can be forced to repeat on every call, and moving half cannot. The
balance invariant and the credit argument are still to come.

### Splitting the steque

The elements that need to move are at the steque's bottom and `pop` reaches only
its top, so the stack looks like the obvious scratch space. It isn't needed.
Popping the steque's top and immediately enqueuing it moves that element to the
bottom — a _rotation_ by one — and $n/2$ rotations carry the top half underneath
the bottom half, leaving the elements that belong on the stack sitting on top,
ready to be popped across in the order the stack wants them. Take the stack
empty and the steque holding $1\ 2\ 3\ 4\ 5\ 6$ bottom to top, and `popLeft`,
which wants $1$:

| Operation   | Primitive                            |     Stack |             Steque | Returns |
| ----------- | ------------------------------------ | --------: | -----------------: | ------: |
| `popLeft()` | stack empty, so split                |           | $1\ 2\ 3\ 4\ 5\ 6$ |         |
|             | steque `pop()` → steque `enqueue(6)` |           | $6\ 1\ 2\ 3\ 4\ 5$ |         |
|             | steque `pop()` → steque `enqueue(5)` |           | $5\ 6\ 1\ 2\ 3\ 4$ |         |
|             | steque `pop()` → steque `enqueue(4)` |           | $4\ 5\ 6\ 1\ 2\ 3$ |         |
|             | steque `pop()` → stack `push(3)`     |       $3$ |    $4\ 5\ 6\ 1\ 2$ |         |
|             | steque `pop()` → stack `push(2)`     |    $3\ 2$ |       $4\ 5\ 6\ 1$ |         |
|             | steque `pop()` → stack `push(1)`     | $3\ 2\ 1$ |          $4\ 5\ 6$ |         |
|             | stack `pop()`                        |    $3\ 2$ |          $4\ 5\ 6$ |     $1$ |

Three rotations turn $1\ 2\ 3\ 4\ 5\ 6$ into $4\ 5\ 6\ 1\ 2\ 3$: the right-hand
half is now on top, in the order $3$, $2$, $1$ that popping produces, which is
exactly the order the stack must receive them in for $1$ to end up on top. The
left-hand half $4\ 5\ 6$ is untouched. The split costs $n/2$ pops and enqueues
plus $n/2$ pops and pushes — $2n$ primitives for $n$ elements — and leaves the
deque reading $1\ 2\ 3\ 4\ 5\ 6$ as before, now spread across both containers.

The mirror case cannot use the same trick directly, because the stack has no
`enqueue`. Take the steque empty and the stack holding $6\ 5\ 4\ 3\ 2\ 1$ bottom
to top — the same deque, with $1$ on top — and `popRight`, which wants $6$ at
the stack's bottom. Pouring the whole stack across, pop by pop, puts it in the
steque in deque order, because popping reverses and pushing reverses again. That
is the state the split already handles:

| Operation    | Primitive                            |              Stack |             Steque | Returns |
| ------------ | ------------------------------------ | -----------------: | -----------------: | ------: |
| `popRight()` | steque empty, so pour                | $6\ 5\ 4\ 3\ 2\ 1$ |                    |         |
|              | stack `pop()` → steque `push(1)`     |    $6\ 5\ 4\ 3\ 2$ |                $1$ |         |
|              | stack `pop()` → steque `push(2)`     |       $6\ 5\ 4\ 3$ |             $1\ 2$ |         |
|              | stack `pop()` → steque `push(3)`     |          $6\ 5\ 4$ |          $1\ 2\ 3$ |         |
|              | stack `pop()` → steque `push(4)`     |             $6\ 5$ |       $1\ 2\ 3\ 4$ |         |
|              | stack `pop()` → steque `push(5)`     |                $6$ |    $1\ 2\ 3\ 4\ 5$ |         |
|              | stack `pop()` → steque `push(6)`     |                    | $1\ 2\ 3\ 4\ 5\ 6$ |         |
|              | then split                           |                    | $1\ 2\ 3\ 4\ 5\ 6$ |         |
|              | steque `pop()` → steque `enqueue(6)` |                    | $6\ 1\ 2\ 3\ 4\ 5$ |         |
|              | steque `pop()` → steque `enqueue(5)` |                    | $5\ 6\ 1\ 2\ 3\ 4$ |         |
|              | steque `pop()` → steque `enqueue(4)` |                    | $4\ 5\ 6\ 1\ 2\ 3$ |         |
|              | steque `pop()` → stack `push(3)`     |                $3$ |    $4\ 5\ 6\ 1\ 2$ |         |
|              | steque `pop()` → stack `push(2)`     |             $3\ 2$ |       $4\ 5\ 6\ 1$ |         |
|              | steque `pop()` → stack `push(1)`     |          $3\ 2\ 1$ |          $4\ 5\ 6$ |         |
|              | steque `pop()`                       |          $3\ 2\ 1$ |             $4\ 5$ |     $6$ |

The pour is $n$ pops and $n$ pushes, $2n$ primitives, and the split another $2n$
— $4n$ in all. The steque's `enqueue` does the rebalancing in both directions;
the stack's only job is to hold its half. One case needs care: after a pour of
a _single_ element it is already on the steque's top and the split would only
move it back, so `popRight` splits only when the pour left more than one.

This settles question 3.

### The amortized bound

A rebalance is expensive, so the argument has to show that one cannot follow
another too soon, and that whatever makes the next one bigger also pays for it.

**Claim.** Immediately after a rebalance of $n$ elements, each container holds
at least $\lfloor n/2 \rfloor$ elements, and once the pop that triggered it has
been served, at least $\lfloor n/2 \rfloor - 1$.

_Proof._ The split rotates $\lfloor n/2 \rfloor$ elements, which stay on the
steque, and moves the remaining $\lceil n/2 \rceil$ onto the empty stack; the
triggering pop then removes one element from one side. $\blacksquare$

A rebalance fires only when a container is empty, and every deque operation
removes at most one element from one container. So between a rebalance of $n$
elements and the next one the caller must make at least
$\lfloor n/2 \rfloor - 1$ pops. Pushes do not help empty anything; they make the
next rebalance _bigger_, because there is more to pour and rotate. But each push
is a deque operation in its own right, so it can carry the cost of the element
it adds. If the caller makes $p$ pushes between rebalances and none of the
pushed elements is popped again (if some are, that only adds pops), then the
next rebalance acts on about $n/2 + p$ elements, and the caller made about
$n/2 + p$ operations to reach it. The size of a rebalance is, within a constant,
the number of deque operations since the previous one.

That turns the cost into a fixed fee, and the fee is easiest to see per element.
Follow one element through a rebalance. The pour touches it twice — stack
`pop()`, steque `push()` — and the split touches it twice more: steque `pop()`
then steque `enqueue()` if it is in the half that rotates, steque `pop()` then
stack `push()` if it is in the half that moves. Four primitives per element,
whichever half it lands in, and $4m$ for a rebalance of $m$ elements.

Now spread that back. The number of elements in a rebalance is about the number
of deque operations since the previous one, so four primitives per element
becomes four per operation. Add the one primitive each operation performs
directly — a `pushLeft` is one stack `push()`, a `popRight` on a non-empty
steque is one steque `pop()` — and every deque operation is charged $5$
primitives: one for its own work and four for its share of the rebalance work it
helped bring about. A rebalance is not charged to the call that happens to
trigger it; it is paid for in advance by the operations that built up the side
being emptied. The total for any sequence of $M$ deque operations is therefore
about $5M$ primitives, give or take a small fixed slack; the tests measure the
ratio actually observed.

Each deque operation costs a constant amortized number of stack and steque
operations, which is what the exercise asks for. The constant is loose — a
`popRight` that pours could serve its pop before splitting, for one — but
tightening it does not change the argument.

### The worst sequence

Two things make a rebalance expensive: how many elements it handles, and whether
it needs the pour. An empty stack costs only the split, $2m$ for $m$ elements.
An empty steque costs the pour and then the split, $4m$, because the pour on its
own leaves the stack empty and the next `popLeft` would have to rebalance all
over again. So the caller who wants to spend the bank empties the steque, every
time.

Before the first pop there is no balance at all. $n$ calls of `pushLeft` put
every element in the stack at one primitive each; a single `popRight` then finds
the steque empty with all $n$ elements on the wrong side and pays $2n$ for the
pour and $2n$ for the split — about $4n$ primitives in one call, the most
expensive single operation there is.

That rebalance leaves about $n/2$ on each side. To force another expensive one
the caller must empty the steque again, which takes $n/2$ pops, and the
rebalance that follows handles only the $n/2$ still in the stack: $2n$
primitives. The next needs $n/4$ pops and costs $n$; the next $n/8$ pops and
$n/2$; and so on, halving each time. The rebalance work over the whole sequence
is about $4n + 2n + n + \dots = 8n$ primitives, spread over roughly $2n$
operations — the $n$ pushes and the $n$ pops — which is $4$ per operation, plus
the $1$ each performed directly. The adversary can make the average reach the
fee of $5$, and nothing can push it past.

Here is that sequence on $n = 8$. The table counts rather than lists: the
`Stack` and `Steque` columns hold the number of elements on each side after the
row, and `Primitives` is what the row cost. Rebalance rows are the pour ($2m$),
the split ($2m$) and the pop that triggered it ($1$); a single element left on
the stack is poured but not split, since after the pour it is already where
`popRight` wants it.

| Operation                              | Stack | Steque | Primitives | Running total |
| -------------------------------------- | ----: | -----: | ---------: | ------------: |
| `pushLeft(1)` … `pushLeft(8)`          |   $8$ |    $0$ |        $8$ |           $8$ |
| `popRight()` — pour $8$, split, pop    |   $4$ |    $3$ |       $33$ |          $41$ |
| `popRight()` three times               |   $4$ |    $0$ |        $3$ |          $44$ |
| `popRight()` — pour $4$, split, pop    |   $2$ |    $1$ |       $17$ |          $61$ |
| `popRight()`                           |   $2$ |    $0$ |        $1$ |          $62$ |
| `popRight()` — pour $2$, split, pop    |   $1$ |    $0$ |        $9$ |          $71$ |
| `popRight()` — pour $1$, pop           |   $0$ |    $0$ |        $3$ |          $74$ |

Each rebalance row breaks down as follows. The pour is one `pop` and one `push`
per element; the split rotates $\lfloor m/2 \rfloor$ elements at a `pop` and an
`enqueue` each, then moves $\lceil m/2 \rceil$ at a `pop` and a `push` each;
the triggering `popRight` then does its own single `pop`:

| Rebalance of $m$ | Pour $2m$ | Rotations $2 \lfloor m/2 \rfloor$ | Moves $2 \lceil m/2 \rceil$ | Pop | Total |
| ---------------: | --------: | --------------------------------: | ----------------------------: | --: | ----: |
|              $8$ |      $16$ |                               $8$ |                           $8$ | $1$ |  $33$ |
|              $4$ |       $8$ |                               $4$ |                           $4$ | $1$ |  $17$ |
|              $2$ |       $4$ |                               $2$ |                           $2$ | $1$ |   $9$ |

The three rebalances cost $32 + 16 + 8 = 56$ primitives — the $4n + 2n + n$
series — the last pour of a single element adds $2$, and the sixteen
operations' own primitives add $16$, for $74$ in all: about $4.6$ per
operation, under the fee of $5$ with the slack going to the rounding at small
sizes.

This settles questions 2 and 4: a constant amortized number of primitives per
deque operation, about $5$, with the worst single operation costing about $4n$
and the worst sequence being $n$ pushes on one end followed by $n$ pops from the
other.

### Practical notes

- Build on your steque — and only through its _public_ API (`push`, `pop`,
  `enqueue`); reaching into its internals dissolves the exercise. If your steque
  is itself built from stacks, each steque operation is amortized $O(1)$ stack
  operations, so your bound composes.
- **Adversarial tests matter more than random ones here:** alternating
  `popLeft`/`popRight` from a large deque is _the_ sequence that kills naive
  rebalancing — make it a named test.
- Oracle: `java.util.ArrayDeque` under long random + adversarial interleavings
  of all four ops; instrument primitive-op counts and check the constant.

### The solution

The deque is the book's deque API over one `Stack` and one `Steque`: the stack
guards the left end and the steque the right, so `pushLeft`/`popLeft` are a
stack `push`/`pop` and `pushRight`/`popRight` a steque `push`/`pop` while the
side being asked is non-empty. When it is empty, `popLeft` splits the steque
and `popRight` pours the stack across and then splits.

```java
public static class DequeWithAStackAndASteque<Item> {
    private Stack<Item> left;
    private Steque<Item> right;

    public DequeWithAStackAndASteque() {
        this(new Stack<>(), new Steque<>());
    }

    DequeWithAStackAndASteque(Stack<Item> left, Steque<Item> right) {
        this.left = left;
        this.right = right;
    }

    private void pour() {
        while (!left.isEmpty()) {
            right.push(left.pop());
        }
    }

    private void split() {
        int rightHalf = right.size() / 2;
        int leftHalf = right.size() - rightHalf;

        for (int i = 0; i < rightHalf; i++) {
            right.enqueue(right.pop());
        }

        for (int i = 0; i < leftHalf; i++) {
            left.push(right.pop());
        }
    }

    public void pushLeft(Item item) {
        left.push(item);
    }

    public Item popLeft() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        if (left.isEmpty()) {
            split();
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
            pour();

            if (right.size() > 1)
                split();
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

- The two halves are computed before the loops, because the steque's size
  changes as the loops run. `rightHalf` is the number rotated, which stay on
  the steque; `leftHalf` is the rest, which move to the stack. With one
  element that is $0$ rotations and $1$ move, so a `popLeft` on a lone steque
  element works without a special case — a draft with a `size() > 1` guard
  inside `split` left the stack empty there and threw.
- The one guard lives in `popRight`, after the pour. A single element poured
  across is already on the steque's top; splitting it would move it straight
  back to the stack and the final `pop` would find the steque empty. The guard
  sits where the knowledge is — `popRight` knows what a pour leaves — and
  keeps `split` itself unconditional for `popLeft`'s sake.
- `pour` and `split` are the two directions anything ever moves: all of the
  stack onto the steque, half of the steque onto the stack. Neither takes
  parameters, since each always acts on the same two fields, and `popRight`
  is the only caller of both.
- The underflow guards come before any container call, so an empty deque
  throws its own `"Deque underflow"` rather than the inner `"Stack underflow"`
  or `"Steque underflow"`.
- The package-private constructor is a testing seam: it lets a test hand in a
  stack and a steque that count their own operations.

**Cost.** An operation on a non-empty side is one primitive. An empty stack
costs a split, $2m$ for $m$ elements on the steque; an empty steque costs a
pour and a split, $4m$ for $m$ elements on the stack. Every deque operation is
a constant amortized number of primitives, about $5$, as the exercise
requires.

**Verification.** `Exercise_1_4_30Test` (18 tests) runs the easy-case trace,
the empty, single-element (on either side), drained, interleaved,
alternating-ends and large ($100{,}000$ pushes then pops from the other end)
cases, and $2{,}000$ random interleavings of up to $500$ operations of all four
kinds against `java.util.ArrayDeque`, with the push probability drawn per run.
A `CountingStack` and a `CountingSteque`, injected through the package-private
constructor, count every primitive: the easy-case trace costs $6$, the
`popLeft` split of six elements costs exactly $2 \cdot 3 + 2 \cdot 3 + 1 = 13$,
the `popRight` pour and split of six costs exactly $2 \cdot 6 + 12 + 1 = 25$,
and the $n = 8$ worst sequence costs exactly $33, 1, 1, 1, 17, 1, 9, 3$ per pop
and $74$ in all. The worst sequence at $n = 8$, $64$, $1{,}024$ and $65{,}536$
stays strictly between $4$ and $5$ primitives per operation ($4.63$, $4.95$,
$4.997$, $4.99995$), and no random interleaving exceeds $5$ — the worst
observed was about $2.6$. Mutation check: the two drafts from the session fail
$4$ and $9$ of the $18$ tests (the `split` guard breaks `popLeft` on a lone
steque element; no guard at all breaks `popRight` on a lone stack element),
and three policy mutants — never rotate and move everything (the
pour-everything policy), rotate but move nothing, and move only one — fail
$8$, $11$ and $9$, each starting with the worst-sequence trace.

<br />
<br />
