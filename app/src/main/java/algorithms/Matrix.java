package algorithms;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

public class Matrix {
    /**
     * Vector Dot Product
     * 
     * <p>
     * Computes the dot product of two vectors represented as arrays of doubles.
     * </p>
     * 
     * <p>
     * The dot product is defined as the sum of the products of corresponding
     * elements in the two vectors:
     * </p>
     * 
     * <pre>
     * x · y = Σ (x[i] * y[i]) for i = 0 to n-1
     * </pre>
     * 
     * <ul>
     * <li>{@code x · y} → the dot product of two vectors 𝑥 and 𝑦.</li>
     * <li>{@code ∑ for i = 0 to n-1} → the sum from 𝑖 = 0 to 𝑖 = 𝑛−1.</li>
     * <li>{@code (x[i] * y[i])} → multiply the 𝑖-th element of 𝑥 by the 𝑖-th
     * element of 𝑦.</li>
     * </ul>
     * 
     * <hr />
     * 
     * @param x the first input vector
     * @param y the second input vector
     * @return the dot product of {@code x} and {@code y}
     * @throws IllegalArgumentException if the input vectors are not the same length
     */
    public static double dot(double[] x, double[] y) {
        if (x.length != y.length)
            throw new IllegalArgumentException("Vectors must have the same length.");

        double product = 0.0;
        for (int i = 0; i < x.length; i++)
            product += x[i] * y[i];

        return product;
    }

    /**
     * Vector-Matrix Product
     * 
     * <p>
     * Multiplies a row vector {@code x} by a matrix {@code a}.
     * </p>
     * 
     * <p>
     * If {@code x} has length m and {@code a} has dimensions m × n, the result will
     * be a vector of length n.
     * </p>
     * 
     * <hr />
     *
     * @param x the row vector (length m)
     * @param a the matrix (m × n)
     * @return the vector-matrix product {@code x × a}
     * @throws IllegalArgumentException if {@code a} is not rectangular; if
     *                                  dimensions are incompatible
     */
    public static double[] mult(double[] x, double[][] a) {
        int cols = a[0].length;
        for (double[] row : a) {
            if (row.length != cols)
                throw new IllegalArgumentException("Matrix a is not rectangular.");
        }

        if (x.length != a.length) {
            throw new IllegalArgumentException(
                    "Incompatible dimensions: vector has length " + x.length + ", matrix has " + a.length + " rows");
        }

        double[] product = new double[cols];

        for (int j = 0; j < cols; j++) {
            double sum = 0.0;
            for (int i = 0; i < a.length; i++)
                sum += x[i] * a[i][j];

            product[j] = sum;
        }

        return product;
    }

    /**
     * Matrix-Vector Product
     * 
     * <p>
     * Multiplies a matrix {@code a} by a vector {@code x}.
     * </p>
     * 
     * <p>
     * If {@code a} has dimensions m × n, then {@code x} must have length n. The
     * result will be a vector of length m.
     * </p>
     * 
     * <hr />
     *
     * @param a the matrix (m × n)
     * @param x the vector (length n)
     * @return the matrix-vector product {@code a × x}
     * @throws IllegalArgumentException if {@code a} is not rectangular; if
     *                                  dimensions are incompatible
     * 
     */
    public static double[] mult(double[][] a, double[] x) {
        int cols = a[0].length;
        for (double[] row : a) {
            if (row.length != cols)
                throw new IllegalArgumentException("Matrix a is not rectangular.");
        }

        if (cols != x.length) {
            throw new IllegalArgumentException(
                    "Incompatible dimensions: matrix has " + cols + " columns, vector has length " + x.length);
        }

        double[] product = new double[a.length];

        for (int i = 0; i < a.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < cols; j++)
                sum += a[i][j] * x[j];

            product[i] = sum;
        }

