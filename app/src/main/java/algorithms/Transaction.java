package algorithms;

import edu.princeton.cs.algs4.In;

public class Transaction {
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

    public static Transaction[] readTransactions(String name) {
        In in = new In(name);
        Queue<Transaction> q = new Queue<Transaction>();

        while (!in.isEmpty()) {
            q.enqueue(new Transaction(in.readLine()));
        }

        int n = q.size();
        Transaction[] transactions = new Transaction[n];

        for (int i = 0; i < n; i++)
            transactions[i] = q.dequeue();

        return transactions;
    }

    @Override
    public String toString() {
        return String.format("%-10s %10s %8.2f", who, when, amount);
    }
}