package com.infusion.calculation.parser;

import com.infusion.calculation.Instrument;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.time.LocalDate;

public class InstrumentLineParserTest {

    private InstrumentLineParser instrumentLineParser = new InstrumentLineParser();

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnNullIfLineCanNotBeParsed(){
        //given
        String line = "fsgsfgb";

        //when
        Instrument instrument = instrumentLineParser.parseLine(line);

        //then
        assertNull(instrument);
    }

    @Test
    public void shouldReturnRowWhenParsedLine(){
        //given
        String line = "INSTRUMENT1,01-Jan-1996,2.4655";

        //when
        Instrument instrument = instrumentLineParser.parseLine(line);

        //then
        assertNotNull(instrument);
        assertEquals("INSTRUMENT1", instrument.getInstrumentName());
        assertEquals(LocalDate.of(1996, 1, 1), instrument.getDate());
        assertEquals(2.4655d, instrument.getPrice(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnNullIfLineIsEmpty(){
        //given
        String line = "";

        //when
        Instrument instrument = instrumentLineParser.parseLine(line);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnNullIfLineIsNull(){
        //given
        String line = null;

        //when
        Instrument instrument = instrumentLineParser.parseLine(line);
    }
}