# Exercise 1.4.10

Modify binary search so that it always returns the element with the smallest
index that matches the search element (and still guarantees logarithmic running
time).

---

## The problem

It's about **duplicates**. The standard `rank` returns _a_ matching index, but
when the array contains repeated values, which one you land on is essentially an
accident of how the halving plays out.

Say the sorted whitelist is:

```
index:  0   1   2   3   4   5   6
value:  3   5   5   5   5   8   9
```

Search for `5`: the first probe is `mid = 3`, it matches, and the standard
implementation returns `3`. But `5` first appears at index `1`. The exercise
wants `rank(5, array)` to return `1` — **always the leftmost occurrence**.

## Why the constraint matters

The requirement "still guarantees logarithmic running time" rules out the lazy
fix: find any match, then walk left one element at a time until the value
changes. That works, but if the whole array is one repeated value, the walk is
linear — O(n), not O(log n).

## The logarithmic solution

When you find a match, don't stop. Record it as your best candidate so far, then
keep binary-searching the **left half**, since any earlier occurrence must be
there:

```java
public static int rank(int key, int[] array) {
    int lo = 0;
    int hi = array.length - 1;
    int result = -1;

    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;

        if (key < array[mid])
            hi = mid - 1;
        else if (key > array[mid])
            lo = mid + 1;
        else {
            result = mid;      // found one, but maybe not the first...
            hi = mid - 1;      // ...so keep looking to the left
        }
    }

    return result;
}
```

## Why it's still O(log n)

The loop still halves the interval on every iteration — a match now behaves like
"too big" for shrinking purposes — so the running time remains logarithmic. If
the key isn't present, `result` is never set and the method still returns `-1`,
so the whitelist `main` works unchanged.

## Testing it

Make a data file with duplicates, search for a repeated value, and check you get
the _first_ index rather than a middle one. An `assert` or a quick unit test
comparing against a simple linear scan (a `for` loop returning the first match)
makes a nice sanity check.

<br />
<br />
