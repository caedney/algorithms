package algorithms.Exercises.Exercise_1_3;

import algorithms.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.42
 * 
 * <p>
 * <i>Copy a stack</i>. Create a new constructor for the linked-list
 * implementation of {@code Stack} so that
 * {@code Stack<Item> t = new Stack<Item>(s);} makes {@code t} a reference to a
 * new and independent copy of the stack {@code s}.
 * </p>
 */
public class Exercise_1_3_42 {
    public static void main(String[] args) {
        Stack<Integer> s = new Stack<>();
        s.push(1);
        s.push(2);
        s.push(3);

        Stack<Integer> t = new Stack<>(s);
        s.pop();
        t.push(4);

        StdOut.println(s.size()); // 2
        StdOut.println(t.size()); // 4
    }
}
