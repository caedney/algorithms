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
    /**
     * Cells read by the sweep helpers, for the doubling test's linear-budget check.
     */
    public static long inspections = 0;

    /**
     * The definition as a predicate: is <code>a[i][j]</code> smaller than all four
     * of its neighbours? A neighbour beyond the edge of the grid is treated as
     * +infinity, so it can never disqualify a cell.
     */
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

    /**
     * Returns the column index of the smallest value in row <code>row</code>
     * between columns <code>loCol</code> and <code>hiCol</code> inclusive -- one
     * sweep, inspecting <code>hiCol - loCol + 1</code> cells.
     */
    public static int indexOfRowMin(int[][] a, int row, int loCol, int hiCol) {
        inspections += hiCol - loCol + 1;
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

    /**
     * Returns the row index of the smallest value in column <code>col</code>
     * between rows <code>loRow</code> and <code>hiRow</code> inclusive -- the
     * mirror of <code>indexOfRowMin</code>.
     */
    public static int indexOfColMin(int[][] a, int col, int loRow, int hiRow) {
        inspections += hiRow - loRow + 1;
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
     * Finds a local minimum in time proportional to 𝑁 by alternately sweeping the
     * middle row and middle column of a shrinking rectangle. A sweep either proves
     * its minimum is a local minimum, or discards the swept line together with the
     * half-rectangle on the far side of it. The pair
     * <code>(bestRow, bestCol)</code> remembers the smallest cell seen so far --
     * the witness that the kept half still contains a cell smaller than everything
     * on the rectangle's boundary, which is what makes a discard safe. Alternating
     * the axes halves the sweeps themselves, so the cells inspected total 𝑁 +
     * 2(𝑁/2 + 𝑁/4 + ...) < 3𝑁.
     */
    public static int[] findLocalMin(int[][] a) {
        int loRow = 0;
        int hiRow = a.length - 1;
        int loCol = 0;
        int hiCol = a.length - 1;
        int bestRow = 0;
        int bestCol = 0;
        boolean sweepRow = true;

        // invariant: the rectangle loRow..hiRow x loCol..hiCol contains a cell
        // smaller than every cell on its boundary, so a local minimum of the
        // full grid is trapped inside it
        while (loRow <= hiRow && loCol <= hiCol) {
            if (sweepRow) {
                int midRow = loRow + (hiRow - loRow) / 2;
                int j = indexOfRowMin(a, midRow, loCol, hiCol);

                if (a[bestRow][bestCol] < a[midRow][j]) {
                    // the remembered best beats the whole swept row, so the row
                    // is a wall the best cell is safe behind: keep the half
                    // where the best cell lives, no neighbour probes needed
                    if (bestRow < midRow) {
                        hiRow = midRow - 1;
                    } else {
                        loRow = midRow + 1;
                    }
                } else {
                    // the sweep's minimum is the smallest value yet seen:
                    // probe the cells above and below it
                    if (midRow > 0 && a[midRow - 1][j] < a[midRow][j]) {
                        // smaller above: keep the top half; the winning
                        // neighbour beats everything ever seen, so it is the
                        // new best
                        hiRow = midRow - 1;
                        bestRow = midRow - 1;
                        bestCol = j;
                    } else if (midRow < a.length - 1 && a[midRow + 1][j] < a[midRow][j]) {
                        loRow = midRow + 1;
                        bestRow = midRow + 1;
                        bestCol = j;
                    } else {
                        // beats its whole row and both vertical neighbours,
                        // and nothing smaller has ever been seen: local minimum
                        return new int[] { midRow, j };
                    }
                }
            } else {
                // the mirror pass: sweep the middle column, probe left and
                // right, and move the column bounds
                int midCol = loCol + (hiCol - loCol) / 2;
                int i = indexOfColMin(a, midCol, loRow, hiRow);

                if (a[bestRow][bestCol] < a[i][midCol]) {
                    if (bestCol < midCol) {
                        hiCol = midCol - 1;
                    } else {
                        loCol = midCol + 1;
                    }
                } else {
                    if (midCol > 0 && a[i][midCol - 1] < a[i][midCol]) {
                        hiCol = midCol - 1;
                        bestCol = midCol - 1;
                        bestRow = i;
                    } else if (midCol < a[0].length - 1 && a[i][midCol + 1] < a[i][midCol]) {
                        loCol = midCol + 1;
                        bestCol = midCol + 1;
                        bestRow = i;
                    } else {
                        return new int[] { i, midCol };
                    }
                }
            }

            // alternate axes so the rectangle -- and with it every future
            // sweep -- shrinks in both dimensions
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
    }
}
