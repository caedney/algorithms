# Exercise 1.4.32

_Amortized analysis_. Prove that, starting from an empty stack, the number of
array accesses used by any sequence of $M$ operations in the resizing array
implementation of `Stack` is proportional to $M$.

---

## The problem

A pure proof exercise, formalising what Section 1.4 asserts about
`ResizingArrayStack`: individual operations occasionally cost $\Theta(n)$ (when
the array doubles or halves), yet **any** sequence of $M$ operations from an
empty stack touches the array only $O(M)$ times. This is _the_ canonical
amortized-analysis result — the one the phrase was practically invented for —
and the composed-ADT constructions (a queue from two stacks, a deque from a
stack and a steque) all lean on the same style of argument informally. Now make
it airtight.

### What exactly is being counted

Fix the cost model: an _array access_ is one read or write of `a[...]`. Tally
where they come from in the book's implementation (Algorithm 1.1 / page 141):

| Work          | Array accesses                                            |
| ------------- | --------------------------------------------------------- |
| `push`        | one write, `a[n] = item`                                  |
| `pop`         | one read, `item = a[n-1]`, and one write, `a[n-1] = null` |
| `resize(cap)` | copies $n$ items: $n$ reads and $n$ writes, $2n$ in all   |

A `push` triggers a doubling when the array is full; a `pop` triggers a halving
when it is one-quarter full.

So the claim to prove: cheap work is $\le cM$ trivially, and **total resize work
across the whole sequence** is also $O(M)$ — the occasional $2n$-access spikes
can't add up to more than a constant per operation.

### Questions to guide your solution

1. Aggregate argument: between a resize _to_ capacity $c$ and the next resize
   (in either direction), how many pushes or pops must have occurred? This is
   the heart — show every resize of cost $\Theta(n)$ is separated from the
   previous one by $\Omega(n)$ operations that each get charged $O(1)$ extra.
2. Why does the implementation halve at **one-quarter** full rather than
   one-half? Exhibit the killer sequence for the half-full policy (a "thrashing"
   alternation at the boundary) and show your proof genuinely breaks for it — if
   your argument doesn't use the $1/4$ threshold anywhere, it's wrong.
3. Alternative framing: define a potential function $\Phi$ — a standard choice
   measures how far $n$ is from half the capacity; design one that is large
   exactly when a resize is near — and show amortized cost $=$ actual $+$
   $\Delta\Phi$ is $O(1)$ for push and pop in every regime. Do both proofs; they
   illuminate different things.
4. Tighten the constant: roughly what is total accesses $/ M$ in the worst case?
   (Not just "proportional" — put a number on $c$.)

### Where a resize leaves the array

Every resize, in either direction, leaves the array in the same state. A
doubling fires when a push finds the array full, so the $c/2$ items of a
capacity-$c/2$ array are copied into a new array of capacity $c$; a halving
fires when a pop leaves the array one-quarter full, so the $c/2$ items of a
capacity-$2c$ array are copied into a new array of capacity $c$. Either way:

**Claim.** Just after a resize to capacity $c$, the array holds exactly $c/2$
items.

With $c = 16$: a full array of $8$ is doubled to $16$ slots holding $8$ items;
an array of $32$ popped down to $8$ is halved to $16$ slots holding $8$ items.

From that half-full state the next resize is some way off, whatever the caller
does. The next doubling needs the array full, $c$ items, so at least $c/2$
pushes; the next halving needs it one-quarter full, $c/4$ items, so at least
$c/4$ pops. Interleaving pushes and pops only lengthens the gap, since each
undoes the other's progress towards a boundary. So:

**Claim.** Between a resize to capacity $c$ and the next resize, at least $c/4$
operations occur.

### Sharing the bill

A resize copies the items present when it fires, and the loop in `resize` reads
and writes each one: $2 \cdot$ `size` accesses. A doubling fires at `size`
$= c$, a halving at `size` $= c/4$. Set the cost of each beside the operations
that must have preceded it:

