# Exercise 1.4.35

_Time costs for pushdown stacks_. Justify the entries in the table below, which
shows typical time costs for various pushdown stack implementations, using a
cost model that counts both _data references_ (references to data pushed onto
the stack, either an array reference or a reference to an object's instance
variable) and _objects created_.

| data structure     | item type | data references | objects created |
| ------------------ | --------- | --------------- | --------------- |
| _linked list_      | `int`     | 2*N*            | _N_             |
|                    | `Integer` | 3*N*            | 2*N*            |
| _resisizing array_ | `int`     | ~5*N*           | lg _N_          |
|                    | `Integer` | ~5*N*           | ~_N_            |

---

## The problem

An accounting exercise with a _specified cost model_ — the table's numbers are
for a workload of **N pushes** onto an initially empty stack, and you must
justify every cell by walking the actual code (the book's linked-list `Stack`,
page 149, and `ResizingArrayStack`, page 141) and tallying two ledgers: (1)
_data references_ — each read or write of a stack item, whether through an array
slot or an object's instance variable; (2) _objects created_ — every `new`,
including nodes, boxed integers, and arrays.

The table rewards understanding of exactly the things earlier exercises measured
empirically: where autoboxing spends (1.4.37), where resizing spends (1.4.32),
and why "objects created" is a cost worth its own column (allocation and GC
pressure, per the chapter's memory discussion).

### How to attack one cell (method, not answers)

Take _linked list / int_ as the pattern: for each `push()`, list every line that
touches an item or allocates. A node is created (ledger 2); the item is written
into `node.item` (ledger 1); ... and does anything else touch the item? Multiply
per-push costs by N, then reconcile with the table's `2N` and `N`. Now repeat
for the other three rows, asking for each:

- **Linked/Integer:** what _extra_ object does each push create beyond the node,
  and which extra data reference does boxing/unboxing add?
- **Resizing/int:** pushes write `a[n]` (N references) — where do the _other_
  ~4N come from? Recall each doubling copies every live element (one read + one
  write per element) and total copy work across all doublings is a geometric
  series. And why is "objects created" only **lg N** — what exactly gets
  allocated, and how many times?
- **Resizing/Integer:** the array copies move _references_ — do copies touch
  items differently than in the `int` case? Which column changes and which
  doesn't, and why does objects-created jump from lg N to ~N?

### Questions to guide your solution

1. Write the per-operation tally table (push, pop, resize-copy per element) for
   each implementation before scaling by N — the table's entries should then be
   one-line sums.
2. Which entries are exact (no ~) and which are tilde-approximate, and what does
   that distinction track? (Hint: which costs depend on where N sits relative to
   a power of two?)
3. The model counts an array _reference_, `a[i]`, as one data reference. Does
   `a[n++] = item` cost one or two in this model? Fix a convention and apply it
   consistently — being explicit about the convention is most of "justifying".
4. Why does the resizing array's ~5N _not_ grow when items are boxed, while the
   linked list's 2N grows to 3N? What does that say about where each
   implementation keeps its items?

### Practical notes

- The book's tilde notation (page 179) is in force: report leading terms, drop
  lower-order ones — e.g. total copy references across doublings is ~4N _for N a
  power of two_; state your assumption.
- If a cell won't reconcile, instrument real code: wrap the item array / node
  fields behind counting accessors and run N pushes — the counter should match
  your formula exactly for exact cells.
- Note the printed quirks — "date references" and "resisizing" are the book's
  own typos; the model's word is _data references_.

<br />
<br />
