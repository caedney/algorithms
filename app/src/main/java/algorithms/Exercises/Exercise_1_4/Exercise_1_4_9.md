# Exercise 1.4.9

Give a formula to predict the running time of a program for a problem of size
$N$ when doubling experiments have shown that the doubling factor is $2^b$ and
the running time for problems of size $N_0$ is $T$.

---

## The problem

You ran a doubling experiment and learned two things: each time the problem size
doubles, the running time multiplies by $2^b$, and at one specific size $N_0$
the program took time $T$. Turn those two measurements into a general formula
that predicts the running time at _any_ size $N$. The exercise's real point: a
doubling ratio plus a single calibration point is enough to extrapolate — you
never need to know the machine's speed.

## Definitions — what each symbol means

| Symbol | Meaning                                                                       | Where it comes from                            |
| ------ | ----------------------------------------------------------------------------- | ---------------------------------------------- |
| $N$    | The problem size you want a prediction for                                    | Your choice                                    |
| $N_0$  | A problem size you actually measured                                          | Your experiment                                |
| $T$    | Measured running time at size $N_0$                                           | Your experiment (the calibration point)        |
| $2^b$  | The doubling factor: what running time is multiplied by each time $N$ doubles | The stabilized ratio column of `DoublingRatio` |
| $b$    | The exponent of growth: $\log_2$ of the doubling factor                       | Derived — see _Decoding the ratio_ below       |

## The hidden assumption: a power law

The whole method assumes running time follows a **power law**:

$$T(N) = a N^b$$

for some constants $a$ (machine/implementation-dependent) and $b$
(algorithm-dependent). A constant ratio in your experiment is evidence the power
law holds; a drifting ratio means it doesn't (yet), and the prediction formula
shouldn't be trusted.

## Why the doubling ratio equals $2^b$

If $T(N) = aN^b$, doubling $N$ doubles every factor of $N$ inside — and there
are $b$ of them multiplied together — so the time picks up $b$ factors of $2$:

$$
\begin{aligned}
\frac{T(2N)}{T(N)} &= \frac{a(2N)^b}{aN^b} \\[10pt]
&= \frac{a \cdot 2^b \cdot N^b}{a \cdot N^b} \\[10pt]
&= \frac{\cancel{a} \cdot 2^b \cdot \cancel{N^b}}{\cancel{a} \cdot \cancel{N^b}} \\[10pt]
&= 2^b
\end{aligned}
$$

**Left side** — the doubling ratio itself: time at size $2N$ divided by time at
size $N$. Exactly the number `DoublingRatio` prints in its third column
(`time / prev`).

**Middle** — substitute the power law into both sizes: the denominator gets $N$,
giving $aN^b$; the numerator gets $2N$ _wherever $N$ appears_, giving $a(2N)^b$.
The parentheses matter — it's the whole quantity $2N$ raised to the $b$, not
$2N^b$.

**Right side** — expand with the power-of-a-product rule $(xy)^b = x^b y^b$, so
$(2N)^b = 2^b N^b$, then cancel $a$ and $N^b$ top and bottom.

**What the cancellations mean:** everything specific vanished — $a$ (machine
speed, language, constant factors) and $N$ (which size you measured at). Only
$b$, the algorithm's intrinsic growth exponent, survives. **The ratio is a
machine-independent, size-independent fingerprint of the algorithm.**

**Concrete check** with $b = 2$, $a = 5$, $N = 100$:

$$T(100) = 5 \cdot 100^2 = 50{,}000 \qquad T(200) = 5 \cdot 200^2 = 200{,}000$$

$$\frac{200{,}000}{50{,}000} = 4 = 2^2 \; \checkmark$$

## Decoding the ratio: $b = \log_2(\text{ratio})$

Since the ratio equals $2^b$, the measurement decodes into the exponent by
taking $\log_2$:

$$b = \log_2(\text{ratio})$$

