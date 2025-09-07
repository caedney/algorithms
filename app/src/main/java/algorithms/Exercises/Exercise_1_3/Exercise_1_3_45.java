package algorithms.Exercises.Exercise_1_3;

import algorithms.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.45
 * 
 * <p>
 * <i>Stack generability</i>. Suppose that we have a sequence of intermixed
 * <i>push</i> and <i>pop</i> operations as with our test stack client, where
 * the integers {@code 0, 1, ..., N-1} in that order (<i>push</i> directives)
 * are intermixed with 𝑁 minus signs (<i>pop</i> directives). Devise an
 * algorithm that determines whether the intermixed sequence causes the stack to
 * underflow. (You may use only an amount of space independent of 𝑁—you cannot
 * store the integers in a data structure.) Devise a linear-time algorithm that
 * determines whether a given permutation can be generated as output by our test
 * client (depending on where the <i>pop</i> directives occur).
 * </p>
 * 
 * <p>
 * <i>Solution</i>: The stack does not overflow unless there exists an integer
 * 𝑘 such that the first 𝑘 pop operations occur before the first 𝑘 push
 * operations. If a given permutation can be generated, it is uniquely generated
 * as follows: if the next integer in the output permutation is in the top of
 * the stack, pop it; otherwise, push it onto the stack.
 * </p>
 */
public class Exercise_1_3_45 {
    public static boolean willUnderflow(String[] operations) {
        int size = 0; // current number of elements in the stack

        for (String op : operations) {
            if (op.equals("push"))
                size++;
            else if (op.equals("pop")) {
                size--;

                if (size < 0)
                    return true; // underflow occurs
            }
        }

        return false; // no underflow
    }

    public static boolean isStackPermutation(int[] permutation) {
        Stack<Integer> stack = new Stack<>();
        int nextPush = 0; // next number to push

        for (int x : permutation) {
            // Push numbers until we reach x
            while (nextPush <= x) {
                stack.push(nextPush);
                nextPush++;
            }

            // Check if x is at the top
            if (stack.peek() == x)
                stack.pop();
            else
                return false; // x is not at the top, cannot generate permutation
        }

        return true;
    }

    public static void main(String[] args) {
        String[] ops = { "push", "pop", "pop" };
        StdOut.println("Will [\"push\", \"pop\", \"pop\"] underflow? " + (willUnderflow(ops) ? "Yes" : "No"));
        // Will underflow? Yes

        int[] perm1 = { 2, 1, 0 };
        int[] perm2 = { 2, 0, 1 };

        System.out.println("Is [2, 1, 0] stack-generable? " + (isStackPermutation(perm1) ? "Yes" : "No")); // "Yes"
        System.out.println("Is [2, 0, 1] stack-generable? " + (isStackPermutation(perm2) ? "Yes" : "No")); // "No"
    }
}
