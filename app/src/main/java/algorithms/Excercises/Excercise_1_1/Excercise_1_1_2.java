package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.2
 * 
 * <p>
 * Give the type and value of each of the following expressions:
 * </p>
 * 
 * <ol type="a">
 * <li>(1 + 2.236) / 2</li>
 * <li>1 + 2 + 3 + 4.0</li>
 * <li>4.1 >= 4</li>
 * <li>1 + 2 + "3"</li>
 * </ol>
 * 
 */
public class Excercise_1_1_2 {
    public static void main() {
        StdOut.println("Excercise 1.1.2");
        StdOut.println("(1 + 2.236) / 2 = " + ((1 + 2.236) / 2));
        StdOut.println("1 + 2 + 3 + 4.0 = " + (1 + 2 + 3 + 4.0));
        StdOut.printf("4.1 >= 4 = %b\n", 4.1 >= 4);
        StdOut.println("1 + 2 + '3' = " + (1 + 2 + "3"));
    }
}
