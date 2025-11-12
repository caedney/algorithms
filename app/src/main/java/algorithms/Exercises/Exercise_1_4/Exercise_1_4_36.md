# Exercise 1.4.36

_Space usage for pushdown stacks_. Justify the entries in the table below, which
shows typical space usage for various pushdown stack implementations. Use a
static nested class for linked-list nodes to avoid the non-static nested class
overhead.

| data structure   | item type | space usage for $N$ int values (bytes) |
| ---------------- | --------- | -------------------------------------- |
| _linked list_    | `int`     | $\sim 32N$                             |
|                  | `Integer` | $\sim 64N$                             |
| _resizing array_ | `int`     | between $\sim 4N$ and $\sim 16N$       |
|                  | `Integer` | between $\sim 32N$ and $\sim 56N$      |

---

## Correction

The linked-list `Integer` entry is printed as $\sim 64N$. With the static nested
node class the exercise asks for, a node is $32$ bytes and each `Integer` is
$24$, so the entry should be $\sim 56N$. The printed $\sim 64N$ is what a
_non-static_ node gives ($40 + 24$), which is the overhead the exercise says to
avoid — and the `int` entry beside it, $\sim 32N$, is the static figure, so the
two linked-list rows are not priced the same way. Unverified against the printed
page.

## The problem

The memory-side counterpart of the chapter's time-cost tables: justify four
memory figures for a stack holding $N$ `int` values, using the book's 64-bit
memory model (Section 1.4, pages 200–204): object overhead $16$ bytes,
references $8$ bytes, padding to a multiple of $8$ — and, per the exercise's
instruction, a **static** nested Node class (a non-static one would add $8$
bytes per node for the outer-instance pointer; part of the justification is
knowing that).

Two structural facts drive the whole table. Linked lists pay a fixed per-element
toll (a node per item, forever), so their entries are single tilde-terms.
Resizing arrays pay per _slot_, not per item, and the number of slots floats
between $N$ (just-doubled... or is it just-full?) and $4N$ (just before halving
at quarter-full) — hence the entries are **ranges**. Reproducing both endpoints
of each range is where the understanding shows.

### Worked pattern (one cell, partially)

Price the object graph of each implementation, one cell at a time:

- **Linked list / `int`:** price one node of the static nested class:

  | Part             | Bytes |
  | ---------------- | ----: |
  | object overhead  |  $16$ |
  | `next` reference |   $8$ |
  | `int` item       |   $4$ |
  | padding          |   $4$ |
  | Total            |  $32$ |

  so $\sim 32N$ in total (the stack object itself is a lower-order constant).

- **Linked list / `Integer`:** the node stores a _reference_ instead of the
  `int` — recompute the node (does padding change?), then add the `Integer`
  object each element drags along (its size is on page 201). Does $\sim 64N$
  reconcile?
- **Resizing array / `int`:** a slot is one `int`, $4$ bytes. Justify both
  endpoints: when is the array exactly full ($4$ bytes per item), and how empty
  can it get before the implementation halves? (The book's `ResizingArrayStack`
  halves at one-quarter full — at just above a quarter full, each item is backed
  by how many slots?)
- **Resizing array / `Integer`:** slots are $8$-byte references, and each _live_
  item also owns a $24$-byte `Integer`. Which of the two components scales with
  slots and which with items? Assemble the $\sim 32N$ to $\sim 56N$ range from
  that split — the asymmetry (empty slots cost $8$, full slots cost $8 + 24$) is
  the whole point of the cell.

### Questions to guide your solution

1. Derive each range endpoint from an explicit array state: capacity $C$ vs
   count $N$, with the doubling/halving policy bounding $C/N$ between which two
   values _in the steady state_?
2. The exercise says "space usage for $N$ int values" — for the `Integer` rows,
   are all $N$ boxes distinct objects? (What would the JVM's $-128$ to $127$
   autobox cache do to a real measurement, and why does the book's model ignore
   it?)
3. Redo the linked/int cell with a _non-static_ node class: which entry does the
   table's instruction save you from, and by how much per node?
4. Redo the table for a 32-bit machine, with its smaller references and object
   overhead: which cells shrink the most, and does the resizing/int range change
   at all?

### The linked list

#### `int` items

A node of the static nested class has two instance variables, `int item` and
`Node next`. Under the book's model, with padding to reach a multiple of $8$:

| Part             | Bytes |
| ---------------- | ----: |
| object overhead  |  $16$ |
| `next` reference |   $8$ |
| `int` item       |   $4$ |
| padding          |   $4$ |
| Total            |  $32$ |

