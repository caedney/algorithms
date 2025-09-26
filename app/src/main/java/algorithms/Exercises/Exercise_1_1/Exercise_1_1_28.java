package algorithms.Exercises.Exercise_1_1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.28
 * 
 * <p>
 * <i>Remove duplicates</i>. Modify the test client in {@code BinarySearch} to
 * remove any duplicate keys in the whitelist after the sort.
 * </p>
 */
public class Exercise_1_1_28 {
    public static int rank(int key, int[] array) {
        int lo = 0;
        int hi = array.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (key < array[mid])
                hi = mid - 1;
            else if (key > array[mid])
                lo = mid + 1;
            else
                return mid;
        }

        return -1;
    }

    public static void main(String[] args) throws IOException {
        System.setIn(new FileInputStream("src/data/algs4/tinyT.txt"));
        In in = new In("src/data/algs4/tinyW.txt");

        int[] whitelist = in.readAllInts();
        Arrays.sort(whitelist);

        Set<Integer> set = new LinkedHashSet<>();
        for (int num : whitelist)
            set.add(num);

        StdOut.println("Set " + set);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();

            if (rank(key, whitelist) < 0)
                StdOut.println(key);
        }
    }
}