| Direction | Operations before it can fire | Accesses when it fires |
| --------- | ----------------------------: | ---------------------: |
| Doubling  |                         $c/2$ |                   $2c$ |
| Halving   |                         $c/4$ |                  $c/2$ |

A resize is one expensive operation that only happens because of a run of cheap
ones before it. Rather than charge the push that triggered a doubling the whole
$2c$, share it equally among the $c/2$ pushes that made it necessary.

$$
2c \div \frac{c}{2} = \frac{2c}{1} \cdot \frac{2}{c} = \frac{4c}{c} = 4
$$

$$
\frac{c}{2} \div \frac{c}{4} = \frac{c}{2} \cdot \frac{4}{c} = \frac{4c}{2c} = 2
$$

The $c$ cancels in both, which is the whole point: each operation's share of the
next resize is a fixed number of accesses however large the array — at most $4$
on the way to a doubling, $2$ on the way to a halving. Add the accesses an
operation does for itself, one for a push and two for a pop (read, then null
out):

**Claim.** Starting from an empty stack, any sequence of $M$ operations uses at
most $6M$ array accesses.

_Proof._ Every resize in the sequence is preceded, since the previous resize, by
at least $c/4$ operations for its capacity $c$, and costs at most $2c$ accesses,
so charging $4$ to each of those operations covers it. Each operation is charged
by at most one resize — the one that follows it — so its total charge is at most
$2 + 4 = 6$, and the $M$ operations together are charged at most $6M$, which is
at least the actual number of accesses. $\blacksquare$

This settles question 1. The $6$ is a first bound, not a tight one; question 4
asks how close the worst case comes to it.

### Why the start matters

The proof leans on one fact: every resize had at least $c/4$ operations in front
of it. That holds because every array was born inside `resize`, half full, and
had to be filled or emptied to a quarter before the next resize could fire. The
only array not born that way is the constructor's — capacity $1$, holding
nothing, further from a boundary still.

"Starting from an empty stack" is what makes that the stack's whole history.
Hand the stack a full array of capacity $1024$ that no resize created and push
once: $M = 1$, and the push doubles the array at a cost of $2048$ accesses with
no earlier operations to share it. Hand it a full array of $2^{20}$ and the
single push costs $2^{21}$. No fixed constant covers every starting state, so
the claim is false for arbitrary starts and true from empty — the phrase is not
about the first operation being cheap, it names the starting states the theorem
is made for.

For this class the condition is automatic, since the constructor is the only way
to make one. An ADT that lets a caller set the starting state — an initial
capacity, a copy constructor — can begin a sequence mid-life, and then the first
resize may cost up to $2c$ with nothing before it. It is a one-off: after it the
array is half full again and the argument resumes, so the bound becomes $6M$
plus a constant that depends on the starting state rather than on $M$.

### Why one-quarter, not one-half

Suppose `pop` halved the array as soon as it became half full instead. Start
from $16$ items in $16$ slots and push: the array doubles to $32$ slots holding
$17$. Pop, and $16$ items in $32$ slots is half full, so the array halves to
$16$ slots holding $16$ — full. Push, and it doubles again; pop, and it halves
again. Alternating one push and one pop at the boundary makes every operation a
resize, a slosh with no gap between one resize and the next.

The table from the sharing argument changes in one row. A halving now fires
after a single operation, and the array it creates is full, so it copies $c$
items for its new capacity $c$:

| Direction | Operations before it can fire | Accesses when it fires |
| --------- | ----------------------------: | ---------------------: |
| Doubling  |                         $c/2$ |                   $2c$ |
| Halving   |                           $1$ |                   $2c$ |

Share $2c$ among one operation and the share is $2c$. Nothing cancels: each
operation's share grows with the array, there is no fixed constant, and $M$
alternating operations cost about $2cM$ accesses rather than a multiple of $M$.

