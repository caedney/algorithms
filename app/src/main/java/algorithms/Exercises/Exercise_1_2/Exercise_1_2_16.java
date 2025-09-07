package algorithms.Exercises.Exercise_1_2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.16
 * 
 * <p>
 * <i>Rational numbers.</i> Implement an immutable data type {@code Rational}
 * for rational numbers that supports addition, subtraction, multiplication, and
 * division.
 * </p>
 * {@code public class Rational}
 * <table>
 * <tbody>
 * <tr>
 * <td></td>
 * <td colspan="2">{@code Rational(int numerator. int denominator)}</td>
 * </tr>
 * <tr>
 * <td>{@code Rational}</td>
 * <td>{@code plus(Rational b)}</td>
 * <td><i>sum of this number and b</i></td>
 * </tr>
 * <tr>
 * <td>{@code Rational}</td>
 * <td>{@code minus(Rational b)}</td>
 * <td><i>difference of this number and b</i></td>
 * </tr>
 * <tr>
 * <td>{@code Rational}</td>
 * <td>{@code times(Rational b)}</td>
 * <td><i>product of this number and b</i></td>
 * </tr>
 * <tr>
 * <td>{@code Rational}</td>
 * <td>{@code divides(Rational b)}</td>
 * <td><i>quotient of this number and b</i></td>
 * </tr>
 * <tr>
 * <td>{@code boolean}</td>
 * <td>{@code equals(Rational that)}</td>
 * <td><i>is this number equal to {@code that}</i></td>
 * </tr>
 * <tr>
 * <td>{@code String}</td>
 * <td>{@code toString()}</td>
 * <td><i>string representation</i></td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * <p>
 * You do not have to worry about testing for overflow (see EXERCISE 1.2.17),
 * but use as instance variables two {@code long} values that represent the
 * numerator and denominator to limit the possibility of overflow. Use Euclid's
 * algorithm (see page 4) to ensure that the numerator and denominator never
 * have any common factors. Include a test client that exercises all of your
 * methods.
 * </p>
 */
public class Exercise_1_2_16 {
    public static class Rational {
        long numerator;
        long denominator;

        public Rational(long numerator, long denominator) {
            if (denominator == 0) {
                throw new ArithmeticException("denominator is zero");
            }

            long g = gcd(numerator, denominator);
            this.numerator = numerator / g;
            this.denominator = denominator / g;
        }

        private static long gcd(long p, long q) {
            if (q == 0)
                return p;

            return gcd(q, p % q);
        }

        public Rational plus(Rational that) {
            long f = gcd(this.numerator, that.numerator);
            long g = gcd(this.denominator, that.denominator);

            Rational s = new Rational(
                    (this.numerator / f) * (that.denominator / g) + (that.numerator / f) * (this.denominator / g),
                    this.denominator * (that.denominator / g));

            s.numerator *= f;

            return s;
        }

        public Rational negate() {
            return new Rational(-numerator, denominator);
        }

        public Rational minus(Rational b) {
            return this.plus(b.negate());
        }

        public Rational times(Rational b) {
            Rational c = new Rational(this.numerator, b.denominator);
            Rational d = new Rational(b.numerator, this.denominator);
            return new Rational(c.numerator * d.numerator, c.denominator * d.denominator);
        }

        public Rational reciprocal() {
            return new Rational(denominator, numerator);
        }

        public Rational divides(Rational b) {
            return this.times(b.reciprocal());
        }

        public boolean equals(Rational that) {
            if (that == null)
                return false;
            if (that.getClass() != this.getClass())
                return false;

            return this.compareTo(that) == 0;
        }

        public int compareTo(Rational b) {
            long lhs = this.numerator * b.denominator;
            long rhs = this.denominator * b.numerator;
            if (lhs < rhs)
                return -1;
            if (lhs > rhs)
                return +1;

            return 0;
        }

        @Override
        public String toString() {
            if (denominator == 1)
                return numerator + "";
            else
                return numerator + "/" + denominator;
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
