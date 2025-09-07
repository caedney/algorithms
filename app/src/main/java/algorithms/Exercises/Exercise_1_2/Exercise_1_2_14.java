package algorithms.Exercises.Exercise_1_2;

import algorithms.Date;
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

        @Override
        public boolean equals(Object other) {
            if (other == this)
                return true;
            if (other == null)
                return false;
            if (other.getClass() != this.getClass())
                return false;

            Transaction that = (Transaction) other;

            return (this.amount == that.amount) && (this.who.equals(that.who)) && (this.when.equals(that.when));
        }
    }

    public static void main(String[] args) {
        Transaction a = new Transaction("Turing   17/06/1990  644.08");
        Transaction b = new Transaction("Tarjan   26/03/2002 4121.85");
        Transaction c = new Transaction("Knuth    14/06/1999  288.34");
        Transaction d = new Transaction("Dijkstra 22/08/2007 2678.40");
        Transaction e = new Transaction("Turing   17/06/1990  644.08");

        StdOut.println("a equals c: " + a.equals(c));
        StdOut.println("b equals d: " + b.equals(d));
        StdOut.println("a equals e: " + a.equals(e));
    }
}