The nodes are not the only thing in memory. The `Stack` object that holds the
list is an object too, with its own two instance variables, `Node first` and
`int N`, and it is priced by the same rules:

| Part              | Bytes |
| ----------------- | ----: |
| object overhead   |  $16$ |
| `first` reference |   $8$ |
| `int` N           |   $4$ |
| padding           |   $4$ |
| Total             |  $32$ |

It does not grow as items are pushed, so a stack of $N$ items takes

$$
32N + 32
$$

bytes exactly. The tilde keeps the leading term only — a fixed $32$ bytes is
negligible beside $32N$ as $N$ grows — which gives the table's $\sim 32N$.

#### `Integer` items

The node's `item` field is now a reference. The `Integer` is not stored inside
the node; it is a separate object, and the node points at it in the same way
that `next` points at another node. So the node is overhead plus two references:

| Part             | Bytes |
| ---------------- | ----: |
| object overhead  |  $16$ |
| `next` reference |   $8$ |
| `item` reference |   $8$ |
| Total            |  $32$ |

No padding is needed, and the node is the same size as before: the reference is
$4$ bytes bigger than the `int` it replaces, and it takes up exactly the $4$
bytes that were padding. The `Integer` object itself is overhead plus one `int`
field, padded:

| Part            | Bytes |
| --------------- | ----: |
| object overhead |  $16$ |
| `int` value     |   $4$ |
| padding         |   $4$ |
| Total           |  $24$ |

Each item on the stack costs one node and one `Integer`:

| Part           | Bytes |
| -------------- | ----: |
| node           |  $32$ |
| `Integer`      |  $24$ |
| Total per item |  $56$ |

so the stack takes $\sim 56N$ — not the table's $\sim 64N$.

#### The non-static node

An object of a non-static nested class carries a hidden $8$-byte reference to
the enclosing instance (page 201), here the `Stack` the node belongs to. Adding
it to each node:

| Part                                | `int` node | `Integer` node |
| ----------------------------------- | ---------: | -------------: |
| object overhead                     |       $16$ |           $16$ |
| reference to the enclosing instance |        $8$ |            $8$ |
| `next` reference                    |        $8$ |            $8$ |
| item                                |        $4$ |            $8$ |
| padding                             |        $4$ |            $0$ |
| Total                               |       $40$ |           $40$ |

With the $24$-byte `Integer` added to its node, the four possibilities are:

| item type | static node | non-static node | table      |
| --------- | ----------- | --------------- | ---------- |
| `int`     | $\sim 32N$  | $\sim 40N$      | $\sim 32N$ |
| `Integer` | $\sim 56N$  | $\sim 64N$      | $\sim 64N$ |

The table's `int` entry is the static figure and its `Integer` entry is the
non-static one. Following the exercise's instruction, the `Integer` entry is
$\sim 56N$.

This settles question 3: declaring the node `static` saves $8$ bytes per node,
$\sim 40N$ down to $\sim 32N$ for `int` items.

### The resizing array

The items now live in one array, and the stack object holding it is a fixed
cost, as before. Write $C$ for the array's length — its capacity, which is not
necessarily the number of items $N$ on the stack.

#### `int` items

An `int[]` of length $C$ takes $24 + 4C$ bytes (page 202): a $24$-byte header
and $4$ bytes per entry, so $\sim 4C$. The cost depends on how $C$ relates to
$N$, and the book's `ResizingArrayStack` keeps that within fixed limits: the
array is never more than full, and it is halved when it falls to one-quarter
full, so just before that there are about $4$ slots for every item.

| State                               | Slots per item | Bytes per slot | Bytes per item |
| ----------------------------------- | -------------: | -------------: | -------------: |
| Full, $C = N$                       |            $1$ |            $4$ |            $4$ |
| Just before halving, $C \approx 4N$ |            $4$ |            $4$ |           $16$ |

Between those two states $C$ lies somewhere from $N$ to $4N$, which is why the
entry is a range, $\sim 4N$ to $\sim 16N$, and not a single figure.

#### `Integer` items

The array is an `Integer[]`: each slot is an $8$-byte reference, and each item
on the stack also has its own $24$-byte `Integer`. The two costs are counted
differently — the references per _slot_, the `Integer` objects per _item_ —
because an empty slot points at nothing.

| Part             |    Full, $C = N$ | Just before halving, $C \approx 4N$ |
| ---------------- | ---------------: | ----------------------------------: |
| slot references  | $1 \times 8 = 8$ |                   $4 \times 8 = 32$ |
| `Integer` object |             $24$ |                                $24$ |
| Total per item   |             $32$ |                                $56$ |

The empty slots cost only their $8$-byte reference, which is why the range runs
from $\sim 32N$ to $\sim 56N$ and not to four times the lower figure.

