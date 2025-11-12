# Exercise 1.4.25

_Throwing two eggs from a building_. Consider the previous question, but now
suppose you only have two eggs, and your cost model is the number of throws.
Devise a strategy to determine $F$ such that the number of throws is at most
$2\sqrt{N}$, then find a way to reduce the cost to $\sim c\sqrt{F}$. This is
analogous to a situation where search hits (egg intact) are much cheaper than
misses (egg broken).

---

## The problem

Same building, same hidden threshold $F$ — but now the resource that made binary
search possible is gone. With only **two eggs**, you get exactly two "break"
events before you're blind: after the first egg breaks you must switch to a
strategy that can never break an egg except at the very answer, and after the
second breaks you'd better be done. Binary search breaks $\sim \lg N$ eggs, so
it's off the table. The exercise asks how good search can be under an
_asymmetric failure budget_ — and the answer changes the complexity class:
$\sqrt{N}$, not $\lg N$.

### Why $\sqrt{N}$ is the natural shape

Egg 1 can afford to probe in _jumps_ (each break is survivable once); egg 2 must
then _crawl_ linearly through the last uncertain gap. If egg 1 jumps in strides
of size $s$, the worst case is roughly

$$\text{throws} \approx \underbrace{N/s}_{\text{jumps by egg 1}} + \underbrace{s}_{\text{crawl by egg 2}}$$

Minimise $N/s + s$ over $s$ — where's the sweet spot, and what total does it
give? That's the $2\sqrt{N}$ part.

### Questions to guide your solution

1. Prove the trade-off above is tight for _fixed_ stride $s$: which $F$ makes
   both terms bite simultaneously?
2. For the $\sim c\sqrt{F}$ refinement, fixed strides overspend when $F$ is
   small (the same critique that drives the unlimited-eggs problem from
   $\sim \lg N$ down to $\sim 2 \lg F$). What if the stride _grows_ as you go —
   jump to positions $1, 3, 6, 10, 15, \ldots$ (gaps $1, 2, 3, 4, \ldots$)?
   After the $k$-th jump you've spent $k$ throws and stand near $k^2/2$. How
   many throws to pass $F$, and how long is the crawl gap you leave behind?
3. The book frames it as "hits much cheaper than misses". Make that precise in
   your solution: how many _misses_ (breaks) does each strategy incur, and what
   does each miss cost you downstream?
4. Sanity check against the unlimited-eggs problem: there the answer was
   $\sim 2 \lg F$; with two eggs it's $\sim c\sqrt{F}$. Can you articulate _why_
   restricting failures forces an exponential-to-polynomial degradation? (What
   information does a throw give you when you cannot afford it to break?)

### Tightness for a fixed stride

Fix a stride $s \ge 2$ and write $m = \lfloor N/s \rfloor$. Egg 1 is thrown from
floors $s, 2s, \ldots, ms$ in turn until it breaks; egg 2 then crawls upward one
floor at a time through the gap below the floor that broke it. The top floor of
a gap is never thrown from: if every floor below it holds, the threshold must be
the jump floor above, and egg 2 stops.

Taking $N = 20$ and $s = 5$, egg 1's jump floors are $5, 10, 15, 20$, and a few
thresholds trace as follows.

|  $F$ | Egg 1 throws from | Egg 2 throws from |      Throws |
| ---: | ----------------: | ----------------: | ----------: |
|  $3$ |               $5$ |         $1, 2, 3$ | $1 + 3 = 4$ |
| $10$ |           $5, 10$ |      $6, 7, 8, 9$ | $2 + 4 = 6$ |
| $14$ |       $5, 10, 15$ |  $11, 12, 13, 14$ | $3 + 4 = 7$ |
| $19$ |   $5, 10, 15, 20$ |  $16, 17, 18, 19$ | $4 + 4 = 8$ |

Egg 1 can make at most $m$ throws and egg 2 at most $s - 1$, so no threshold
costs more than $m + s - 1$. At $F = 19$ both eggs are at their maximum and the
total reaches that bound.

**Claim.** For every $N$ and every stride $s \ge 2$, the threshold $F = sm - 1$
— the floor one below egg 1's last jump — costs exactly
$\lfloor N/s \rfloor + s - 1$ throws.

_Proof._ Egg 1's count follows from comparing each jump floor with $F = sm - 1$.
The jump before last survives: substituting for $F$ and cancelling $ms$ from
both sides,

