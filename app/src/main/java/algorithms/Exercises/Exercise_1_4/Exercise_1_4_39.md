# Exercise 1.4.39

_Improved accuracy for doubling test_. Modify `DoublingRatio` to take a second
command-line argument that specifies the number of calls to make to
`timeTrial()` for each value of `N`. Run your program for 10, 100, and 1,000
trials and comment on the precision of the results.

---

## The problem

`DoublingRatio` (page 193) times `ThreeSum.count()` **once** per value of N and
prints the ratio to the previous N. One sample per data point is statistics on a
knife-edge: the JIT may still be compiling, the OS may schedule something else
mid-run, GC may fire — and a single noisy trial contaminates _two_ printed
ratios (as numerator once and denominator once). The fix is the oldest tool in
experimental science: repeat the measurement. The exercise asks you to
parameterise the repeat count and then _quantify_ what repetition buys.

### What to build

```text
% java DoublingRatio 1        % java DoublingRatio 100
(one trial per N, as before)  (100 trials per N, aggregated)
```

Design decisions you must make explicitly — they _are_ the exercise:

- **Aggregate how?** Sum the trial times? Mean? Median? Minimum? Each answers a
  different question (the minimum estimates the noise-free cost; the mean
  includes GC reality; the median resists outliers). Choose, justify, and
  ideally report more than one.
- **Fresh input per trial or fixed input?** Random arrays vary in _actual_ work
  (the count differs); a fixed array isolates timing noise from input variation.
  Which variance are you trying to measure?

### Questions to guide your solution

1. Before running: what should the ratio converge to for `ThreeSum`? (Cubic →
   8.) With that ground truth, "precision" is measurable: how far do printed
   ratios stray from 8 at 10 vs 100 vs 1,000 trials?
2. Quantify, don't eyeball: for each N, also print the standard deviation (or
   min/max spread) of the trials. Does the spread of the _ratio_ shrink like
   you'd expect as trials grow — and does the improvement saturate (systematic
   error that repetition can't remove, e.g. warmup bias in the first trials)?
3. There's a cost axis too: 1,000 trials at large N takes real time. For a fixed
   time budget, are you better off with more trials at small N or fewer trials
   at larger N? (Where is the doubling ratio itself most trustworthy?)
4. Bonus rigor: discard the first few trials as warmup and see whether 10-trial
   precision improves disproportionately — that isolates JIT warmup from
   steady-state jitter.

### Practical notes

- Keep the doubling structure (N \*= 2 forever) and print running ratios exactly
  as the book's version does so outputs are comparable side by side.
- Use `System.nanoTime()`-based `Stopwatch` granularity awareness: at small N,
  single trials can be near timer resolution — another independent reason
  small-N ratios wobble.
- Consume the count result (print or accumulate) so the JIT cannot elide work;
  run with a quiet machine and note the environment.
- This harness is reusable: 1.4.37, 1.4.38, and 1.4.43 all get more trustworthy
  with the multi-trial version — build it well once.

<br />
<br />
