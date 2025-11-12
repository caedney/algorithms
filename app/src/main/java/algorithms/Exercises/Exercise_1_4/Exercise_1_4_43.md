# Exercise 1.4.43

_Resizing arrays versus linked lists_. Run experiments to validate the
hypothesis that resizing arrays are faster than linked lists for stacks (see
EXERCISE 1.4.35 and EXERCISE 1.4.36). Do so by developing a version of
`DoublingRatio` that computes the ratio of the running times of the two
programs.

---

## The problem

Exercises 1.4.35/36 predicted on paper that the resizing-array stack should beat
the linked-list stack — fewer objects created (lg N vs N for int workloads),
better memory density (~4–16 bytes/item vs ~32). This exercise closes the
scientific loop: state that prediction as a hypothesis, then build the
measurement apparatus and see whether your machine agrees. It's 1.4.38's
ratio-harness pattern applied to data structures instead of algorithms.

### What to build

A `DoublingRatio` variant where each timed trial is a stack workload — N pushes
then N pops (and consider a mixed random push/pop sequence as a second workload)
— run against both `ResizingArrayStack` and the linked-list `Stack`, printing
per-N times and the ratio linked/array:

```text
N        T_array   T_linked   ratio
1M         ...        ...       ?
2M         ...        ...       ?    ← flat ratio? growing? that's the finding
```

### Questions to guide your solution

1. Formulate the hypothesis quantitatively before running: from 1.4.35's
   operation counts alone, what ratio would you naively expect? Then list what
   the operation counts _don't_ capture — allocation cost per node, GC pressure
   from N short-lived nodes, and cache behaviour (array = sequential access;
   linked list = pointer-chasing over scattered heap) — and predict which way
   each pushes the measured ratio.
2. Both structures are amortized O(1) per op, so each program's own doubling
   ratio should approach 2 — verify that control first. The _ratio between them_
   is a constant-factor comparison: does it hold steady as N grows, or grow with
   N (a classic cache-locality signature as the linked nodes overflow cache
   levels)?
3. Item type is a second axis: repeat with `Integer` items (both structures box)
   vs a primitive-specialised variant if you built one for 1.4.37. Does boxing
   narrow the gap (both now allocate per push) — as 1.4.35's table hints?
4. The resizing array has occasional O(N) spikes (doubling copies). Does the
   workload N-pushes-then-N-pops sit at a flattering or unflattering array size
   (just past a doubling?) — and does randomising the workload length damp that
   artefact?

### Practical notes

- Use the book's two implementations (pages 141 and 149) unmodified apart from
  instrumentation; same JVM, same warmup discipline, multiple trials (1.4.39's
  harness) — you are measuring small constant factors, exactly where sloppy
  benchmarking lies the most.
- Interleave which structure is timed first at each N, and keep results (e.g.
  checksum of popped values) live to defeat dead-code elimination.
- GC pollutes linked-list timings disproportionately — run with a generous fixed
  heap and, as a variant, report timings with an explicit `System.gc()` between
  trials vs without; comment on the difference.
- Expected outcome on typical machines: array wins by a small-integer factor
  that _grows_ somewhat with N — but the exercise asks what happens on _your_
  machine; report what you see.

<br />
<br />
