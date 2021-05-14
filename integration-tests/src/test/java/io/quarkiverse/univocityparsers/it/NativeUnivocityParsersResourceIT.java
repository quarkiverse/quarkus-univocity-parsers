package io.quarkiverse.univocityparsers.it;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeUnivocityParsersResourceIT extends UnivocityParsersResourceTest {

    /**
     * Expected example.csv generated values
     *
     * @return
     */
    @Override
    protected Map<String, String> generatedExceptedCSVFileExamples() {

        Map<String, String> jsonStringResult = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        builder.append(
                "{\"admin\":").append(true).append(",")
                .append("\"billable\":").append(true).append(",")
                .append("\"code\":").append("\"DMEADRA-DA\"").append(",");
        builder.append("\"createdAt\":").append("\"2015-10-29T23:00:00Z[UTC]\"").append(",");
        builder.append("\"date\":").append("\"2017-09-21\"").append(",")
                .append("\"fees\":").append("12.90").append(",")
                .append("\"profile\":").append(9876543211L).append(",")
                .append("\"stars\":").append(143).append(",")
                .append("\"type\":").append("\"USER\"").append(",")
                .append("\"user\":").append("\"ROGER\"").append(",")
                .append("\"weight\":").append(159.7).append(",")
                .append("\"years\":").append(2017).append("}");
        jsonStringResult.put("\"DMEADRA-DA\"", builder.toString());
        builder.setLength(0);

        builder.append(
                "{\"admin\":").append(true).append(",")
                .append("\"billable\":").append(false).append(",")
                .append("\"code\":").append("\"DMEADRA-DB\"").append(",");
        builder.append("\"createdAt\":").append("\"2001-01-20T23:00:00Z[UTC]\"").append(",");
        builder.append("\"date\":").append("\"2018-09-21\"").append(",")
                .append("\"fees\":").append("0.00").append(",")
                .append("\"profile\":").append(9876543212L).append(",")
                .append("\"type\":").append("\"SYSTEM\"").append(",")
                .append("\"user\":").append("\"MICKAEL\"").append(",")
                .append("\"weight\":").append(169.7).append(",")
                .append("\"years\":").append(2018).append("}")
                .toString();
        jsonStringResult.put("\"DMEADRA-DB\"", builder.toString());
        builder.setLength(0);

        builder.append(
                "{\"admin\":").append(false).append(",")
                .append("\"billable\":").append(false).append(",")
                .append("\"code\":").append("\"DMEADRA-DC\"").append(",");
        builder.append("\"createdAt\":").append("\"2002-05-08T22:00:00Z[UTC]\"").append(",");
        builder.append("\"date\":").append("\"2019-09-21\"").append(",")
                .append("\"fees\":").append(98.9078).append(",")
                .append("\"profile\":").append(9876543213L).append(",")
                .append("\"stars\":").append(1).append(",")
                .append("\"type\":").append("\"SYSTEM\"").append(",")
                .append("\"user\":").append("\"LUCAS\"").append(",")
                .append("\"weight\":").append(179.7).append(",")
                .append("\"years\":").append(2019).append("}")
                .toString();
        jsonStringResult.put("\"DMEADRA-DC\"", builder.toString());
        builder.setLength(0);

        builder.append(
                "{\"admin\":").append(false).append(",")
                .append("\"billable\":").append(true).append(",")
                .append("\"code\":").append("\"DMEADRA-DD\"").append(",");
        builder.append("\"createdAt\":").append("\"2017-12-29T23:00:00Z[UTC]\"").append(",");
        builder.append("\"date\":").append("\"2020-08-20\"").append(",")
                .append("\"fees\":").append(0.15).append(",")
                .append("\"profile\":").append(9876543214L).append(",")
                .append("\"stars\":").append(9999).append(",")
                .append("\"type\":").append("\"USER\"").append(",")
                .append("\"user\":").append("\"PHILIPPE\"").append(",")
                .append("\"weight\":").append(189.7).append(",")
                .append("\"years\":").append(2020).append("}")
                .toString();
        jsonStringResult.put("\"DMEADRA-DD\"", builder.toString());
        builder.setLength(0);

        builder.append(
                "{\"admin\":").append(true).append(",")
                .append("\"billable\":").append(false).append(",")
                .append("\"code\":").append("\"DMEADRA-DE\"").append(",");
        builder.append("\"createdAt\":").append("\"1999-11-14T23:00:00Z[UTC]\"").append(",");
        builder.append("\"date\":").append("\"2021-08-20\"").append(",")
                .append("\"fees\":").append("3999.90").append(",")
                .append("\"profile\":").append(9876543215L).append(",")
                .append("\"stars\":").append(2147483647).append(",")
                .append("\"type\":").append("\"USER\"").append(",")
                .append("\"user\":").append("\"JEAN-CLAUDE\"").append(",")
                .append("\"weight\":").append(199.7).append(",")
                .append("\"years\":").append(2021).append("}")
                .toString();
        jsonStringResult.put("\"DMEADRA-DE\"", builder.toString());
        return jsonStringResult;
    }
}
