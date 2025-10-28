package algorithms;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * run {@code ./gradlew run --args="src/data/algs4/tinyW.txt" <
 * "app/src/data/algs4/tinyT.txt" -PmainClass=Whitelist}
 */
public class Whitelist {
    public static void main(String[] args) {
        int[] w = new In(args[0]).readAllInts();
        StaticSETofInts set = new StaticSETofInts(w);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            if (set.rank(key) == -1)
                StdOut.println(key);
        }
    }
}