The $1/4$ threshold enters the proof in exactly one place, the number of
operations between a halving and the next resize. Halving at one-quarter full
creates an array that is half full, $c/4$ pops and $c/2$ pushes from either
boundary; halving at one-half full creates an array that is full, zero pushes
from the next doubling. Half full versus full is the whole difference.

This settles question 2.

### The fund

The sharing argument looks backwards from each resize at the operations that
led to it. A _potential function_ does the same accounting forwards: every
operation hands over a fixed fee, cheap operations leave most of theirs in a
fund, and when a resize comes the fund pays for it. If the fund never goes
negative, the real accesses cannot exceed the fees collected, which is the fee
times $M$.

The fund's balance $\Phi$ is read off the current state, $n$ items in $c$
slots. It is $0$ just after a resize, when the array is half full and nothing
has been saved, and it grows towards whichever boundary the array is heading
for. Each push above the half-way mark saves $4$, the share found above, so that
$c/2$ pushes have saved the $2c$ a doubling costs; each pop below it saves $2$,
so that $c/4$ pops have saved the $c/2$ a halving costs:

$$
\Phi = \begin{cases} 4n - 2c & \text{if } n \ge c/2 \\ c - 2n & \text{if } n < c/2 \end{cases}
$$

Both pieces are $0$ at $n = c/2$. With $c = 16$:

|  $n$ |  $c$ | $\Phi$ | Meaning                                    |
| ---: | ---: | -----: | ------------------------------------------ |
|  $8$ | $16$ |    $0$ | just resized, nothing saved                |
|  $9$ | $16$ |    $4$ | one push above half, one deposit of $4$    |
| $16$ | $16$ |   $32$ | full: exactly the $2c$ the doubling costs  |
|  $6$ | $16$ |    $4$ | two pops below half, two deposits of $2$   |
|  $4$ | $16$ |    $8$ | one-quarter full: the $c/2$ a halving costs |

The test for each kind of operation is

$$
\text{real cost} + (\Phi_{\text{after}} - \Phi_{\text{before}}) \le \text{fee}
$$

since the real cost is what comes out of the fund and the change in balance is
what stays in; their sum is what the operation handed over. Six cases, each worked with $c$ unspecified so that the result holds for every size. Four are routine — one piece of $\Phi$ and one capacity throughout. In the other two something switches underfoot: a resize changes the capacity while the piece stays the same, and a crossing of the half-way mark changes the piece while the capacity stays the same, so each gets a check of its own.

**An ordinary push**, above half full. Real cost $1$. $\Phi$ rises from
$4n - 2c$ to $4(n + 1) - 2c$, a change of $4$. Booked at $1 + 4 = 5$.

**The doubling push.** Before, $n = c$ in capacity $c$; after, $n = c + 1$ in
capacity $2c$:

$$
\begin{aligned}
\Phi_{\text{before}} &= 4c - 2c \\
&= 2c \\[6pt]
\Phi_{\text{after}} &= 4(c + 1) - 2(2c) \\
&= 4c + 4 - 4c \\
&= 4
\end{aligned}
$$

Real cost $2c$ for the copy plus $1$ for the write:

$$
\begin{aligned}
\text{real} + (\Phi_{\text{after}} - \Phi_{\text{before}}) &= (2c + 1) + (4 - 2c) \\
&= 2c + 1 + 4 - 2c \\
&= 5
\end{aligned}
$$

The $c$ cancels. With $c = 16$: real $33$, balance $32$ down to $4$, booked
$33 - 28 = 5$.

**An ordinary pop**, below half full. Real cost $2$, the read and the null-out.
$\Phi$ rises from $c - 2n$ to

$$
\begin{aligned}
c - 2(n - 1) &= c - 2n + 2
\end{aligned}
$$

a change of $2$. Booked at $2 + 2 = 4$.

