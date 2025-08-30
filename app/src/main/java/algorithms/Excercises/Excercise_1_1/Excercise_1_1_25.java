package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.25
 * 
 * <p>
 * Use mathematical induction to prove that Euclid's algorithm computes the
 * greatest common divisor of any pair of nonnegative integers <i>p</i> and
 * <i>q</i>.
 * </p>
 * 
 * <b>Euclid’s algorithm</b>
 * 
 * <p>
 * For nonnegative integers 𝑝, 𝑞:
 * </p>
 * 
 * <ul>
 * <li>if 𝑞 = 0, return 𝑝</li>
 * <li>Otherwise, compute 𝑟 = 𝑝 % 𝑞 and recurse on (𝑞, 𝑟)</li>
 * </ul>
 * 
 * <p>
 * We also note it terminates because 0 ≤ 𝑟 < 𝑞, so the second argument
 * strictly decreases and cannot decrease forever.
 * </p>
 * 
 * <b>Key Lemma (gcd-invariance)</b>
 * 
 * <p>
 * For all integers, 𝑝, 𝑞 with 𝑞 ≥ 1, gcd(𝑝, 𝑞) = gcd(𝑞, 𝑝 % 𝑞).
 * </p>
 * 
 * <p>
 * <b>Proof of lemma.</b> Write 𝑝 = 𝑘𝑞 + 𝑟 with 𝑟 = 𝑝 % 𝑞
 * </p>
 * 
 * <ul>
 * <li>If 𝑑 divides both 𝑝 and 𝑞, then 𝑑 divides 𝑝 − 𝑘𝑞 = 𝑟; hence 𝑑
 * divides both 𝑞 and 𝑟.</li>
 * <li>Conversely, if 𝑑 divides both 𝑞 and 𝑟, then 𝑑 divides 𝑘𝑞 + 𝑟 = 𝑝;
 * hence 𝑑 divides both 𝑝 and 𝑞. Thus the sets of common divisors of (𝑝, 𝑞)
 * and (𝑞, 𝑟) coincide, so their greatest common divisors are equal.</li>
 * </ul>
 * 
 * <b>Induction on the second argument 𝑞 (strong induction)</b>
 * 
 * <p>
 * <b>Claim</b>. For all 𝑝 ≥ 0 and 𝑞 ≥ 0, Euclid’s algorithm on (𝑝, 𝑞)
 * returns gcd(𝑝, 𝑞).
 * </p>
 * 
 * <p>
 * <b>Base case</b> (𝑞 = 0). The algorithm returns 𝑝. By definition gcd(𝑝, 0)
 * = 𝑝. Hence correct.
 * </p>
 * 
 * <p>
 * <b>Inductive step.</b> Assume for some 𝑞 > 0 that for every pair (𝑝′, 𝑞′)
 * with 0 ≤ 𝑞′ < 𝑞, Euclid’s algorithm returns gcd⁡(𝑝′, 𝑞′). Consider
 * arbitrary 𝑝 ≥ 0 with this 𝑞. Let 𝑟 = 𝑝 % 𝑞 (so 0 ≤ 𝑟 < 𝑞). The
 * algorithm makes the recursive call on (𝑞, 𝑟). By the induction hypothesis
 * (since 𝑟 < 𝑞), this call returns gcd(𝑞, 𝑟). By the lemma, gcd⁡(𝑞, 𝑟) =
 * gcd⁡(𝑝, 𝑞). Therefore the value returned by the algorithm is gcd⁡(𝑝, 𝑞),
 * as required.
 * </p>
 * 
 * <p>
 * By strong induction, the algorithm is correct for all nonnegative 𝑝, 𝑞
 * (with the usual convention that gcd(0, 0) = 0 if needed).
 * </p>
 * 
 * <p>
 * <b>Summary</b>: Termination follows from strictly decreasing 𝑞; correctness
 * follows from the invariant gcd(𝑝, 𝑞) = gcd(𝑞, 𝑝 % 𝑞) plus strong
 * induction on 𝑞.
 * </p>
 */
public class Excercise_1_1_25 {
    public static void main() {
        StdOut.println("Excercise 1.1.25");
    }
}
