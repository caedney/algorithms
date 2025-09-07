package algorithms.Exercises.Exercise_1_1;

/**
 * Exercise 1.1.17
 * 
 * <p>
 * Criticize the following recursive function:
 * </p>
 * 
 * {@snippet :
 * public static String exR2(int n) {
 *     String s = exR2(n - 3) + n + exR2(n - 2) + n;
 *     if (n <= 0)
 *         return "";
 *     return s;
 * }
 * }
 * 
 * <p>
 * <i>Answer</i>: The base case will never be reached. A call to {@code exR2(3)}
 * will result in calls to {@code exR2(0)}, {@code exR2 (-3)}, {@code exR3(-6)},
 * and so forth until a {@code StackOverflowError} occurs.
 * </p>
 */
public class Exercise_1_1_17 {
    public static String exR2(int n) {
        String s = exR2(n - 3) + n + exR2(n - 2) + n;

        if (n <= 0)
            return "";

        return s;
    }

    public static void main(String[] args) {
    }
}
