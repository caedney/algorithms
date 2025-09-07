package algorithms.Exercises.Exercise_1_3;

/**
 * Exercise 1.3.22
 * 
 * <p>
 * Suppose that {@code x} is a linked list {@code Node}. What does the following
 * code fragment do?
 * </p>
 * {@snippet :
 * t.next = x.next;
 * x.next = t;
 * }
 * <p>
 * <i>Answer</i>: Inserts node {@code t} immediately after node {@code x}.
 * </p>
 */
public class Exercise_1_3_22 {
    public static void main(String[] args) {
        // x → y → z
        // t.next = x.next
        // t → y → z
        // x.next = t
        // x → t → y → z
    }
}
