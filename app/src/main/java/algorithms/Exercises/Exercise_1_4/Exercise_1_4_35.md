# Exercise 1.4.35

_Time costs for pushdown stacks_. Justify the entries in the table below, which
shows typical time costs for various pushdown stack implementations, using a
cost model that counts both _data references_ (references to data pushed onto
the stack, either an array reference or a reference to an object's instance
variable) and _objects created_.

| data structure   | item type | data references | objects created |
| ---------------- | --------- | --------------- | --------------- |
| _linked list_    | `int`     | $2N$            | $N$             |
|                  | `Integer` | $3N$            | $2N$            |
| _resizing array_ | `int`     | $\sim 5N$       | $\lg N$         |
|                  | `Integer` | $\sim 5N$       | $\sim N$        |

---

## The problem

An accounting exercise with a _specified cost model_ — the table's numbers are
for a workload of **$N$ pushes** onto an initially empty stack, and you must
justify every cell by walking the actual code (the book's linked-list `Stack`,
page 149, and `ResizingArrayStack`, page 141) and tallying two ledgers: (1)
_data references_ — each read or write of a stack item, whether through an array
slot or an object's instance variable; (2) _objects created_ — every `new`,
including nodes, boxed integers, and arrays.

The table rewards understanding of exactly the things the chapter measures
empirically: where autoboxing spends, where resizing spends, and why "objects
created" is a cost worth its own column (allocation and GC pressure, per the
chapter's memory discussion).

### How to attack one cell (method, not answers)

The method is the same for every cell: for one `push()`, list every line that
touches an item or allocates, decide which ledger each line goes in (data
reference, object created, or neither), multiply the per-push costs by $N$, and
reconcile with the table's entry. Then ask, row by row:

- **Linked list / `int`:** of the five lines in `push()`, which ones write an
  instance variable of the object that holds the pushed data, and which only
  touch the stack's own bookkeeping? Which line says `new`?
- **Linked list / `Integer`:** the same five lines run, but the caller passes an
  `int` and `item` is an `Integer`, so each push boxes the value first. What
  _extra_ object does that create beyond the node, and which extra data
  reference does the boxing add?
- **Resizing array / `int`:** pushes write `a[n]` ($N$ references) — where do
  the _other_ $\sim 4N$ come from? Recall each doubling copies every live
  element (one read + one write per element) and total copy work across all
  doublings is a geometric series. And why is "objects created" only $\lg N$ —
  what exactly gets allocated, and how many times?
- **Resizing array / `Integer`:** the array copies move _references_ — do copies
  touch items differently than in the `int` case? Which column changes and which
  doesn't, and why does objects-created jump from $\lg N$ to $\sim N$?

### Questions to guide your solution

1. Write the per-operation tally table (push, pop, resize-copy per element) for
   each implementation before scaling by $N$ — the table's entries should then
   be one-line sums.
2. Which entries are exact (no ~) and which are tilde-approximate, and what does
   that distinction track? (Hint: which costs depend on where $N$ sits relative
   to a power of two?)
3. The model counts an array _reference_, `a[i]`, as one data reference. Does
   `a[n++] = item` cost one or two in this model? Fix a convention and apply it
   consistently — being explicit about the convention is most of "justifying".
4. Boxing the items changes both implementations. By how much does each column
   grow for the linked list, and by how much for the resizing array? Is the
   printed table consistent about it, and what does the answer say about where
   each implementation keeps its items?

### The linked list

The book's linked-list `push(item)` is five lines:

1. `Node oldfirst = first;`
2. `first = new Node();`
3. `first.item = item;`
4. `first.next = oldfirst;`
5. `N++;`

The model counts references to _data pushed onto the stack_, made through an
array slot or an object's instance variable. Lines 1 and 5 write `first` and
`N`, which are instance variables of the stack itself — bookkeeping, not pushed
data — so they do not count. Lines 3 and 4 write the two instance variables of
the node, the object that holds the pushed item: two data references. Line 2 is
the only `new`: one object. Over $N$ pushes that is $2N$ data references and $N$
objects, both exact — nothing about a push depends on $N$.

An implementation that builds the node with a constructor,
`first = new Node(item, oldfirst)`, does lines 2–4 in one expression; the
constructor body still writes `item` and `next`, so the count is the same two
references and one object.

**`Integer` items.** The generic `Stack<Item>` cannot hold a primitive, so the
`int` row means a stack written for `int` (with `int item` in its node) and the
`Integer` row means `Stack<Integer>`. A client with `int`s in hand still writes
`push(5)`; the compiler inserts `Integer.valueOf(5)` at the call, which makes an
`Integer` object and writes the $5$ into its `value` field. So each push now
creates two objects — the node and the box — and makes three data references:
the two node writes plus the write into the box. Over $N$ pushes, $3N$ and $2N$,
again exact. (`Integer.valueOf` caches $-128$ to $127$, so boxing those values
allocates nothing; the table counts the general case, and an instrumented run
must push values above $127$ to match it.)

