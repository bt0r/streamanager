package configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


/**
 * Created by btor on 22/03/2016.
 */
public class LogFormat extends Formatter {
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(record.getLoggerName() + " ");
        builder.append(record.getLevel() + " ");
        builder.append(record.getMessage());
        builder.append("\n");
        return builder.toString();
    }

}