$$
\begin{aligned}
(m - 1)s &< sm - 1 \\
ms - s &< ms - 1 \\
-s &< -1 \\
s &> 1 ,
\end{aligned}
$$

and the stride is at least $2$ by assumption. The last jump breaks:

$$
\begin{aligned}
ms &\ge sm - 1 \\
0 &\ge -1 ,
\end{aligned}
$$

which always holds. Egg 1 therefore survives its first $m - 1$ throws and breaks
on the $m$-th, for exactly $m$ throws. Egg 2 then crawls through the gap
$(m - 1)s + 1, \ldots, ms - 1$, which holds $s - 1$ floors, and $F$ is the top
one, so every floor in the gap is thrown from, for exactly $s - 1$ throws. The
total is $m + s - 1$. $\blacksquare$

Note that the worst threshold is $sm - 1$, not $N - 1$. When $s$ does not divide
$N$, the final gap from $ms + 1$ up to $N$ is shorter than a full stride and
cannot make egg 2 work hard; both terms bite only at the top of the last
full-length gap. With $N = 20$ and $s = 6$ the jump floors are $6, 12, 18$:

|  $F$ | Egg 1 throws from |    Egg 2 throws from |      Throws |
| ---: | ----------------: | -------------------: | ----------: |
| $17$ |       $6, 12, 18$ | $13, 14, 15, 16, 17$ | $3 + 5 = 8$ |
| $20$ |       $6, 12, 18$ |                 $19$ | $3 + 1 = 4$ |

When $s$ divides $N$ the last jump lands on $N$ itself and $sm - 1 = N - 1$, so
the two descriptions agree.

The $-1$ and the floor brackets vanish in the asymptotic statement.
$\lfloor N/s \rfloor + s - 1$ and $N/s + s$ differ by less than $2$, a constant,
while the quantity itself grows without bound as $N$ does, so their ratio tends
to $1$ and the two are equal under $\sim$. The trade-off $\sim N/s + s$ is
therefore tight: for every stride there is a threshold that attains it, which
settles the first guiding question.

### Choosing the stride

Tightness says that every stride $s$ has a threshold costing
$\lfloor N/s \rfloor + s - 1$ throws. Those worst cases differ from stride to
stride: a small stride makes egg 1 do most of the work, since $N/s$ is large,
and a large stride makes egg 2 do most of it, since $s$ is large. The stride to
choose is the one whose worst case is smallest. Tabulating for $N = 100$:

|  $s$ | $\lfloor 100/s \rfloor$ | $s - 1$ | Throws |
| ---: | ----------------------: | ------: | -----: |
|  $2$ |                    $50$ |     $1$ |   $51$ |
|  $5$ |                    $20$ |     $4$ |   $24$ |
| $10$ |                    $10$ |     $9$ |   $19$ |
| $20$ |                     $5$ |    $19$ |   $24$ |
| $50$ |                     $2$ |    $49$ |   $51$ |

The table is symmetric: $s = 2$ and $s = 50$ cost the same, as do $s = 5$ and
$s = 20$, because swapping $s$ for $N/s$ swaps the two terms of the sum without
changing it. The minimum sits in the middle, at $s = 10 = \sqrt{N}$, where the
two terms are equal. A table only shows this for one $N$, though. The general
statement needs a proof, and it has a short one that uses nothing more than the
fact that a square is never negative.

**Claim.** For every $N$ and every stride $s > 0$,

$$
\frac{N}{s} + s \ge 2\sqrt{N} ,
$$

with equality exactly when $s = \sqrt{N}$.

_Proof._ Subtract $2\sqrt{N}$ from both sides and the claim becomes

$$
\frac{N}{s} + s - 2\sqrt{N} \ge 0 \qquad \text{for every } s > 0 .
$$

In words, the expression on the left is never negative. The cleanest way to
prove that about any expression is to show it is the square of something, since
a square is never negative. So the proof is a search: what is
$N/s - 2\sqrt{N} + s$ the square of?

To answer that you need to know what squares look like once multiplied out, and
the perfect-square identities from school algebra are the catalogue. They hold
for any two numbers $a$ and $b$, and come from multiplying the bracket by
itself:

$$
\begin{aligned}
(a + b)^2 &= a^2 + 2ab + b^2 \\
(a - b)^2 &= a^2 - 2ab + b^2 .
\end{aligned}
$$

