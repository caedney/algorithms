package algorithms.Exercises.Exercise_1_1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

import algorithms.BinarySearch;
import algorithms.BruteForceSearch;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.38
 * 
 * <p>
 * <i>Binary search versus brute-force search</i>. Write a program
 * {@code BruteForceSearch} that uses the brute-force search method given on
 * page 48 and compare its running time on your computer with that of {@code
 * BinarySearch} for {@code largeW.txt} and {@code largeT.txt}.
 * </p>
 */
public class Exercise_1_1_38 {
    public static void binarySearch() throws FileNotFoundException {
        System.setIn(new FileInputStream("src/data/algs4/largeW.txt"));
        In in = new In("src/data/algs4/largeT.txt");

        int[] whitelist = in.readAllInts();
        Arrays.sort(whitelist);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();

            if (BinarySearch.rank(key, whitelist) > -1)
                StdOut.println(key);
        }
    }

    public static void bruteForceSearch() throws FileNotFoundException {
        System.setIn(new FileInputStream("src/data/algs4/largeW.txt"));
        In in = new In("src/data/algs4/largeT.txt");

        int[] whitelist = in.readAllInts();
        Arrays.sort(whitelist);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();

            if (BruteForceSearch.rank(key, whitelist) > -1)
                StdOut.println(key);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        binarySearch();
    }
}
