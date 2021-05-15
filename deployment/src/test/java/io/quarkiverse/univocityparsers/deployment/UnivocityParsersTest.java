package io.quarkiverse.univocityparsers.deployment;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.BeanProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import io.quarkiverse.univocityparsers.deployment.converters.ConversionDateCSV;
import io.quarkiverse.univocityparsers.deployment.models.CSVFileExample;
import io.quarkiverse.univocityparsers.deployment.readers.CSVSettings;
import io.quarkus.test.QuarkusUnitTest;

public class UnivocityParsersTest {

    private static final Logger log = Logger.getLogger("io.quarkiverse.univocityparsers.deployment.UnivocityParsersTest");
    public static final String EXAMPLE_CSV_FILE = "example.csv";

    // Start unit test with your extension loaded
    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClasses(ConversionDateCSV.class,
                    CSVFileExample.class,
                    CSVSettings.class)
                    .addAsResource(EXAMPLE_CSV_FILE));
    private Map<String, CSVFileExample> toBeTested;

    @Test
    public void manyParseUseCaseUnitTest() throws IOException {
        Map<String, CSVFileExample> toBeTested = generatedExceptedCSVFileExamples();

        try (InputStreamReader isr = new InputStreamReader(
                Objects.requireNonNull(this.getClass().getResourceAsStream("/" + EXAMPLE_CSV_FILE)),
                Charset.forName("ISO-8859-15"))) {
            CsvParserSettings parserSettings = CSVSettings.initParser(EXAMPLE_CSV_FILE);
            BeanProcessor<CSVFileExample> beanProcessor = new BeanProcessor<CSVFileExample>(CSVFileExample.class) {
                @Override
                public void beanProcessed(CSVFileExample exampleCSVFile, ParsingContext parsingContext) {
                    assertTrue(toBeTested.containsKey(exampleCSVFile.getCode()));
                    CSVFileExample generatedExceptedCSVFileExample = toBeTested.get(exampleCSVFile.getCode());
                    assertEquals(generatedExceptedCSVFileExample, exampleCSVFile);
                    toBeTested.remove(exampleCSVFile.getCode());
                }
            };
            parserSettings.setProcessor(beanProcessor);

            CsvParser parser = new CsvParser(parserSettings);
            parser.parse(isr);
        }
        await().during(5, TimeUnit.SECONDS);
        assertEquals(0, toBeTested.size());
    }

    /**
     * Expected example.csv generated values
     *
     * @return
     */
    private Map<String, CSVFileExample> generatedExceptedCSVFileExamples() {
        Map<String, CSVFileExample> generatedMap = new HashMap<>();

        Calendar calendar = new GregorianCalendar(2015, Calendar.OCTOBER, 30);
        generatedMap.put("DMEADRA-DA", new CSVFileExample("DMEADRA-DA", 2017, LocalDate.of(2017, 9, 21),
                159.7f, Boolean.TRUE, 9876543211L, "ROGER", calendar.getTime(), new BigDecimal("12.90"), Boolean.TRUE,
                CSVFileExample.Type.USER, 143));

        calendar.set(2001, Calendar.JANUARY, 21);
        generatedMap.put("DMEADRA-DB", new CSVFileExample("DMEADRA-DB", 2018, LocalDate.of(2018, 9, 21),
                169.7f, Boolean.FALSE, 9876543212L, "MICKAEL", calendar.getTime(), new BigDecimal("0.00"), Boolean.TRUE,
                CSVFileExample.Type.SYSTEM, null));

        calendar.set(2002, Calendar.MAY, 9);
        generatedMap.put("DMEADRA-DC", new CSVFileExample("DMEADRA-DC", 2019, LocalDate.of(2019, 9, 21),
                179.7f, Boolean.FALSE, 9876543213L, "LUCAS", calendar.getTime(), new BigDecimal("98.9078"), Boolean.FALSE,
                CSVFileExample.Type.SYSTEM, 1));

        calendar.set(2017, Calendar.DECEMBER, 30);
        generatedMap.put("DMEADRA-DD", new CSVFileExample("DMEADRA-DD", 2020, LocalDate.of(2020, 8, 20),
                189.7f, Boolean.TRUE, 9876543214L, "PHILIPPE", calendar.getTime(), new BigDecimal("0.15"), Boolean.FALSE,
                CSVFileExample.Type.USER, 9999));

        calendar.set(1999, Calendar.NOVEMBER, 15);
        generatedMap.put("DMEADRA-DE", new CSVFileExample("DMEADRA-DE", 2021, LocalDate.of(2021, 8, 20),
                199.7f, Boolean.FALSE, 9876543215L, "JEAN-CLAUDE", calendar.getTime(), new BigDecimal("3999.90"), Boolean.TRUE,
                CSVFileExample.Type.USER, 2147483647));
        return generatedMap;
    }
}
