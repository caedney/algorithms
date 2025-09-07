package algorithms.Exercises.Exercise_1_3;

/**
 * Exercise 1.3.23
 * 
 * <p>
 * Why does the following code fragment not do the same thing as in the previous
 * question?
 * </p>
 * {@snippet :
 * x.next = t;
 * t.next = x.next;
 * }
 * <p>
 * <i>Answer</i>: When it comes time to update {@code t.next}, {@code x.next} is
 * no longer the original node following {@code x}, but is instead {@code t}
 * itself!
 * </p>
 */
public class Exercise_1_3_23 {
    public static void main(String[] args) {
        // x → y → z
        // x.next = t
        // x → t
        // t.next = x.next;
        // x → t → ?
    }
}
