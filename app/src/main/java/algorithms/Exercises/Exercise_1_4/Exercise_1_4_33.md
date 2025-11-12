# Exercise 1.4.33

_Memory requirements on a 32-bit machine_. Give the memory requirements for
`Integer`, `Date`, `Counter`, `int[]`, `double[]`, `double[][]`, `String`,
`Node`, and `Stack` (linked-list representation) for a 32-bit machine. Assume
that references are 4 bytes, object overhead is 8 bytes, and padding is to a
multiple of 4 bytes.

---

## The problem

Section 1.4's memory-model discussion (pages 200–204) works every one of these
examples for a **64-bit** machine: $8$-byte references, $16$-byte object
overhead, padding to multiples of $8$. This exercise re-runs the whole table
under 32-bit rules — references $4$, overhead $8$, padding to $4$. It's an
accounting drill, and the durable skill is the _method_: decompose any object
into overhead + fields + padding, recursively.

### The rules, side by side

| ingredient                  | 64-bit (book's text)                          | 32-bit (this exercise)      |
| --------------------------- | --------------------------------------------- | --------------------------- |
| object overhead             | $16$ bytes                                    | $8$ bytes                   |
| reference                   | $8$ bytes                                     | $4$ bytes                   |
| padding multiple            | $8$ bytes                                     | $4$ bytes                   |
| extra for inner class       | $8$ (outer ref)                               | $4$ (outer ref)             |
| array                       | overhead + length int + padding, then entries | same recipe, 32-bit numbers |
| `int` / `double` primitives | $4$ / $8$                                     | $4$ / $8$ (unchanged!)      |

Primitives don't shrink — only references, overhead, and padding do. That's why
the savings differ so much between, say, `double[]` (mostly primitive payload)
and a linked `Stack` (mostly pointers and overhead).

### One worked example to set the pattern

`Integer` on 32-bit: $8$ (overhead) $+\ 4$ (the `int` field) $= 12$, already a
multiple of $4$ → **$12$ bytes** (the book's 64-bit answer is $24$ — note it
_halved_, coincidentally).

Now do the rest yourself; for each, first list the instance variables from the
book's own class (Date: three `int`s; Counter: a `String` ref + an `int`; Node:
two references — and is it an inner class?; String: in the book's model, a
`char[]` reference plus three `int`s).

### Questions to guide your solution

1. For each type, write the formula _symbolically first_ (e.g. Stack of $N$
   items $=$ stack object $+\ N$ nodes $+\ N$ item objects?) then substitute
   32-bit numbers. Which totals should be functions of $N$?
2. `Node` in the book's `Stack` is a (non-static) inner class — where does the
   extra outer-instance reference show up in your count, and how much would a
   `static` nested class save?
3. `double[][]` with $M$ rows and $N$ columns: how many array-overhead charges
   do you pay, and why does the row-array overhead matter less as $N$ grows?
4. Does a `Stack<Integer>` holding $N$ values cost the same as the sum of a
   Stack's nodes plus $N$ `Integer`s? When might values be shared (autoboxing
   cache for $-128 \ldots 127$) and does the accounting change?
5. Sanity check each answer against the book's 64-bit figure: which types drop
   by the biggest _ratio_, and can you explain the pattern (pointer-heavy vs
   primitive-heavy)?

### Practical notes

- Use the book's class definitions (Date page 91, Counter page 89, Node page
  149, the String model on page 202) — real JDK internals differ (e.g. modern
  compact strings), but the exercise is about the book's model.
- Padding bites only occasionally at multiple-of-$4$: note the cases where the
  32-bit total needs no padding but the 64-bit one did.
- Present the result as a table mirroring page 201 so the two columns can be
  compared at a glance.

## Answers

Each type gets the same tally as the book's 64-bit figures: object overhead
first, then the instance variables, then padding to round the total up to a
multiple of $4$. Methods, local variables and `static` members cost nothing per
object. The book's figure sits beside each total so the saving can be read off.

### `Integer`

One `int` field. $8 + 4 = 12$, already a multiple of $4$.

| Item            | Type  | 32-bit | 64-bit |
| --------------- | ----- | ------ | ------ |
| object overhead | —     | $8$    | $16$   |
| `value`         | `int` | $4$    | $4$    |
| padding         | —     | —      | $4$    |
| **Total**       |       | $12$   | $24$   |

Book, 64-bit: $16 + 4 = 20$, padded to $24$. Exactly half.

### `Date`

Three `int` instance variables (`day`, `month`, `year`). $8 + 3 \cdot 4 = 20$,
no padding — on 64-bit the raw $28$ had to be padded to $32$.

