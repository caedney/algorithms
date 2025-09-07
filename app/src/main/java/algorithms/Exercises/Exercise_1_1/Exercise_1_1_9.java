package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.9
 * 
 * <p>
 * Write a code fragment that puts the binary representation of a positive
 * integer {@code N} into a {@code String s}.
 * </p>
 * 
 * <p>
 * <i>Solution</i>: Java has a built-in method {@code Integer.toBinaryString(N)}
 * for this job, but the point of the exercise is to see how such a method might
 * be implemented. Here is a particularly concise solution:
 * </p>
 * 
 * {@snippet :
 * String s = "";
 * for (int n = N; n > 0; n /= 2)
 *     s = (n % 2) + s;
 * }
 */
public class Exercise_1_1_9 {
    private static String toBinaryString(int N) {
        if (N < 0)
            N = -N;

        String s = "";
        for (int n = N; n > 0; n /= 2)
            s = (n % 2) + s;

        return s;
    }

    public static void main(String[] args) {
        String s = toBinaryString(-12);
        StdOut.println(s); // 1100
    }
}
