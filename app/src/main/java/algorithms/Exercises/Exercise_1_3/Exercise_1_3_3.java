package algorithms.Exercises.Exercise_1_3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.3
 * 
 * <p>
 * Suppose that a client performs an intermixed sequence of (stack) <i>push</i>
 * and <i>pop</i> operations. The push operations put the integers 0 through 9
 * in order onto the stack; the pop operations print out the return values.
 * Which of the following sequence(s) could <i>not</i> occur?
 * </p>
 * 
 * <pre>
 *<i>a.</i> 4 3 2 1 0 9 8 7 6 5
 *<i>b.</i> 4 6 8 7 5 3 2 9 0 1
 *<i>c.</i> 2 5 6 7 4 8 9 3 1 0
 *<i>d.</i> 4 3 2 1 0 5 6 7 8 9
 *<i>e.</i> 1 2 3 4 5 6 9 8 7 0
 *<i>f.</i> 0 4 6 5 3 8 1 7 2 9
 *<i>g.</i> 1 4 7 9 8 6 5 3 0 2
 *<i>h.</i> 2 1 4 3 6 5 8 7 9 0
 * </pre>
 */
public class Exercise_1_3_3 {
    public static void main(String[] args) {
        StdOut.println("✅ 4 3 2 1 0 9 8 7 6 5");
        // push(0,1,2,3,4) → pop(4,3,2,1,0)
        // push(5,6,7,8,9) → pop(9,8,7,6,5)
        StdOut.println("❌ 4 6 8 7 5 3 2 9 0 1");
        // push(0,1,2,3,4) → pop(4)
        // push(5,6) → pop(6)
        // push(7,8) → pop(8,7,5,3,2)
        // push(9) → pop(9,1,0)
        StdOut.println("✅ 2 5 6 7 4 8 9 3 1 0");
        // push(0,1,2) → pop(2)
        // push(3,4,5) → pop(5)
        // push(6) → pop(6)
        // push(7) → pop(7,4)
        // push(8) → pop(8)
        // push(9) → pop(9,3,1,0)
        StdOut.println("✅ 4 3 2 1 0 5 6 7 8 9");
        // push(0,1,2,3,4) → pop(4,3,2,1,0)
        // push(5) → pop(5)
        // push(6) → pop(6)
        // push(7) → pop(7)
        // push(8) → pop(8)
        // push(9) → pop(9)
        StdOut.println("✅ 1 2 3 4 5 6 9 8 7 0");
        // push(0,1) → pop(1)
        // push(2) → pop(2)
        // push(3) → pop(3)
        // push(4) → pop(4)
        // push(5) → pop(5)
        // push(6) → pop(6)
        // push(7,8,9) → pop(9,8,7,0)
        StdOut.println("❌ 0 4 6 5 3 8 1 7 2 9");
        // push(0) → pop(0)
        // push(1,2,3,4) → pop(4)
        // push(5,6) → pop(6,5,3)
        // push(7,8) → pop(8,7,2,1)
        StdOut.println("❌ 1 4 7 9 8 6 5 3 0 2");
        // push(0,1) → pop(1)
        // push(2,3,4) → pop(4)
        // push(5,6,7) → pop(7)
        // push(8,9) → pop(9,8,6,5,3,2,0)
        StdOut.println("✅ 2 1 4 3 6 5 8 7 9 0");
        // push(0,1,2) → pop(2,1)
        // push(3,4) → pop(4,3)
        // push(5,6) → pop(6,5)
        // push(7,8) → pop(8,7)
        // push(9) → pop(9,0)
    }
}
