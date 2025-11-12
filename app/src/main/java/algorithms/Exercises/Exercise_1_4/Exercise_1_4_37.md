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
allocation plus a pointer indirection plus eventual GC work — the chapter's cost
model prices them on paper (an extra object per item, roughly twice the memory);
this exercise asks what they cost _in wall-clock time, on your machine_.

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

1. Predict before you measure — counting the data references and objects each
   implementation touches per item, what ratio would you expect? Writing the
   prediction down first is what makes it an experiment rather than a demo.
2. Both implementations are O(N) for the workload, so the doubling _ratio_ of
   each should approach 2. What you're comparing is the _constant factor_
   between them. Does the penalty ratio stay flat as N grows, or move — and what
   would movement suggest (cache effects, GC kicking in)?
3. The JVM complicates naive timing: just-in-time compilation warms up, GC
   pauses land where they like, and the autobox cache (−128..127) makes _small_
   values cheaper than large ones. How will you control each — warmup runs,
   multiple trials, and choosing the range of pushed values?
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

### The prediction

Count what each operation does per item, treating every step as equally cheap.
The unboxed push stores the `int` into the array; the unboxed pop reads it back.
The boxed push has to allocate an `Integer` on the heap, write the value into
it, and store the _reference_ into the array; the boxed pop reads the reference,
follows it to wherever the `Integer` lives, and reads the value out.

| Stack | push                                         | pop                                           | Steps per item |
| ----- | -------------------------------------------- | --------------------------------------------- | -------------: |
| `int` | store the value                              | read the value                                |            $2$ |
| boxed | allocate, write the value, store a reference | read the reference, follow it, read the value |            $6$ |

By the count alone the boxed stack does $6 \div 2 = 3$ times the work. Two
things push the true ratio above that, both in the same direction. The
allocation step is far dearer than a plain store. And the table misses a cost
entirely: $N$ allocations are $N$ objects the garbage collector must deal with,
at times of its own choosing — nothing is freed on `pop`, and with a
fixed-capacity array the slot is not even cleared, so the reference stays live
until a later push overwrites it.

So the prediction is a ratio of **at least $3$**, higher in practice. Both
stacks do $N$ pushes and $N$ pops, so each one's time should double when $N$
doubles, and collector work is proportional to the objects allocated, also
linear — so the ratio should hold roughly flat as $N$ grows. A gradual rise at
large $N$ is plausible: once $N$ scattered `Integer` objects no longer fit in
cache, the follow-the-reference step gets dearer.

This settles question 1.

### What the harness must control

Three things stand between a stopwatch and an honest number.

**The compiler deleting the work.** The JIT is entitled to remove work whose
result nobody reads. A loop that pops and discards can be optimised down to
nothing, and escape analysis can then remove the allocations that fed it — the
table would look wonderful and measure nothing. Every popped value is therefore
folded into a static field with exclusive or (XOR), the cheapest operation that depends on it,
and the field is printed once at the end so that it, too, is observably used.
The printed value is a receipt, not a result. It comes out as $0$ because every
value is XORed in an even number of times (two stacks, five trials each), and
$x \oplus x = 0$.

**The collector landing where it likes.** A collection is a pause charged to
whichever trial it interrupts. Each row is the median of several trials, so one
pause cannot decide it. But the heap's _size_ matters too: by default the JVM
starts the heap small and grows it as the run proceeds, and the young generation
grows with it, so how often collections land changes from row to row for reasons
that have nothing to do with $N$. Pinning the heap with `-Xms1g -Xmx1g` removes
that, and makes the table reproducible on another machine.

**The JIT compiling mid-table.** Both timing paths are run twenty times, on
$262{,}144$ values, before the first row is recorded, so the compiled code is
what gets measured.

This settles questions 2 and 3. The one choice question 3 leaves to the client
is the range of the pushed values: by default they are drawn from the whole
`int` range, so almost none fall in the autobox cache.

### The measurement

The client times the same workload on each stack — push every value from an
array generated outside the timer, then pop until empty — and prints, as $N$
doubles, the median time of each, their ratio, and each stack's own doubling
ratio.