This settles question 1 for the linked list, and the linked-list half of
question 4: boxing adds a third object field that every item passes through.

### The resizing array, `int`

The convention for question 3: `a[N++] = item` is one data reference — the write
of `a[N]` — and the `N++` is stack bookkeeping, like line 5 above. So the $N$
pushes write $N$ array slots. The only other place an item is touched is
`resize`, whose loop `temp[i] = a[i]` reads one slot and writes another: two
references per item copied.

How many items are copied depends on $N$. Starting from an array of one slot, a
push that finds the array full doubles it first, copying every item present. For
$N = 16$ pushes the doublings happen on pushes $2, 3, 5, 9$:

| Push | Capacity before | Items copied | Running total |
| ---: | --------------: | -----------: | ------------: |
|  $2$ |             $1$ |          $1$ |           $1$ |
|  $3$ |             $2$ |          $2$ |           $3$ |
|  $5$ |             $4$ |          $4$ |           $7$ |
|  $9$ |             $8$ |          $8$ |          $15$ |

$15$ copies, about $N$, so about $2N$ copy references and about $3N$ in all —
less than the table says. One more push changes that: at $N = 17$ the array of
$16$ is full, the seventeenth push copies all $16$, and the total is $31$
copies, about $2N$:

$$
\begin{aligned}
1 + 2 + 4 + 8 + 16 &= 31 \\
&\approx 2N
\end{aligned}
$$

Two references each gives $\sim 4N$ copy references, plus the $N$ push writes:
$\sim 5N$. That is the table's entry, and it is the worst case, $N$ one past a
power of two, when the last doubling has just copied nearly everything. At a
power of two the total is $\sim 3N$, and for other $N$ it lies between.

Objects created: the only `new` is the array in `resize`, once per doubling, and
the capacity doubles from $1$ to at least $N$, so there are
$\lceil \lg N \rceil$ doublings — the table's $\lg N$.

This settles question 2: the tilde marks the cells whose cost depends on where
$N$ sits relative to a power of two; the linked-list cells are exact because a
push there costs the same whatever $N$ is. It also settles question 3 and the
resizing-array `int` row of question 1.

### The resizing array, `Integer`

The array now holds references to `Integer` boxes rather than the numbers
themselves. Nothing about the array's own work changes: a push still writes one
slot, and `temp[i] = a[i]` still reads one slot and writes another. A copy moves
the reference to a box; it does not make a new box.

Objects created: one box per push, made by the same `Integer.valueOf` call as in
the linked list, plus the $\lceil \lg N \rceil$ arrays. The arrays are the
lower-order term, so the total is $\sim N$ — the table's entry.

Data references: the convention fixed in the linked-list section is that the
write of the number into the box's `value` field counts, because that field
holds data pushed onto the stack. Applying the same convention here, in the
worst case of $N$ one past a power of two:

| Source                     | References |
| -------------------------- | ---------: |
| push writes `a[N]`         |        $N$ |
| copies, two per item moved |  $\sim 4N$ |
| writes into the boxes      |        $N$ |
| total                      |  $\sim 6N$ |

That is $\sim 6N$, not the table's $\sim 5N$, and the tilde does not cover the
gap: the two differ in the leading term.

**The two `Integer` rows use different conventions.** The linked list's step
from $2N$ to $3N$ only works if the box write counts. The resizing array staying
at $\sim 5N$ only works if it does not — if the table counts the stack's own
references, $N + 4N$, and leaves the box write to the client, whose call site is
where the boxing happens. Either convention is defensible, and each gives a
consistent pair:

| Convention                | linked list, `Integer` | resizing array, `Integer` |
| ------------------------- | ---------------------: | ------------------------: |
| box write counts          |                   $3N$ |                 $\sim 6N$ |
| box write is the client's |                   $2N$ |                 $\sim 5N$ |
| as printed                |                   $3N$ |                 $\sim 5N$ |

The printed pair takes one entry from each. The table describes its costs as
"typical", and this write-up keeps the first convention throughout, so the
justified entry for the resizing array of `Integer` is $\sim 6N$.

This settles question 4. Boxing costs both implementations the same extra $N$
references and the same extra $N$ objects, because both keep a reference to a
box where they used to keep the number. What differs is what that $N$ is added
to: $2N$ and $N$ for the linked list, so boxing is a large share of its cost;
$\sim 5N$ and $\lg N$ for the array, so the references barely move while the
objects go from almost none to one per item.

### Practical notes

- The book's tilde notation (page 179) is in force: report leading terms, drop
  lower-order ones — e.g. total copy references across doublings is $\sim 4N$
  _for $N$ one past a power of two_; state your assumption.
