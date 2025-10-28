package algorithms;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * Whitelist
 * 
 * <pre>
 * ./gradlew run -PmainClass=Whitelist --args="src/data/algs4/tinyW.txt" < "app/src/data/algs4/tinyT.txt"
 * </pre>
 ******************************************************************************/
public class Whitelist {
    public static void main(String[] args) {
        int[] whitelist = new In(args[0]).readAllInts();
        StaticSETofInts set = new StaticSETofInts(whitelist);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();

            if (set.rank(key) == -1)
                StdOut.println(key);
        }
    }
}