```java
public class Exercise_1_4_37 {
    /**
     * A stack of {@code int} values backed by an array whose length is fixed when
     * the stack is created. The array holds the values themselves, so a push stores
     * an {@code int} and a pop reads one, with no boxing on either side.
     */
    static class FixedCapacityStackOfInts {
        private int[] array;
        private int size;

        public FixedCapacityStackOfInts(int capacity) {
            this.array = new int[capacity];
            this.size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == array.length;
        }

        public int size() {
            return size;
        }

        public void push(int item) {
            array[size++] = item;
        }

        public int pop() {
            return array[--size];
        }
    }

    /**
     * Every popped value is folded in here, and the total is printed at the end, so
     * that the compiler cannot treat the pops as unused work and remove them. The
     * value itself means nothing.
     */
    private static int sink;

    /**
     * Pushes every value onto a stack of {@code int} and pops them all back,
     * returning the elapsed seconds. The values are generated by the caller, so
     * only the pushes and pops are timed.
     */
    private static double timeInts(int[] values) {
        FixedCapacityStackOfInts stack = new FixedCapacityStackOfInts(values.length);
        Stopwatch timer = new Stopwatch();

        for (int i = 0; i < values.length; i++) {
            stack.push(values[i]);
        }

        while (!stack.isEmpty()) {
            sink ^= stack.pop();
        }

        return timer.elapsedTime();
    }

    /**
     * The same workload on the generic stack holding {@code Integer}: each push
     * boxes an {@code int} and each pop unboxes one. The body is kept identical to
     * {@link #timeInts} so that boxing is the only difference being timed.
     */
    private static double timeIntegers(int[] values) {
        FixedCapacityStack<Integer> stack = new FixedCapacityStack<>(values.length);
        Stopwatch timer = new Stopwatch();

        for (int i = 0; i < values.length; i++) {
            stack.push(values[i]);
        }

        while (!stack.isEmpty()) {
            sink ^= stack.pop();
        }

        return timer.elapsedTime();
    }

    /**
     * Runs the workload the given number of times and returns the median, so a
     * single garbage collection or JIT pause does not decide a row.
     */
    private static double median(int[] values, int trials, boolean boxed) {
        double[] times = new double[trials];

        for (int t = 0; t < trials; t++) {
            times[t] = boxed ? timeIntegers(values) : timeInts(values);
        }

        Arrays.sort(times);

        return times[trials / 2];
    }

    /**
     * 𝑁 random values. By default they span the whole {@code int} range, so almost
     * none fall in the autobox cache; with {@code cached} they are drawn from −128
     * to 127, so every box is a shared, ready-made {@code Integer}.
     */
    private static int[] randomValues(int n, Random random, boolean cached) {
        int[] values = new int[n];

        for (int i = 0; i < n; i++) {
            values[i] = cached ? random.nextInt(256) - 128 : random.nextInt();
        }

        return values;
    }

    /**
     * Times the same pushes and pops on both stacks as 𝑁 doubles. After a warm-up
     * that is not recorded, each row prints the median time in microseconds for
     * the stack of {@code int}, the same for the stack of {@code Integer}, the
     * ratio of the two, and how much each time grew since the previous row.
     *
     * <p>
     * Optional arguments: the smallest 𝑁 (default 2048), the largest 𝑁 (default
     * 4194304), the number of trials per row (default 5), and {@code cached} to
     * draw every value from −128 to 127.
     * </p>
     */
    public static void main(String[] args) {
        int smallest = args.length > 0 ? Integer.parseInt(args[0]) : 1 << 11;
        int largest = args.length > 1 ? Integer.parseInt(args[1]) : 1 << 22;
        int trials = args.length > 2 ? Integer.parseInt(args[2]) : 5;
        boolean cached = args.length > 3 && args[3].equals("cached");

        System.out.println(System.getProperty("java.vm.name") + " " + System.getProperty("java.version") + ", max heap "
                + Runtime.getRuntime().maxMemory() / (1 << 20) + " MB, values "
                + (cached ? "-128..127" : "full int range"));

        Random random = new Random(37L); // fixed seed -> reproducible rows

        // Warm up: let the JIT compile both paths before anything is recorded.
        int[] warmup = randomValues(1 << 18, random, cached);

        for (int t = 0; t < 20; t++) {
            timeInts(warmup);
            timeIntegers(warmup);
        }

        System.out.printf("%10s %12s %12s %8s %8s %8s%n", "N", "int (µs)", "Integer (µs)", "ratio", "int x", "Integer x");

        double previousInts = 0;
        double previousIntegers = 0;

        for (int n = smallest; n <= largest; n *= 2) {
            int[] values = randomValues(n, random, cached);
            double ints = median(values, trials, false);
            double integers = median(values, trials, true);

            System.out.printf("%10d %12.1f %12.1f %8.2f", n, ints * 1e6, integers * 1e6, integers / ints);

            if (previousInts > 0) {
                System.out.printf(" %8.2f %8.2f", ints / previousInts, integers / previousIntegers);
            }

            System.out.println();

            previousInts = ints;
            previousIntegers = integers;
        }

        System.out.println("(sink " + sink + ")");
    }
}
```

