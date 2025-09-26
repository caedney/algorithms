package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.12
 * 
 * <p>
 * What does the following code fragment print?
 * </p>
 * 
 * {@snippet :
 * int[] a = new int[10];
 * for (int i = 0; i < 10; i++)
 *     a[i] = 9 - i;
 * for (int i = 0; i < 10; i++)
 *     a[i] = a[a[i]];
 * for (int i = 0; i < 10; i++)
 *     StdOut.println(i);
 * }
 */
public class Exercise_1_1_12 {
    public static void main(String[] args) {
        int[] a = new int[10];

        for (int i = 0; i < 10; i++)
            a[i] = 9 - i; // a = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]

        for (int i = 0; i < 10; i++)
            a[i] = a[a[i]]; // a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

        for (int i = 0; i < 10; i++)
            StdOut.println(i); // a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
    }
}
