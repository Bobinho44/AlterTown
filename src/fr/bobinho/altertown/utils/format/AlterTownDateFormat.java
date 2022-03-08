package fr.bobinho.altertown.utils.format;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AlterTownDateFormat {

    /**
     * Gets a date as day:month:year format
     *
     * @param calendar the date to format
     * @return the date as day:month:year format
     */
    public static String getAsDMAFormat(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + ":" + calendar.get(Calendar.MONTH) + ":" + calendar.get(Calendar.YEAR);
    }

    /**
     * Gets the number of day since a date
     *
     * @param calendarAsString the date
     * @return the number of day since the date
     */
    public static long getAsDayIntervalFormat(String calendarAsString) {
        String[] calendarInformations = calendarAsString.split(":");
        Calendar oldCalendar = Calendar.getInstance();
        oldCalendar.set(Integer.parseInt(calendarInformations[2]), Integer.parseInt(calendarInformations[1]), Integer.parseInt(calendarInformations[0]));
        return TimeUnit.MILLISECONDS.toDays(Math.abs(oldCalendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()));
    }

}