# Exercise 1.4.32

_Amortized analysis_. Prove that, starting from an empty stack, the number of
array accesses used by any sequence of _M_ operations in the resizing array
implementation of `Stack` is proportional to _M_.

---

## The problem

A pure proof exercise, formalising what Section 1.4 asserts about
`ResizingArrayStack`: individual operations occasionally cost Θ(n) (when the
array doubles or halves), yet **any** sequence of M operations from an empty
stack touches the array only O(M) times. This is _the_ canonical
amortized-analysis result — the one the phrase was practically invented for —
and the exercises 1.4.27–31 you may have just done all leaned on the same style
of argument informally. Now make it airtight.

### What exactly is being counted

Fix the cost model: an _array access_ is one read or write of `a[...]`. Tally
where they come from in the book's implementation (Algorithm 1.1 / page 141):

```text
cheap work:  push → one write a[n] = item          (plus null-out on pop)
             pop  → one read  item = a[n-1], one write a[n-1] = null
resize(cap): copies n items → n reads + n writes = 2n accesses
             push triggers doubling when full;
             pop triggers halving when one-quarter full
```

So the claim to prove: cheap work is ≤ cM trivially, and **total resize work
across the whole sequence** is also O(M) — the occasional 2n-access spikes can't
add up to more than a constant per operation.

### Questions to guide your solution

1. Aggregate argument: between a resize _to_ capacity c and the next resize (in
   either direction), how many pushes or pops must have occurred? This is the
   heart — show every resize of cost Θ(n) is separated from the previous one by
   Ω(n) operations that each get charged O(1) extra.
2. Why does the implementation halve at **one-quarter** full rather than
   one-half? Exhibit the killer sequence for the half-full policy (a "thrashing"
   alternation at the boundary) and show your proof genuinely breaks for it — if
   your argument doesn't use the ¼ threshold anywhere, it's wrong.
3. Alternative framing: define a potential function Φ (a standard choice: Φ =
   |2n − capacity/... | — design one that is large exactly when a resize is
   near) and show amortized cost = actual + ΔΦ is O(1) for push and pop in every
   regime. Do both proofs; they illuminate different things.
4. Tighten the constant: roughly what is total accesses / M in the worst case?
   (Not just "proportional" — put a number on c.)

### Practical notes

- Be careful with the phrase "starting from an empty stack" — it's load-bearing.
  The amortized bound is a statement about _sequences from a known state_, not
  about single operations; a hostile starting state (full array, one element)
  changes the constant for short sequences.
- **Empirical companion:** instrument `ResizingArrayStack` with an access
  counter and run random and adversarial (grow-shrink oscillation) workloads —
  plot accesses/M as M doubles and watch it flatten to your constant. Seeing the
  ¼-threshold version stay flat while a ½-threshold variant blows up on the
  thrash sequence is the best possible check of question 2.

<br />
<br />