        return product;
    }

    /**
     * Matrix-Matrix Product
     * 
     * <p>
     * Multiplies two matrices {@code a} and {@code b}.
     * </p>
     * 
     * <p>
     * The number of columns in {@code a} must equal the number of rows in
     * {@code b}. The resulting matrix will have dimensions {@code a.length} ×
     * {@code b[0].length}.
     * </p>
     * 
     * <hr />
     *
     * @param a the left matrix
     * @param b the right matrix
     * @return the matrix-matrix product {@code a × b}
     * @throws IllegalArgumentException if inputs are not rectangular; if dimensions
     *                                  are incompatible
     */
    public static double[][] mult(double[][] a, double[][] b) {
        int aCols = a[0].length;
        for (double[] row : a) {
            if (row.length != aCols)
                throw new IllegalArgumentException("Matrix a is not rectangular.");

        }

        int bCols = b[0].length;
        for (double[] row : b) {
            if (row.length != bCols)
                throw new IllegalArgumentException("Matrix b is not rectangular.");

        }

        if (aCols != b.length) {
            throw new IllegalArgumentException("Incompatible matrix dimensions: a is " + a.length + "x" + aCols
                    + ", b is " + b.length + "x" + bCols);
        }

        double[][] product = new double[a.length][bCols];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < bCols; j++) {
                double sum = 0.0;
                for (int k = 0; k < aCols; k++)
                    sum += a[i][k] * b[k][j];

                product[i][j] = sum;
            }
        }

        return product;
    }

    /**
     * Transpose
     * 
     * <p>
     * The transpose of a matrix will "flip" a matrix over its diagonal, turning
     * rows into columns (and columns into rows).
     * </p>
     * 
     * <pre>
     * aT[i][j] = a[j][i]
     * </pre>
     * 
     * <hr />
     *
     * @param a the input matrix {@code m x n}
     * @return the transposed matrix {@code n x m}
     * @throws IllegalArgumentException if {@code a} is not rectangular
     */
    public static double[][] transpose(double[][] a) {
        int cols = a[0].length;
        for (double[] row : a) {
            if (row.length != cols)
                throw new IllegalArgumentException("Matrix is not rectangular.");

        }

        int rows = a.length;
        double[][] transposedMatrix = new double[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                transposedMatrix[j][i] = a[i][j];

        }

        return transposedMatrix;
    }

    public static void main(String[] args) {
        double[] a = { 1.0, 2.0, 3.0 };
        double[] b = { 4.0, 5.0, 6.0 };
        double result = dot(a, b);
        StdOut.println("Vector Dot Product = " + result);
        // Expected output = 32.0
        // (1.0 * 4.0) + (2.0 * 5.0) + (3.0 * 6.0) = 32.0

        double[] c = { 1, 2 }; // row vector of length 2
        double[][] d = { { 3, 4, 5 }, { 6, 7, 8 } }; // 2 x 3 matrix
        double[] e = mult(c, d);
        StdOut.println("Vector-Matrix Product = " + Arrays.toString(e));
        // Expected output = [15.0, 18.0, 21.0]
        // (1.0 * 3.0) + (2.0 * 6.0) = 15.0
        // (1.0 * 4.0) + (2.0 * 7.0) = 18.0
        // (1.0 * 5.0) + (2.0 * 8.0) = 21.0

        double[][] f = { { 1, 2, 3 }, { 4, 5, 6 } };
        double[] g = { 7, 8, 9 };
        double[] h = mult(f, g);
        StdOut.println("Matrix-Vector Product = " + Arrays.toString(h));
        // Expected output = [50.0, 122.0]
        // (1.0 * 7.0) + (2.0 * 8.0) + (3.0 * 9.0) = 50.0
        // (4.0 * 7.0) + (5.0 * 8.0) + (6.0 * 9.0) = 122.0

        double[][] i = { { 1, 2, 3 }, { 4, 5, 6 } };
        double[][] j = { { 7, 8 }, { 9, 10 }, { 11, 12 } };
        double[][] k = mult(i, j);
        StdOut.println("Matrix-Matrix Product = " + Arrays.deepToString(k));
        // Expected output = [[58.0, 64.0], [139.0, 154.0]]
        // (1.0 * 7.0) + (2.0 * 9.0) + (3.0 * 11.0) = 58.0,
        // (1.0 * 8.0) + (2.0 * 10.0) + (3.0 * 12.0) = 64.0
        // (4.0 * 7.0) + (5.0 * 9.0) + (6.0 * 11.0) = 139.0,
        // (4.0 * 8.0) + (5.0 * 10.0) + (6.0 * 12.0) = 154.0

        double[][] l = { { 1, 2, 3 }, { 4, 5, 6 } }; // 2 x 3 matrix
        double[][] m = transpose(l);
        StdOut.println("Transpose = " + Arrays.deepToString(m));
        // Expected output = [[1.0, 4.0], [2.0, 5.0], [3.0, 6.0]]
        // 1.0 4.0
        // 2.0 5.0
        // 3.0 6.0
    }
}
