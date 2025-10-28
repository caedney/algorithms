package algorithms;

import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code Example} class demonstrates the different types of methods
 * available in Java: instance methods, static methods, final methods,
 * synchronized methods, varargs methods, and constructors.
 */

public class Methods {
    private int value;
    private static int counter;

    /**
     * Constructs an {@code Example} object with the given initial value.
     *
     * @param v the initial value for this instance
     */
    public Methods(int v) {
        this.value = v;
    }

    /**
     * Returns twice the given integer.
     *
     * <p>
     * This is a helper method used internally to perform doubling operations.
     * </p>
     *
     * @param x the integer to double
     * @return the result of doubling {@code x} (i.e., 2 * x)
     */
    private static int doubleInt(int x) {
        return 2 * x;
    }

    /**
     * Returns four times the given integer.
     *
     * <p>
     * This method uses the {@link #doubleInt(int)} method internally
     * to double the input twice.
     * </p>
     *
     * @param x the integer to quadruple
     * @return the result of quadrupling {@code x} (i.e., 4 * x)
     * @see #doubleInt(int)
     */
    public static int quadrupleInt(int x) {
        return doubleInt(doubleInt(x));
    }

    /**
     * Returns the value of this instance.
     *
     * @return the current value of this instance
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the current global counter shared across all instances.
     *
     * @return the global counter
     */
    public static int getCounter() {
        return counter;
    }

    /**
     * Logs a message to the console. Only used internally by this class.
     *
     * @param msg the message to log
     */
    private void log(String msg) {
        StdOut.println("LOG: " + msg);
    }

    /**
     * Prints a locked message. This method cannot be overridden by subclasses.
     */
    public final void cannotChange() {
        StdOut.println("Locked behavior");
    }

    /**
     * Safely increments the value of this instance in a thread-safe way.
     * <p>
     * Because this method is {@code synchronized}, only one thread can execute it
     * at a time on the same instance.
     * </p>
     */
    public synchronized void safeIncrement() {
        value++;
    }

    /**
     * Prints all the arguments passed to it.
     *
     * @param args variable number of strings to print
     */
    public void printAll(String... args) {
        for (String a : args)
            log(a);
    }

    /*
     * Example placeholders for advanced method types:
     *
     * {@code abstract} methods must be implemented in a subclass.
     * {@code native} methods are defined in another language (e.g., C).
     */
}