The decisions that matter:

- The two timing methods are deliberately identical, line for line. The stacks
  share no type, so the loop cannot be written once; the duplication is the
  honest way to guarantee that boxing is the only difference being timed.
- The random values are generated before the `Stopwatch` starts, and the same
  array is reused for every trial of a row, so only pushes and pops are timed.
- `median` sorts the trial times and takes the middle one.

Measured on OpenJDK $25.0.3$ with its default G1 collector, on a $64$ GB machine with the heap pinned at $1$ GB (`-Xms1g -Xmx1g`). Times are in microseconds, each row the median of five trials:

| $N$             | `int` (µs) | `Integer` (µs) |   ratio | `int` doubling | `Integer` doubling |
| --------------- | ---------: | -------------: | ------: | -------------: | -----------------: |
| $2{,}048$       |        $1$ |            $6$ |  $8.06$ |              — |                  — |
| $4{,}096$       |        $1$ |           $11$ |  $8.73$ |         $1.77$ |             $1.91$ |
| $8{,}192$       |        $2$ |           $21$ |  $8.54$ |         $1.97$ |             $1.92$ |
| $16{,}384$      |        $5$ |           $40$ |  $7.84$ |         $2.09$ |             $1.91$ |
| $32{,}768$      |       $10$ |           $82$ |  $8.66$ |         $1.85$ |             $2.05$ |
| $65{,}536$      |       $20$ |          $178$ |  $8.90$ |         $2.11$ |             $2.17$ |
| $131{,}072$     |       $37$ |          $443$ | $11.86$ |         $1.86$ |             $2.48$ |
| $262{,}144$     |       $75$ |          $691$ |  $9.26$ |         $2.00$ |             $1.56$ |
| $524{,}288$     |      $163$ |      $1{,}747$ | $10.74$ |         $2.18$ |             $2.53$ |
| $1{,}048{,}576$ |      $310$ |      $4{,}217$ | $13.61$ |         $1.90$ |             $2.41$ |
| $2{,}097{,}152$ |      $609$ |     $10{,}521$ | $17.28$ |         $1.97$ |             $2.49$ |
| $4{,}194{,}304$ |  $1{,}707$ |     $25{,}822$ | $15.13$ |         $2.80$ |             $2.45$ |

The `int` column is clean, a doubling ratio of $2$ from $8{,}192$ up. The `Integer` doubling ratio sits at $1.9$–$2.2$ up to
$65{,}536$ and then settles around $2.4$–$2.5$, and the penalty moves smoothly:
about $8$–$9\times$ for small $N$, climbing through $11$, $13$ and $17$ to
settle at $15$–$17\times$ at the top. That is the predicted shape — well above
the $3\times$ floor throughout, roughly flat, rising gradually at large $N$ —
with the rise coming from the collector rather than the cache. A doubling ratio
steady above $2$ means the boxed stack grows slightly faster than linear, and
the reason is that during the pop phase all $N$ `Integer` objects are still
live: a young-generation collection copies every live object it finds, so
doubling $N$ doubles both the garbage _and_ the live set each collection has to
copy. The `int` stack has no such term, hence its clean $2$.

#### Values from the autobox cache

Boxing an `int` goes through `Integer.valueOf`, which keeps one ready-made
`Integer` for each value from $-128$ to $127$ and hands that same object back
every time. Run with `cached` as the fourth argument, the client draws every
value from that range, so the boxed stack allocates nothing and leaves no
garbage; what remains of the penalty is the range check on the way in and the
reference to follow on the way out. Same machine, same $1$ GB heap:

