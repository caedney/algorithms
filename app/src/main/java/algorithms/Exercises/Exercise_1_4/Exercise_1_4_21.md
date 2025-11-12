# Exercise 1.4.21

_Binary search on distinct values_. Develop an implementation of binary search
for `StaticSETofInts` (see page 98) where the running time of `contains()` is
guaranteed to be ~lg _R_, where _R_ is the number of different integers in the
array given as argument to the constructor.

---

## The problem

`StaticSETofInts` (the whitelist class from Section 1.1) answers `contains(key)`
with binary search over a sorted copy of the constructor's array. If the input
has lots of _duplicate_ values, the sorted array is longer than it needs to be:
N entries but only R distinct values, `R ≤ N`. Standard binary search costs ~lg
N; the exercise asks you to guarantee **~lg R** — the cost should depend on the
amount of _information_ in the set, not the amount of raw input.

This is a preprocessing exercise, not a search exercise: `contains()` should
remain a bog-standard binary search. All the work happens once, in the
constructor.

### A small example

```text
constructor input (N = 12):
a = [ 5, 9, 5, 5, 2, 9, 5, 2, 5, 9, 2, 5 ]

distinct values (R = 3):  { 2, 5, 9 }
```

Binary search over 12 sorted entries costs ~lg 12 ≈ 3.6 compares; over 3
distinct entries, ~lg 3 ≈ 1.6. Same answers to every possible query.

### Questions to guide your solution

1. After the constructor sorts its copy, where do the duplicates sit? What
   single linear pass transforms the sorted array into what `contains()`
   actually needs?
2. Does the standard `rank()`/binary-search code need _any_ change once the
   constructor's output is right? (It shouldn't — that's the elegance.)
3. What is the constructor's total cost, and why is spending it acceptable under
   this class's usage model (build once, query many times — the whitelist
   scenario)?
4. Bonus: the deduplication idea assumes equality is cheap to detect in sorted
   order. Why do equal values always end up adjacent after sorting?

### Practical notes

- **Don't mutate the caller's array** — `StaticSETofInts` already defensively
  copies before sorting; preserve that.
- **In-place dedup** with two indices (read cursor, write cursor) avoids a
  second allocation; alternatively count distinct first, then copy into a
  right-sized array so no capacity is wasted.
- **Edge cases:** empty array, all-equal array (R = 1), already-distinct array
  (R = N).
- **Testing:** for random arrays, check `contains()` agrees with a
  `HashSet`-based oracle for every value in a covering range; verify the
  internal array length equals R.

<br />
<br />
