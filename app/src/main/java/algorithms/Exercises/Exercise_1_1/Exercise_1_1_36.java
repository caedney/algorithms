package algorithms.Exercises.Exercise_1_1;

import algorithms.ShuffleTest;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.36
 * 
 * <p>
 * <i>Empirical shuffle check</i>. Run computational experiments to check that
 * our shuffling code on page 32 works as advertised. Write a program
 * {@code ShuffleTest} that takes command-line arguments 𝑀 and 𝑁, does 𝑁
 * shuffles of an array of size 𝑀 that is initialized with {@code a[i] = i}
 * before each shuffle, and prints an 𝑀-by-𝑀 table such that row {@code i}
 * gives the number of times {@code i} wound up in position {@code j} for all
 * {@code j}. All entries in the array should be close to 𝑁/𝑀.
 * </p>
 */
public class Exercise_1_1_36 {
    public static void main(String[] args) {
        int M = 5; // size of array
        int N = 1000; // number of trials
        int[][] counts = new int[M][M]; // counts[i][j] = #times i ends in pos j
        int[] a = new int[M];

        for (int trial = 0; trial < N; trial++) {
            for (int i = 0; i < M; i++)
                a[i] = i; // initialize

            ShuffleTest.shuffle(a);

            for (int j = 0; j < M; j++)
                counts[a[j]][j]++; // record results
        }

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++)
                StdOut.printf("%7d", counts[i][j]);

            StdOut.println();
        }
    }
}
