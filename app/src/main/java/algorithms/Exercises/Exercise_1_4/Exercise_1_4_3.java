package algorithms.Exercises.Exercise_1_4;

import algorithms.Stopwatch;
import algorithms.ThreeSum;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/******************************************************************************
 * Exercise 1.4.3
 *
 * <p>
 * Modify {@code DoublingTest} to use {@code StdDraw} to produce standard and
 * log-log plots, rescaling as necessary so the plots always fill a substantial
 * portion of the window.
 * </p>
 *
 * <p>
 * Standard plot on the left half, log-log plot on the right half. After every
 * trial the window is cleared and all points are redrawn, normalized to the
 * data seen so far — that's the "rescaling".
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_3
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_3 {
    private static final int MAX_TRIALS = 12;

    public static double timeTrial(int N) {
        int MAX = 1000000;
        int[] a = new int[N];

        for (int i = 0; i < N; i++)
            a[i] = StdRandom.uniformInt(-MAX, MAX);

        Stopwatch timer = new Stopwatch();
        ThreeSum.count(a);

        return timer.elapsedTime();
    }

    private static void redraw(double[] x, double[] y, int n) {
        StdDraw.clear();
        drawStandard(x, y, n, 0.06, 0.46); // left half
        drawLogLog(x, y, n, 0.56, 0.96); // right half
        StdDraw.show();
    }

    private static void drawStandard(double[] x, double[] y, int n, double lo, double hi) {
        double maxX = x[n - 1];
        double maxY = 0;

        for (int i = 0; i < n; i++)
            maxY = Math.max(maxY, y[i]);

        if (maxY == 0)
            maxY = 1e-6; // all times still below timer resolution

        StdDraw.setPenRadius(0.01);

        for (int i = 0; i < n; i++) {
            double px = lo + (hi - lo) * (x[i] / maxX);
            double py = 0.10 + 0.80 * (y[i] / maxY);
            StdDraw.point(px, py);
        }

        StdDraw.setPenRadius();
        StdDraw.text((lo + hi) / 2, 0.03, "standard plot (N vs T)");
    }

    private static void drawLogLog(double[] x, double[] y, int n, double lo, double hi) {
        // lg is undefined for zero times, so use only positive ones
        double minLgY = Double.POSITIVE_INFINITY;
        double maxLgY = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < n; i++) {
            if (y[i] <= 0)
                continue;

            minLgY = Math.min(minLgY, lg(y[i]));
            maxLgY = Math.max(maxLgY, lg(y[i]));
        }

        if (minLgY == Double.POSITIVE_INFINITY)
            return; // no positive times yet

        if (maxLgY == minLgY)
            maxLgY = minLgY + 1; // avoid zero range

        double minLgX = lg(x[0]);
        double maxLgX = lg(x[n - 1]);

        if (maxLgX == minLgX)
            maxLgX = minLgX + 1;

        StdDraw.setPenRadius(0.01);

        for (int i = 0; i < n; i++) {
            if (y[i] <= 0)
                continue;

            double px = lo + (hi - lo) * ((lg(x[i]) - minLgX) / (maxLgX - minLgX));
            double py = 0.10 + 0.80 * ((lg(y[i]) - minLgY) / (maxLgY - minLgY));

            StdDraw.point(px, py);
        }

        StdDraw.setPenRadius();
        StdDraw.text((lo + hi) / 2, 0.03, "log-log plot (lg N vs lg T)");
    }

    private static double lg(double v) {
        return Math.log(v) / Math.log(2);
    }

    public static void main(String[] args) {
        double[] sizes = new double[MAX_TRIALS];
        double[] times = new double[MAX_TRIALS];

        StdDraw.setCanvasSize(1000, 500);
        StdDraw.setXscale(0, 1); // work in the unit square;
        StdDraw.setYscale(0, 1); // points are normalized manually
        StdDraw.enableDoubleBuffering();

        int N = 250;

        for (int t = 0; t < MAX_TRIALS; t++, N += N) {
            sizes[t] = N;
            times[t] = timeTrial(N);
            StdOut.printf("%7d %7.2f%n", N, times[t]);
            redraw(sizes, times, t + 1);
        }
    }
}
