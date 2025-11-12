# Exercise 1.4.30

_Deque with a stack and a steque_. Implement a deque with a stack and a steque
(see EXERCISE 1.3.32) so that each deque operation takes a constant amortized
number of stack and steque operations.

---

## The problem

Climb one more rung of the ladder: a _deque_ supports insertion and removal at
**both** ends (`pushLeft`, `popLeft`, `pushRight`, `popRight` — the book's
1.3.33 names). Your building blocks are one stack (one flexible end) and one
steque (flexible top _plus_ insert-at-bottom, from 1.4.29). Constant amortized
cost again.

Inventory the primitive capabilities against the four required operations — this
is an exercise in matching interfaces:

```text
              insert-top  remove-top  insert-bottom  remove-bottom
stack             ✓           ✓             ✗              ✗
steque            ✓           ✓             ✓              ✗
deque needs at both ends: insert ✓✓  remove ✓✓
```

Neither component can _remove from its bottom_ — so each component must be
oriented with its removable end facing outward, and the crunch comes when one
side empties while the other still holds elements. Rebalancing is where all the
thinking lives.

### The crunch, concretely

Say the stack guards the left end and the steque the right. `popLeft` on an
empty stack means the leftmost element is at the _bottom_ of the steque —
unreachable directly. You must transfer elements across. A full pour reverses
order (stack semantics); the steque's `enqueue` (insert-at-bottom) is the extra
tool 1.4.27 didn't have. Which transfer, of _how many_ elements, restores the
invariant while keeping the amortized bound? (Transferring everything each time
is tempting but examine what an alternating `popLeft`/`popRight` adversary does
to it.)

### Questions to guide your solution

1. Write the representation invariant (deque left-to-right = stack
   top-to-bottom + steque top-to-bottom, or your variant) and verify all four
   operations against it.
2. When one side empties, moving _all_ n elements to the other container can be
   forced to repeat by an adversary alternating end-pops. Does moving only
   _half_ the elements change the analysis? What invariant about balance does
   that maintain, and what does the potential/credit argument look like?
3. How do you even _split_ the steque's contents in half using the available
   primitives — what sequence of pops/pushes/enqueues (possibly routing through
   the stack) achieves "bottom half stays, top half moves"?
4. Total up: what constant do you get per deque operation, and which operation
   sequence realises the worst case?

### Practical notes

- Build on your 1.4.29 steque — and only through its _public_ API (`push`,
  `pop`, `enqueue`); reaching into its internals dissolves the exercise.
  Remember each steque op is itself amortized O(1) stack ops, so your bound
  composes.
- **Adversarial tests matter more than random ones here:** alternating
  `popLeft`/`popRight` from a large deque is _the_ sequence that kills naive
  rebalancing — make it a named test.
- Oracle: `java.util.ArrayDeque` under long random + adversarial interleavings
  of all four ops; instrument primitive-op counts and check the constant.

<br />
<br />
