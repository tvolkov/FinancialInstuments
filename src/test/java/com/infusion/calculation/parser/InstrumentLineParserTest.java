package com.infusion.calculation.parser;

import com.infusion.calculation.Row;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InstrumentLineParserTest {

    private InstrumentLineParser instrumentLineParser = new InstrumentLineParser();

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnNullIfLineCanNotBeParsed(){
        //given
        String line = "fsgsfgb";

        //when
        Row row = instrumentLineParser.parseLine(line);

        //then
        assertNull(row);
    }

    @Test
    public void shouldReturnRowWhenParsedLine(){
        //given
        String line = "INSTRUMENT1,01-Jan-1996,2.4655";

        //when
        Row row = instrumentLineParser.parseLine(line);

        //then
        assertNotNull(row);
        assertEquals("INSTRUMENT1", row.getInstrumentName());
        assertEquals("01-Jan-1996", row.getDate());
        assertEquals(2.4655d, row.getPrice(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnNullIfLineIsEmpty(){
        //given
        String line = "";

        //when
        Row row = instrumentLineParser.parseLine(line);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnNullIfLineIsNull(){
        //given
        String line = null;

        //when
        Row row = instrumentLineParser.parseLine(line);
    }
}