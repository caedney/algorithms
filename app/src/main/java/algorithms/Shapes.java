package algorithms;

/**
 * Abstract Methods
 *
 * - Declared <b>without a body</b> in an abstract class or interface.
 * - Must be implemented by <b>subclasses</b> (or implementing classes).
 */

abstract class Shape {
    abstract double area();
}

class Circle extends Shape {
    double radius;

    Circle(double r) {
        radius = r;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}