| Item            | Type  | 32-bit | 64-bit |
| --------------- | ----- | ------ | ------ |
| object overhead | —     | $8$    | $16$   |
| `day`           | `int` | $4$    | $4$    |
| `month`         | `int` | $4$    | $4$    |
| `year`          | `int` | $4$    | $4$    |
| padding         | —     | —      | $4$    |
| **Total**       |       | $20$   | $32$   |

Book, 64-bit: $32$.

### `Counter`

A reference and an `int`. The `name` field is a $4$-byte address, whatever it
points at; the `String` object it leads to is counted on its own row, not here.

| Item            | Type      | 32-bit | 64-bit |
| --------------- | --------- | ------ | ------ |
| object overhead | —         | $8$    | $16$   |
| `name`          | reference | $4$    | $8$    |
| `count`         | `int`     | $4$    | $4$    |
| padding         | —         | —      | $4$    |
| **Total**       |           | $16$   | $32$   |

Book, 64-bit: $16 + 8 + 4 = 28$, padded to $32$. Half again.

### `int[]`

An array is an object: overhead, then a $4$-byte `int` holding its `length`,
then the entries. The fixed part is $12$, a multiple of $4$, so no padding — on
64-bit it is $16 + 4 = 20$, padded to $24$.

| Item            | Type       | 32-bit    | 64-bit    |
| --------------- | ---------- | --------- | --------- |
| object overhead | —          | $8$       | $16$      |
| `length`        | `int`      | $4$       | $4$       |
| padding         | —          | —         | $4$       |
| entries         | $N$ `int`s | $4N$      | $4N$      |
| **Total**       |            | $12 + 4N$ | $24 + 4N$ |

Book, 64-bit: $24 + 4N$.

### `double[]`

Same shape with $8$-byte entries. Only the $12$-byte fixed part shrank — the
payload is primitive and stays exactly as it was.

| Item            | Type          | 32-bit    | 64-bit    |
| --------------- | ------------- | --------- | --------- |
| object overhead | —             | $8$       | $16$      |
| `length`        | `int`         | $4$       | $4$       |
| padding         | —             | —         | $4$       |
| entries         | $N$ `double`s | $8N$      | $8N$      |
| **Total**       |               | $12 + 8N$ | $24 + 8N$ |

Book, 64-bit: $24 + 8N$.

### `double[][]` ($M$ rows, $N$ columns)

Two layers: an outer array of $M$ references, and $M$ row arrays, each a
`double[]` of length $N$ priced above.

| Item                      | Type                  | 32-bit           | 64-bit           |
| ------------------------- | --------------------- | ---------------- | ---------------- |
| outer array: overhead     | —                     | $8$              | $16$             |
| outer array: `length`     | `int`                 | $4$              | $4$              |
| outer array: padding      | —                     | —                | $4$              |
| outer array: entries      | $M$ references        | $4M$             | $8M$             |
| **Total (outer array)**   |                       | $12 + 4M$        | $24 + 8M$        |
| the $M$ rows it points to | $M \times$ `double[]` | $M(12 + 8N)$     | $M(24 + 8N)$     |
| **Total (all in)**        |                       | $12 + 16M + 8MN$ | $24 + 32M + 8MN$ |

Expanding the 32-bit total, outer array plus rows:

$$
\begin{aligned}
(12 + 4M) + M(12 + 8N) &= 12 + 4M + 12M + 8MN \\
&= 12 + 16M + 8MN
\end{aligned}
$$

Book, 64-bit: $24 + 32M + 8MN$. There are $M + 1$ array overheads to pay; the
$16M$ is per-row bookkeeping, and as $N$ grows it shrinks relative to the $8MN$
payload, which is the same on both machines. This settles question 3.

### `String`

Two parts, as in the book's model: the `String` object (a `char[]` reference
plus three `int`s — `offset`, `count`, `hash`) and the `char[]` it points to. A
`char` is $2$ bytes, so the array's $2N$ bytes of entries are a multiple of $4$
only when $N$ is even — the one place on this machine where padding bites.

| Item                      | Type              | 32-bit                                     | 64-bit                                     |
| ------------------------- | ----------------- | ------------------------------------------ | ------------------------------------------ |
| object overhead           | —                 | $8$                                        | $16$                                       |
| `value`                   | reference         | $4$                                        | $8$                                        |
| `offset`                  | `int`             | $4$                                        | $4$                                        |
| `count`                   | `int`             | $4$                                        | $4$                                        |
| `hash`                    | `int`             | $4$                                        | $4$                                        |
| padding                   | —                 | —                                          | $4$                                        |
| **Total (object only)**   |                   | $24$                                       | $40$                                       |
| the `char[]` it points to | array, length $N$ | $12 + 2N$ ($+2$ when $N$ is odd)           | $24 + 2N$ (padded to a multiple of $8$)    |
| **Total (all in)**        |                   | $36 + 2N$, rounded up to a multiple of $4$ | $64 + 2N$, rounded up to a multiple of $8$ |

