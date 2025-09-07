package algorithms.Exercises.Exercise_1_3;

import algorithms.Stack;

/**
 * Exercise 1.3.12
 * 
 * <p>
 * Write an iterable {@code Stack} <i>client</i> that has a static method
 * {@code copy()} that takes a stack of strings as argument and returns a copy
 * of the stack. <i>Note</i>: This ability is a prime example of the value of
 * having an iterator, because it allows development of such functionality
 * without changing the basic API.
 * </p>
 */
public class Exercise_1_3_12 {
    /**
     * Returns a copy of the given stack of strings. This method uses only the
     * stack's iterator and push().
     *
     * @param original the stack to copy
     * @return a new stack containing the same elements in the same order
     */
    public static Stack<String> copy(Stack<String> original) {
        Stack<String> result = new Stack<>();

        for (String s : original)
            result.push(s);

        return result;
    }

    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        stack.push("one");
        stack.push("two");
        stack.push("three");

        Stack<String> copy = copy(stack);

        System.out.println("Original: " + stack);
        System.out.println("Copy:     " + copy);
    }
}
