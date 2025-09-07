package algorithms.Exercises.Exercise_1_3;

/**
 * Exercise 1.3.18
 * 
 * <p>
 * Suppose {@code x} is a linked-list node and not the last node on the list.
 * What is the effect of the following code fragment?
 * </p>
 * <p>
 * {@code x.next = x.next.next}
 * </p>
 * <p>
 * <i>Answer</i>: Deletes from the list the node immediately following
 * {@code x}.
 * </p>
 */
public class Exercise_1_3_18 {
    public static void main(String[] args) {
        // x → y → z
        // x.next = x.next.next
        // x → z
    }
}
