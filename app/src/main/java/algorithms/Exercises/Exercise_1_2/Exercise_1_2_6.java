package algorithms.Exercises.Exercise_1_2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.6
 * 
 * <p>
 * A string {@code s} is a <i>circular rotation</i> of a string {@code t} if it
 * matches when the characters are circularly shifted by any number of
 * positions; e.g., {@code ACTGACG} is a circular shift of {@code TGACGAC}, and
 * vice versa. Detecting this condition is important in the study of genomic
 * sequences. Write a program that checks whether two given strings {@code s}
 * and {@code t} are circular shifts of one another. <i>Hint</i>: The solution
 * is a one-liner with {@code indexOf()}, {@code length()}, and string
 * concatenation.
 * </p>
 */
public class Exercise_1_2_6 {
    private static boolean isCircularShift(String s, String t) {
        return s.length() == t.length() && (s + s).indexOf(t) > -1;
    }

    public static void main(String[] args) {
        String s = "ACTGACG";
        String t = "TGACGAC";
        StdOut.println("ACTGACG - TGACGAC isCircularShift: " + isCircularShift(s, t));
    }
}
