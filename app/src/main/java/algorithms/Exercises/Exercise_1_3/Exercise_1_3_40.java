package algorithms.Exercises.Exercise_1_3;

import java.io.ByteArrayInputStream;

import algorithms.MoveToFront;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.40
 * 
 * <p>
 * <i>Move-to-front</i>. Read in a sequence of characters from standard input
 * and maintain the characters in a linked list with no duplicates. When you
 * read in a previously unseen character, insert it at the front of the list.
 * When you read in a duplicate character, delete it from the list and reinsert
 * it at the beginning. Name your program {@code MoveToFront}: it implements the
 * well-known <i>move-to-front</i> strategy, which is useful for caching, data
 * compression, and many other applications where items that have been recently
 * accessed are more likely to be reaccessed.
 * </p>
 */
public class Exercise_1_3_40 {
    public static void main(String[] args) {
        String input = "panama";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        MoveToFront list = new MoveToFront();

        while (!StdIn.isEmpty())
            list.add(StdIn.readChar());

        for (char value : list)
            StdOut.print(value + " ");

        StdOut.println();
    }
}
