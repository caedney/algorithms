package algorithms.Exercises.Exercise_1_2;

import algorithms.Transaction;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.13
 * 
 * <p>
 * Using our implementation of {@code Date} as a model (page 91), develop an
 * implementation of {@code Transaction}.
 * </p>
 */
public class Exercise_1_2_13 {
    public static void main(String[] args) {
        Transaction[] a = new Transaction[4];
        a[0] = new Transaction("Turing   06/17/1990  644.08");
        a[1] = new Transaction("Tarjan   03/26/2002 4121.85");
        a[2] = new Transaction("Knuth    06/14/1999  288.34");
        a[3] = new Transaction("Dijkstra 08/22/2007 2678.40");

        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
    }
}