| Measured ratio | $b$ | Growth               | Example                 |
| -------------- | --- | -------------------- | ----------------------- |
| $\approx 2$    | $1$ | linear $\sim N$      | `count` (sort + scan)\* |
| $\approx 4$    | $2$ | quadratic $\sim N^2$ | `countBrute`            |
| $\approx 8$    | $3$ | cubic $\sim N^3$     | `ThreeSum.count`        |

### \*The ratio-2 wrinkle

A truly linear algorithm gives a ratio of exactly $2$ — but a **linearithmic**
one ($\sim N \log N$) gives approximately $2$ as well. Computing its doubling
ratio, with the shorthand $L = \log_2 N$:

$$
\begin{aligned}
\frac{T(2N)}{T(N)} &= \frac{2N \log_2(2N)}{N \log_2 N} \\[10pt]
&= \frac{2\cancel{N} \log_2(2N)}{\cancel{N} \log_2 N} && \text{cancel } N \\[10pt]
&= \frac{2\,(1 + \log_2 N)}{\log_2 N} && \text{product rule: } \log_2(2N) = \log_2 2 + \log_2 N = 1 + \log_2 N \\[10pt]
&= \frac{2(1 + L)}{L} && \text{shorthand: } L = \log_2 N \\[10pt]
&= 2\left(\frac{1}{L} + \frac{L}{L}\right) && \text{split the fraction: } \tfrac{x+y}{z} = \tfrac{x}{z} + \tfrac{y}{z} \\[10pt]
&= 2\left(\frac{1}{\log_2 N} + 1 \right) && \tfrac{L}{L} = 1 \\[10pt]
&\longrightarrow\; 2 \text{ (slowly, from above)} && \tfrac{1}{\log_2 N} \to 0 \text{ as } N \text{ grows}
\end{aligned}
$$

The extra term $\frac{1}{\log_2 N}$ is always positive (hence _from above_) and
shrinks only as fast as $\log_2 N$ grows (hence _slowly_):

| $N$         | $\log_2 N$ | ratio $= 2\left(1 + \frac{1}{\log_2 N}\right)$ |
| ----------- | ---------- | ---------------------------------------------- |
| $1{,}024$   | $10$       | $2.2$                                          |
| $\sim 10^6$ | $20$       | $2.1$                                          |
| $\sim 10^9$ | $30$       | $2.07$                                         |

So the doubling test cannot cleanly distinguish $N$ from $N \log N$ at realistic
sizes — even at a billion elements the difference from $2$ is smaller than
typical timing noise. Ratios $4$ and $8$ are unambiguous; a ratio $\approx 2$
means "linear-ish, possibly with a log factor." The power-law model absorbs the
log as a $b$ slightly above $1$ — fine for nearby predictions, imperfect in
principle.

## Deriving the prediction formula

The derivation runs in three steps: count how many doublings separate $N_0$ from
$N$, charge the doubling factor once per doubling, then simplify.

### Step 1 — count the doublings

To grow from $N_0$ to $N$, you double $d$ times. Solving for $d$ takes three
statements:

$$N_0 \cdot 2^d = N \quad\Longrightarrow\quad 2^d = \frac{N}{N_0} \quad\Longrightarrow\quad d = \log_2\!\frac{N}{N_0}$$

(The $\Longrightarrow$ reads "implies" / "rearranges to" — connecting
statements. Not to be confused with $\longrightarrow$, "tends toward a limit,"
from the ratio-2 wrinkle.)

**The setup.** This encodes the _meaning_ of doubling: start at $N_0$, double
once ($N_0 \cdot 2$), twice ($N_0 \cdot 4$), $d$ times ($N_0 \cdot 2^d$), and
the claim is you've arrived at $N$. It isn't derived from anywhere — it's the
definition of "double $d$ times to get from $N_0$ to $N$," written as an
equation with $d$ unknown:

$$N_0 \cdot 2^d = N$$

**Isolate the power.** Divide both sides by $N_0$. The unknown now sits alone in
an exponent, and the right side is a known number — the size ratio:

