package algorithms.Exercises.Exercise_1_2;

import algorithms.Date;
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
    public static class Transaction {
        private final String who;
        private final Date when;
        private final double amount;

        public Transaction(String transaction) {
            String[] a = transaction.split("\\s+");
            who = a[0];
            when = new Date(a[1]);
            amount = Double.parseDouble(a[2]);

            if (Double.isNaN(amount) || Double.isInfinite(amount))
                throw new IllegalArgumentException("Amount cannot be NaN or infinite");
        }

        public String who() {
            return who;
        }

        public Date when() {
            return when;
        }

        public double amount() {
            return amount;
        }

        @Override
        public String toString() {
            return String.format("%-10s %10s %8.2f", who, when, amount);
        }
    }

    public static void main(String[] args) {
        Transaction[] a = new Transaction[4];
        a[0] = new Transaction("Turing   17/06/1990  644.08");
        a[1] = new Transaction("Tarjan   26/03/2002 4121.85");
        a[2] = new Transaction("Knuth    14/06/1999  288.34");
        a[3] = new Transaction("Dijkstra 22/08/2007 2678.40");

        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
    }
}
