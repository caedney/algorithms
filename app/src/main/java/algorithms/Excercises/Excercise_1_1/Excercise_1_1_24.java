package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.24
 * 
 * <p>
 * Give the sequence of values of <i>p</i> and <i>q</i> that are computed when
 * Euclid's algorithm is used to compute the greatest common divisor of 105 and
 * 24. Extend the code given on page 4 to develop a program {@code Euclid} that
 * takes two integers from the command line and computes their greatest common
 * divisor, printing out the two arguments for each call on the recursive
 * method. Use your program to compute the greatest common divisor or 1111111
 * and 1234567.
 * </p>
 * 
 */
public class Excercise_1_1_24 {
    public static int gcd(int p, int q) {
        StdOut.println("p = " + p + ", q = " + q);

        if (q == 0)
            return p;

        return gcd(q, p % q);
    }

    public static void main(String[] args) {
        StdOut.println("Excercise 1.1.24");

        if (args.length < 2) {
            StdOut.println("Usage: java Euclid <p> <q>");
            return;
        }

        int p = Integer.parseInt(args[0]);
        int q = Integer.parseInt(args[1]);

        int result = gcd(p, q);

        StdOut.println("GCD = " + (result == 1 ? "relatively prime" : result));

        // Two numbers are relatively prime (or coprime) if their gcd is 1.
        // This does not mean both numbers are prime.
        // It just means they don’t share any common factor greater than 1
    }
}
