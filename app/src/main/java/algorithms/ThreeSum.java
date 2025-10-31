package algorithms;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Three-sum. Write a program that, given an array of N integers, finds all
 * triples that sum to zero.
 * 
 * <pre>
 * ./gradlew run -PmainClass=ThreeSum --args="src/data/algs4/1Kints.txt"
 * </pre>
 */
public class ThreeSum {
    public static int count(int[] a) {
        int N = a.length;
        int cnt = 0;

        for (int i = 0; i < N; i++)
            for (int j = i + 1; j < N; j++)
                for (int k = j + 1; k < N; k++)
                    if (a[i] + a[j] + a[k] == 0)
                        cnt++;

        return cnt;
    }

    public static void main(String[] args) {
        int[] ints = new In(args[0]).readAllInts();
        Stopwatch timer = new Stopwatch();
        int cnt = count(ints);
        double time = timer.elapsedTime();
        StdOut.println(cnt + " in " + time + " seconds");
    }
}
