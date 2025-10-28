package algorithms.Exercises.Exercise_1_3;

import algorithms.Deque;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.48
 * 
 * <p>
 * <i>Two stacks with a deque</i>. Implement two stacks with a single deque so
 * that each operation takes a constant number of deque operations (see EXERCISE
 * 1.3.33).
 * </p>
 */
public class Exercise_1_3_48 {
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.pushLeft("A");
        deque.pushLeft("B");
        deque.pushLeft("C");
        StdOut.println(deque.popLeft()); // C

        deque.pushRight("X");
        deque.pushRight("Y");
        deque.pushRight("Z");
        StdOut.println(deque.popRight()); // Z
    }
}
