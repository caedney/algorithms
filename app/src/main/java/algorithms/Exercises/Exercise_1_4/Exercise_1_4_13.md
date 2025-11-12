# Exercise 1.4.13

Using the assumptions developed in the text, give the amount of memory needed to
represent an object of each of the following types:

$a$. $\quad$ `Accumulator`  
$b$. $\quad$ `Transaction`  
$c$. $\quad$ `FixedCapacityStackOfStrings` with capacity $C$ and $N$ entries  
$d$. $\quad$ `Point2D`  
$e$. $\quad$ `Interval1D`  
$f$. $\quad$ `Interval2D`  
$g$. $\quad$ `Double`

---

## The problem

Section 1.4 develops a simple model for estimating how much memory a Java
program uses. This exercise asks you to apply that model to seven concrete types
from earlier in the book: for each one, find its instance variables and add up
the cost of the object, field by field, under the book's assumptions.

All the raw material you need is in the memory-model discussion on pages 200–204
(the two figure pages are reproduced in the tables below). The work is
bookkeeping: look up each class, identify its fields, and account for overhead,
primitives, references, arrays, and padding.

## The memory model

### Basic assumptions

| Item              | Bytes | Notes                                                                        |
| ----------------- | ----- | ---------------------------------------------------------------------------- |
| Object overhead   | 16    | Every object, no exceptions                                                  |
| Reference         | 8     | A pointer to another object — not the object itself                          |
| Padding           | —     | Each object's total is rounded up to a multiple of 8                         |
| `boolean`         | 1     |                                                                              |
| `byte`            | 1     |                                                                              |
| `char`            | 2     |                                                                              |
| `int`             | 4     |                                                                              |
| `float`           | 4     |                                                                              |
| `long`            | 8     |                                                                              |
| `double`          | 8     |                                                                              |
| Inner-class extra | 8     | Non-static nested classes carry a hidden reference to the enclosing instance |

### Arrays (page 203)

An array is an object: 16 bytes of overhead, plus 4 bytes for the `length` int,
plus padding, plus the payload. That header comes to 24 bytes.

| Type                  | Total memory                           | Breakdown                                                 | ~    |
| --------------------- | -------------------------------------- | --------------------------------------------------------- | ---- |
| `int[]` (length N)    | 24 + 4N                                | header + N int values                                     | ~4N  |
| `double[]` (length N) | 24 + 8N                                | header + N double values                                  | ~8N  |
| `Date[]` (length N)   | 24 + 8N + 32N = 24 + 40N               | header + N references + N `Date` objects at 32 bytes each | ~40N |
| `double[][]` (M-by-N) | 24 + 8M + M×(24 + 8N) = 24 + 32M + 8MN | header + M references + M row arrays                      | ~8MN |

The `Date[]` row is a useful worked example for this exercise: each `Date`
object costs 16 (overhead) + 3×4 (three `int` fields) + 4 (padding) = 32 bytes,
and the array of N of them costs the references _plus_ the referenced objects.

### Strings (page 204)

The (Java 6-era) `String` object the book models has four fields: a `char[]`
reference plus three `int`s (`offset`, `count`, `hash`).

| Item                          | Total memory | Breakdown                                                                           |
| ----------------------------- | ------------ | ----------------------------------------------------------------------------------- |
| `String` object itself        | 40           | 16 overhead + 8 reference + 3×4 ints + 4 padding                                    |
| `char[]` (length N)           | 24 + 2N      | array header + N char values (plus padding when N is odd)                           |
| `String` of length N (all in) | 64 + 2N      | the object + the char array it points to                                            |
| Substring                     | 40           | a new `String` object that _shares_ the original char array — constant extra memory |

The substring point matters: `substring()` creates no new characters, just a new
40-byte object with a different `offset` and `count` into the same array.

### Reference objects: `Date` and `String` sized in full

These two come up repeatedly (a `Date[]` on page 203, `String` throughout), so
here are their complete tallies in the same format as the answers below.

**`Date`** — three `int` instance variables (`day`, `month`, `year`):

| Item            | Type  | Bytes  |
| --------------- | ----- | ------ |
| object overhead | —     | 16     |
| `day`           | `int` | 4      |
| `month`         | `int` | 4      |
| `year`          | `int` | 4      |
| padding         | —     | 4      |
| **Total**       |       | **32** |

**`String`** — one reference (`value`, pointing at a `char[]`) and three `int`s
(`offset`, `count`, `hash`):

| Item                      | Type            | Bytes       |
| ------------------------- | --------------- | ----------- |
| object overhead           | —               | 16          |
| `value`                   | reference       | 8           |
| `offset`                  | `int`           | 4           |
| `count`                   | `int`           | 4           |
| `hash`                    | `int`           | 4           |
| padding                   | —               | 4           |
| **Total (object only)**   |                 | **40**      |
| the `char[]` it points to | array, length N | 24 + 2N     |
| **Total (all in)**        |                 | **64 + 2N** |

