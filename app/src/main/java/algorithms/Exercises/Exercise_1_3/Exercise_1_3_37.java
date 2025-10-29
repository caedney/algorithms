package algorithms.Exercises.Exercise_1_3;

import algorithms.Josephus;

/**
 * Exercise 1.3.37
 * 
 * <p>
 * <i>Josephus problem</i>. In the Josephus problem from antiquity, 𝑁 people
 * are in dire straits and agree to the following strategy to reduce the
 * population. They arrange themselves in a circle (at positions numbered from 0
 * to 𝑁-1) and proceed around the circle, eliminating every 𝑀th person until
 * only one person is left. Legend has it that Josephus figured out where to sit
 * to avoid being eliminated. Write a {@code Queue} client {@code Josephus} that
 * takes 𝑁 and 𝑀 from the command line and prints out the order in which
 * people are eliminated (and thus would show Josephus where to sit in the
 * circle).
 * </p>
 * 
 * {@snippet :
 * % java Josephus 7 2
 * 1 3 5 0 4 2 6
 * }
 */
public class Exercise_1_3_37 {
    public static void main(String[] args) {
        Josephus.main(new String[] { "7", "2" });
    }
}
