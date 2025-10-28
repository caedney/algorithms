package algorithms;

public class MathUtils {
    public static int getAbsoluteNumber(int x) {
        if (x < 0)
            return -x;
        else
            return x;
    }

    public static double getAbsoluteNumber(double x) {
        if (x < 0.0)
            return -x;
        else
            return x;
    }

    public static boolean isPrimeNumber(int N) {
        if (N < 2)
            return false;

        for (int i = 2; i * i <= N; i++) {
            if (N % i == 0)
                return false;
        }

        return true;
    }

    public static int square(int x) {
        return x * x;
    }

    public static double getSquareRoot(double c) {
        if (c < 0)
            return Double.NaN;

        double err = 1e-15;
        double t = c;

        while (Math.abs(t - c / t) > err * t)
            t = (c / t + t) / 2.0;

        return t;
    }

    public static int lg(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("N must be positive");

        int result = 0;

        while (N > 1) {
            N = N / 2;
            result++;
        }

        return result;
    }

    public static long fib(int N, long[] memo) {
        if (N == 0)
            return 0;

        if (N == 1)
            return 1;

        if (memo[N] != 0)
            return memo[N];

        memo[N] = fib(N - 1, memo) + fib(N - 2, memo);

        return memo[N];
    }

    /**
     * Computes ln(N!) recursively.
     * 
     * @param n nonnegative integer
     * @return natural log of N! (ln(N!))
     * @throws IllegalArgumentException if n < 0
     */
    public static double logFactorial(int n) {
        if (n < 0)
            throw new IllegalArgumentException("n must be >= 0");

        if (n <= 1)
            return 0.0; // ln(0!) = ln(1!) = 0

        return Math.log(n) + logFactorial(n - 1);
    }

    /**
     * Returns the binomial coefficient {@code C(n, k)} — the number of ways to
     * choose {@code k} elements from a set of {@code n} elements, disregarding
     * order.
     *
     * <p>
     * Computed with the multiplicative formula rather than factorials, so
     * intermediate values never overflow unless the result itself exceeds
     * {@code Long.MAX_VALUE}:
     *
     * <pre>
     * C(n, k) = n! / (k! (n-k)!)  =  &prod;<sub>i=1..k</sub> (n - k + i) / i
     * </pre>
     *
     * <p>
     * The symmetry {@code C(n, k) == C(n, n-k)} is used to minimize the number of
     * iterations. Each intermediate division is exact, because after {@code i}
     * steps the running product is itself a binomial coefficient
     * {@code C(n-k+i, i)} and therefore an integer.
     *
     * <p>
     * Out-of-range arguments ({@code k < 0} or {@code k > n}) return 0 by
     * convention, since there are no ways to make such a selection.
     *
     * <p>
     * <b>Note:</b> results are not guarded against overflow. The result exceeds
     * {@code Long.MAX_VALUE} for inputs as small as {@code C(67, 33)}; for
     * {@code k = 2} it is safe for all {@code n} up to ~4.3 billion.
     *
     * @param n the size of the set; must be non-negative
     * @param k the number of elements to choose
     * @return the number of {@code k}-element subsets of an {@code n}-element set,
     *         or 0 if {@code k < 0} or {@code k > n}
     */
    public static long binom(long n, long k) {
        if (k < 0 || k > n)
            return 0;

        k = Math.min(k, n - k); // C(n, k) == C(n, n-k); use the smaller
        long result = 1;

        for (long i = 1; i <= k; i++)
            result = result * (n - k + i) / i; // always divides exactly

        return result;
    }
}