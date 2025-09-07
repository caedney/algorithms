package algorithms.Exercises.Exercise_1_3;

import java.util.Arrays;

import algorithms.Transaction;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.17
 * 
 * <p>
 * DO EXERCISE 1.3.16 for {@code Transaction}
 * </p>
 * <p>
 * <i>This list of exercises is intended to give you experience in working with
 * linked lists. Suggestion: make drawings using the visual representation
 * described in the text.</i>
 * </p>
 */
public class Exercise_1_3_17 {
    public static void main(String[] args) {
        Transaction[] transactions = Transaction.readTransactions("src/main/resources/data/transactions.txt");
        StdOut.println(Arrays.toString(transactions));
    }
}
