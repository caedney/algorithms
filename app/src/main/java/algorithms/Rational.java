package algorithms;

public class Rational {
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
