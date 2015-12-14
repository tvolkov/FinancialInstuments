package com.infusion.reader.parser;

import com.infusion.Row;

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
