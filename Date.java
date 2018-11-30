import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date {

    public static String func() {

        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        LocalDateTime now = LocalDateTime.now();

        return date.format(now);
    }

}
