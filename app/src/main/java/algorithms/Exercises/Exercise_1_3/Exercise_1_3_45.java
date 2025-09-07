package algorithms.Exercises.Exercise_1_3;

/**
 * Exercise 1.3.45
 * 
 * <p>
 * <i>Stack generability</i>. Suppose that we have a sequence of intermixed
 * <i>push</i> and pop operations as with our test stack client, where the
 * integers {@code 0, 1, ..., N-1} in that order (<i>push</i> directives) are
 * intermixed with 𝑁 minus signs (<i>pop</i> directives). Devise an algorithm
 * that determines whether the intermixed sequence causes the stack to
 * under-flow. (You may use only an amount of space independent of 𝑁—you cannot
 * store the integers in a data structure.) Devise a linear-time algorithm that
 * determines whether a given permutation can be generated as output by our test
 * client (depending on where the <i>pop</i> directives occur).
 * </p>
 * <p>
 * <i>Solution</i>: The stack does not overflow unless there exists an integer
 * 𝑘 such that the first 𝑘 pop operations occur before the first 𝑘 push
 * operations. If a given permutation can be generated, it is uniquely generated
 * as follows: if the next integer in the output permutation is in the top of
 * the stack, pop it; otherwise, push it onto the stack.
 * </p>
 */
public class Exercise_1_3_45 {
    public static void main(String[] args) {
    }
}
