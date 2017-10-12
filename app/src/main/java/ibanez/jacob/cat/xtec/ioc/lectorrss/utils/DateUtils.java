package ibanez.jacob.cat.xtec.ioc.lectorrss.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class with date utils
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class DateUtils {

    public static final String RSS_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";

    /**
     * This method transforms a string in a given format to a {@link Date}
     *
     * @param date   The String representing a date
     * @param format The format of the string. See {@link SimpleDateFormat}
     * @return The resulting {@link Date} if all went OK. {@code null} otherwise
     */
    public static Date stringToDate(String date, String format) {
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.parse(date, pos);
    }

    /**
     * This method transforms a {@link Date} to a String in a given format
     *
     * @param date   The {@link Date}
     * @param format The format of the resulting string. See {@link SimpleDateFormat}
     * @return The String formatted according to {@code format} parameter if all went OK.
     * {@code null} otherwise
     */
    public static String dateToString(Date date, String format) {
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}
