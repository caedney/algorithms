package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.6
 * 
 * <p>
 * What does the following program print?
 * </p>
 * 
 * {@snippet :
 * int f = 0;
 * int g = 1;
 * for (int i = 0; i <= 15; i++) {
 *     StdOut.println(f);
 *     f = f + g;
 *     g = f - g;
 * }
 * }
 */
public class Exercise_1_1_6 {
    private static int fibonacci(int N) {
        int f = 0;
        int g = 1;

        for (int i = 0; i <= N; i++) {
            f = f + g;
            g = f - g;
        }

        return g;
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 10; i += 1)
            StdOut.print(fibonacci(i) + " "); // 0 1 1 2 3 5 8 13 21 34 55
    }
}
