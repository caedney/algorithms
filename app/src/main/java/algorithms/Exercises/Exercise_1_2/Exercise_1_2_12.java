package algorithms.Exercises.Exercise_1_2;

import algorithms.SmartDate;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.12
 * 
 * <p>
 * Add a method {@code dayOfTheWeek()} to {@code SmartDate} that returns a
 * {@code String} value {@code Monday}, {@code Tuesday}, {@code Wednesday},
 * {@code Thursday}, {@code Friday}, {@code Saturday}, or {@code Sunday}, giving
 * the appropriate day of the week for the date. You may assume that the date is
 * in the 21st century.
 * </p>
 */
public class Exercise_1_2_12 {
    public static void main(String[] args) {
        SmartDate date = new SmartDate(31, 8, 2025);
        String day = date.dayOfTheWeek();
        StdOut.println(date + " is a " + day);
    }
}
