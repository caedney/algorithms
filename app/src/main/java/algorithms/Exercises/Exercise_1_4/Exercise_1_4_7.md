# Exercise 1.4.7

Analyze `ThreeSum` under a cost model that counts arithmetic operations (and
comparisons) involving the input numbers.

---

## The problem

You're given the standard `ThreeSum` program and asked to redo its running-time
analysis with a different cost model: instead of counting array accesses (as the
book does), count only the arithmetic operations ($+$) and comparisons ($==$)
performed _on the input numbers themselves_ — loop-index bookkeeping like
$i{+}{+}$ and $j < N$ doesn't count. The deliverable is a tilde approximation
for each operation count. The exercise's real point: changing the cost model
changes what you tally and the constant you get, but not the order of growth —
everything is still driven by how many times the inner loop runs.

## Cost model

When analyzing an algorithm you can't count everything the computer does, so you
pick one representative operation and count only that — that choice is the _cost
model_. The book analyzes `ThreeSum` by counting array accesses (each read of
$a[\,\cdot\,]$). This exercise asks you to redo the analysis counting arithmetic
operations ($+$) and comparisons ($==$) that involve the input numbers instead.

## The input numbers

`ThreeSum` takes an array $a[\,]$ of $N$ integers — those are the input numbers.
The phrase "involving the input numbers" excludes bookkeeping like $i{+}{+}$ or
$j < N$: those operate on loop indices, not your data, so they don't count under
this model.

## Where the operations happen

```java
public static int count(int[] a) {
    int N = a.length;
    int cnt = 0;

    for (int i = 0; i < N; i++)
        for (int j = i+1; j < N; j++)
            for (int k = j+1; k < N; k++)
                if (a[i] + a[j] + a[k] == 0)   // <-- the only line that counts
                    cnt++;

    return cnt;
}
```

The `if` line is the only one touching input numbers. Each execution does
exactly:

$$\underbrace{a[i] + a[j]}_{\text{addition 1}} \quad \underbrace{+\; a[k]}_{\text{addition 2}} \quad \underbrace{==\; 0}_{\text{1 comparison}}$$

## Analysis

The `if` line runs once for every distinct triple $i < j < k$. The number of
ways to choose 3 distinct indices out of $N$ (order doesn't matter, since the
loops enforce $i < j < k$) is

$$\binom{N}{3} = \frac{N(N-1)(N-2)}{6} \sim \frac{N^3}{6}$$

So we know **how many times** the line executes. Next we need **what each
execution costs** under our model. From the previous section, each execution
performs exactly 2 additions and 1 comparison.

Total count of an operation = (number of executions) × (operations per
execution):

**Additions** — 2 per execution:

$$2 \cdot \frac{N^3}{6} = \frac{2N^3}{6} \sim \frac{N^3}{3}$$

**Comparisons** — 1 per execution:

$$1 \cdot \frac{N^3}{6} \sim \frac{N^3}{6}$$

**Total operations** — 3 per execution (2 additions + 1 comparison):

$$3 \cdot \frac{N^3}{6} = \frac{3N^3}{6} \sim \frac{N^3}{2}$$

Note the pattern: the $\sim N^3/6$ execution count is the hard part of the
analysis; each cost model just multiplies it by a different small constant (2,
1, or 3). That's why changing the model changes the constant but never the $N^3$
growth rate.

<br />
<br />
