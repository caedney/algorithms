package algorithms.Exercises.Exercise_1_1;

import java.util.Arrays;
import java.util.Random;

import algorithms.BinarySearch;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.39
 * 
 * <p>
 * <i>Random matches</i>. Write a {@code BinarySearch} client that takes an
 * {@code int} value {@code T} as command-line argument and runs 𝑇 trials of
 * the following experiment for 𝑁=10³, 10⁴, 10⁵, and 10⁶: generate two arrays
 * of 𝑁 randomly generated positive six-digit {@code int} values, and find the
 * number of values that appear in both arrays. Print a table giving the average
 * value of this quantity over the 𝑇 trials for each value of 𝑁.
 * </p>
 */
public class Exercise_1_1_39 {
    public static int countMatches(int[] a, int[] b) {
        Arrays.sort(b); // prepare for binary search
        int count = 0;

        for (int key : a) {
            if (BinarySearch.rank(key, b) != -1)
                count++;
        }

        return count;
    }

    // generate an array of N random 6-digit positive ints
    public static int[] randomArray(int N, Random rand) {
        int[] arr = new int[N];

        for (int i = 0; i < N; i++)
            arr[i] = 100000 + rand.nextInt(900000); // 100000 to 999999

        return arr;
    }

    public static void main(String[] args) {
        int T = 10;
        int[] sizes = { 1000, 10000, 100000, 1000000 };
        Random rand = new Random();

        StdOut.printf("%10s %15s\n", "N", "Average Matches");
        StdOut.println("-----------------------------------");

        for (int N : sizes) {
            long totalMatches = 0;

            for (int t = 0; t < T; t++) {
                int[] a = randomArray(N, rand);
                int[] b = randomArray(N, rand);
                totalMatches += countMatches(a, b);
            }

            double avg = (double) totalMatches / T;
            StdOut.printf("%10d %15.2f\n", N, avg);
        }
    }
}
