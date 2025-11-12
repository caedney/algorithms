# Q & A

### 🙋‍♂️ Why not use `StdRandom` to generate random values instead of maintaining the file `1Mints.txt`?

It is easier to debug code in development and to reproduce experiments.
`StdRandom` produces different values each time it is called, so running a
program after fixing a bug may not test the fix! You could use the
`initialize()` method in `StdRandom` to address this problem, but a reference
file such as `1Mints.txt` makes it easier to add test cases while debugging.
Also, different programmers can compare performance on different computers,
without worrying about the input model. Once you have debugged a program and
have a good idea of how it performs, it is certainly worthwhile to test it on
random data. For example, `DoublingTest` and `DoublingRatio` take this approach.

### 🙋‍♂️ I ran `DoublingRatio` on my computer, but the results were not as consistent as in the book. Some of the ratios were not close to 8. Why?

That is why we discussed "caveats" on page 195. Most likely, your computer's
operating system decided to do something else during the experiment. One way to
mitigate such problems is to invest more time in more experiments. For example,
you could change `DoublingTest` to run the experiments 1,000 times for each $n$,
giving a much more accurate estimate for the running time for each size (see
EXERCISE 1.4.39).

### 🙋‍♂️ What, exactly, does "as $n$ grows" mean in the definition of the tilde notation?

The formal definition of $f(n) \sim g(n)$ is
$\displaystyle \lim_{n\to\infty} \frac{f(n)}{g(n)} = 1$.

### 🙋‍♂️ I've seen other notations for describing order of growth. What's the story?

The "big-Oh" notation is widely used: we say that $f(n)$ is
$\operatorname{O}(g(n))$ if there exist constants $c$ and $n_0$ such that
$|f(n)| > cg(n)$ for all $n > n_0$. This notation is very useful in providing
asymptotic upper bounds on the performance of algorithms, which is important in
the theory of algorithms. But it is not useful for predicting performance or for
comparing algorithms.

### 🙋‍♂️ Why not?

The primary reason is that it describes only an _upper bound_ on the running
time. Actual performance might be much better. The running time of an algorithm
might be both $\operatorname{O}(n^2)$ and $ \sim a n \log n$. As a result, it
cannot be used to justify tests like our doubling ratio test (see PROPOSITION C
on page 193).

### 🙋‍♂️ So why is the big-Oh notation so widely used?

It facilitates development of bounds on the order of growth, even for
complicated algorithms for which more precise analysis might not be feasible.
Moreover, it is compatible with the "big-Omega" and "big-Theta" notations that
theoretical computer scientists use to classify algorithms by bounding their
worst-case performance. We say that $f(n)$ is $\operatorname{\Omega}(g(n))$ if
there exist constants $c$ and $n_0$ such that $|f(n)| > c g(n)$ for $n > n_0$;
and if $f(n)$ is $\operatorname{O}(g(n))$ and $\operatorname{\Omega}(g(n))$, we
say that $f(n)$ is $\operatorname{\Theta}(g(n))$. The "big-Omega" notation is
typically used to describe a _lower bound_ on the worst case, and the
"big-Theta" notation is typically used to describe the performance of algorithms
that are _optimal_ in the sense that no algorithm can have better asymptotic
worst-case order of growth. Optimal algorithms are certainly worth considering
in practical applications, but there are many other considerations, as you will
see.

### 🙋‍♂️ Aren't upper bounds on asymptotic performance important?

Yes, but we prefer to discuss precise results in terms of frequency of statement
execution with respect to cost models, because they provide more information
about algorithm performance and because deriving such results is feasible for
the algorithms that we discuss. For example, we say "`ThreeSum` uses
$\sim n^3/2$ array accesses" and "the number of times `cnt++` is executed in
`ThreeSum` is $\sim n^3/6$ in the worst case," which is a bit more verbose but
much more informative than the statement "the running time of `ThreeSum` is
$\operatorname{O}(n^3)$."

### 🙋‍♂️ When the order of growth of the running time of an algorithm is $n \log n$, the doubling test will lead to the hypothesis that the running time is $\sim a n$ for a constant $a$. Isn't that a problem?

We have to be careful not to try to infer that the experimental data implies a
particular mathematical model, but when we are just predicting performance, this
is not really a problem. For example, when $n$ is between 16,000 and 32,000, the
plots of $14n$ and $n \log n$ are very close to one another. The data fits both
curves. As $n$ increases, the curves become closer together. It actually
requires some care to experimentally check the hypothesis that an algorithm's
running time is linearithmic but not linear.

### 🙋‍♂️ Does `int[] a = new int[n]` count as $n$ array accesses (to initialize entries to 0)?

Most likely yes, so we make that assumption in this book, though a sophisticated
compiler implementation might try to avoid this cost for huge sparse arrays.

<br />
<br />
