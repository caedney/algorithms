# Exercise 1.4.36

_Space usage for pushdown stacks_. Justify the entries in the table below, which
shows typical space usage for various pushdown stack implementations. Use a
static nested class for linked-list nodes to avoid the non-static nested class
overhead.

| data structure     | item type | space usage for _N_ int values (bytes) |
| ------------------ | --------- | -------------------------------------- |
| _linked list_      | `int`     | ~32*N*                                 |
|                    | `Integer` | ~64*N*                                 |
| _resisizing array_ | `int`     | between ~4*N* and ~16*N*               |
|                    | `Integer` | between ~32*N* and ~56*N*              |

---

## The problem

The space-side companion to 1.4.35: justify four memory figures for a stack
holding N int values, using the book's 64-bit memory model (Section 1.4, pages
200–204): object overhead 16 bytes, references 8 bytes, padding to a multiple of
8 — and, per the exercise's instruction, a **static** nested Node class (a
non-static one would add 8 bytes per node for the outer-instance pointer; part
of the justification is knowing that).

Two structural facts drive the whole table. Linked lists pay a fixed per-element
toll (a node per item, forever), so their entries are single tilde-terms.
Resizing arrays pay per _slot_, not per item, and the number of slots floats
between N (just-doubled... or is it just-full?) and 4N (just before halving at
quarter-full) — hence the entries are **ranges**. Reproducing both endpoints of
each range is where the understanding shows.

### Worked pattern (one cell, partially)

_Linked list / int_: each node = 16 (overhead) + 8 (next reference) + 4 (int
item) + 4 (padding) = **32 bytes** → ~32N total (the stack object itself is a
lower-order constant). Now the other three cells:

- **Linked/Integer:** the node stores a _reference_ instead of the int —
  recompute the node (does padding change?), then add the `Integer` object each
  element drags along (its size is on page 201). Does 64N reconcile?
- **Resizing/int:** a slot is one `int` = 4 bytes. Justify both endpoints: when
  is the array exactly full (4 bytes/item), and how empty can it get before the
  implementation halves? (The ¼-full policy from 1.4.32 returns — at
  just-above-quarter-full, each item is backed by how many slots?)
- **Resizing/Integer:** slots are 8-byte references, and each _live_ item also
  owns a 24-byte `Integer`. Which of the two components scales with slots and
  which with items? Assemble the ~32N..~56N range from that split — the
  asymmetry (empty slots cost 8, full slots cost 8+24) is the whole point of the
  cell.

### Questions to guide your solution

1. Derive each range endpoint from an explicit array state: capacity C vs count
   N, with the doubling/halving policy bounding C/N between which two values _in
   the steady state_?
2. The exercise says "space usage for N int values" — for the Integer rows, are
   all N boxes distinct objects? (What would the JVM's −128..127 autobox cache
   do to a real measurement, and why does the book's model ignore it?)
3. Redo the linked/int cell with a _non-static_ node class: which entry does the
   table's instruction save you from, and by how much per node?
4. Cross-check against 1.4.33's 32-bit rules: which cells would shrink the most
   on a 32-bit machine, and does the resizing/int range change at all?

### Practical notes

- Draw the object graphs (stack → array → boxes, stack → node → node → ...) and
  price each box — arithmetic errors here almost always come from forgetting
  array overhead (16 + 4 length + 4 padding = 24 bytes) or padding rules, not
  from the concept.
- The array itself is one object whose size depends on capacity: 24 + 4C (int)
  or 24 + 8C (references) — lower-order overall but write it down before
  discarding it as non-leading.
- "resisizing" is the book's typo again; the policy is Algorithm 1.1's: double
  when full, halve when quarter-full.

<br />
<br />
