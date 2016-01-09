package com.infusion.calculation.parser;

import com.infusion.calculation.Instrument;

public interface LineParser {
   Instrument parseLine(String line);
}
