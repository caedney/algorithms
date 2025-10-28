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

        while (Math.abs(t - c / t) > err * t) {
            t = (c / t + t) / 2.0;
        }

        return t;
    }

    public static int lg(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be positive");
        }

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

        if (memo[N] != 0) {
            return memo[N];
        }

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
}