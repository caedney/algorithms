package algorithms.Exercises.Exercise_1_3;

import algorithms.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.28
 * 
 * <p>
 * Develop a recursive solution to the previous question.
 * </p>
 */
public class Exercise_1_3_28 {
    public static void main(String[] args) {
        In in = new In("src/main/resources/data/keys.txt");
        LinkedList<Integer> list = new LinkedList<Integer>();

        while (!in.isEmpty()) {
            int item = in.readInt();
            list.append(item);
        }

        int max = list.max();
        StdOut.println(max); // 12343
    }
}
