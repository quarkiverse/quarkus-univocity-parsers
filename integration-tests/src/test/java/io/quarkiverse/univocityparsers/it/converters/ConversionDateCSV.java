package io.quarkiverse.univocityparsers.it.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import com.univocity.parsers.conversions.Conversion;

public class ConversionDateCSV implements Conversion<String, LocalDate> {

    private static final Pattern DATE_FORMAT_PATTERN = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyy");

    @Override
    public LocalDate execute(String s) {
        if (null != s && DATE_FORMAT_PATTERN.matcher(s).matches()) {
            return LocalDate.parse(s, DATE_FORMATTER);
        } else {
            return null;
        }
    }

    @Override
    public String revert(LocalDate o) {
        return null;
    }
}
