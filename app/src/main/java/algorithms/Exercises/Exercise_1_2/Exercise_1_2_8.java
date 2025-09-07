package algorithms.Exercises.Exercise_1_2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.8
 * 
 * <p>
 * Suppose that {@code a[]} and {@code b[]} are each integer arrays consisting
 * of millions of integers. What does the follow code do? Is it reasonably
 * efficient?
 * </p>
 * 
 * {@code int[] t = a; a = b; b = t; }
 * 
 * <p>
 * <i>Answer</i>: It swaps them. It could hardly be more efficient because it
 * does so by copying references, so that it is not necessary to copy millions
 * of elements.
 * </p>
 */
public class Exercise_1_2_8 {
    private static void printArray(String name, int[] arr) {
        StdOut.print(name + "[");

        for (int i = 0; i < arr.length; i++) {
            StdOut.print(arr[i]);
            if (i < arr.length - 1)
                StdOut.print(", ");
        }

        StdOut.println("]");
    }

    public static void main(String[] args) {
        int[] a = { 1, 2, 3 };
        int[] b = { 9, 8, 7 };

        StdOut.println("Before swap:");
        printArray("a = ", a);
        printArray("b = ", b);

        int[] t = a;
        a = b;
        b = t;

        StdOut.println("\nAfter swap:");
        printArray("a = ", a);
        printArray("b = ", b);
    }
}
