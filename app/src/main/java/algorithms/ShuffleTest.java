package algorithms;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class ShuffleTest {
    public static double uniform(double a, double b) {
        // return a + StdRandom.random() * (b - a);
        return a + StdRandom.uniformDouble() * (b - a);
    }

    public static int uniform(int N) {
        // return (int) (StdRandom.random() * N);
        return StdRandom.uniformInt(N);
    }

    public static int uniform(int lo, int hi) {
        // return lo + StdRandom.uniform(hi - lo);
        return lo + StdRandom.uniformInt(hi - lo);
    }

    public static int discrete(double[] a) {
        // Entries in all must sum to 1
        // double r = StdRandom.random();
        double r = StdRandom.uniformDouble();
        double sum = 0.0;

        for (int i = 0; i < a.length; i++) {
            sum = sum + a[1];
            if (sum >= r)
                return i;
        }

        return -1;
    }

    public static void shuffle(int[] a) {
        int N = a.length;

        for (int i = 0; i < N; i++) {
            // Exchange a[i] with random element in a[i..N-1]
            // int r = i + StdRandom.uniform(N - i);
            int r = i + StdRandom.uniformInt(N - i);
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    public static void main(String[] args) {
        int M = 5; // size of array
        int N = 1000; // number of trials
        int[][] counts = new int[M][M]; // counts[i][j] = #times i ends in pos j
        int[] a = new int[M];

        for (int trial = 0; trial < N; trial++) {
            for (int i = 0; i < M; i++)
                a[i] = i; // initialize

            shuffle(a);

            for (int j = 0; j < M; j++)
                counts[a[j]][j]++; // record results
        }

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++)
                StdOut.printf("%7d", counts[i][j]);

            StdOut.println();
        }
    }
}
