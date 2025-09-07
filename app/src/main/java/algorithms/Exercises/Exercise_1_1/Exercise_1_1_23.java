package algorithms.Exercises.Exercise_1_1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import algorithms.BinarySearch;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.23
 * 
 * <p>
 * Add to the {@code BinarySearch} test client the ability to respond to a
 * second argument: + to print numbers from standard input that <i>are not</i>
 * in the whitelist, - to print numbers that <i>are</i> in the whitelist.
 * </p>
 */
public class Exercise_1_1_23 {
    public static void main(String[] args) throws IOException {
        System.setIn(new FileInputStream("src/data/algs4/tinyT.txt"));
        In in = new In("src/data/algs4/tinyW.txt");
        String included = "-";

        int[] whitelist = in.readAllInts();
        Arrays.sort(whitelist);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            int ranked = BinarySearch.rank(key, whitelist);

            if (ranked > -1 && included == "+")
                StdOut.println(key);
            else if (ranked < 0 && included == "-")
                StdOut.println(key);
        }
    }
}
