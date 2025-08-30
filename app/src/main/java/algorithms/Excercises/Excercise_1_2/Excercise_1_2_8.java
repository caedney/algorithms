package algorithms.Excercises.Excercise_1_2;

/**
 * Excercise 1.2.8
 * 
 * <p>
 * Suppose that {@code a[]} and {@code b[]} are each integer arrays consisting
 * of millions of inte-gers. What does the follow code do? Is it reasonably
 * efficient?
 * </p>
 * 
 * {@code int[] t = a; a = b; b = t; }
 * 
 * <p>
 * <i>Answer</i>. It swaps them. It could hardly be more efficient because it
 * does so by copying references, so that it is not necessary to copy millions
 * of elements.
 * </p>
 */
public class Excercise_1_2_8 {
    public static void main(String[] args) {
    }
}
