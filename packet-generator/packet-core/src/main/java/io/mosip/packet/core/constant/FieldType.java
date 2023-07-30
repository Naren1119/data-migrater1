package io.mosip.packet.core.constant;

import java.util.Arrays;
import java.util.List;

public enum FieldType {
    DATE,
    VARCHAR,
    TIMESTAMP,
    NUMBER,
    BOOLEAN;

    private static String[] varcharTypes = {"character varying", "character", "text", "varchar"};
    private static String[] dateTypes = { "date"};
    private static String[] timestampTypes = {"time without time zone","timestamp without time zone","time without time zone","timestamp without time zone"};
    private static String[] numberTypes = {"numeric", "smallint", "bigint", "integer", "int" };
    private static String[] booleanTypes = {"boolean"};
    private static String[] decimalType = {"float"};
    private static List<String> varCharList;
    private static List<String> dateList;
    private static List<String> timestampList;
    private static List<String> numberList;
    private static List<String> booleanList;

    public static FieldType getFieldType(String dataType) {
        if(varCharList == null) varCharList = Arrays.asList(varcharTypes);
        if(dateList == null) dateList = Arrays.asList(dateTypes);
        if(timestampList == null) timestampList = Arrays.asList(timestampTypes);
        if(numberList == null) numberList = Arrays.asList(numberTypes);
        if(booleanList == null) booleanList = Arrays.asList(booleanTypes);


        if(varCharList.contains(dataType)) return VARCHAR;
        else if(dateList.contains(dataType)) return DATE;
        else if(timestampList.contains(dataType)) return TIMESTAMP;
        else if(numberList.contains(dataType)) return NUMBER;
        else if(booleanList.contains(dataType)) return BOOLEAN;
        else return null;
    }

}
