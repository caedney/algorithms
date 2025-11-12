package algorithms.Exercises.Exercise_1_4;

/******************************************************************************
 * Exercise 1.4.23
 * 
 * <p>
 * <i>Binary search for a fraction</i>. Devise a method that uses a logarithmic
 * number of queries of the form <i>Is the number less than x</i>? to find a
 * rational number 𝑝/𝑞 such that 0 < 𝑝 < 𝑞 < 𝑁. <i>Hint</i>: Two fractions
 * with denominators less than 𝑁 cannot differ by more than 1/𝑁².
 * </p>
 * 
 * <p>
 * <i>Correction</i>: the hint's inequality is printed reversed. Two distinct
 * fractions with denominators less than 𝑁 cannot differ by <i>less</i> than
 * 1/𝑁²; they are always further apart than that, never closer.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_23
 * ./gradlew test --tests "Exercise_1_4_23Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_23 {
    static class Fraction {
        long p;
        long q;

        public Fraction(long p, long q) {
            this.p = p;
            this.q = q;
        }

        public String toString() {
            return p + "/" + q;
        }
    }

    static class Oracle {
        Fraction target;
        int denominatorBound;
        int count;

        public Oracle(int denominatorBound, Fraction target) {
            if (0 >= target.p || target.p >= target.q || target.q >= denominatorBound) {
                throw new IllegalArgumentException("Need 0 < p < q < N");
            }

            this.target = target;
            this.denominatorBound = denominatorBound;
        }

        public boolean isTargetLessThan(Fraction x) {
            count++;
            return target.p * x.q < x.p * target.q;
        }
    }

    public static Fraction findFraction(Oracle oracle) {
        long lo = 0;
        long hi = 1;
        long denominator = 1;
        int N = oracle.denominatorBound;
        long limit = (long) N * N;

        while (denominator < limit) {
            Fraction midpoint = new Fraction(lo + hi, 2 * denominator);

            // System.out.println("lo: " + lo + " hi: " + hi + " denominator: " +
            // denominator);

            if (oracle.isTargetLessThan(midpoint)) {
                lo = 2 * lo;
                hi = midpoint.p;
            } else {
                lo = midpoint.p;
                hi = 2 * hi;
            }

            denominator *= 2;
        }

        // System.out.println("lo: " + lo + " hi: " + hi + " denominator: " +
        // denominator);

        for (int q = 2; q < N; q++) {
            long p = (lo * q + denominator - 1) / denominator;

            if (p * denominator < hi * q) {
                return new Fraction(p, q);
            }
        }

        throw new IllegalStateException("No candidate in interval - Phase 1 invariant broken");
    }

    public static void main(String[] args) {
        Oracle oracle = new Oracle(10, new Fraction(3, 7));
        Fraction fraction = findFraction(oracle);
        System.out.println("Target fraction: " + fraction);
    }
}
