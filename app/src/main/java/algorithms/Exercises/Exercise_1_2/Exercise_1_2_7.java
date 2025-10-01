package algorithms.Exercises.Exercise_1_2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.7
 * 
 * <p>
 * What does the following recursive function return?
 * </p>
 * 
 * {@snippet :
 * public static String mystery(String s) {
 *     int N = s.length();
 *     if (N <= 1)
 *         return s;
 *     String a = s.substring(0, N / 2);
 *     String b = s.substring(N / 2, N);
 *     return mystery(b) + mystery(a);
 * }
 * }
 */
public class Exercise_1_2_7 {
    private static String mystery(String s) {
        int N = s.length();

        if (N <= 1)
            return s;

        String a = s.substring(0, N / 2);
        String b = s.substring(N / 2, N);

        return mystery(b) + mystery(a);
    }

    public static void main(String[] args) {
        String reversed = mystery("The quick brown fox");
        StdOut.println(reversed);
    }
}
