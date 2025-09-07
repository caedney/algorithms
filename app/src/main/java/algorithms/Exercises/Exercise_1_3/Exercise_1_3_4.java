package algorithms.Exercises.Exercise_1_3;

import java.io.FileInputStream;
import java.io.IOException;

import algorithms.Parentheses;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.4
 * 
 * <p>
 * Write a stack client {@code Parentheses} that reads in a text stream from
 * standard input and uses a stack to determine whether its parentheses are
 * properly balanced. For example, your program should print {@code true} for
 * <code>[()]{}{[()()]()}</code> and {@code false} for {@code [(])}.
 * </p>
 */
public class Exercise_1_3_4 {
    public static void main(String[] args) throws IOException {
        System.setIn(new FileInputStream("src/main/resources/data/balanced-parentheses.txt"));

        while (!StdIn.isEmpty()) {
            String str = StdIn.readString().trim();
            StdOut.println(Parentheses.isBalanced(str));
        }
    }
}