Either way the result has three terms: two squares, which are always positive,
and a doubled cross term in the middle whose sign is the sign in the bracket. So
an expression is a perfect square when its two outer terms are positive, and the
middle term then tells you which identity it came from. Our expression
$N/s - 2\sqrt{N} + s$ has positive outer terms and a minus in the middle, so it
matches the difference.

Matching the two squared terms gives $a^2 = N/s$ and $b^2 = s$, that is
$a = \sqrt{N/s}$ and $b = \sqrt{s}$, both real because $N$ and $s$ are positive.
That leaves the middle term to check: the cross term must come out as
$2\sqrt{N}$ for the match to be exact. It does, because the two roots combine
under one root and the $s$ cancels:

$$
2ab = 2 \sqrt{\frac{N}{s}} \cdot \sqrt{s} = 2 \sqrt{\frac{N}{s} \cdot s} = 2\sqrt{N} .
$$

Putting the three pieces together,

$$
\left( \sqrt{\frac{N}{s}} - \sqrt{s} \right)^2 = \frac{N}{s} - 2\sqrt{N} + s .
$$

The left side is a square, so it is at least $0$, and therefore so is the right
side:

$$
\begin{aligned}
\frac{N}{s} - 2\sqrt{N} + s &\ge 0 \\
\frac{N}{s} + s &\ge 2\sqrt{N} .
\end{aligned}
$$

That is the bound. For the equality case, a square is $0$ only when the thing
squared is $0$, so the bound is reached exactly when
$\sqrt{N/s} - \sqrt{s} = 0$. Squaring both sides of $\sqrt{N/s} = \sqrt{s}$
gives $N/s = s$, so $N = s^2$ and $s = \sqrt{N}$. $\blacksquare$

The claim settles both halves of the question at once. No stride can bring the
worst case below $2\sqrt{N}$, and the stride $s = \sqrt{N}$ reaches it.
Substituting that stride into the trade-off directly shows the same total:

$$
\frac{N}{\sqrt{N}} + \sqrt{N} = \frac{\sqrt{N} \cdot \sqrt{N}}{\sqrt{N}} + \sqrt{N} = \sqrt{N} + \sqrt{N} = 2\sqrt{N} .
$$

The claim is about real numbers, but a stride is a number of floors, so it has
to be a whole number. When $N$ is a perfect square there is no conflict:
$\sqrt{N}$ is whole, the stride is $\sqrt{N}$, and the exact worst case is
$\lfloor N/s \rfloor + s - 1 = 2\sqrt{N} - 1$, the $19$ in the table for
$N = 100$. When $N$ is not a perfect square, $\sqrt{N}$ falls between two whole
numbers and the stride has to be one of them. Rounding up, to
$s = \lceil \sqrt{N} \rceil$, keeps the exact count under the bound:
$N/s \le N/\sqrt{N} = \sqrt{N}$ because $s \ge \sqrt{N}$, and $s - 1 < \sqrt{N}$
because $s$ is the next whole number up, so

$$
\left\lfloor \frac{N}{s} \right\rfloor + s - 1 < 2\sqrt{N} .
$$

For $N = 50$, say, $\sqrt{50} \approx 7.07$ and the stride is $8$, giving
$\lfloor 50/8 \rfloor + 8 - 1 = 13$ throws against a bound of
$2\sqrt{50} \approx 14.1$. The count is a whole number and the bound is not,
which is fine: the bound is a ceiling the strategy has to stay under, not a
number of throws anyone makes. Either way the two-egg strategy with a fixed
stride of about $\sqrt{N}$ finds $F$ in at most $\sim 2\sqrt{N}$ throws, which
is the first bound the exercise asks for and the answer to the sweet-spot
question above.

### Growing the stride

The fixed stride is tuned to $N$, and that is its weakness. Take $N = 10{,}000$,
so the stride is $s = 100$, and suppose the threshold is $F = 150$. Egg 1 throws
from $100$ (intact) and $200$ (breaks): $2$ throws. Egg 2 then crawls
$101, 102, \ldots, 150$: $50$ throws. The total is $52$, and had $F$ been $199$
it would have been $101$ — against a target of $2\sqrt{F} \approx 24$. Egg 1 is
cheap; the waste is all in the crawl. Its length is set by $s = \sqrt{N}$,
chosen with the whole $10{,}000$-floor building in mind, and it stays that size
however low down the threshold turns out to be. A threshold near $150$ wanted a
stride near $\sqrt{150} \approx 12$.

