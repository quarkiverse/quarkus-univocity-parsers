/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.quarkiverse.univocityparsers.it;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.univocity.parsers.common.Context;
import com.univocity.parsers.common.DataProcessingException;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.BeanProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

@Path("/univocity-parsers")
@ApplicationScoped
public class UnivocityParsersResource {
    private static final Logger log = Logger.getLogger("io.quarkiverse.univocityparsers.it.UnivocityParsersResource");

    @POST
    @Path("/csv/parse/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response csvParse(@FormParam("fromClassPathFile") final String fromClassPathFile,
            @FormParam("toClassPathClass") final String toClassPathClass)
            throws IOException, ClassNotFoundException, InterruptedException {
        List<Object> generatedRows = new ArrayList<>();

        log.infof("Univocity parsers resource timezone: [%s]", System.getProperty("user.timezone"));
        // For determining parsing ending
        int lines = 0;

        try (LineNumberReader lineNumberReader = new LineNumberReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(fromClassPathFile)))) {
            lineNumberReader.skip(Long.MAX_VALUE);
            lines = lineNumberReader.getLineNumber();
        }

        CountDownLatch latch = new CountDownLatch(lines);

        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(fromClassPathFile)) {
            CsvParserSettings parserSettings = new CsvParserSettings();
            parserSettings.getFormat().setDelimiter(';');
            parserSettings.setHeaderExtractionEnabled(true);
            parserSettings.setSkipEmptyLines(true);
            parserSettings.setProcessorErrorHandler(
                    (DataProcessingException error, Object[] inputRow,
                            ParsingContext context) -> log.warn("Error while parsing '" + fromClassPathFile + "' " +
                                    ": column '" + error.getColumnName() + "'" +
                                    ", (index '" + error.getColumnIndex() + "') has value " + inputRow[error.getColumnIndex()],
                                    error));

            Class<?> targetedClass = Class.forName(toClassPathClass);
            BeanProcessor<?> beanProcessor = new BeanProcessor(targetedClass) {
                @Override
                public void processStarted(ParsingContext context) {

                }

                @Override
                public void rowProcessed(String[] row, ParsingContext context) {

                }

                @Override
                public void processEnded(ParsingContext context) {
                }

                @Override
                public void beanProcessed(Object bean, Context context) {
                    generatedRows.add(bean);
                    latch.countDown();
                }
            };
            parserSettings.setProcessor(beanProcessor);
            CsvParser parser = new CsvParser(parserSettings);
            parser.parse(in, StandardCharsets.ISO_8859_1);
        }

        // For waiting parsing ending
        latch.await();
        return Response.ok(generatedRows).build();
    }
}
