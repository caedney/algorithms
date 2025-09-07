package algorithms;

public class SmartDate {
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