$$\frac{\cancel{N_0} \cdot 2^d}{\cancel{N_0}} = \frac{N}{N_0} \quad\Longrightarrow\quad 2^d = \frac{N}{N_0}$$

**Extract the exponent.** The question "$2$ to what power equals
$\frac{N}{N_0}$?" is precisely the question $\log_2$ answers. Take $\log_2$ of
both sides and use $\log_2(2^d) = d$ — log base 2 undoes powers of 2:

$$\log_2(2^d) = \log_2\frac{N}{N_0} \quad\Longrightarrow\quad d = \log_2\frac{N}{N_0}$$

**Concrete check.** With $N_0 = 32{,}000$ and $N = 256{,}000$:

$$2^d = \frac{256{,}000}{32{,}000} = 8 \quad\Longrightarrow\quad d = \log_2 8 = 3$$

Verify against the setup: $32{,}000 \to 64{,}000 \to 128{,}000 \to 256{,}000$ —
three doublings. ✓

### Step 2 — apply the doubling factor once per doubling

Each doubling multiplies time by $2^b$, so after $d$ doublings the measured time
$T$ has been multiplied by $2^b$, $d$ times over:

$$T(N) = T \cdot \left(2^b\right)^{d} = T \cdot \left(2^b\right)^{\log_2 (N/N_0)}$$

### Step 3 — simplify

Step 2 left the formula in a usable-but-ugly form. Simplifying it rests on the
exponent rule

$$(2^b)^{\log_2 x} = \left(2^{\log_2 x}\right)^b = x^b$$

which compresses two facts into one line.

**Fact 1 — power of a power: $(x^m)^n = x^{m \cdot n}$.** Raising a power to a
power multiplies the exponents: $(2^3)^2 = 8^2 = 64 = 2^6$. And since
multiplication commutes ($m \cdot n = n \cdot m$), you can regroup in either
order — the exponents $b$ and $\log_2 x$ swap places, nothing is evaluated, the
tower is just rebracketed:

$$\left(2^b\right)^{\log_2 x} = 2^{\,b \,\cdot\, \log_2 x} = 2^{\,\log_2 x \,\cdot\, b} = \left(2^{\log_2 x}\right)^b$$

**Fact 2 — the exponential undoes the log: $2^{\log_2 x} = x$.** This is the
inverse-function relationship: $\log_2 x$ is _defined_ as "the power you raise
$2$ to, to get $x$," so raising $2$ to exactly that power hands $x$ back — the
same way $\sqrt{x^2}$ undoes squaring. Check: $2^{\log_2 8} = 2^3 = 8$. ✓

**Why the swap is necessary.** In the original form
$\left(2^b\right)^{\log_2 x}$, the $2$ and the $\log_2 x$ are separated — the
$b$ sits between them, so the inverse pair can't annihilate. The regrouping
moves $\log_2 x$ directly onto the $2$, creating the $2^{\log_2 x}$ package,
which collapses to $x$; the leftover $b$ then applies to the result:

$$\left(2^{\log_2 x}\right)^b = x^b$$

**Applying it** with $x = \frac{N}{N_0}$, the formula from Step 2 collapses:

$$T \cdot \left(2^b\right)^{\log_2(N/N_0)} = T \cdot \left(2^{\log_2(N/N_0)}\right)^b = T \cdot \left(\frac{N}{N_0}\right)^b$$

$$\boxed{\,T(N) = T \cdot \left(\frac{N}{N_0}\right)^{b}\,}$$

**In words:** _the predicted time is the measured time, scaled by the size ratio
raised to the growth exponent._

**Numeric trace** with $b = 2$ and $N/N_0 = 8$ (so $\log_2 8 = 3$):

$$\underbrace{(2^2)^3 = 4^3 = 64}_{\text{original form}} \qquad \underbrace{(2^3)^2 = 8^2 = 64}_{\text{swapped form}} \qquad \underbrace{8^2 = 64}_{\text{collapsed form}}$$

