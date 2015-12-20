package com.infusion.calculation.parser;

import com.infusion.calculation.Row;

public interface LineParser {

    /**
     * returns null if line can't be parsed
     * @param line
     * @return
     */
   Row parseLine(String line);
}
