package algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import edu.princeton.cs.algs4.StdOut;

public class Memoizer {
    public static <T, R> java.util.function.Function<T, R> memoizer(
            BiFunction<java.util.function.Function<T, R>, T, R> formula) {

        Map<T, R> cache = new HashMap<>();

        java.util.function.Function<T, R> recur = new java.util.function.Function<T, R>() {
            @Override
            public R apply(T n) {
                return cache.computeIfAbsent(n, key -> formula.apply(this, key));
            }
        };

        return recur;
    }

    public static void main(String[] args) {
        java.util.function.Function<Integer, Integer> fibonacci = memoizer((recur, n) -> {
            if (n < 2)
                return n;

            return recur.apply(n - 1) + recur.apply(n - 2);
        });

        for (int i = 0; i < 50; i++) {
            StdOut.println("fib(" + i + ") = " + fibonacci.apply(i));
        }
    }
}
