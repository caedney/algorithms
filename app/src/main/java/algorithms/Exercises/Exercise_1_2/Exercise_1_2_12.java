package algorithms.Exercises.Exercise_1_2;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.2.12
 * 
 * <p>
 * Add a method {@code dayOfTheWeek()} to {@code SmartDate} that returns a
 * {@code String} value {@code Monday}, {@code Tuesday}, {@code Wednesday},
 * {@code Thursday}, {@code Friday}, {@code Saturday}, or {@code Sunday}, giving
 * the appropriate day of the week for the date. You may assume that the date is
 * in the 21st century.
 * </p>
 */
public class Exercise_1_2_12 {
    public static class SmartDate {
        private static final int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        private final int day;
        private final int month;
        private final int year;

        public SmartDate(int d, int m, int y) {
            if (!isValidDate(d, m, y)) {
                throw new IllegalArgumentException("Is not a valid date");
            }

            day = d;
            month = m;
            year = y;
        }

        public int day() {
            return day;
        }

        public int month() {
            return month;
        }

        public int year() {
            return year;
        }

        public String dayOfTheWeek() {
            int day = day();
            int month = month();
            int year = year();

            if (month < 3) {
                month += 12;
                year -= 1;
            }

            int K = year % 100;
            int J = year / 100;

            // Zeller’s Congruence
            int h = (day + (13 * (month + 1)) / 5 + K + (K / 4) + (J / 4) + (5 * J)) % 7;

            String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
            int index = (h + 5) % 7;

            return days[index];
        }

        private static boolean isValidDate(int d, int m, int y) {
            if (d < 1 || d > DAYS[m])
                return false;
            if (m < 1 || m > 12)
                return false;
            if (m == 2 && d == 29 && !isLeapYear(y))
                return false;
            return true;
        }

        private static boolean isLeapYear(int y) {
            if (y % 400 == 0)
                return true;
            if (y % 100 == 0)
                return false;
            return y % 4 == 0;
        }

        @Override
        public String toString() {
            return day() + "/" + month() + "/" + year();
        }
    }

    public static void main(String[] args) {
        SmartDate date = new SmartDate(31, 8, 2025);
        String day = date.dayOfTheWeek();
        StdOut.println(date + " is a " + day);
    }
}