Since $F$ is not known in advance, no single stride will do. What is wanted
instead is small gaps low in the building, where a small $F$ would be found
cheaply, and larger gaps higher up, where a large $F$ has already shown it is
worth jumping further: a stride that _grows_ as egg 1 climbs. The question is
how fast it should grow, and the two-term trade-off from the fixed stride
reappears inside that choice. Gaps that grow too fast make egg 1 quick and egg 2
slow; gaps that grow too slowly do the reverse.

Fibonacci gaps $1, 1, 2, 3, 5, 8, 13, \ldots$ are a natural first try, putting
egg 1's jump floors at the running totals
$1, 2, 4, 7, 12, 20, 33, 54, 88, 143, 232, \ldots$

|   $F$ |                          Egg 1 throws from |       Egg 2 throws from |         Throws |
| ----: | -----------------------------------------: | ----------------------: | -------------: |
| $144$ | $1, 2, 4, 7, 12, 20, 33, 54, 88, 143, 232$ |                   $144$ |  $11 + 1 = 12$ |
| $231$ | $1, 2, 4, 7, 12, 20, 33, 54, 88, 143, 232$ | $144, 145, \ldots, 231$ | $11 + 88 = 99$ |

Egg 1 is very cheap, but the gap below $232$ holds $88$ floors, and
$88 / 231 \approx 0.38$. That fraction is the same at every scale, because each
Fibonacci number is about $0.38$ of the running total just above it, so the
worst-case crawl is proportional to $F$ itself. Fibonacci gaps grow
exponentially, and the cost is linear in $F$ — worse than the fixed stride, not
better.

The target tells us the right rate. For the cost to be about $\sqrt{F}$ when egg
1 breaks just above $F$, the number of jumps $k$ must be about $\sqrt{F}$, that
is, the position after $k$ jumps must be about $k^2$. The $k$-th gap is then the
difference between consecutive positions,

$$
\begin{aligned}
k^2 - (k - 1)^2 &= k^2 - (k^2 - 2k + 1) \\
&= k^2 - k^2 + 2k - 1 \\
&= 2k - 1 ,
\end{aligned}
$$

which is linear in $k$. The first line expands $(k - 1)^2$ by the perfect-square
identity with $a = k$ and $b = 1$; the second removes the bracket, flipping the
sign of each term inside it because of the minus in front; the $k^2$ terms then
cancel. So the gaps must grow by a constant each jump: the sequence of gaps is
an arithmetic progression. The simplest choice is gaps $1, 2, 3, 4, \ldots$,
which puts the jump floors at the triangular numbers
$1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78, 91, 105, 120, 136, 153, \ldots$,
with the $k$-th jump at floor $k(k + 1)/2$. Two traces, each at the worst
threshold for its gap:

|    $N$ |   $F$ | Egg 1 breaks on jump |       Egg 2 throws from |         Throws |
| -----: | ----: | -------------------: | ----------------------: | -------------: |
|  $100$ |  $90$ |    $13$ (floor $91$) |    $79, 80, \ldots, 90$ | $13 + 12 = 25$ |
| $1000$ | $135$ |   $16$ (floor $136$) | $121, 122, \ldots, 135$ | $16 + 15 = 31$ |

Both totals have the form $2k - 1$, and that is general. If egg 1 breaks on its
$k$-th jump it has made $k$ throws. The gap below that jump is the $k$-th gap,
which holds $k - 1$ floors, so egg 2 crawls at most $k - 1$ throws. The worst
case is therefore

$$
k + (k - 1) = 2k - 1 ,
$$

and nothing in it mentions $N$: the height of the building has dropped out
entirely.

The exercise wants the cost in terms of $F$, not $k$, so the last step is to
translate. The worst $F$ sits just below the $k$-th jump floor, so
$F \approx k(k + 1)/2 \approx k^2/2$. Rearranging for $k$,

$$
\begin{aligned}
2F &\approx k^2 \\
k &\approx \sqrt{2F} = \sqrt{2} \cdot \sqrt{F} ,
\end{aligned}
$$

using the product-of-roots rule $\sqrt{xy} = \sqrt{x} \cdot \sqrt{y}$ to split
the root. Substituting into the cost,

$$
2k - 1 \approx 2 \sqrt{2} \cdot \sqrt{F} - 1 \sim 2\sqrt{2}\, \sqrt{F} .
$$

