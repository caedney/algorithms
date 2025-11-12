package algorithms.Exercises.Exercise_1_4;

import algorithms.Stopwatch;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * Exercise 1.4.2
 * 
 * <p>
 * Modify {@code ThreeSum} to work properly even when the int values are so
 * large that adding two of them might cause overflow.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_2 --args="src/data/algs4/1Kints.txt"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_2 {
    public static int count(int[] a) {
        int N = a.length;
        int cnt = 0;

        for (int i = 0; i < N; i++)
            for (int j = i + 1; j < N; j++)
                for (int k = j + 1; k < N; k++)
                    if ((long) a[i] + a[j] + a[k] == 0)
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