This settles question 1: $C/N$ lies between $1$ (full) and $4$ (one-quarter
full, just before halving), and each endpoint of each range is one of those two
states.

### Shared `Integer` objects

Both `Integer` rows assume every item has an `Integer` of its own. That is not
always so. A client pushing an `int` onto a `Stack<Integer>` has it converted by
`Integer.valueOf`, which keeps one ready-made `Integer` for each value from
$-128$ to $127$ and returns that same object every time; outside that range it
makes a new object on each call.

Push the value $5$ a thousand times and there are a thousand nodes but one
`Integer`, with every node's `item` reference pointing at it. The reference is
already part of the $32$-byte node, so the stack costs $32$ bytes per node plus
a single shared $24$-byte `Integer`, which the tilde drops: $\sim 32N$. The
linked-list stack of `Integer` items therefore costs between $\sim 32N$, when
every value is in the shared range, and $\sim 56N$, when none is.

The table prices the second case. It is the worst case, and it is also the
typical one: the shared range covers $256$ of the roughly four billion values an
`int` can hold, so for $N$ arbitrary values almost every push gets its own
`Integer`. The same assumption is behind the $24$ bytes per item in the
resizing-array `Integer` row.

This settles question 2.

### On a 32-bit machine

On a 32-bit machine references are $4$ bytes, object overhead is $8$ bytes, and
padding is to a multiple of $4$. Pricing the same objects by those rules:

**Linked list / `int`.** The node needs no padding, since $16$ is already a
multiple of $4$:

| Part             | Bytes |
| ---------------- | ----: |
| object overhead  |   $8$ |
| `next` reference |   $4$ |
| `int` item       |   $4$ |
| Total            |  $16$ |

That gives $\sim 16N$.

**Linked list / `Integer`.** The node is again $16$ bytes, with a $4$-byte
reference in place of the `int`:

| Part             | Bytes |
| ---------------- | ----: |
| object overhead  |   $8$ |
| `next` reference |   $4$ |
| `item` reference |   $4$ |
| Total            |  $16$ |

An `Integer` is already a multiple of $4$, so it takes no padding either:

| Part            | Bytes |
| --------------- | ----: |
| object overhead |   $8$ |
| `int` value     |   $4$ |
| Total           |  $12$ |

Each item costs one of each:

| Part           | Bytes |
| -------------- | ----: |
| node           |  $16$ |
| `Integer`      |  $12$ |
| Total per item |  $28$ |

That gives $\sim 28N$.

**Resizing array / `int`.** An `int` is $4$ bytes on either machine, and the
array's smaller header is a constant the tilde drops, so the range is unchanged:
$\sim 4N$ to $\sim 16N$.

**Resizing array / `Integer`.** A slot is a $4$-byte reference and an `Integer`
is $12$ bytes:

| Part             |    Full, $C = N$ | Just before halving, $C \approx 4N$ |
| ---------------- | ---------------: | ----------------------------------: |
| slot references  | $1 \times 4 = 4$ |                   $4 \times 4 = 16$ |
| `Integer` object |             $12$ |                                $12$ |
| Total per item   |             $16$ |                                $28$ |

That gives $\sim 16N$ to $\sim 28N$.

Side by side, with the 64-bit linked-list `Integer` entry at its corrected
value:

| data structure   | item type | 64-bit                            | 32-bit                            |
| ---------------- | --------- | --------------------------------- | --------------------------------- |
| _linked list_    | `int`     | $\sim 32N$                        | $\sim 16N$                        |
|                  | `Integer` | $\sim 56N$                        | $\sim 28N$                        |
| _resizing array_ | `int`     | between $\sim 4N$ and $\sim 16N$  | between $\sim 4N$ and $\sim 16N$  |
|                  | `Integer` | between $\sim 32N$ and $\sim 56N$ | between $\sim 16N$ and $\sim 28N$ |

This settles question 4: every entry built from references and object overhead
halves, and the resizing array of `int` items, which has neither, does not
change at all.

### Practical notes

- Draw the object graphs (stack → array → boxes, stack → node → node → ...) and
  price each box — arithmetic errors here almost always come from forgetting
  array overhead ($16 + 4 + 4 = 24$ bytes: overhead, length, padding) or padding
  rules, not from the concept.
- The array itself is one object whose size depends on capacity: $24 + 4C$
  (`int`) or $24 + 8C$ (references) — lower-order overall but write it down
  before discarding it as non-leading.
- The resize policy is the book's `ResizingArrayStack` (Algorithm 1.1): double
  when full, halve when one-quarter full.

<br />
<br />
