package algorithms.Excercises.Excercise_1_2;

/**
 * Excercise 1.2.7
 * 
 * <p>
 * What does the following recursive function return?
 * </p>
 * 
 * {@snippet :
 * public static String mystery(String s) {
 *     int N = s.length();
 *     if (N <= 1)
 *         return s;
 *     String a = s.substring(0, N / 2);
 *     String b = s.substring(N / 2, N);
 *     return mystery(b) + mystery(a);
 * }
 * }
 */
public class Excercise_1_2_7 {
    public static String mystery(String s) {
        int N = s.length();

        if (N <= 1)
            return s;

        String a = s.substring(0, N / 2);
        String b = s.substring(N / 2, N);

        return mystery(b) + mystery(a);
    }

    public static void main(String[] args) {
    }
}
