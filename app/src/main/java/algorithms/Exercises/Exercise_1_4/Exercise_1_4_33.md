# Exercise 1.4.33

_Memory requirements on a 32-bit machine_. Give the memory requirements for
`Integer`, `Date`, `Counter`, `int[]`, `double[]`, `double[][]`, `String`,
`Node`, and `Stack` (linked-list representation) for a 32-bit machine. Assume
that references are 4 bytes, object overhead is 8 bytes, and padding is to a
multiple of 4 bytes.

---

## The problem

Section 1.4's memory-model discussion (pages 200–204) works every one of these
examples for a **64-bit** machine: 8-byte references, 16-byte object overhead,
padding to multiples of 8. This exercise re-runs the whole table under 32-bit
rules — references 4, overhead 8, padding to 4. It's an accounting drill, and
the durable skill is the _method_: decompose any object into overhead + fields +
padding, recursively.

### The rules, side by side

| ingredient                  | 64-bit (book's text)                          | 32-bit (this exercise)      |
| --------------------------- | --------------------------------------------- | --------------------------- |
| object overhead             | 16 bytes                                      | 8 bytes                     |
| reference                   | 8 bytes                                       | 4 bytes                     |
| padding multiple            | 8 bytes                                       | 4 bytes                     |
| extra for inner class       | 8 (outer ref)                                 | 4 (outer ref)               |
| array                       | overhead + length int + padding, then entries | same recipe, 32-bit numbers |
| `int` / `double` primitives | 4 / 8                                         | 4 / 8 (unchanged!)          |

Primitives don't shrink — only references, overhead, and padding do. That's why
the savings differ so much between, say, `double[]` (mostly primitive payload)
and a linked `Stack` (mostly pointers and overhead).

### One worked example to set the pattern

`Integer` on 32-bit: 8 (overhead) + 4 (the `int` field) = 12, already a multiple
of 4 → **12 bytes** (the book's 64-bit answer is 24 — note it _halved_,
coincidentally).

Now do the rest yourself; for each, first list the instance variables from the
book's own class (Date: three `int`s; Counter: a `String` ref + an `int`; Node:
two references — and is it an inner class?; String: in the book's model, a
`char[]` reference plus three `int`s).

### Questions to guide your solution

1. For each type, write the formula _symbolically first_ (e.g. Stack of N items
   = stack object + N nodes + N item objects?) then substitute 32-bit numbers.
   Which totals should be functions of N?
2. `Node` in the book's `Stack` is a (non-static) inner class — where does the
   extra outer-instance reference show up in your count, and how much would a
   `static` nested class save?
3. `double[][]` with M rows and N columns: how many array-overhead charges do
   you pay, and why does the row-array overhead matter less as N grows?
4. Does a `Stack<Integer>` holding N values cost the same as the sum of a
   Stack's nodes plus N `Integer`s? When might values be shared (autoboxing
   cache for −128..127) and does the accounting change?
5. Sanity check each answer against the book's 64-bit figure: which types drop
   by the biggest _ratio_, and can you explain the pattern (pointer-heavy vs
   primitive-heavy)?

### Practical notes

- Use the book's class definitions (Date page 91, Counter page 89, Node page
  149, the String model on page 202) — real JDK internals differ (e.g. modern
  compact strings), but the exercise is about the book's model.
- Padding bites only occasionally at multiple-of-4: note the cases where the
  32-bit total needs no padding but the 64-bit one did.
- Present the result as a table mirroring page 201 so the two columns can be
  compared at a glance.

<br />
<br />
