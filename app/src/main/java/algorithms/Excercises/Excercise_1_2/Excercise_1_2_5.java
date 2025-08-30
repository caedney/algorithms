package algorithms.Excercises.Excercise_1_2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.2.5
 * 
 * <p>
 * What does the following code fragment print?
 * </p>
 * 
 * {@snippet :
 * String s = "Hello World";
 * s.toUpperCase();
 * s.substring(6, 11);
 * StdOut.println(s);
 * }
 * 
 * <p>
 * <i>Answer</i>: {@code "Hello World"}. String objects are immutable — string
 * methods return a new {@code String} object with the appropriate value (but
 * they do not change the value of the object that was used to invoke them).
 * This code ignores the objects returned and just prints the original string.
 * To print {@code "WORLD"}, use {@code s = s.toUpperCase()} and
 * {@code s = s.substring(6, 11)}.
 * </p>
 */
public class Excercise_1_2_5 {
    public static void main(String[] args) {
        String s = "Hello World";
        s.toUpperCase();
        s.substring(6, 11);
        StdOut.println(s); // Hello World
    }
}
