package algorithms.Exercises.Exercise_1_2;

import algorithms.SmartDate;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.11
 * 
 * <p>
 * Develop an implementation {@code SmartDate} of our {@code Date} API that
 * raises an exception if the date is not legal.
 * </p>
 */
public class Exercise_1_2_11 {
    public static void main(String[] args) {
        SmartDate date = new SmartDate(29, 2, 1988);
        StdOut.println(date);
    }
}