The product $2 \cdot \sqrt{2} \cdot \sqrt{F}$ is grouped as
$(2\sqrt{2}) \cdot \sqrt{F}$ because the exercise asks for the shape
$c\sqrt{F}$: the $2$ and the $\sqrt{2}$ are both constants and belong in $c$,
while $\sqrt{F}$ is the only part that changes with the threshold. So
$c = 2\sqrt{2} \approx 2.83$. Checking against the traces, $F = 90$ predicts
about $2.83 \times 9.5 \approx 27$ throws against the $25$ counted, and
$F = 135$ predicts about $2.83 \times 11.6 \approx 33$ against $31$; the $-1$
and the rounding of $k(k + 1)/2$ to $k^2/2$ account for the difference, and both
vanish under $\sim$.

So growing the gaps by one floor per jump finds $F$ in
$\sim 2\sqrt{2}\,\sqrt{F}$ throws with no dependence on $N$, which is the second
bound the exercise asks for and settles the second guiding question.

### Hits, misses and what a throw is worth

Every throw is a hit (the egg survives) or a miss (it breaks), and the book's
remark that hits are much cheaper than misses is the key to both the shape of
the strategies and the gap between $\lg F$ and $\sqrt{F}$.

Count the misses first. In a complete run of either two-egg strategy, egg 1
breaks exactly once, on the jump that overshoots $F$, and egg 2 breaks at most
once, on $F$ itself — or not at all, when it clears every floor of the gap and
infers that $F$ is the jump floor above. In the $N = 100$, $F = 90$ trace the
two misses were at floors $91$ and $90$; in the $N = 20$, $s = 5$, $F = 10$
trace egg 2 crawled $6, 7, 8, 9$ intact and never broke, so there was one.
Either way the count is at most $2$, fixed, and independent of $F$: the egg
budget forces it.

Now the price of a miss. Before egg 1 breaks, every hit clears a whole gap in
one throw. The moment it breaks, the search is handed to the last egg, which can
never be thrown above a floor that has not already been cleared, so every throw
from then on clears exactly one floor. A hit costs one throw and nothing else. A
miss costs one throw plus the entire crawl that follows it — up to $s - 1$
throws for the fixed stride, up to $k - 1$ for the growing one — because it
demotes the search from gap-sized steps to floor-sized steps. Both strategies
are built around that asymmetry: delay the first miss as long as the eggs allow,
and arrange for the second miss, or its inference, to land exactly on $F$.

The same asymmetry explains why restricting misses degrades the search from
logarithmic to polynomial. Suppose the uncertain range holds $g$ floors and ask
what one throw is worth. With unlimited eggs you throw at the midpoint, and
whether it hits or misses, half the range is gone: $g$ becomes $g/2$. Halving
repeatedly reaches a single floor in about $\lg g$ throws. With the last egg you
are forced to throw from the lowest uncleared floor, and a hit says only that it
is not this one: $g$ becomes $g - 1$. Subtracting repeatedly reaches a single
floor in $g - 1$ throws. A throw is worth a halving when breaking is affordable
and worth one floor when it is not.

Two eggs sit between the two. The first egg buys exactly one run of big steps —
a sequence of hits that each clear a whole gap, ending in a single miss — but
because that miss hands the search to the last egg, every gap it skips has to be
small enough for the last egg to crawl. The two phases are tied: after $k$ jumps
the gaps can be no bigger than about $k$, so $k$ throws cover about $k^2/2$
floors, and reaching $F$ takes about $\sqrt{2F}$ of them. Unlimited eggs have no
such tie, since a miss costs nothing beyond its own throw, so the range shrinks
geometrically and the count is $\lg F$. Halving against subtracting, with one
burst of big steps squeezed in before the subtracting begins, is the whole of
the exponential-to-polynomial gap.

In short, binary search is fast but spends eggs freely; the two-egg strategy is
frugal with eggs and pays in throws. The exercise's cost model counts only
throws, which is why the two-egg answer looks worse on paper: the egg budget is
a constraint, not a cost. This settles the third and fourth guiding questions.

### Practical notes

- **Simulate with a hard egg budget:** the harness should _throw an exception_
  (fittingly) if a third egg breaks — that catches subtle strategy bugs no
  averaging will.
- **Sweep every $F$** from $1$ to $N$ for small $N$ and record worst-case
  throws; check the $2\sqrt{N}$ bound exactly and fit the constant $c$ in
  $c\sqrt{F}$ for the adaptive version.
- **Triangular-number bookkeeping** (positions $1, 3, 6, 10, \ldots$) invites
  off-by-ones — pin down whether a jump position that breaks the egg leaves a
  crawl range that includes or excludes the previous jump position.

<br />
<br />
