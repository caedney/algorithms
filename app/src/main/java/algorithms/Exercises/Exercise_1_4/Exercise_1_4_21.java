package algorithms.Exercises.Exercise_1_4;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * Exercise 1.4.21
 * 
 * <p>
 * <i>Binary search on distinct values</i>. Develop an implementation of binary
 * search for <code>StaticSETofInts</code> (see page 98) where the running time
 * of <code>contains()</code> is guaranteed to be ~lg 𝑅, where 𝑅 is the number
 * of different integers in the array given as argument to the constructor.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_21
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_21 {
    private int[] a;

    public String toString() {
        return Arrays.toString(a);
    }

    public Exercise_1_4_21(int[] keys) {
        a = keys.clone();
        Arrays.sort(a);
        a = removeDuplicates(a);
    }

    private static int[] removeDuplicates(int[] sorted) {
        int r = 0;

        for (int i = 0; i < sorted.length; i++)
            if (i == 0 || sorted[i] != sorted[i - 1])
                sorted[r++] = sorted[i];

        return Arrays.copyOf(sorted, r);
    }

    public boolean contains(int key) {
        return rank(key) != -1;
    }

    public int rank(int key) {
        int lo = 0;
        int hi = a.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (key < a[mid])
                hi = mid - 1;
            else if (key > a[mid])
                lo = mid + 1;
            else
                return mid;
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] keys = new int[] { 5, 9, 5, 5, 2, 9, 5, 2, 5, 9, 2, 5 };
        Exercise_1_4_21 set = new Exercise_1_4_21(keys);
        StdOut.println(set.contains(5)); // true
        StdOut.println(set.contains(9)); // true
        StdOut.println(set.contains(2)); // true
        StdOut.println(set.contains(1)); // false
    }
}
