package com.infusion.calculation.parser;

import com.infusion.calculation.Row;

/**
 * Created by tvolkov on 12.12.2015.
 */
public interface LineParser {

    /**
     * returns null if line can't be parsed
     * @param line
     * @return
     */
   Row parseLine(String line);
}
