package algorithms.Exercises.Exercise_1_2;

import algorithms.Transaction;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.14
 * 
 * <p>
 * Using our implementation of {@code equals()} in {@code Date} as a model (page
 * 103), develop implementations of {@code equals()} for {@code Transaction}.
 * </p>
 */
public class Exercise_1_2_14 {
    public static void main(String[] args) {
        Transaction a = new Transaction("Turing   06/17/1990  644.08");
        Transaction b = new Transaction("Tarjan   03/26/2002 4121.85");
        Transaction c = new Transaction("Knuth    06/14/1999  288.34");
        Transaction d = new Transaction("Dijkstra 08/22/2007 2678.40");
        Transaction e = new Transaction("Turing   06/17/1990  644.08");

        StdOut.println("a equals c: " + a.equals(c));
        StdOut.println("b equals d: " + b.equals(d));
        StdOut.println("a equals e: " + a.equals(e));
    }
}
