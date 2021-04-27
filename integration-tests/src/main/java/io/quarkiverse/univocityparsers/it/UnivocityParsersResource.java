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

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
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
            throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        Set<Object> generatedRows = new HashSet<>();

        // For determining parsing ending
        File file = new File(this.getClass().getResource("/" + fromClassPathFile).toURI());
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
        lineNumberReader.skip(Long.MAX_VALUE);
        int lines = lineNumberReader.getLineNumber();
        CountDownLatch latch = new CountDownLatch(lines);
        lineNumberReader.close();

        try (InputStream is = new FileInputStream(file)) {

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
            parser.parse(is, Charset.forName("ISO-8859-15"));

        }

        // For waiting parsing ending
        latch.await();
        Response response = Response.ok(generatedRows).build();
        return response;
    }
}
