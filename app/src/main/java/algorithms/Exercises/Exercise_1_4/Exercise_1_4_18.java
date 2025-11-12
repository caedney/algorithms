package algorithms.Exercises.Exercise_1_4;

/**
 * Exercise 1.4.18
 * 
 * <p>
 * <i>Local minimum of an array</i>. Write a program that, given an array
 * <code>a[]</code> of <i>N</i> distinct integers, finds a <i>local minimum</i>:
 * an index <code>i</code> such that <code>a[i-1] < a[i] < a[i+1]</code>. Your
 * program should use ~2lg <i>N</i> compares in the worst case.
 * </p>
 * 
 * <p>
 * <i>Answer</i>: Examine the middle value <code>a[N/2]</code> and its two
 * neighbors <code>a[N/2 - 1]</code> and <code>a[N/2 + 1]</code>. If
 * <code>a[N/2]</code> is a local minimum, stop; otherwise search in the half
 * with the smaller neighbor.
 * </p>
 */
public class Exercise_1_4_18 {
    public static void main(String[] args) {
    }
}
