package algorithms.Exercises.Exercise_1_4;

import java.util.Arrays;

/******************************************************************************
 * Exercise 1.4.19
 * 
 * <p>
 * <i>Local minimum of a matrix.</i> Given an 𝑁-by-𝑁 array <code>a[][]</code>
 * of 𝑁² distinct integers, design an algorithm that runs in time proportional
 * to 𝑁 to find a <i>local minimum</i>: a pair of indices <code>i</code> and
 * <code>j</code> such that <code>a[i][j] < a[i+1][j], a[i][j] < a[i][j+1],
 * a[i][j] < a[i-1][j]</code>, and <code>a[i][j] < a[i][j-1]</code>. The running
 * time of your program should be proportional to 𝑁 in the worst case.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_19
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_19 {
    public static boolean isLocalMin(int[][] a, int i, int j) {
        int n = a.length;
        // @formatter:off
        boolean upOk    = (i == 0)     || a[i][j] < a[i - 1][j];
        boolean downOk  = (i == n - 1) || a[i][j] < a[i + 1][j];
        boolean leftOk  = (j == 0)     || a[i][j] < a[i][j - 1];
        boolean rightOk = (j == n - 1) || a[i][j] < a[i][j + 1];
        // @formatter:on

        return upOk && downOk && leftOk && rightOk;
    }

    public static int indexOfRowMin(int[][] a, int row, int loCol, int hiCol) {
        int min = a[row][loCol];
        int index = loCol;

        for (int j = loCol + 1; j <= hiCol; j++) {
            if (a[row][j] < min) {
                min = a[row][j];
                index = j;
            }
        }

        return index;
    }

    public static int indexOfColMin(int[][] a, int col, int loRow, int hiRow) {
        int min = a[loRow][col];
        int index = loRow;

        for (int i = loRow + 1; i <= hiRow; i++) {
            if (a[i][col] < min) {
                min = a[i][col];
                index = i;
            }
        }

        return index;
    }

    /**
     * Finds a local minimum by exhaustive search: scans the grid row by row and
     * returns the indices of the first cell that is smaller than all of its
     * neighbours. Runs in time proportional to 𝑁² and serves as the correctness
     * oracle for the fast version.
     */
    public static int[] findLocalMinBruteForce(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (isLocalMin(a, i, j))
                    return new int[] { i, j };
            }
        }

        throw new AssertionError("unreachable: every array has a local minimum");
    }

    public static int[] findLocalMin(int[][] a) {
        int loRow = 0;
        int hiRow = a.length - 1;
        int loCol = 0;
        int hiCol = a[0].length - 1;
        boolean sweepRow = true;

        while (loRow <= hiRow && loCol <= hiCol) {
            int midRow = loRow + (hiRow - loRow) / 2;
            int midCol = loCol + (hiCol - loCol) / 2;
            int j = indexOfRowMin(a, midRow, loCol, hiCol);
            int i = indexOfColMin(a, midCol, loRow, hiRow);

            if (sweepRow) {
                if (midRow > 0 && a[midRow - 1][j] < a[midRow][j]) {
                    hiRow = midRow - 1;
                } else if (midRow < a.length - 1 && a[midRow + 1][j] < a[midRow][j]) {
                    loRow = midRow + 1;
                } else {
                    return new int[] { midRow, j };
                }
            } else {
                if (midCol > 0 && a[i][midCol - 1] < a[i][midCol]) {
                    hiCol = midCol - 1;
                } else if (midCol < a[0].length - 1 && a[i][midCol + 1] < a[i][midCol]) {
                    loCol = midCol + 1;
                } else {
                    return new int[] { i, midCol };
                }
            }

            sweepRow = !sweepRow;
        }

        throw new AssertionError("unreachable: every array has a local minimum");
    }

    public static void main(String[] args) {
        // @formatter:off
        int[][] a = {
            { 28, 13, 23, 20, 30, 43, 53, 61 },
            { 14,  3,  9, 18, 24, 32, 45, 54 },
            { 10,  2,  1,  8, 22, 25, 34, 46 },
            { 26, 12,  4, 16, 27, 36, 47, 55 },
            { 29, 31,  6, 33, 35, 37, 39, 41 },
            { 48, 38, 44, 40, 49, 56, 62, 66 },
            { 57, 50, 73, 51, 58, 63, 67, 69 },
            { 64, 59, 72, 60, 65, 68, 70, 71 },
        };
        // @formatter:on
        int[] localMin = findLocalMin(a);
        System.out.println("local minimum is at coordinates: " + Arrays.toString(localMin));
        // @formatter:off
        int[][] bad = {
            { 48,  7, 16, 34, 17, 37, 39 },
            { 43, 47, 28, 42, 35, 15, 18 },
            {  8, 26,  6, 22,  1, 23, 38 },
            { 21, 10, 12, 20, 14,  4, 27 },
            { 24, 33,  3, 40, 36,  0, 46 },
            { 32, 25, 11, 44, 45,  9, 13 },
            { 29,  2,  5, 31, 30, 19, 41 },
        };
        // @formatter:on
        int[] localMinBad = findLocalMin(bad);
        System.out.println("local minimum is at coordinates: " + Arrays.toString(localMinBad));
    }
}
