package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.18
 * 
 * <p>
 * Consider the following recursive function:
 * </p>
 * 
 * {@snippet :
 * public static int mystery(int a, int b) {
 *     if (b == 0)
 *         return 0;
 *     if (b % 2 == 0)
 *         return mystery(a + a, b / 2);
 *     return mystery(a + a, b / 2) + a;
 * }
 * }
 * 
 * <p>
 * What are the values of {@code mystery(2, 25)} and {@code mystery(3, 11)}?
 * Given positive integers {@code a} and {@code b}, describe what value
 * {@code mystery(a, b)} computes. Answer the same question, but replace + with
 * * and replace {@code return 0} with {@code return 1}.
 * </p>
 */
public class Exercise_1_1_18 {
    public static int multiply(int a, int b) {
        if (b == 0)
            return 0;

        if (b % 2 == 0)
            return multiply(a + a, b / 2);

        return multiply(a + a, b / 2) + a;
    }

    public static int power(int a, int b) {
        if (b == 0)
            return 1;

        if (b % 2 == 0)
            return power(a * a, b / 2);

        return power(a * a, b / 2) * a;
    }

    public static void main(String[] args) {
        int result = multiply(2, 25);
        // multiply(2, 25)
        // multiply(4, 12) + 2
        // multiply(8, 6);
        // multiply(16, 3);
        // multiply(32, 1) + 16
        // multiply(64, 0) + 32
        // 0
        StdOut.println(result); // 50

        result = multiply(3, 11);
        // multiply(3, 11);
        // multiply(6, 5) + 3
        // multiply(12, 2) + 6
        // multiply(24, 1)
        // multiply(48, 0) + 24
        // 0
        StdOut.println(result); // 33

        result = power(2, 25);
        // power(2, 25)
        // power(4, 12) * 2
        // power(16, 6)
        // power(256, 3)
        // power(65536, 1) * 256
        // power(4294967296, 0) * 65536
        // 1
        StdOut.println(result); // 33554432

        result = power(3, 11);
        // power(3, 11)
        // power(9, 5) * 3
        // power(81, 2) * 9
        // power(6561, 1)
        // power(43046721, 0) * 6561
        // 1
        StdOut.println(result); // 177147
    }
}
