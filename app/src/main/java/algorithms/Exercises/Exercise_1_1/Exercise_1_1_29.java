package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.29
 * 
 * <p>
 * <i>Equal keys</i>. Add to {@code BinarySearch} a static method {@code rank()}
 * that takes a key and a sorted array of {@code int} values (some of which may
 * be equal) as arguments and returns the number of elements that are smaller
 * than the key and a similar method {@code count}) that returns the number of
 * elements equal to the key. <i>Note</i>: If i and j are the values returned by
 * {@code rank(key, a)} and {@code count(key, a)} respectively, then a [i
 * ..i+j-1] are the values in the array that are equal to key.
 * </p>
 */
public class Exercise_1_1_29 {
    public static int rank(int key, int[] array) {
        int lo = 0;
        int hi = array.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (key <= array[mid])
                hi = mid - 1;
            else
                lo = mid + 1;

            StdOut.println("lo: " + lo);
            StdOut.println("hi: " + hi);
            StdOut.println(" ");
        }

        return lo;
    }

    public static int count(int key, int[] array) {
        StdOut.println("first occurrence of key or where it would be");
        int i = rank(key, array);
        StdOut.println("first element strictly greater than key");
        int j = rank(key + 1, array);

        return j - i;
    }

    public static void main(String[] args) {
        int key = 16;
        int[] array = { 1, 4, 7, 12, 15, 16, 16, 16, 18, 23, 29, 34, 39, 42 };

        int i = rank(key, array);
        int j = count(key, array);

        System.out.println("numbers less than key: " + i);
        System.out.println("numbers mathching key: " + j);
    }
}
