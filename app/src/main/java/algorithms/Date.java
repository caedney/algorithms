package algorithms;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Date implements Comparable<Date> {
    private final int day;
    private final int month;
    private final int year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date(String str) {
        String[] date = str.split("/");

        try {
            this.day = Integer.parseInt(date[0]);
            this.month = Integer.parseInt(date[1]);
            this.year = Integer.parseInt(date[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Date must of format DD/MM/YYYY");
        }
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

    @Override
    public boolean equals(Object date) {
        if (this == date)
            return true;
        if (date == null)
            return false;
        if (this.getClass() != date.getClass())
            return false;

        Date other = (Date) date;
        if (this.day != other.day)
            return false;
        if (this.month != other.month)
            return false;
        if (this.year != other.year)
            return false;

        return true;
    }

    @Override
    public int compareTo(Date other) {
        if (this.year != other.year)
            return this.year - other.year;

        if (this.month != other.month)
            return this.month - other.month;

        return this.day - other.day;
    }

    @Override
    public String toString() {
        return day() + "/" + month() + "/" + year();
    }

    public static void main(String[] args) {
        Date date = new Date("22/05/1939");
        StdOut.println(date);
    }
}
