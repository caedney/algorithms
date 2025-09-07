package algorithms.Exercises.Exercise_1_3;

import java.util.ConcurrentModificationException;

import algorithms.StackFailFast;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.50
 * 
 * <p>
 * <i>Fail-fast iterator</i>. Modify the iterator code in {@code Stack} to
 * immediately throw a {@code java.util.ConcurrentModificationException} if the
 * client modifies the collection (via {@code push()} or {@code pop()}) during
 * iteration? 𝑏).
 * </p>
 * 
 * <p>
 * <i>Solution</i>: Maintain a counter that counts the number of {@code push()}
 * and {@code pop()} operations. When creating an iterator, store this value as
 * an {@code Iterator} instance variable. Before each call to {@code hasNext()}
 * and {@code next()}, check that this value has not changed since construction
 * of the iterator; if it has, throw the exception.
 * </p>
 */
public class Exercise_1_3_50 {
    public static void main(String[] args) {
        StackFailFast<String> stack = new StackFailFast<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");

        StdOut.println("Iterating over stack...");
        var iterator = stack.iterator();
        StdOut.println(iterator.next()); // C
        StdOut.println(iterator.next()); // B

        // Modify stack during iteration
        StdOut.println("Pushing new element...");
        stack.push("D");

        try {
            StdOut.println(iterator.next()); // Next call should fail
        } catch (ConcurrentModificationException e) {
            StdOut.println("Caught expected ConcurrentModificationException!");
        }
    }
}
