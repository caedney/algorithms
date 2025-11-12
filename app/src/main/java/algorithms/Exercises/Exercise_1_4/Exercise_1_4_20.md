# Exercise 1.4.20

_Bitonic search_. An array is _bitonic_ if it is comprised of an increasing
sequence of integers followed immediately by a decreasing sequence of integers.
Write a program that, given a bitonic array of _N_ distinct `int` values,
determines whether a given integer is in the array. Your program sould use ~3lg
_N_ compares in the worst case.

---

## The problem

Search in an array that goes up and then comes down — sorted, but in two
opposite directions with an unknown split point. Plain binary search needs
monotone order, so it can't be applied to the whole array as-is. The budget of
**~3 lg N** compares is practically an architecture diagram: three logarithmic
phases. Your job is to figure out what the three phases are and wire them
together.

### A small example

```text
a = [ 2, 7, 11, 25, 19, 12, 5, 1 ]
      └── increasing ──┘└─ decreasing ─┘
                   ▲
              peak = 25 at index 3
```

Is `12` in the array? Is `9`? Note `9` is greater than `a[0] = 2` and less than
the peak, yet absent — membership can't be decided from the extremes alone.

### Questions to guide your solution

1. If you knew the index of the _peak_ (the maximum), how would the problem
   decompose, and what would each piece cost?
2. You don't know the peak. Can you locate it with a binary-search-style loop?
   What local test at position `mid` (one compare against a neighbour) tells you
   which side of the peak you're on? This is 1.4.18's idea inverted — a _local
   maximum_ in an array that has exactly one.
3. Add up the pieces — does your total land at ~3 lg N? Which phase could be
   skipped when the target is found early?
4. (Harder, from the book's web exercises) Can you decide membership in ~2 lg N
   compares, _without_ ever locating the peak exactly? What can you learn from
   comparing `a[mid]` to the target alone?

### Practical notes

- **Descending binary search** is the same algorithm with the comparison flipped
  — write it once, parameterise the direction, and test it separately; sign
  errors here are the classic bug.
- **Edge shapes:** purely increasing (peak at the end), purely decreasing (peak
  at the start), N = 1, N = 2 — make sure the peak-finding loop terminates on
  all of them.
- **Distinctness** is what makes the neighbour test at the peak unambiguous;
  note where you rely on it.
- **Testing:** generate bitonic arrays by sorting random distinct values,
  splitting at a random point, and reversing the tail; probe with present
  values, absent values inside the range, and values outside the range.

<br />
<br />
