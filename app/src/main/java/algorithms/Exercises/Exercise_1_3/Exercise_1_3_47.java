package algorithms.Exercises.Exercise_1_3;

import algorithms.CatenableQueue;
import algorithms.CatenableStack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.47
 * 
 * <p>
 * <i>Catenable queues, stacks, or steques</i>. Add an extra operation
 * <i>catenation</i> that (destructively) concatenates two queues, stacks, or
 * steques (see EXERCISE 1.3.32). <i>Hint</i>: Use a circular linked list,
 * maintaining a pointer to the last item.
 * </p>
 */
public class Exercise_1_3_47 {
    public static void main(String[] args) {
        CatenableQueue<String> q = new CatenableQueue<>();
        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");

        CatenableQueue<String> r = new CatenableQueue<>();
        r.enqueue("D");
        r.enqueue("E");
        r.enqueue("F");
        q.catenate(r);
        q.dequeue();
        q.dequeue();

        StdOut.println(q); // [C, D, E, F]

        CatenableStack<String> s = new CatenableStack<>();
        s.push("A");
        s.push("B");
        s.push("C");

        CatenableStack<String> t = new CatenableStack<>();
        t.push("D");
        t.push("E");
        t.push("F");
        s.catenate(t);
        s.pop();
        s.pop();

        StdOut.println(s); // [D, C, B, A]
    }
}