| $N$             | `int` (µs) | `Integer` (µs) |    ratio | `int` doubling | `Integer` doubling |
| --------------- | ---------: | -------------: | -------: | -------------: | -----------------: |
| $2{,}048$       |        $1$ |            $3$ |   $3.87$ |              — |                  — |
| $4{,}096$       |        $1$ |            $5$ |   $4.16$ |         $1.94$ |             $2.08$ |
| $8{,}192$       |        $3$ |           $11$ |   $4.21$ |         $2.03$ |             $2.05$ |
| $16{,}384$      |        $5$ |           $22$ |   $4.16$ |         $1.97$ |             $1.95$ |
| $32{,}768$      |        $9$ |           $40$ |   $4.23$ |         $1.82$ |             $1.85$ |
| $65{,}536$      |       $20$ |           $96$ |   $4.76$ |         $2.13$ |             $2.40$ |
| $131{,}072$     |       $37$ |      $9{,}317$ | $249.83$ |         $1.86$ |            $97.56$ |
| $262{,}144$     |       $75$ |     $18{,}317$ | $245.18$ |         $2.00$ |             $1.97$ |
| $524{,}288$     |      $150$ |     $37{,}117$ | $247.72$ |         $2.01$ |             $2.03$ |
| $1{,}048{,}576$ |      $303$ |     $74{,}757$ | $246.72$ |         $2.02$ |             $2.01$ |
| $2{,}097{,}152$ |      $604$ |    $146{,}877$ | $243.33$ |         $1.99$ |             $1.96$ |
| $4{,}194{,}304$ |  $1{,}226$ |    $302{,}845$ | $247.09$ |         $2.03$ |             $2.06$ |

Up to $65{,}536$ the penalty is a steady $4\times$, down from $8$–$9\times$:
allocation and collection were about half of it. That is the cache observed
directly.

From $131{,}072$ the boxed time jumps a hundredfold in one doubling and stays
there, which is not a boxing cost. It is the collector. The JVM's default
collector, G1, divides the heap into regions — $1$ MB each for a $1$ GB heap —
and $131{,}072$ references is exactly $1$ MB, so this is the first row whose
array no longer fits in a single region. Two further runs confirm it: with
`-XX:G1HeapRegionSize=4m` the jump moves to $524{,}288$, where the array reaches
$4$ MB, and with the Parallel collector (`-XX:+UseParallelGC`) it does not
happen at all:

| $N$             | `int` (µs) | `Integer` (µs) |  ratio | `int` doubling | `Integer` doubling |
| --------------- | ---------: | -------------: | -----: | -------------: | -----------------: |
| $2{,}048$       |        $1$ |            $2$ | $2.25$ |              — |                  — |
| $4{,}096$       |        $1$ |            $2$ | $1.87$ |         $1.94$ |             $1.61$ |
| $8{,}192$       |        $3$ |            $5$ | $1.87$ |         $1.97$ |             $1.97$ |
| $16{,}384$      |        $5$ |            $9$ | $1.82$ |         $2.05$ |             $1.99$ |
| $32{,}768$      |       $11$ |           $28$ | $2.70$ |         $2.02$ |             $3.00$ |
| $65{,}536$      |       $21$ |           $38$ | $1.85$ |         $1.97$ |             $1.35$ |
| $131{,}072$     |       $40$ |           $72$ | $1.78$ |         $1.94$ |             $1.86$ |
| $262{,}144$     |       $78$ |          $155$ | $1.98$ |         $1.95$ |             $2.16$ |
| $524{,}288$     |      $158$ |          $307$ | $1.95$ |         $2.01$ |             $1.99$ |
| $1{,}048{,}576$ |      $327$ |          $606$ | $1.85$ |         $2.08$ |             $1.97$ |
| $2{,}097{,}152$ |      $644$ |      $1{,}220$ | $1.89$ |         $1.97$ |             $2.01$ |
| $4{,}194{,}304$ |  $1{,}239$ |      $2{,}421$ | $1.95$ |         $1.92$ |             $1.98$ |

Why storing references into an array that spans regions is so slow under G1,
when nothing is being allocated, is left open here; the full-range run stores
into the same arrays and shows no such jump. The Parallel table carries its own
result: with cached values the penalty is about $1.9\times$, not $4\times$, so
roughly half of what the first cached table shows was G1's bookkeeping on every
reference store. The penalty depends on the collector as well as the machine.

This settles question 4.

**Verification.** The `int` doubling ratio is $2$ to within noise on every row
past the first few, in every table, which checks the harness before the
hypothesis. Under the conditions stated above the first table, autoboxing costs roughly an order of
magnitude, $8\times$ for a few thousand items rising to $15\times$ or more for
millions. With every value inside the autobox cache it falls to about $4\times$
under G1 while the array fits in one region, and to about $1.9\times$ under the
Parallel collector.

<br />
<br />
