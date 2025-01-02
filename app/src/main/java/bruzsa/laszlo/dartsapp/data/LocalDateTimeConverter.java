package bruzsa.laszlo.dartsapp.data;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTimeConverter {

    @TypeConverter
    public static LocalDateTime toLocaleDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDateTime.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateTimeString(LocalDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }

    @TypeConverter
    public static LocalDate toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDate.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateString(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }

}
