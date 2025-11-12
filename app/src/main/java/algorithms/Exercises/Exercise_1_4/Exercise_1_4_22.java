package algorithms.Exercises.Exercise_1_4;

/**
 * Exercise 1.4.22
 * 
 * <p>
 * <i>Binary search with only addition and subtraction</i>. [Mihai Patrascu]
 * Write a program that, given an array of <i>N</i> distinct <code>int</code>
 * values in ascending order, determines whether a given integer is in the
 * array. You may use only additions and subtractions and a constant amount of
 * extra memory. The running time of your program should be proportional to log
 * <i>N</i> in the worst case.
 * </p>
 * 
 * <p>
 * <i>Answer</i>: Instead of searching based on powers of two (binary search),
 * use Fibonacci numbers (which also grow exponentially). Maintain the current
 * search range to be the interval [<i>i</i>, <i>i</i> + <i>Fₖ</i>] and keep Fₖ
 * and <i>Fₖ₋₁</i>, in two variables. At each step compute <i>Fₖ₋₂</i> via
 * subtraction, check element <i>i</i> + <i>Fₖ₋₂</i>, and update the current
 * range to either [<i>i</i>, <i>i</i> + <i>Fₖ₋₂</i>] or [<i>i</i> +
 * <i>Fₖ₋₂</i>, i + <i>Fₖ₋₂</i> + <i>Fₖ₋₁</i>].
 * </p>
 */
public class Exercise_1_4_22 {
    public static void main(String[] args) {
    }
}