So "how big is a `String`?" has two answers depending on convention: 40 bytes
for the object itself, 64 + 2N counting the character data it owns. A substring
is 40 bytes flat, because it shares the original's `char[]`. Keep both numbers
handy — the stack of strings in part (c) needs exactly this distinction.

### How references chain

Every field whose type is a class rather than a primitive is stored as an 8-byte
pointer — and `String` itself has one: its `value` field, whose type is
`char[]`. Arrays are objects in Java, so a `String` can't embed its characters
any more than a `Transaction` can embed a `String`. Inside the 40-byte `String`
object, those 8 bytes are the pointer to a separate `char[]` object where the
characters actually live.

`who` in `Transaction` and `value` in `String` are the same kind of thing:
reference fields. Both cost 8 bytes in the object that declares them, and both
point to another object that has its own overhead, fields, and padding. Java
objects never contain other objects — only primitives and pointers. Which means
memory forms chains:

```
Transaction (40)                String (40)               char[] (24 + 2M)
├── overhead      16            ├── overhead    16        ├── overhead   16
├── who           8  ───────►   ├── value       8  ───►   ├── length     4
├── when          8  ──► Date   ├── offset      4         ├── padding    4
└── amount        8      (32)   ├── count       4         └── M chars    2M
                                ├── hash        4
                                └── padding     4
```

Follow `who` all the way and you pay at three stations: 8 bytes for the pointer
in `Transaction`, 40 for the `String` object it lands on, and 24 + 2M for the
`char[]` at the end. That's exactly where 64 + 2M comes from, and why the chain
terminates there — `char[]` contains only primitives, so there's nothing left to
point to.

This is also why the two counting conventions exist. **Object only** cuts every
chain at the first pointer; **all in** follows chains to the end. And it's why
`substring()` is cheap: the new `String` is a fresh 40-byte first link whose
`value` pointer aims at the _old_ char array — chains can share their tails.

One nuance to file away: the book's model says a reference is 8 bytes, but
overhead and reference sizes are JVM implementation details (real JVMs often
compress references to 4 bytes), which is why the book always says "typical" and
the exercise says "estimate."

## The classes to analyse

| Type                          | Where in the book | Instance data to look up                        |
| ----------------------------- | ----------------- | ----------------------------------------------- |
| `Accumulator`                 | page 93           | its running-statistics fields                   |
| `Transaction`                 | page 79           | a mix of primitive and reference fields         |
| `FixedCapacityStackOfStrings` | page 135          | an array sized to capacity C, holding N entries |
| `Point2D`                     | page 77           | coordinate fields                               |
| `Interval1D`                  | page 77           | endpoint fields                                 |
| `Interval2D`                  | page 77           | built from other objects in this list           |
| `Double`                      | Java library      | the wrapper for one `double` value              |

## Guiding questions

- For each class, which fields are primitives (counted inline) and which are
  references (8 bytes, plus whatever they point to)?
- When a field is a reference, does your estimate include the referenced object
  too, or just the pointer? Be consistent, and say which convention you're using
  — the interesting answers count the whole reachable structure.
- `FixedCapacityStackOfStrings` is the one with real content: the array has C
  slots but only N are occupied. What do the C − N empty slots cost? And what do
  the N `String` entries cost — does string length matter, per the table above?
- `Interval2D` holds references to `Interval1D` objects. Does composing objects
  change the per-object arithmetic, or just add layers to sum?
- Don't forget padding: after adding overhead + fields, round each object up to
  a multiple of 8.
- Sanity check: `Double` should come out embarrassingly large relative to the 8
  bytes of information it stores. How large, as a ratio?

## Answers

Each class gets the same tally: object overhead first, then the instance
variables priced from the assumptions table, then padding to round the total up
to a multiple of 8. Methods, local variables, and `static` members cost nothing
per object.

### $a$. `Accumulator`

| Item            | Type     | Bytes  |
| --------------- | -------- | ------ |
| object overhead | —        | 16     |
| `m`             | `double` | 8      |
| `s`             | `double` | 8      |
| `N`             | `int`    | 4      |
| padding         | —        | 4      |
| **Total**       |          | **40** |

20 bytes of actual data, 40 bytes paid — the overhead and padding double the
cost. Note the padding fires here because the raw total (36) isn't a multiple of
8; a fourth `int` field would have been free.

### $b$. `Transaction`

A `Transaction` doesn't contain a `String` or a `Date` — it contains 8-byte
references to them; the objects themselves live elsewhere on the heap. The
object-only total conveniently needs no padding (it lands on 40, already a
multiple of 8).

| Item                                         | Type              | Bytes        |
| -------------------------------------------- | ----------------- | ------------ |
| object overhead                              | —                 | 16           |
| `who`                                        | reference         | 8            |
| `when`                                       | reference         | 8            |
| `amount`                                     | `double`          | 8            |
| **Total (object only)**                      |                   | **40**       |
| the `Date` it points to                      | object            | 32           |
| the `String` it points to (length M, all in) | object + `char[]` | 64 + 2M      |
| **Total (all in)**                           |                   | **136 + 2M** |

