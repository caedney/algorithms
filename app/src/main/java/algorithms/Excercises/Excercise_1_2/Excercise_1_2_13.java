package algorithms.Excercises.Excercise_1_2;

import java.util.Arrays;

import algorithms.Transaction;
import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.2.13
 * 
 * <p>
 * Using our implmentation of {@code Date} as a model (page 91), develop an
 * implementation of {@code Transaction}.
 * </p>
 */
public class Excercise_1_2_13 {
    public static void main(String[] args) {
        Transaction[] a = new Transaction[4];
        a[0] = new Transaction("Turing   17/06/1990  644.08");
        a[1] = new Transaction("Tarjan   26/03/2002 4121.85");
        a[2] = new Transaction("Knuth    14/06/1999  288.34");
        a[3] = new Transaction("Dijkstra 22/08/2007 2678.40");

        StdOut.println("Unsorted");
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Sort by date");
        Arrays.sort(a, new Transaction.WhenOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Sort by customer");
        Arrays.sort(a, new Transaction.WhoOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();

        StdOut.println("Sort by amount");
        Arrays.sort(a, new Transaction.HowMuchOrder());
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
        StdOut.println();
    }
}
