import java.util.Calendar;

public class MyDate {
    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;
    private final long coefficientYear = 4563766470487201L;
    private final long coefficientMonth = 73232;
    private final long coefficientDay = 4;
    private final long coefficientHour = 1234;
    private final long coefficientMinute = 99998;
    private final long coefficientSum = 98924;
    private final boolean randomBoolean;

    /**
     * Creates a new date consisting of a calendar and a boolean value deciding how to calculate the hash value.
     *
     * @param date          The date.
     * @param randomBoolean The boolean value used to determine the calculation of the hash value.
     */
    public MyDate(Calendar date, boolean randomBoolean) {
        this.randomBoolean = randomBoolean;
        year = date.get(Calendar.YEAR);
        month = date.get(Calendar.MONTH);
        day = date.get(Calendar.DAY_OF_MONTH);
        hour = date.get(Calendar.HOUR_OF_DAY);
        minute = date.get(Calendar.MINUTE);
    }

    /**
     * Returns the year of the date stored in this object.
     *
     * @return The year of the date stored in this object.
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the month of the date stored in this object.
     *
     * @return The month of the date stored in this object.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the day of month of the date stored in this object.
     *
     * @return The day of month of the date stored in this object.
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns the hour of the date stored in this object.
     *
     * @return The hour of the date stored in this object.
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the minute of the date stored in this object.
     *
     * @return The minute of the date stored in this object.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Return the boolean value set in the constructor.
     *
     * @return The boolean value set in the constructor.
     */
    public boolean getBool() {
        return randomBoolean;
    }

    @Override
    public int hashCode() {
        return randomBoolean ? m(m((long) m(year) * m(coefficientYear)) + m((long) m(month) * m(coefficientMonth)) + m((long) m(day) * m(coefficientDay)) + m((long) m(hour) * m(coefficientHour)) + m((long) m(minute) * m(coefficientMinute))) : m((long) m(m(year) + m(month) + m(day) + m(hour) + m(minute)) * m(coefficientSum));
    }

    private static int m(long l) {
        return Math.floorMod(l, Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass().equals(getClass()) && obj.hashCode() == hashCode();
    }
}
