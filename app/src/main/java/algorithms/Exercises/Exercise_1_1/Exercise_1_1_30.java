package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.30
 * 
 * <p>
 * <i>Array exercise</i>. Write a code fragment that creates an 𝑁-by-𝑁 boolean
 * array a[][] such that a[i][j] is {@code true} if i and j are relatively prime
 * (have no common factors), and {@code false} otherwise.
 * </p>
 */
public class Exercise_1_1_30 {
    public static int gcd(int p, int q) {
        while (q != 0) {
            int r = p % q;
            p = q;
            q = r;
        }

        return p;
    }

    public static void main(String[] args) {
        int N = 4;
        boolean[][] array = new boolean[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                array[i][j] = gcd(i + 1, j + 1) == 1;
        }

        StdOut.printf("%-4s", "");
        for (int col = 0; col < array[0].length; col++)
            StdOut.printf("%-6s", col);

        StdOut.println();

        for (int row = 0; row < array.length; row++) {
            StdOut.printf("%-4s", row);

            for (int col = 0; col < array[row].length; col++)
                StdOut.printf("%-6b", array[row][col]);

            StdOut.println();
        }
    }
}
