package algorithms.Exercises.Exercise_1_3;

import algorithms.EvaluatePostfix;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.11
 * 
 * <p>
 * Write a program {@code EvaluatePostfix} that takes a postfix expression from
 * standard input, evaluates it, and prints the value. (Piping the output of
 * your program from the previous exercise to this program gives equivalent
 * behavior to {@code Evaluate}.
 * </p>
 */
public class Exercise_1_3_11 {
    public static void main(String[] args) {
        String postfix = "3 4 5 + *";
        Integer evaluated = EvaluatePostfix.evaluate(postfix);
        StdOut.println("Postfix:   " + postfix);
        StdOut.println("Evaluated: " + evaluated);
        // push(3, 4, 5) → [3, 4, 5]
        // pop(5, 4) → (5 + 4 = 9) → push(9) → [3, 9]
        // pop(9, 3) → (9 * 3 = 27)

        postfix = "1 2 3 4 5 * + 6 * * +";
        evaluated = EvaluatePostfix.evaluate(postfix);
        StdOut.println("Postfix:   " + postfix);
        StdOut.println("Evaluated: " + evaluated);
        // push(1, 2, 3, 4, 5) → [1, 2, 3, 4, 5]
        // pop(5, 4) → (5 * 4 = 20) → push(20) → [1, 2, 3, 20]
        // pop(20, 3) → (20 + 3 = 23) → push(23) → [1, 2, 23]
        // push(6) → [1, 2, 23, 6]
        // pop(6, 23) → (6 * 23 = 138) → push(138) → [1, 2, 138]
        // pop(138, 2) → (138 * 2 = 276) → push(276) → [1, 276]
        // pop(276, 1) → (276 + 1 = 277)
    }
}