All three routes agree — and the collapsed form is literally "the size ratio,
squared."

**The conceptual takeaway.** The derivation built the answer as "$d$ doublings,
each costing $2^b$" — a statement about repeated _time_ multiplication. The
exponent swap re-reads the same quantity as "the size ratio, raised to $b$" — a
statement about _sizes_. Same number, two interpretations; the second is the
usable formula because it no longer mentions $d$.

### Why $a$ never appears

The power law has two unknowns, $a$ and $b$ — but the formula only needs $b$,
because $a$ cancels in the ratio:

$$\frac{T(N)}{T(N_0)} = \frac{a N^b}{a N_0^b} = \left(\frac{N}{N_0}\right)^b$$

You never need to know how fast your machine is in absolute terms; the
measurement $T$ carries all of that information implicitly.

### Sanity checks

- $N = N_0$: $\;T \cdot 1^b = T$ — predicts the measurement itself ✓
- $N = 2N_0$: $\;T \cdot 2^b$ — exactly one doubling factor ✓
- $N = 4N_0$: $\;T \cdot 4^b = T \cdot (2^b)^2$ — two doubling factors ✓

## Worked example (from the equal-pairs experiment)

### Brute force (quadratic)

`countBrute` showed ratio $\approx 4$, so $b = 2$. Suppose it measured $T = 0.5$
s at $N_0 = 32{,}000$. Predict $N = 32{,}000{,}000$:

$$T(N) = 0.5 \cdot \left(\frac{32{,}000{,}000}{32{,}000}\right)^{2} = 0.5 \cdot 1000^2 = 500{,}000 \text{ s} \approx 5.8 \text{ days}$$

No measurement can check this one — that's _why_ we predict it.

### Sort + scan (linearithmic)

`count` showed ratio $\approx 2$, so $b = 1$. It can't be calibrated at
$N_0 = 32{,}000$ — at that size it finishes in microseconds, pure timing noise —
so calibrate at $N_0 = 2{,}000{,}000$, where it measured $T = 0.0145$ s. Predict
$N = 32{,}000{,}000$:

$$T(N) = 0.0145 \cdot \left(\frac{32{,}000{,}000}{2{,}000{,}000}\right)^{1} = 0.0145 \cdot 16 = 0.232 \text{ s}$$

The actual measurement at $N = 32{,}000{,}000$ was $0.206$ s — the prediction
lands within about 10%, i.e. within timing noise. ✓

_(The ratio-2 wrinkle, quantified: modeling the log factor explicitly with
$T \cdot \frac{N}{N_0} \cdot \frac{\log_2 N}{\log_2 N_0} = 0.0145 \cdot 16 \cdot \frac{25}{21} \approx 0.276$
s predicts slightly high instead of slightly low. At these sizes the log
correction is smaller than the noise — which is exactly why the power-law model
gets away with ignoring it.)_

### The comparison

Same problem, same $N = 32{,}000{,}000$:

$$\frac{\text{brute force (predicted)}}{\text{sort + scan (measured)}} = \frac{500{,}000 \text{ s}}{0.206 \text{ s}} \approx 2.4 \text{ million} \times$$

— and the gap widens with every further doubling, since brute force picks up
$\times 4$ per doubling while sort + scan picks up only $\times 2$. The formula
quantifies exactly how much the better algorithm buys you: days versus a fifth
of a second.

## Caveats

- **Trust it near your data.** The power law is a model. Extrapolate too far and
  real machines betray it: caches overflow, memory swaps, GC pressure grows. A
  few doublings beyond your measurements is reasonable; a thousandfold
  extrapolation is an estimate, not a promise.
- **Use the stabilized ratio.** Early rows of `DoublingRatio` are polluted by
  JIT warmup and timing noise; take $2^b$ from where the column settles.
- **Log factors hide inside $b$** — see _The ratio-2 wrinkle_ above.

<br />
<br />
