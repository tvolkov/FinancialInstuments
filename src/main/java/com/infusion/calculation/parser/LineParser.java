package com.infusion.calculation.parser;

import com.infusion.calculation.Instrument;

public interface LineParser {

    /**
     * returns null if line can't be parsed
     * @param line
     * @return
     */
   Instrument parseLine(String line);
}
