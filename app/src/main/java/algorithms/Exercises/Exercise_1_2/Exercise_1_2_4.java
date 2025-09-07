package algorithms.Exercises.Exercise_1_2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.4
 * 
 * <p>
 * What does the following code fragment print?
 * </p>
 *
 * {@snippet :
 * String string1 = "hello";
 * String string2 = string1;
 * string1 = "world";
 * StdOut.println(string1);
 * StdOut.println(string2);
 * }
 */
public class Exercise_1_2_4 {
    public static void main(String[] args) {
        String string1 = "hello";
        String string2 = string1;
        string1 = "world";
        StdOut.println(string1); // world
        StdOut.println(string2); // hello
    }
}
