package algorithms.Exercises.Exercise_1_4;

/**
 * Exercise 1.4.6
 * 
 * <p>
 * Give the order of growth (as a function of <i>N</i>) of the running times of
 * each of the following code fragments:
 * </p>
 * 
 * <ol>
 * <li>
 * 
 * <pre>
 * <code>
 *int sum = 0;
 *for (int n = N; n > 0; n /= 2)
 *    for (int i = 0; i < n; i++)
 *        sum++;
 * </code>
 * </pre>
 * 
 * </li>
 * <li>
 * 
 * <pre>
 * <code>
 *int sum = 0;
 *for (int i = 1; i < N; i *= 2)
 *    for (int j = 0; j < i; j++)
 *        sum++;
 * </code>
 * </pre>
 * 
 * </li>
 * <li>
 * 
 * <pre>
 * <code>
 *int sum = 0;
 *for (int i = 1; i < N; i *= 2)
 *    for (int j = 0; j < N; j++)
 *        sum++;
 * </code>
 * </pre>
 * 
 * </li>
 * </ol>
 */
public class Exercise_1_4_6 {
    public static void main(String[] args) {
    }
}
