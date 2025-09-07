package algorithms;

import edu.princeton.cs.algs4.StdOut;

public class Rational {
    private final long numerator;
    private final long denominator;

    public Rational(int numerator, int denominator) {
        this((long) numerator, (long) denominator);
    }

    public Rational(long numerator, long denominator) {
        if (denominator == 0)
            throw new ArithmeticException("denominator is zero");

        // Normalize sign so denominator is always positive
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }

        // Reduce fraction
        long gcd = gcd(Math.abs(numerator), denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;

        assert this.denominator > 0 : "Denominator must be positive";
        assert gcd(Math.abs(this.numerator), this.denominator) == 1 : "Rational not in lowest terms";
    }

    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public Rational plus(Rational other) {
        long num = this.numerator * other.denominator + other.numerator * this.denominator;
        long den = this.denominator * other.denominator;
        return new Rational(num, den);
    }

    public Rational minus(Rational other) {
        long num = this.numerator * other.denominator - other.numerator * this.denominator;
        long den = this.denominator * other.denominator;
        return new Rational(num, den);
    }

    public Rational times(Rational other) {
        long num = this.numerator * other.numerator;
        long den = this.denominator * other.denominator;
        return new Rational(num, den);
    }

    public Rational divides(Rational other) {
        if (other.numerator == 0)
            throw new ArithmeticException("Division by zero");

        long num = this.numerator * other.denominator;
        long den = this.denominator * other.numerator;
        return new Rational(num, den);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Rational))
            return false;

        Rational r = (Rational) other;
        return numerator == r.numerator && denominator == r.denominator;
    }

    @Override
    public String toString() {
        if (denominator == 1)
            return Long.toString(numerator);
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