**The halving pop.** Before, $n = c/4 + 1$ in capacity $c$; after, $n = c/4$
in capacity $c/2$:

$$
\begin{aligned}
\Phi_{\text{before}} &= c - 2\left(\frac{c}{4} + 1\right) \\
&= c - \frac{c}{2} - 2 \\
&= \frac{c}{2} - 2 \\[6pt]
\Phi_{\text{after}} &= \frac{c}{2} - 2 \cdot \frac{c}{4} \\
&= 0
\end{aligned}
$$

Real cost $2$ for the pop plus $2 \cdot c/4 = c/2$ for the copy:

$$
\begin{aligned}
\text{real} + (\Phi_{\text{after}} - \Phi_{\text{before}}) &= \left(2 + \frac{c}{2}\right) + \left(0 - \left(\frac{c}{2} - 2\right)\right) \\
&= 2 + \frac{c}{2} - \frac{c}{2} + 2 \\
&= 4
\end{aligned}
$$

With $c = 16$: real $10$, balance $6$ down to $0$, booked $10 - 6 = 4$.

**Crossing the half-way mark.** A pop from $n = c/2$ to $c/2 - 1$ switches from
the upper piece to the lower: $\Phi$ goes from $0$ to

$$
\begin{aligned}
c - 2\left(\frac{c}{2} - 1\right) &= c - c + 2 \\
&= 2
\end{aligned}
$$

booked at $2 + 2 = 4$. The push back, from $c/2 - 1$ to $c/2$, goes from $2$ to
$0$ and is booked at $1 - 2 = -1$, cheaper than usual, which the fee covers
trivially.

**Claim.** With the fund $\Phi$ above, every push is booked at $5$ and every pop
at $4$, resize or not, on either side of the half-way mark or across it.

_Proof._ The six cases above are exhaustive: an operation either stays on one
piece of $\Phi$, crosses between the pieces at $n = c/2$, or triggers a resize,
and each is booked at $5$ or less. $\Phi$ is never negative, and for the empty
stack — no items in the constructor's one slot — it is $1$, so over any $M$
operations the real accesses are at most the fees plus that starting balance,
$5M + 1$. $\blacksquare$

This settles question 3. The fund lands on the same per-operation numbers as
the sharing argument — a push is $1$ of its own plus the $4$ share, a pop $2$ of
its own plus the $2$ share — and tightens the first bound from $6M$ to about
$5M$, since no operation pays both shares.

### The worst sequence

A bound is a ceiling, not what happens. Pushes are the expensive direction — a
push's share is $4$ against a pop's $2$ — so the worst sequence is pushes, and
the question is only where to stop.

Push $16$ items from empty, with the initial capacity of $1$. Doublings fire on
pushes $2$, $3$, $5$ and $9$, copying $1$, $2$, $4$ and $8$ items:

| Push | Doubles to | Items copied | Accesses |
| ---: | ---------: | -----------: | -------: |
|  $2$ |        $2$ |          $1$ |      $3$ |
|  $3$ |        $4$ |          $2$ |      $5$ |
|  $5$ |        $8$ |          $4$ |      $9$ |
|  $9$ |       $16$ |          $8$ |     $17$ |

The copies total $1 + 2 + 4 + 8 = 15$, one less than $16$; for $M$ pushes with
$M$ a power of two they total $M - 1$, so the accesses are $2(M - 1) + M$, just
under $3M$. Well short of $5$, and for a reason: after each doubling the array
is half full, so half the pushes are below the half-way mark, saving towards a
halving that never comes.

Pops don't raise it. Popping the $16$ back off halves the array on the pops
down to $4$, $2$ and $1$, copying $7$ items: $14$ accesses plus $32$ for the
pops, $46$, the same as the pushes cost — $92$ accesses for $32$ operations,
under $3$ each.