- If a cell won't reconcile, instrument real code: wrap the item array / node
  fields behind counting accessors and run $N$ pushes — the counter should match
  your formula exactly for exact cells.

### The measurement

There is no algorithm to write, so the code measures the table instead: the
book's two stacks, each in a version for `int` and a generic version used with
`Integer`, with one counter for each cost. Four workloads push $N$ numbers and
return the two counts; `main` runs them at every power of two and at one past
it.

```java
/**
 * The book's linked-list stack written for {@code int} items, counting the
 * exercise's two costs. A data reference is a read or write of an instance
 * variable of a node; the stack's own {@code first} and {@code size} are
 * bookkeeping and are not counted.
 */
static class CountingLinkedStackOfInts {
    private Node first;
    private int size;
    long dataReferences;
    long objectsCreated;

    private class Node {
        int item;
        Node next;
    }

    void push(int item) {
        Node oldFirst = first;

        first = new Node();
        objectsCreated++; // the node

        first.item = item;
        dataReferences++;

        first.next = oldFirst;
        dataReferences++;

        size++;
    }

    int size() {
        return size;
    }
}

/**
 * The book's generic linked-list stack with the same two counters. The stack
 * counts only what it does itself; the cost of boxing an {@code int} is added
 * by the client that boxes it.
 */
static class CountingLinkedStack<Item> {
    private Node first;
    private int size;
    long dataReferences;
    long objectsCreated;

    private class Node {
        Item item;
        Node next;
    }

    void push(Item item) {
        Node oldFirst = first;

        first = new Node();
        objectsCreated++; // the node

        first.item = item;
        dataReferences++;

        first.next = oldFirst;
        dataReferences++;

        size++;
    }

    int size() {
        return size;
    }
}

/**
 * The book's resizing array stack written for {@code int} items. A data
 * reference is a read or write of an array slot. The one-slot array made by
 * the constructor is not counted: the costs are those of the pushes.
 */
static class CountingResizingArrayStackOfInts {
    private int[] array = new int[1];
    private int size;
    long dataReferences;
    long objectsCreated;

    private void resize(int max) {
        int[] newArray = new int[max];
        objectsCreated++; // the array

        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
            dataReferences += 2; // one read, one write
        }

        array = newArray;
    }

    void push(int item) {
        if (size == array.length)
            resize(2 * array.length);

        array[size++] = item;
        dataReferences++;
    }

    int size() {
        return size;
    }
}

/**
 * The book's generic resizing array stack with the same two counters. As with
 * the linked list, the cost of boxing is added by the client that boxes.
 */
static class CountingResizingArrayStack<Item> {
    @SuppressWarnings("unchecked")
    private Item[] array = (Item[]) new Object[1];
    private int size;
    long dataReferences;
    long objectsCreated;

    @SuppressWarnings("unchecked")
    private void resize(int max) {
        Item[] newArray = (Item[]) new Object[max];
        objectsCreated++; // the array

        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
            dataReferences += 2; // one read, one write
        }

        array = newArray;
    }

    void push(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        array[size++] = item;
        dataReferences++;
    }

    int size() {
        return size;
    }
}

/**
 * The first value pushed. {@code Integer.valueOf} hands out shared objects for
 * -128 to 127, so every value pushed is kept above that range and each box is
 * a new object.
 */
private static final int FIRST_VALUE = 1000;

/** Pushes 𝑁 {@code int}s onto a linked-list stack of {@code int}. */
static long[] linkedListOfInts(int n) {
    CountingLinkedStackOfInts stack = new CountingLinkedStackOfInts();

    for (int i = 0; i < n; i++)
        stack.push(FIRST_VALUE + i);

    return new long[] { stack.dataReferences, stack.objectsCreated };
}

/**
 * Pushes 𝑁 {@code int}s onto a linked-list stack of {@code Integer}. Each
 * push boxes its value first: one more object, and one more data reference
 * for the write of the value into the box.
 */
static long[] linkedListOfIntegers(int n) {
    CountingLinkedStack<Integer> stack = new CountingLinkedStack<>();

    for (int i = 0; i < n; i++) {
        Integer box = Integer.valueOf(FIRST_VALUE + i);
        stack.objectsCreated++; // the box
        stack.dataReferences++; // the value written into the box

        stack.push(box);
    }

    return new long[] { stack.dataReferences, stack.objectsCreated };
}

/** Pushes 𝑁 {@code int}s onto a resizing array stack of {@code int}. */
static long[] resizingArrayOfInts(int n) {
    CountingResizingArrayStackOfInts stack = new CountingResizingArrayStackOfInts();

    for (int i = 0; i < n; i++)
        stack.push(FIRST_VALUE + i);

    return new long[] { stack.dataReferences, stack.objectsCreated };
}

/**
 * Pushes 𝑁 {@code int}s onto a resizing array stack of {@code Integer},
 * boxing each one first and counting the box as above.
 */
static long[] resizingArrayOfIntegers(int n) {
    CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>();

    for (int i = 0; i < n; i++) {
        Integer box = Integer.valueOf(FIRST_VALUE + i);
        stack.objectsCreated++; // the box
        stack.dataReferences++; // the value written into the box

        stack.push(box);
    }

    return new long[] { stack.dataReferences, stack.objectsCreated };
}
```

