package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.18
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
 * 
 */
public class Excercise_1_1_18 {
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

    public static void main() {
        StdOut.println("Excercise 1.1.18");

        int multiply_result_1 = multiply(2, 25);
        // mystery(2, 25)
        // mystery(4, 12) + 2
        // mystery(8, 6);
        // mystery(16, 3);
        // mystery(32, 1) + 16
        // mystery(64, 0) + 32
        // 0
        StdOut.println(multiply_result_1); // 50
        int multiply_result_2 = multiply(3, 11);
        // mystery(3, 11);
        // mystery(6, 5) + 3
        // mystery(12, 2) + 6
        // mystery(24, 1)
        // mystery(48, 0) + 24
        // 0
        StdOut.println(multiply_result_2); // 33

        int power_result_1 = power(2, 25);
        // mystery_alt(2, 25)
        // mystery_alt(4, 12) * 2
        // mystery_alt(16, 6)
        // mystery_alt(256, 3)
        // mystery_alt(65536, 1) * 256
        // mystery_alt(4294967296, 0) * 65536
        // 1
        StdOut.println(power_result_1); // 33554432
        int power_result_2 = power(3, 11);
        // mystery_alt(3, 11)
        // mystery_alt(9, 5) * 3
        // mystery_alt(81, 2) * 9
        // mystery_alt(6561, 1)
        // mystery_alt(43046721, 0) * 6561
        // 1
        StdOut.println(power_result_2); // 177147
    }
}
