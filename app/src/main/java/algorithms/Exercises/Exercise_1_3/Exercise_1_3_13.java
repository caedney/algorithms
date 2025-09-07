package algorithms.Exercises.Exercise_1_3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.13
 * 
 * <p>
 * Suppose that a client performs an intermixed sequence of (queue)
 * <i>enqueue</i> and <i>dequeue</i> operations. The enqueue operations put the
 * integers 0 through 9 in order onto the queue; the dequeue operations print
 * out the return value. Which of the following sequence(s) could <i>not</i>
 * occur?
 * </p>
 * 
 * <pre>
 *<i>a.</i> 0 1 2 3 4 5 6 7 8 9
 *<i>b.</i> 4 6 8 7 5 3 2 9 0 1
 *<i>c.</i> 2 5 6 7 4 8 9 3 1 0
 *<i>d.</i> 4 3 2 1 0 5 6 7 8 9
 * </pre>
 */
public class Exercise_1_3_13 {
    public static void main(String[] args) {
        StdOut.println("✅ 0 1 2 3 4 5 6 7 8 9");
        // enqueue(0, 1, 2, 3, 4, 5, 6, 7, 8, 9) → dequeue(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        StdOut.println("❌ 4 6 8 7 5 3 2 9 0 1");
        // enqueue(0, 1, 2, 3, 4) → dequeue(0, 1, 2, 3, 4)
        StdOut.println("❌ 2 5 6 7 4 8 9 3 1 0");
        // enqueue(0, 1, 2) → dequeue(0, 1, 2)
        StdOut.println("❌ 4 3 2 1 0 5 6 7 8 9");
        // enqueue(0, 1, 2, 3, 4) → dequeue(0, 1, 2, 3, 4)
    }
}
