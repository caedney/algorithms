package algorithms.Exercises.Exercise_1_2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.7
 * 
 * <p>
 * What does the following recursive function return?
 * </p>
 * 
 * {@snippet :
 * public static String mystery(String s) {
 *     int N = s.length();
 * 
 *     if (N <= 1)
 *         return s;
 * 
 *     String a = s.substring(0, N / 2);
 *     String b = s.substring(N / 2, N);
 * 
 *     return mystery(b) + mystery(a);
 * }
 * }
 */
public class Exercise_1_2_7 {
    public static String reverse(String s) {
        int N = s.length();

        if (N <= 1)
            return s;

        String a = s.substring(0, N / 2);
        String b = s.substring(N / 2, N);

        return reverse(b) + reverse(a);
    }

    public static void main(String[] args) {
        String reversed = reverse("The quick brown fox");
        StdOut.println("The quick brown fox");
        StdOut.println("( brown fox)(The quick)");
        StdOut.println("((n fox)( brow))((quick)(The ))");
        StdOut.println("(((fox)(n ))((row)( b)))(((ick)(qu))((e )(Th)))");
        StdOut.println("((((ox)(f))(( )(n)))(((ow)(r))((b)( ))))((((ck)(i))((u)(q)))((( )(e))((h)(T))))");
        StdOut.println("(((((x)(o))(f))(( )(n)))((((w)(o))(r))((b)( ))))(((((k)(c))(i))((u)(q)))((( )(e))((h)(T))))");
        StdOut.println(reversed);
    }
}
