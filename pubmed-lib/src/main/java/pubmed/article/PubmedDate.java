
package pubmed.article;

import java.time.DateTimeException;
import java.time.LocalDate;

import jam.lang.JamException;

/**
 * Encapsulates a date as represented in the {@code PubMed} database.
 */
public final class PubmedDate implements Comparable<PubmedDate> {
    private final LocalDate date;

    private PubmedDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns a {@code PubMed} date instance for a fixed calendar date.
     *
     * @param date the calendar date.
     *
     * @return a {@code PubMed} date instance representing the given
     * calendar date.
     */
    public static PubmedDate instance(LocalDate date) {
        return new PubmedDate(date);
    }

    /**
     * Returns a {@code PubMed} date instance for a fixed calendar date.
     *
     * @param year the four-digit year.
     *
     * @param month the unit-offset month index (1 = January, 2 = February, ...)
     *
     * @param day the day of the month.
     *
     * @return a {@code PubMed} date instance representing the given
     * calendar date.
     */
    public static PubmedDate instance(int year, int month, int day) {
        return instance(LocalDate.of(year, month, day));
    }

    /**
     * Determines whether a YMD triplet is a valid publication date.
     *
     * @param year the year to validate.
     *
     * @param month the month to validate.
     *
     * @param day the day to validate.
     *
     * @return {@code true} iff the specified YMD triplet is a valid
     * calendar date.
     */
    public static boolean isValid(int year, int month, int day) {
        try {
            LocalDate.of(year, month, day);
            return true;
        }
        catch (DateTimeException ex) {
            return false;
        }
    }

    /**
     * Returns the standard string representation of this date:
     * {@code YYYY-MM-DD}.
     *
     * @return the standard string representation of this date.
     */
    public String format() {
        return String.format("%04d-%02d-%02d", getYear(), getMonthIndex(), getDay());
    }

    /**
     * Returns the underlying date object.
     *
     * @return the four-digit year of this date.
     */
    public LocalDate getLocalDate() {
        return date;
    }

    /**
     * Returns the four-digit year for this date.
     *
     * @return the four-digit year for this date.
     */
    public int getYear() {
        return date.getYear();
    }

    /**
     * Returns the unit-offset month index for this date:
     * (1 = January, 2 = February, ...)
     *
     * @return the unit-offset month index for this date.
     */
    public int getMonthIndex() {
        return date.getMonthValue();
    }

    /**
     * Returns the day of the month for this date.
     *
     * @return the day of the month for this date.
     */
    public int getDay() {
        return date.getDayOfMonth();
    }

    @Override public int compareTo(PubmedDate that) {
        return this.date.compareTo(that.date);
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof PubmedDate) && equalsDate((PubmedDate) obj);
    }

    private boolean equalsDate(PubmedDate that) {
        return this.date.equals(that.date);
    }

    @Override public int hashCode() {
        return date.hashCode();
    }

    @Override public String toString() {
        return String.format("PubmedDate(%s)", format());
    }
}
