package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.26
 * 
 * <p>
 * <i>Sorting three numbers</i>. Suppose that the variables {@code a},
 * {@code b}, {@code c}, and {@code t} are all of the same numeric primitive
 * type. Show that the following code puts {@code a}, {@code b}, and {@code c}
 * in ascending order:
 * </p>
 * 
 * {@snippet :
 * if (a > b) {
 *     t = a;
 *     a = b;
 *     b = t;
 * }
 * 
 * if (a > c) {
 *     t = a;
 *     a = c;
 *     c = t;
 * }
 * 
 * if (b > c) {
 *     t = b;
 *     b = c;
 *     c = t;
 * }
 * }
 */
public class Exercise_1_1_26 {
    private static void sortNumbers(int a, int b, int c) {
        int t;

        if (a > b) {
            t = a;
            a = b;
            b = t;
        }

        if (a > c) {
            t = a;
            a = c;
            c = t;
        }

        if (b > c) {
            t = b;
            b = c;
            c = t;
        }

        StdOut.printf("%d %d %d\n", a, b, c);
    }

    public static void main(String[] args) {
        sortNumbers(5, 12, 3); // 3, 5, 12
    }
}