Book, 64-bit: $40$ for the object, $64 + 2N$ all in, rounded up to a multiple of
$8$.

### `Node`

The linked-list cell inside the book's `Stack`:

```java
private class Node {
    Item item;
    Node next;
}
```

Two references — plus a hidden third one. `Node` is declared without `static`,
so it is an inner class, and every instance carries a reference to the enclosing
`Stack` object.

| Item               | Type      | 32-bit | 64-bit |
| ------------------ | --------- | ------ | ------ |
| object overhead    | —         | $8$    | $16$   |
| `item`             | reference | $4$    | $8$    |
| `next`             | reference | $4$    | $8$    |
| enclosing-instance | reference | $4$    | $8$    |
| **Total**          |           | $20$   | $40$   |

Book, 64-bit: $16 + 3 \cdot 8 = 40$. A `static` nested class would drop the
hidden reference: $16$ bytes here, $32$ on 64-bit. This settles question 2.

### `Stack` (linked list, $N$ items)

The `Stack` object has two fields, `Node first` and `int n`. `first` is a
$4$-byte reference; the $N$ nodes it leads to are counted separately at $20$
each.

| Item                    | Type                 | 32-bit     | 64-bit     |
| ----------------------- | -------------------- | ---------- | ---------- |
| object overhead         | —                    | $8$        | $16$       |
| `first`                 | reference            | $4$        | $8$        |
| `n`                     | `int`                | $4$        | $4$        |
| padding                 | —                    | —          | $4$        |
| **Total (object only)** |                      | $16$       | $32$       |
| the $N$ nodes           | $N \times$ `Node`    | $20N$      | $40N$      |
| **Total (structure)**   |                      | $16 + 20N$ | $32 + 40N$ |
| $N$ `Integer` items     | $N \times$ `Integer` | $12N$      | $24N$      |
| **Total (all in)**      |                      | $16 + 32N$ | $32 + 64N$ |

Book, 64-bit: $32$ for the object, $40$ per node and $24$ per `Integer` item,
giving $32 + 64N$ for a `Stack<Integer>`. The book's figure counts the items, so
the matching 32-bit figure is $16 + 32N$; the structure alone is $16 + 20N$. The
all-in figure is an upper bound: items can be shared — autoboxing reuses one
`Integer` object for each value in $-128 \ldots 127$ — and a shared item is paid
for once, not once per node. This settles question 4.

### Summary

| Type         | 32-bit                                       | 64-bit                          |
| ------------ | -------------------------------------------- | ------------------------------- |
| `Integer`    | $12$                                         | $24$                            |
| `Date`       | $20$                                         | $32$                            |
| `Counter`    | $16$                                         | $32$                            |
| `int[]`      | $12 + 4N$                                    | $24 + 4N$                       |
| `double[]`   | $12 + 8N$                                    | $24 + 8N$                       |
| `double[][]` | $12 + 16M + 8MN$                             | $24 + 32M + 8MN$                |
| `String`     | $24$ object, $36 + 2N$ all in                | $40$ object, $64 + 2N$ all in   |
| `Node`       | $20$                                         | $40$                            |
| `Stack`      | $16 + 20N$ ($16 + 32N$ with `Integer` items) | $32 + 64N$ with `Integer` items |

The totals that are functions of $N$ are the arrays, `String` and `Stack` —
everything that owns a variable number of entries; the single objects are
constants. This settles question 1.

The pattern in the ratios settles question 5. Pointer-heavy types halve: `Node`
goes $40 \to 20$, `Counter` $32 \to 16$, a linked `Stack` of `Integer`s
$64N \to 32N$ per item, because every ingredient in them — overhead, references,
padding — is one of the things that shrinks. Primitive-heavy types barely move:
`double[]` goes from $24 + 8N$ to $12 + 8N$, a saving of $12$ bytes however long
the array is, and `double[][]` keeps its $8MN$ payload untouched. `int` and
`double` are the same size on both machines, so the saving is always in the
bookkeeping, never in the data.

Padding fires only once on 32-bit — the `char[]` of an odd-length `String` —
where on 64-bit it fired for `Integer`, `Date`, `Counter`, the array headers and
the `String` object. With every field $4$ bytes or wider and the multiple $4$,
an object's raw total is already aligned.

<br />
<br />
