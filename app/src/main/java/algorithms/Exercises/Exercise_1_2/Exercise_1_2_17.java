package algorithms.Exercises.Exercise_1_2;

import java.math.BigInteger;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.17
 * 
 * <p>
 * <i>Robust implementation of rational numbers</i>. Use assertions to develop
 * an implementation of {@code Rational} (see EXERCISE 1.2.16) that is immune to
 * overflow.
 * </p>
 */
public class Exercise_1_2_17 {
    public static class Rational {
        private final BigInteger numerator;
        private final BigInteger denominator;

        public Rational(BigInteger num, BigInteger den) {
            assert den != null && !den.equals(BigInteger.ZERO) : "Denominator cannot be zero";

            // Normalize sign so denominator is always positive
            if (den.signum() < 0) {
                num = num.negate();
                den = den.negate();
            }

            // Reduce fraction
            BigInteger gcd = num.gcd(den);
            this.numerator = num.divide(gcd);
            this.denominator = den.divide(gcd);

            assert this.denominator.signum() > 0 : "Denominator must be positive";
            assert numerator.gcd(denominator).equals(BigInteger.ONE) : "Rational not in lowest terms";
        }

        public Rational(int num, int den) {
            this(BigInteger.valueOf(num), BigInteger.valueOf(den));
        }

        public Rational plus(Rational other) {
            BigInteger num = this.numerator.multiply(other.denominator).add(other.numerator.multiply(this.denominator));
            BigInteger den = this.denominator.multiply(other.denominator);
            return new Rational(num, den);
        }

        public Rational minus(Rational other) {
            BigInteger num = this.numerator.multiply(other.denominator)
                    .subtract(other.numerator.multiply(this.denominator));
            BigInteger den = this.denominator.multiply(other.denominator);
            return new Rational(num, den);
        }

        public Rational times(Rational other) {
            BigInteger num = this.numerator.multiply(other.numerator);
            BigInteger den = this.denominator.multiply(other.denominator);
            return new Rational(num, den);
        }

        public Rational divides(Rational other) {
            assert !other.numerator.equals(BigInteger.ZERO) : "Division by zero";
            BigInteger num = this.numerator.multiply(other.denominator);
            BigInteger den = this.denominator.multiply(other.numerator);
            return new Rational(num, den);
        }

        public Rational reciprocal() {
            assert !this.numerator.equals(BigInteger.ZERO) : "Zero has no reciprocal";
            return new Rational(this.denominator, this.numerator);
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Rational))
                return false;
            Rational r = (Rational) other;
            return numerator.equals(r.numerator) && denominator.equals(r.denominator);
        }

        @Override
        public String toString() {
            if (denominator.equals(BigInteger.ONE))
                return numerator.toString();
            return numerator + "/" + denominator;
        }

        public BigInteger numerator() {
            return numerator;
        }

        public BigInteger denominator() {
            return denominator;
        }
    }

    public static void main(String[] args) {
        Rational x, y, z;

        // 1/2 + 1/3 = 5/6
        x = new Rational(1, 2);
        y = new Rational(1, 3);
        z = x.plus(y);
        StdOut.println(z);

        // 1/4 + 1/9 = 13/36
        x = new Rational(1, 4);
        y = new Rational(1, 9);
        z = x.plus(y);
        StdOut.println(z);

        // 8/9 + 1/9 = 1
        x = new Rational(8, 9);
        y = new Rational(1, 9);
        z = x.plus(y);
        StdOut.println(z);

        // 1/200000000 + 1/300000000 = 1/120000000
        x = new Rational(1, 200000000);
        y = new Rational(1, 300000000);
        z = x.plus(y);
        StdOut.println(z);

        // 1073741789/20 + 1073741789/30 = 1073741789/12
        x = new Rational(1073741789, 20);
        y = new Rational(1073741789, 30);
        z = x.plus(y);
        StdOut.println(z);

        // 4/17 * 17/4 = 1
        x = new Rational(4, 17);
        y = new Rational(17, 4);
        z = x.times(y);
        StdOut.println(z);

        // 3037141/3247033 * 3037547/3246599 = 841/961
        x = new Rational(3037141, 3247033);
        y = new Rational(3037547, 3246599);
        z = x.times(y);
        StdOut.println(z);

        // 1/6 - -4/-8 = -1/3
        x = new Rational(1, 6);
        y = new Rational(-4, -8);
        z = x.minus(y);
        StdOut.println(z);
    }
}
