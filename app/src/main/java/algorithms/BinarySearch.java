package algorithms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BinarySearch {
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

    public static void main(String[] args) throws FileNotFoundException {
        System.setIn(new FileInputStream("src/data/algs4/tinyT.txt"));
        In in = new In("src/data/algs4/tinyW.txt");

        int[] whitelist = in.readAllInts();
        Arrays.sort(whitelist);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();

            if (rank(key, whitelist) < 0)
                StdOut.println(key);
        }
    }
}