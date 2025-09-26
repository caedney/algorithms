package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.16
 * 
 * <p>
 * Give the value of {@code exR1(6)}:
 * </p>
 * 
 * {@snippet :
 * public static String exR1(int n) {
 *     if (n <= 0)
 *         return "";
 * 
 *     return exR1(n - 3) + n + exR1(n - 2) + n;
 * }
 * }
 */
public class Exercise_1_1_16 {
    public static String exR1(int n) {
        if (n <= 0)
            return "";

        return exR1(n - 3) + n + exR1(n - 2) + n;
    }

    public static void main(String[] args) {
        String result = exR1(6);
        // (("")3(("")1("")1)3)6((("")1("")1)4(("")2("")2)4)6
        StdOut.println(result); // 311361142246

        result = exR1(3);
        // ("")3(("")1("")1)3
        StdOut.println(result); // 3113
    }
}
