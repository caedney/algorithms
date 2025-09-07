package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.8
 * 
 * <p>
 * What do each of the following print?
 * </p>
 * 
 * <ol type="a">
 * <li>StdOut.println('b');</li>
 * <li>StdOut.println('b' + 'c');</li>
 * <li>StdOut.println((char) ('a' + 4));</li>
 * </ol>
 * 
 * <p>
 * Explain each outcome.
 * </p>
 */
public class Exercise_1_1_8 {
    public static void main(String[] args) {
        StdOut.println('b'); // b
        // 'b' is a char literal.
        // Java treats it as a character.
        // Underlying value: ASCII value 98, but since it’s a char, it displays as the
        // character 'b'.
        StdOut.println('b' + 'c'); // 197
        // Both 'b' and 'c' are char literals.
        // In arithmetic, Java promotes char to int automatically.
        // 'b' → 98, 'c' → 99
        // Addition: 98 + 99 = 197
        StdOut.println((char) ('a' + 4)); // e
        // 'a' is a char literal.
        // In arithmetic, Java promotes char to int automatically.
        // 'a' → 97
        // Addition: 97 + 4 = 101
        // (char) 101 → converts back to a character → 'e'
    }
}
