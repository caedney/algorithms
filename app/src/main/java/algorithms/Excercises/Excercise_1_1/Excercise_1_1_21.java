package algorithms.Excercises.Excercise_1_1;

import java.util.Scanner;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.21
 * 
 * <p>
 * Write a program that reads in lines from standard input with each line
 * containing a name and two integers and then uses {@code printf()} to print a
 * table with a column of the names, the integers, and the result of dividing
 * the first by the second, accurate to three decimal places. You could use a
 * program like this to tabulate batting averages for baseball players or grades
 * for students.
 * </p>
 */
public class Excercise_1_1_21 {
    public static void main() {
        StdOut.println("Excercise 1.1.21");

        Scanner scanner = new Scanner(System.in);

        StdOut.printf("%-15s %10s %10s %12s%n", "Name", "Value1", "Value2", "Result");
        StdOut.println("----------------------------------------------------------");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue; // Skip empty lines

            Scanner lineScanner = new Scanner(line);
            String name = lineScanner.next(); // First token is the name
            int value1 = lineScanner.nextInt();
            int value2 = lineScanner.nextInt();

            double result = (value2 != 0) ? (double) value1 / value2 : Double.NaN;

            StdOut.printf("%-15s %10d %10d %12.3f%n", name, value1, value2, result);

            lineScanner.close();
        }

        scanner.close();
    }
}
