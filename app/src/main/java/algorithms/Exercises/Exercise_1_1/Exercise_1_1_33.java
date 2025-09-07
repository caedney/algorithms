package algorithms.Exercises.Exercise_1_1;

import java.util.Arrays;

import algorithms.Matrix;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.33
 * 
 * <p>
 * <i>Matrix library</i>. Write a library Matrix that implements the following
 * API:
 * </p>
 * {@code public class Matrix}
 * <table>
 * <tbody>
 * <tr>
 * <td>{@code static}</td>
 * <td>{@code double dot(double[] x, double[] y)}</td>
 * <td><i>Vector Dot Product</i></td>
 * </tr>
 * <tr>
 * <td>{@code static}</td>
 * <td>{@code double[] mult(double[] y, double[][] a)}</td>
 * <td><i>Vector-Matrix product</i></td>
 * </tr>
 * <tr>
 * <td>{@code static}</td>
 * <td>{@code double[] mult(double[][] a, double[] x)}</td>
 * <td><i>Matrix-Vector Product</i></td>
 * </tr>
 * <tr>
 * <td>{@code static}</td>
 * <td>{@code double[][] mult(double[][] a, double[][] b)}</td>
 * <td><i>Matrix-Matrix Product</i></td>
 * </tr>
 * <tr>
 * <td>{@code static}</td>
 * <td>{@code double[][] transpose(double[][] a)}</td>
 * <td><i>Transpose</i></td>
 * </tr>
 * </tbody>
 * </table>
 */
public class Exercise_1_1_33 {
    public static void main(String[] args) {
        double[] a = { 1.0, 2.0, 3.0 };
        double[] b = { 4.0, 5.0, 6.0 };
        double result = Matrix.dot(a, b);
        StdOut.println("Vector Dot Product = " + result);
        // Expected output = 32.0
        // (1.0 * 4.0) + (2.0 * 5.0) + (3.0 * 6.0) = 32.0

        double[] c = { 1, 2 }; // row vector of length 2
        double[][] d = { { 3, 4, 5 }, { 6, 7, 8 } }; // 2 x 3 matrix
        double[] e = Matrix.mult(c, d);
        StdOut.println("Vector-Matrix Product = " + Arrays.toString(e));
        // Expected output = [15.0, 18.0, 21.0]
        // (1.0 * 3.0) + (2.0 * 6.0) = 15.0
        // (1.0 * 4.0) + (2.0 * 7.0) = 18.0
        // (1.0 * 5.0) + (2.0 * 8.0) = 21.0

        double[][] f = { { 1, 2, 3 }, { 4, 5, 6 } };
        double[] g = { 7, 8, 9 };
        double[] h = Matrix.mult(f, g);
        StdOut.println("Matrix-Vector Product = " + Arrays.toString(h));
        // Expected output = [50.0, 122.0]
        // (1.0 * 7.0) + (2.0 * 8.0) + (3.0 * 9.0) = 50.0
        // (4.0 * 7.0) + (5.0 * 8.0) + (6.0 * 9.0) = 122.0

        double[][] i = { { 1, 2, 3 }, { 4, 5, 6 } };
        double[][] j = { { 7, 8 }, { 9, 10 }, { 11, 12 } };
        double[][] k = Matrix.mult(i, j);
        StdOut.println("Matrix-Matrix Product = " + Arrays.deepToString(k));
        // Expected output = [[58.0, 64.0], [139.0, 154.0]]
        // (1.0 * 7.0) + (2.0 * 9.0) + (3.0 * 11.0) = 58.0,
        // (1.0 * 8.0) + (2.0 * 10.0) + (3.0 * 12.0) = 64.0
        // (4.0 * 7.0) + (5.0 * 9.0) + (6.0 * 11.0) = 139.0,
        // (4.0 * 8.0) + (5.0 * 10.0) + (6.0 * 12.0) = 154.0

        double[][] l = { { 1, 2, 3 }, { 4, 5, 6 } }; // 2 x 3 matrix
        double[][] m = Matrix.transpose(l);
        StdOut.println("Transpose = " + Arrays.deepToString(m));
        // Expected output = [[1.0, 4.0], [2.0, 5.0], [3.0, 6.0]]
        // 1.0 4.0
        // 2.0 5.0
        // 3.0 6.0
    }
}
