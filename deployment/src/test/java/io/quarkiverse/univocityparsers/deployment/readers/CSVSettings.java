package io.quarkiverse.univocityparsers.deployment.readers;

import org.jboss.logging.Logger;

import com.univocity.parsers.common.DataProcessingException;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.csv.CsvParserSettings;

public class CSVSettings {
    private static final Logger log = Logger.getLogger("io.quarkiverse.univocityparsers.readers.CSVSettings");

    public static CsvParserSettings initParser(String filename) {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.getFormat().setDelimiter(';');
        parserSettings.setHeaderExtractionEnabled(true);
        parserSettings.setSkipEmptyLines(true);
        parserSettings.setProcessorErrorHandler(
                (DataProcessingException error, Object[] inputRow,
                        ParsingContext context) -> log.warn("Error while parsing '" + filename + "' " +
                                ": column '" + error.getColumnName() + "'" +
                                ", (index '" + error.getColumnIndex() + "') has value " + inputRow[error.getColumnIndex()],
                                error));
        return parserSettings;
    }
}
