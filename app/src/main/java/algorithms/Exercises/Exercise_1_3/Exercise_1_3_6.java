package algorithms.Exercises.Exercise_1_3;

import java.io.ByteArrayInputStream;

import algorithms.Queue;
import algorithms.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.6
 * 
 * <p>
 * What does the following code fragment do to the queue {@code q}?
 * </p>
 * 
 * {@snippet :
 * Stack<String> stack = new Stack<String>();
 * while (!q.isEmpty())
 *     stack.push(q.dequeue());
 * while (!stack.isEmpty())
 *     q.enqueue(stack.pop());
 * }
 * 
 * <i>Answer</i>: The order of elements is reversed because a stack is LIFO
 * (Last-In, First-Out).
 */
public class Exercise_1_3_6 {
    public static void main(String[] args) {
        String input = "it was the best of times it was the";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Stack<String> stack = new Stack<String>();
        Queue<String> queue = new Queue<String>();

        // add items to the queue
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            queue.enqueue(item);
        }

        // transfer elements from queue to stack
        while (!queue.isEmpty())
            stack.push(queue.dequeue()); // "it was the best of times it was the"

        // transfer elements back from stack to queue
        while (!stack.isEmpty())
            queue.enqueue(stack.pop()); // the was it times of best the was it

        for (String q : queue)
            StdOut.print(q + " "); // the was it times of best the was it
    }
}