Stopping at a power of two is the best moment to stop. Push $17$ instead and the
seventeenth push doubles the array once more, copying $16$:
$1 + 2 + 4 + 8 + 16 = 31$ items, $62$ accesses, plus $17$ writes, $79$ for $17$
pushes — just under $5$ each. One past a power of two copies $2M - 3$ items in
all, so the accesses are

$$
\begin{aligned}
2(2M - 3) + M &= 4M - 6 + M \\
&= 5M - 6
\end{aligned}
$$

approaching $5$ per push from below as $M$ grows ($M = 1025$ gives $4.994$) and
never reaching it.

This settles question 4: the worst-case ratio is $5$, and the fund's $5M$ bound
is tight. The sharing argument's $6$ was loose only because it allowed an
operation to pay both shares, which none ever does.

### Practical notes

- Be careful with the phrase "starting from an empty stack" — it's load-bearing.
  The amortized bound is a statement about _sequences from a known state_, not
  about single operations; a hostile starting state (full array, one element)
  changes the constant for short sequences.
- **Empirical companion:** instrument `ResizingArrayStack` with an access
  counter and run random and adversarial (grow-shrink oscillation) workloads —
  plot accesses $/ M$ as $M$ doubles and watch it flatten to your constant.
  Seeing the $1/4$-threshold version stay flat while a $1/2$-threshold variant
  blows up on the thrash sequence is the best possible check of question 2.

### The measurement

