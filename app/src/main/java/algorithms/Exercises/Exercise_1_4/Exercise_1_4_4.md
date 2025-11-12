# Exercise 1.4.4

Develop a table like the one on page 181 for `TwoSum`.

---

## The problem

Reproduce the book's analysis method on a smaller target: build the
page-181-style table for `TwoSum` — every statement block, its time per
execution, and its frequency — and reduce the total to a tilde approximation.
The work is in getting the frequencies right, which is the pair-counting from
1.4.1 one level down.

## `ThreeSum`

```java
public static int count(int[] a) {
    int N = a.length;
    int cnt = 0;

    for (int i = 0; i < N; i++)
        for (int j = i + 1; j < N; j++)
            for (int k = j + 1; k < N; k++)
                if (a[i] + a[j] + a[k] == 0)
                    cnt++;

    return cnt;
}
```

| statement block                   | time in seconds | frequency                | total time                 |
| --------------------------------- | --------------- | ------------------------ | -------------------------- |
| E: `cnt++`                        | $t_0$           | $x$ _(depends on input)_ | $t_0 x$                    |
| D: `if (a[i] + a[j] + a[k] == 0)` | $t_1$           | $N^3/6 − N^2/2 + N/3$    | $t_1(N^3/6 − N^2/2 + N/3)$ |
| C: `j < N; j++`                   | $t_2$           | $N^2/2 − N/2$            | $t_2(N^2/2 − N/2)$         |
| B: `i < N; i++`                   | $t_3$           | $N$                      | $t_3 N$                    |
| A: `N`, `cnt`                     | $t_4$           | $1$                      | $t_4$                      |

Grand total:

$$
\begin{aligned}
(t_1/6)N^3
  &+ (t_2/2 - t_1/2)N^2 \\
  &+ (t_1/3 - t_2/2 + t_3)N \\
  &+ t_4 \\
  &+ t_0x
\end{aligned}
$$

Tilde approximation:

$$
  \sim (t_1/6)N^3 \enspace \text{(valid for small $x$)}
$$

Order of growth:

$$
  N^3
$$

## `TwoSum`

```java
public static int count(int[] a) {
    int N = a.length;
    int cnt = 0;

    for (int i = 0; i < N; i++)
        for (int j = i + 1; j < N; j++)
            if (a[i] + a[j] == 0)
                cnt++;

    return cnt;
}
```

| statement block            | time in seconds | frequency                | total time         |
| -------------------------- | --------------- | ------------------------ | ------------------ |
| D: `cnt++`                 | $t_0$           | $x$ _(depends on input)_ | $t_0 x$            |
| C: `if (a[i] + a[j] == 0)` | $t_1$           | $N^2/2 − N/2$            | $t_1(N^2/2 − N/2)$ |
| B: `i < N; i++`            | $t_2$           | $N$                      | $t_2 N$            |
| A: `N`, `cnt`              | $t_3$           | $1$                      | $t_3$              |

Grand total:

$$
\begin{aligned}
(t_1/2)N^2
  &+ (t_2 − t_1/2)N \\
  &+ t_3 \\
  &+ t_0x
\end{aligned}
$$

Tilde approximation:

$$
  \sim (t_1/2)N^2 \enspace \text{(valid for small $x$)}
$$

Order of growth:

$$
  N^2
$$

Each frequency is just "number of times we reach that depth": the if is reached
once per pair, $C(N,2) = N^2/2 − N/2$; the $j$-loop header once per value of
$i$, $N$ times; declarations once.

## $t$

The $t$'s are the bridge between your code and your actual computer. Each $t_i$
is a constant: the time in seconds to execute that statement block once on a
particular machine — $t_1$ might be 2 nanoseconds for the if comparison on your
laptop, something else on another device.

## Frequency of the `if` statement in ThreeSum

The `if` runs once for every triple $(i, j, k)$ with $i < j < k$ — that is,
"choose 3 indices out of $N$":

$$
\binom{N}{3} = \frac{N(N-1)(N-2)}{6}
$$

The $6$ appears because order doesn't matter: the loops visit each set of three
indices in exactly one order (ascending), and 3 items can be arranged $3! = 6$
ways, so of the $N(N-1)(N-2)$ _ordered_ pickings, the loops perform one sixth.

### Expanding to the book's form

$$
\begin{aligned}
N(N-1)(N-2) &= N(N^2 - 2N - N + 2) \\
&= N(N^2 - 3N + 2) \\
&= N^3 - 3N^2 + 2N
\end{aligned}
$$

Divide by 6:

$$
\frac{N^3}{6} - \frac{3N^2}{6} + \frac{2N}{6}
= \frac{N^3}{6} - \frac{N^2}{2} + \frac{N}{3}
$$

Same expression, multiplied out — the book writes it expanded so it's already
grouped by powers of $N$.

### Sanity check ($N = 4$)

$$
\frac{64}{6} - \frac{16}{2} + \frac{4}{3} = \frac{32}{3} - 8 + \frac{4}{3} = 4
$$

and indeed $\binom{4}{3} = 4$.

$C(4, 3) = 4$ → there are 4 ways to choose a triple from 4 elements (the
ThreeSum check)

### Same idea, one level down (TwoSum)

$$
\binom{N}{2} = \frac{N(N-1)}{2}
$$

Expand it out:

$$
\begin{aligned}
N(N-1) &= N^2 - N
\end{aligned}
$$

Divide by 2:

$$
\frac{N^2}{2} - \frac{N}{2}
$$

### Sanity check ($N = 4$)

$$
\frac{16}{2} - \frac{4}{2} = 8 - 2 = 6
$$

and indeed $\binom{4}{2} = 6$.

$C(4, 2) = 6$ → there are 6 ways to choose a pair from 4 elements (the TwoSum
check)

<br />
<br />
