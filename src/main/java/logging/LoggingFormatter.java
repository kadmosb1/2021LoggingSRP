package logging;

import login.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoggingFormatter extends Formatter {

    protected static String getFormattedDate (LocalDateTime date) {
        return date.format (DateTimeFormatter.ofPattern ("dd-MM-yyyy"));
    }

    protected static String getFormattedDateAndTime (LocalDateTime time) {
        return getFormattedDate (time) + time.format (DateTimeFormatter.ofPattern (" hh:mm:ss"));
    }

    private String getFileNameForLogging () {
        LocalDateTime now = LocalDateTime.now ();
        return "src\\main\\resources\\logging\\" + getFormattedDate (now) + ".log";
    }

    protected File getFileForLogging () {
        return new File (getFileNameForLogging ());
    }

    protected String format (LogRecord record, LocalDateTime time) {
        String pre = getFormattedDateAndTime(time) + " ";
        pre += String.format ("%-20s ", AuthenticationSimple.getInstance ().getUserNameOfActiveUser ());
        return pre + formatMessage (record) + System.lineSeparator ();
    }

    @Override
    public String format(LogRecord record) {
        return format (record, LocalDateTime.now ());
    }

    protected boolean logFileIsEmpty() {
        File file = getFileForLogging ();
        return !file.exists () || (file.length () == 0);
    }

    @Override
    public String getHead (Handler handler) {

        // Als de logfile nog niet bestaat moet bovenaan de file een header worden geplaatst.
        if (logFileIsEmpty()) {
            return String.format("%-19s %-20s %s%n", "Date", "Gebruikersnaam", "Logging");
        }
        else {
            return super.getHead(handler);
        }
    }
}