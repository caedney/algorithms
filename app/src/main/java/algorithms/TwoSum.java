package algorithms;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class TwoSum {
    public static int count(int[] a) {
        Arrays.sort(a);
        int N = a.length;
        int cnt = 0;

        for (int i = 0; i < N; i++)
            for (int j = i + 1; j < N; j++)
                if (a[i] + a[j] == 0)
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
