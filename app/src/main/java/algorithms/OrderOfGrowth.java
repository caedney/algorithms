package algorithms;

import java.lang.reflect.Method;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/******************************************************************************
 * Order of growth
 *
 * <p>
 * Reusable doubling-ratio harness. Runs a doubling test against any class in
 * this package that has a static <code>count(int[])</code> method, found by
 * reflection from the class name given as the first argument — no edits to this
 * file are needed to benchmark a new algorithm. The optional second argument
 * sets the starting problem size (default 250); choose it so the first rows
 * take a measurable fraction of a second, e.g. 250 for cubic algorithms, 8000
 * for quadratic ones. The ratio column converging on 2ᵇ indicates order of
 * growth Nᵇ: 8 for cubic, 4 for quadratic, 2 for linear. Early rows are
 * unreliable (JIT warm-up, timer granularity) — trust the ratios once times
 * exceed a few tenths of a second.
 * </p>
 *
 * <p>
 * The value range scales with N (never below ±1,000,000) so that the input
 * distribution stays comparable as the problem doubles. With a fixed range,
 * large N floods the array with duplicate values, which quietly changes the
 * cost being measured — e.g. binary searches start terminating early on runs of
 * equal keys, dragging an N log N algorithm's ratio down to 2.
 * </p>
 *
 * <pre>
 * ./gradlew run -PmainClass=OrderOfGrowth --args="ThreeSum 250"
 * ./gradlew run -PmainClass=OrderOfGrowth --args="ThreeSumFast 4000"
 * ./gradlew run -PmainClass=OrderOfGrowth --args="ThreeSumFaster 8000"
 * ./gradlew run -PmainClass=OrderOfGrowth --args="TwoSum 32000"
 * ./gradlew run -PmainClass=OrderOfGrowth --args="TwoSumFast 2000000"
 * ./gradlew run -PmainClass=OrderOfGrowth --args="TwoSumFaster 4000000"
 * </pre>
 ******************************************************************************/
public class OrderOfGrowth {
    public static double timeTrial(Method count, int N) throws Exception {
        int MAX = Math.max(N, 1000000);
        int[] a = new int[N];

        for (int i = 0; i < N; i++)
            a[i] = StdRandom.uniformInt(-MAX, MAX);

        Stopwatch timer = new Stopwatch();
        count.invoke(null, (Object) a); // null receiver: count is static
        return timer.elapsedTime();
    }

    public static void main(String[] args) throws Exception {
        Class<?> algorithm = Class.forName("algorithms." + args[0]);
        Method count = algorithm.getMethod("count", int[].class);
        int start = args.length > 1 ? Integer.parseInt(args[1]) : 250;

        double prev = timeTrial(count, start / 2);

        for (int N = start; true; N += N) {
            double time = timeTrial(count, N);
            StdOut.printf("%8d %9.2f %5.1f%n", N, time, time / prev);
            prev = time;
        }
    }
}