The book's expected answer is the object-only figure: **a `Transaction` uses 40
bytes, plus the memory for the `String` and `Date` objects it references.** Note
that if you count the referenced objects, you add them _on top of_ the
references, not instead of them — you pay for the pointer and the thing it
points to.

### $c$. `FixedCapacityStackOfStrings` (capacity C, N entries)

Three layers: the stack object, the `String[]` it points to, and the N live
strings hanging off that. The array has all C slots from day one
(`new String[capacity]`), but only N of them point at a `String` — the other C −
N hold `null`, which costs nothing beyond the 8-byte slot already counted.

| Item                                       | Type            | Bytes                   |
| ------------------------------------------ | --------------- | ----------------------- |
| object overhead                            | —               | 16                      |
| `array`                                    | reference       | 8                       |
| `size`                                     | `int`           | 4                       |
| padding                                    | —               | 4                       |
| **Total (object only)**                    |                 | **32**                  |
| the `String[]` it points to                | array, length C | 24 + 8C                 |
| **Total (structure)**                      |                 | **56 + 8C**             |
| the N live strings (length M each, all in) | N × (64 + 2M)   | 64N + 2MN               |
| **Total (all in)**                         |                 | **56 + 8C + 64N + 2MN** |

The book's expected answer is the structure figure: **56 + 8C bytes, plus the
memory for the strings themselves.** The string lengths aren't given (the all-in
row assumes every string has length M), and the stack arguably doesn't own its
strings anyway — `push` stores a reference to a string the caller created, which
may be referenced elsewhere or even sit on the stack twice.

Note where each variable lands: C appears in the structural cost (capacity is
paid in full at construction, used or not), while N appears only in the strings
clause. At N = 0 a freshly built capacity-C stack still costs 56 + 8C — the
fixed-capacity trade-off in one line.

### $d$. `Point2D`

A point in the plane: two `double` coordinates (deduced from the API on page 77
— `x()` and `y()` return doubles). Lands on a multiple of 8, so no padding.

| Item            | Type     | Bytes  |
| --------------- | -------- | ------ |
| object overhead | —        | 16     |
| `x`             | `double` | 8      |
| `y`             | `double` | 8      |
| **Total**       |          | **32** |

### $e$. `Interval1D`

An interval on a line: its two endpoints, two `double`s (the constructor takes
`(double lo, double hi)`). Identical layout to `Point2D` — two doubles is two
doubles, whatever they mean.

| Item            | Type     | Bytes  |
| --------------- | -------- | ------ |
| object overhead | —        | 16     |
| `min`           | `double` | 8      |
| `max`           | `double` | 8      |
| **Total**       |          | **32** |

### $f$. `Interval2D`

The `Transaction` pattern again: the constructor takes
`(Interval1D x, Interval1D y)`, so the object holds two references to
`Interval1D` objects priced in part (e). Composition, not containment.

| Item                               | Type      | Bytes       |
| ---------------------------------- | --------- | ----------- |
| object overhead                    | —         | 16          |
| `x`                                | reference | 8           |
| `y`                                | reference | 8           |
| **Total (object only)**            |           | **32**      |
| the two `Interval1D`s it points to | objects   | 2 × 32 = 64 |
| **Total (all in)**                 |           | **96**      |

96 bytes to describe a box that is, mathematically, four doubles (32 bytes of
data) — three objects' worth of overhead for one rectangle.

### $g$. `Double`

The wrapper class whose entire purpose is to make one `double` into an object: a
single `double` field. 16 + 8 = 24, already a multiple of 8, so no padding.

| Item            | Type     | Bytes  |
| --------------- | -------- | ------ |
| object overhead | —        | 16     |
| `value`         | `double` | 8      |
| **Total**       |          | **24** |

The smallest object on the list, and still two-thirds overhead: 24 bytes to
store 8 bytes of information, a 3× markup — before counting the 8-byte reference
something must hold to reach it, which brings a `Double`-valued field to 32
bytes against `double`'s 8, a 4× markup. This is the book's parting shot:
autoboxing a billion doubles into `Double`s doesn't cost you a little, it
quadruples your memory.

### Summary

| Type                          | Object only | All in                                             |
| ----------------------------- | ----------- | -------------------------------------------------- |
| `Accumulator`                 | 40          | —                                                  |
| `Transaction`                 | 40          | 136 + 2M                                           |
| `FixedCapacityStackOfStrings` | 32          | 56 + 8C plus the strings (+ 64N + 2MN at length M) |
| `Point2D`                     | 32          | —                                                  |
| `Interval1D`                  | 32          | —                                                  |
| `Interval2D`                  | 32          | 96                                                 |
| `Double`                      | 24          | —                                                  |

Everything lands on 24, 32, or 40 — with fields costing 4 or 8 and totals
rounding to multiples of 8, small objects can only hit a few sizes. Four of the
seven come out at 32, the same as `Date`, despite holding completely different
fields.

<br />
<br />
