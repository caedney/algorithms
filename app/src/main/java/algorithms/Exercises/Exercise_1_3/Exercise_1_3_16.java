package algorithms.Exercises.Exercise_1_3;

import java.util.Arrays;

import algorithms.Date;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.16
 * 
 * <p>
 * Using {@code readInts()} on page 126 as a model, write a static method
 * {@code readDates()} for {@code Date} that reads dates from standard input in
 * the format specified in the table on page 119 and returns an array containing
 * them.
 * </p>
 * 
 * {@snippet :
 * public static int[] readInts(String name) {
 *     In in = new In(name);
 *     Queue<Integer> q = new Queue<Integer>();
 * 
 *     while (!in.isEmpty())
 *         q.enqueue(in.readInt());
 * 
 *     int n = q.size();
 *     int[] a = new int[n];
 * 
 *     for (int i = 0; i < n; i++)
 *         a[i] = q.dequeue();
 * 
 *     return a;
 * }
 * }
 */
public class Exercise_1_3_16 {
    public static void main(String[] args) {
        Date[] dates = Date.readDates("src/main/resources/data/dates.txt");
        StdOut.println(Arrays.toString(dates));
    }
}