There is no algorithm to write, so the code measures the proof instead: the
book's stack with an access counter, and a shrink threshold that can be set to
one-quarter (the book's policy) or one-half (the variant that sloshes). Three
workloads run as $M$ doubles and print the accesses per operation under both
policies.

```java
/**
 * The book's resizing array stack, counting every read or write of the backing
 * array. The shrink divisor is the fraction full at which a pop halves the
 * array: 4 for the book's policy, 2 for the variant that thrashes.
 */
static class CountingResizingArrayStack<Item> {
    private static final int INIT_CAPACITY = 1;
    private final int shrinkDivisor;
    private Item[] array;
    private int size;
    long accesses;

    @SuppressWarnings("unchecked")
    CountingResizingArrayStack(int shrinkDivisor) {
        this.shrinkDivisor = shrinkDivisor;
        this.array = (Item[]) new Object[INIT_CAPACITY];
        this.size = 0;
    }

    boolean isEmpty() {
        return size == 0;
    }

    int size() {
        return size;
    }

    int capacity() {
        return array.length;
    }

    @SuppressWarnings("unchecked")
    private void resize(int max) {
        Item[] newArray = (Item[]) new Object[max];

        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
            accesses += 2; // one read, one write
        }

        array = newArray;
    }

    void push(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        array[size++] = item;
        accesses++;
    }

    @SuppressWarnings("null")
    Item pop() {
        Item item = array[--size];
        accesses++;

        array[size] = null; // avoid loitering
        accesses++;

        if (size > 0 && size == array.length / shrinkDivisor)
            resize(array.length / 2);

        return item;
    }
}

/**
 * Pushes 𝑀 + 1 items from empty — one past a power of two, so the last push
 * doubles the array — and returns the accesses per operation.
 */
private static double pushesOnly(int m, int shrinkDivisor) {
    CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(shrinkDivisor);
    int operations = m + 1;

    for (int i = 0; i < operations; i++)
        stack.push(i);

    return (double) stack.accesses / operations;
}

/**
 * Performs 𝑀 operations from empty, each a push or (when the stack is
 * non-empty) a pop with equal probability, and returns the accesses per
 * operation.
 */
private static double random(int m, int shrinkDivisor) {
    CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(shrinkDivisor);
    Random random = new Random(32L); // fixed seed -> reproducible rows

    for (int i = 0; i < m; i++) {
        if (stack.isEmpty() || random.nextBoolean())
            stack.push(i);
        else
            stack.pop();
    }

    return (double) stack.accesses / m;
}

/**
 * Pushes 𝑀 / 2 + 1 items from empty, one past a power of two so that the array
 * has just doubled, then alternates pop and push 𝑀 / 2 times at that boundary.
 * Returns the accesses per operation over the whole sequence. Halving at
 * one-half full resizes on every one of the alternating operations; halving at
 * one-quarter full never resizes again.
 */
private static double slosh(int m, int shrinkDivisor) {
    CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(shrinkDivisor);
    int climb = m / 2 + 1;

    for (int i = 0; i < climb; i++)
        stack.push(i);

    for (int i = 0; i < m / 2; i++) {
        if (i % 2 == 0)
            stack.pop();
        else
            stack.push(i);
    }

    return (double) stack.accesses / (climb + m / 2);
}
```

The decisions that matter:

- **The counter lives in a nested copy, not in `ResizingArrayStack`.** The
  stack under test is the book's code line for line, with `accesses++` beside
  every read and write and `accesses += 2` per item copied; the real ADT stays
  uninstrumented.
- **The threshold is a constructor parameter.** `shrinkDivisor` is the only
  difference between the two policies — `array.length / 4` against
  `array.length / 2` in `pop` — so the comparison that answers question 2 is the
  same class run twice.
- **Each workload stops where the proof says it should.** `pushesOnly` pushes
  $M + 1$ items, one past a power of two, so the last push doubles the array;
  `slosh` climbs to the same point and then alternates pop and push at the
  boundary; `random` uses a fixed seed so the rows are reproducible.

Accesses per operation, from `./gradlew run`:

| $M$ | pushes $1/4$ | random $1/4$ | random $1/2$ | slosh $1/4$ | slosh $1/2$ |
| ---: | ---: | ---: | ---: | ---: | ---: |
| $16$ | $4.647$ | $1.750$ | $2.750$ | $3.000$ | $10.529$ |
| $256$ | $4.977$ | $2.012$ | $3.371$ | $3.233$ | $130.735$ |
| $4{,}096$ | $4.999$ | $1.689$ | $2.646$ | $3.249$ | $2{,}050.749$ |
| $65{,}536$ | $5.000$ | $1.634$ | $4.333$ | $3.250$ | $32{,}770.750$ |

The pushes column is $(5M - 6) / M$ exactly — $79 / 17 = 4.647$ in the first
row — and climbs to $5$. Under the book's policy nothing exceeds it: random
interleavings sit around $2$, and the slosh settles at $3.25$ because after the
climb the array never resizes again. Under the one-half policy the slosh is
about $M / 2$ per operation, doubling with every row: linear, not constant.
The pushes-only column is the same for both policies, since a pure climb never
halves.

**Verification.** `Exercise_1_4_32Test` ($14$ tests) asserts the exact counts
the sections above derive — $46$ accesses for $16$ pushes, $79$ for $17$, $92$
for the round trip of $16$ up and $16$ down, and $5M - 6$ for one past every
power of two up to $2^{16} + 1$ — along with the mechanics the proof rests on:
doublings fire on pushes $2$, $3$, $5$ and $9$, halvings on the pops that leave
$4$, $2$ and $1$ items, and every resize creates a half-full array. The
pushes-only ratio stays below $5$ and rises monotonically; $2{,}000$ random
interleavings with a random push bias stay within $5M + 1$; $2{,}000$ more agree
with `java.util.ArrayDeque` on every return value and size. For the threshold,
one test runs the alternation at one-quarter and asserts the array stays at
$32$ slots with $3$ accesses per pop-push pair, and two run it at one-half,
asserting a resize on every operation ($67$ accesses per pair) and a per-pair
cost that at least doubles each time the climb doubles. Mutants: halving at
one-half regardless of the divisor fails $5$ tests, counting one access per
copied item fails $5$, doubling on the push that fills rather than the one that
finds the array full fails $9$, shrinking to a quarter instead of a half fails
$5$, and dropping the null-out access fails $3$.

<br />
<br />
