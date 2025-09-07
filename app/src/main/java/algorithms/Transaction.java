package algorithms;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Transaction {
    private final String who;
    private final Date when;
    private final double amount;

    public Transaction(String transaction) {
        String[] a = transaction.split("\\s+");
        this.who = a[0];
        this.when = new Date(a[1]);
        this.amount = Double.parseDouble(a[2]);

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