The decisions that matter:

- **Each stack is the book's code with a counter beside the line that costs.**
  `dataReferences++` follows each write of a node field or an array slot,
  `dataReferences += 2` each item copied, `objectsCreated++` each `new`. The
  stack's own `first` and `size` are bookkeeping and have no counter.
- **Boxing is counted where it happens.** The generic stacks know nothing about
  boxes; the two `Integer` workloads box each value themselves and add one
  object and one reference for it, so the cost of boxing is the same line in
  both and the convention is visible in one place.
- **The values pushed start at $1000$.** `Integer.valueOf` shares its objects
  for $-128$ to $127$, so a workload that pushed small numbers would be counting
  boxes that were never made.
- **The constructor's one-slot array is not counted.** The costs are those of
  the $N$ pushes, which is what makes the array count come out at
  $\lceil \lg N \rceil$.

Data references per push and objects created, from `./gradlew run`:

|        $N$ | list `int` refs |    objects | list `Integer` refs |     objects | array `int` refs | objects | array `Integer` refs |    objects |
| ---------: | --------------: | ---------: | ------------------: | ----------: | ---------------: | ------: | -------------------: | ---------: |
|       $16$ |         $2.000$ |       $16$ |             $3.000$ |        $32$ |          $2.875$ |     $4$ |              $3.875$ |       $20$ |
|       $17$ |         $2.000$ |       $17$ |             $3.000$ |        $34$ |          $4.647$ |     $5$ |              $5.647$ |       $22$ |
|  $1{,}024$ |         $2.000$ |  $1{,}024$ |             $3.000$ |   $2{,}048$ |          $2.998$ |    $10$ |              $3.998$ |  $1{,}034$ |
|  $1{,}025$ |         $2.000$ |  $1{,}025$ |             $3.000$ |   $2{,}050$ |          $4.994$ |    $11$ |              $5.994$ |  $1{,}036$ |
| $65{,}536$ |         $2.000$ | $65{,}536$ |             $3.000$ | $131{,}072$ |          $3.000$ |    $16$ |              $4.000$ | $65{,}552$ |
| $65{,}537$ |         $2.000$ | $65{,}537$ |             $3.000$ | $131{,}074$ |          $5.000$ |    $17$ |              $6.000$ | $65{,}554$ |

The linked-list columns are $2$, $N$, $3$ and $2N$ on every row: exact, as
derived. The resizing array of `int` is $(3N - 2)/N$ at a power of two and
$(5N - 6)/N$ one past it — $46/16 = 2.875$ and $79/17 = 4.647$ in the first two
rows — climbing to $3$ and $5$, with $\lceil \lg N \rceil$ arrays. The resizing
array of `Integer` is exactly one reference per push and $N$ objects more, so
its worst case climbs to $6$, not the table's $5$.

**Verification.** `Exercise_1_4_35Test` ($16$ tests) asserts the exact counts
the sections above derive: $2N$ and $N$, then $3N$ and $2N$, for the linked list
at every $N$ up to $1{,}025$ and at $65{,}536$; $46$ references and $4$ arrays
for $16$ pushes onto the resizing array, $79$ and $5$ for $17$; $3N - 2$ at
every power of two and $5N - 6$ one past it, up to $2^{16} + 1$; and for every
$N$ up to $2{,}049$ the count $N$ plus twice the items copied, where the items
copied are worked out independently by walking the capacity. New arrays appear
on pushes $2$, $3$, $5$, $9$ and $17$. For boxing, the tests assert that each
generic stack handed a ready-made box costs exactly what its `int` twin does,
that the `Integer` workloads cost $N$ references and $N$ objects more than the
`int` ones on both implementations ($96$ and $22$ for $17$ pushes onto the
array, $6N - 6$ one past every power of two), and that `Integer.valueOf` returns
a new object for $1000$ and a shared one for $5$. Mutants: counting one
reference per copied item fails $7$ tests, doubling on the push that fills
rather than the one that finds the array full fails $8$, counting `size++` as a
data reference fails $4$, not counting the node fails $4$, not counting the
write into the box fails $3$, not counting the box fails $2$, and growing the
array by half instead of doubling fails $9$. Starting the pushed values at $0$
fails nothing, because the box is counted by the workload rather than detected;
the `Integer.valueOf` test is what guards that premise.

<br />
<br />
