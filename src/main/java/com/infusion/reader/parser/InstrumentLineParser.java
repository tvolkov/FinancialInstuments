package com.infusion.reader.parser;

import com.infusion.Row;

/**
 * Created by tvolkov on 12/14/15.
 */
public class InstrumentLineParser implements LineParser {

    private static final String SEPARATOR = ",";

    @Override
    public Row parseLine(String line) {
        if (line == null){
            return null;
        }

        if (line.isEmpty()){
            return null;
        }
        String[] tokens = line.split(SEPARATOR);
        if (tokens.length != 3){
            return null;
        }
        Row row = new Row(tokens[0], tokens[1], Double.parseDouble(tokens[2]));
        return row;
    }
}
