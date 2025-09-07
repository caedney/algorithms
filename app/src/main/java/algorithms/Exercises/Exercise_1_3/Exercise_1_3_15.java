package algorithms.Exercises.Exercise_1_3;

import algorithms.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.15
 * 
 * <p>
 * Write a {@code Queue} client that takes a command-line argument {@code k} and
 * prints the {@code k}th from the last string found on standard input (assuming
 * that standard input has {@code k} or more strings).
 * </p>
 */
public class Exercise_1_3_15 {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        Queue<String> q = new Queue<>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.enqueue(item);
        }

        int index = 0;
        for (String string : q) {
            if (index == q.size() - k)
                StdOut.println(string);

            index++;
        }
    }
}
