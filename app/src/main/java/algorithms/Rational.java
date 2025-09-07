package algorithms;

import java.math.BigInteger;

import edu.princeton.cs.algs4.StdOut;

public class Rational {
    private final BigInteger numerator;
    private final BigInteger denominator;

    public Rational(int numerator, int denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public Rational(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO))
            throw new ArithmeticException("denominator is zero");

        // Normalize sign so denominator is always positive
        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }

        // Reduce fraction
        BigInteger gcd = numerator.gcd(denominator);
        this.numerator = numerator.divide(gcd);
        this.denominator = denominator.divide(gcd);

        assert this.denominator.signum() > 0 : "Denominator must be positive";
        assert numerator.gcd(denominator).equals(BigInteger.ONE) : "Rational not in lowest terms";
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

    public static void main(String[] args) {
        Rational x, y, z;

        x = new Rational(1, 2);
        y = new Rational(1, 3);
        z = x.plus(y);
        StdOut.println(z); // 5/6

        x = new Rational(1, 4);
        y = new Rational(1, 9);
        z = x.plus(y);
        StdOut.println(z); // 13/36

        x = new Rational(8, 9);
        y = new Rational(1, 9);
        z = x.plus(y);
        StdOut.println(z); // 1

        x = new Rational(1, 200000000);
        y = new Rational(1, 300000000);
        z = x.plus(y);
        StdOut.println(z); // 1/120000000

        x = new Rational(1073741789, 20);
        y = new Rational(1073741789, 30);
        z = x.plus(y);
        StdOut.println(z); // 1073741789/12

        x = new Rational(4, 17);
        y = new Rational(17, 4);
        z = x.times(y);
        StdOut.println(z); // 1

        x = new Rational(3037141, 3247033);
        y = new Rational(3037547, 3246599);
        z = x.times(y);
        StdOut.println(z); // 841/961

        x = new Rational(1, 6);
        y = new Rational(-4, -8);
        z = x.minus(y);
        StdOut.println(z); // -1/3
    }
}
