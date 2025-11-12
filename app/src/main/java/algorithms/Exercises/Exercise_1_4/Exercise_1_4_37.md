# Exercise 1.4.37

_Autoboxing performance penalty_. Run experiments to determine the performance
penalty on your machine for using autoboxing and auto-unboxing. Develop an
implementation `FixedCapacityStackOfInts` and use a client such as
`DoublingRatio` to compare its performance with the generic
`FixedCapacityStack<Integer>`, for a large number of `push()` and `pop()`
operations.

---

## The problem

The first of the chapter's _experimental_ exercises: no clever algorithm, but a
disciplined measurement. Generics in Java can't hold primitives, so
`FixedCapacityStack<Integer>` silently wraps every pushed `int` in an `Integer`
object (autoboxing) and unwraps on pop (auto-unboxing). Each box is a heap
allocation plus a pointer indirection plus eventual GC work — 1.4.35/36
predicted the costs on paper (2× objects created, ~2× memory); this exercise
asks what they cost _in wall-clock time, on your machine_.

### The experimental setup

Two implementations differing in exactly one variable — that's the discipline:

```text
FixedCapacityStackOfInts     int[] a;   push(int)   pop() → int
FixedCapacityStack<Integer>  Item[] a;  push(Item)  pop() → Item
```

Write the int version yourself (mirror the book's `FixedCapacityStackOfStrings`,
page 133, changing only the type). The workload: for doubling values of N, push
N random ints then pop them all; time each implementation with `Stopwatch`;
report the ratio boxed/unboxed as N doubles.

### Questions to guide your solution

1. Predict before you measure — from 1.4.35 (3N vs 2N data references, 2N vs N
   objects for the linked case; here it's arrays) what ratio would you expect?
   Writing the prediction down first is what makes it an experiment rather than
   a demo.
2. Both implementations are O(N) for the workload, so the doubling _ratio_ of
   each should approach 2. What you're comparing is the _constant factor_
   between them. Does the penalty ratio stay flat as N grows, or move — and what
   would movement suggest (cache effects, GC kicking in)?
3. The JVM complicates naive timing: just-in-time compilation warms up, GC
   pauses land where they like, and the autobox cache (−128..127) makes _small_
   values cheaper than large ones. How will you control each — warmup runs,
   multiple trials (1.4.39's theme), and choosing the range of pushed values?
4. Try pushing values all inside −128..127 vs uniformly random ints as a
   secondary experiment. If the penalty differs, you've directly observed the
   `Integer.valueOf` cache — explain the mechanism.

### Practical notes

- Keep a reference to popped values (e.g. XOR into a running sum and print it)
  so the JIT can't dead-code-eliminate your workload — silently measuring
  nothing is the classic benchmarking failure.
- Run with a fixed heap (`-Xmx`) and note it; boxed runs allocate ~N objects,
  and heap size changes GC frequency, which changes the answer.
- Report medians over ≥5 trials per N, not single runs; state your machine/JVM
  version in the write-up — "on your machine" is in the exercise text for a
  reason.
- Typical findings land in the 2×–10× range depending on JVM and N — if you see
  1.0× or 100×, suspect the harness before the hypothesis.

<br />
<br />
