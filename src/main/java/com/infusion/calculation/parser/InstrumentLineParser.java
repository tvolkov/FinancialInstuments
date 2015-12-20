package com.infusion.calculation.parser;

import com.infusion.calculation.Row;

public class InstrumentLineParser implements LineParser {

    private static final String SEPARATOR = ",";

    @Override
    public Row parseLine(String line) {
        if (line == null){
            fail("Line is null");
        }

        if (line.isEmpty()){
            fail("Line is empty");
        }
        String[] tokens = line.split(SEPARATOR);
        if (tokens.length != 3){
            fail("Incorrect format of the incoming line");
        }
        return new Row(tokens[0], tokens[1], Double.parseDouble(tokens[2]));
    }

    private void fail(String message){
        throw new IllegalArgumentException(message);
    }
}
