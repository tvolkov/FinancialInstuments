package com.infusion.output;

import com.infusion.output.printer.Printer;

public class PlainTextResultWriter extends AbstractResultWriter {
    private static final String SEPARATOR = "----------------------------------------";
    private static final String CRLF = System.lineSeparator();

    public PlainTextResultWriter(Printer printer) {
        super(printer);
    }

    @Override
    protected String createContent(CalculationResult calculationResult) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SEPARATOR).append(CRLF);
        stringBuilder.append("Calculated values").append(CRLF);
        stringBuilder.append(SEPARATOR).append(CRLF);
        calculationResult.getIterator().forEachRemaining(entry -> stringBuilder.append(entry.getKey()).append(": ")
                .append(entry.getValue().getResult()).append(CRLF));

        stringBuilder.append(SEPARATOR).append(CRLF)
                .append("Total instruments: ").append(calculationResult.getNumberOfInstruments()).append(CRLF)
                .append(SEPARATOR).append(CRLF)
                .append("Number of lines processed: ").append(calculationResult.getNumberOfLinesProcessed()).append(CRLF)
                .append(SEPARATOR).append(CRLF)
                .append("Time elapsed: ").append(calculateExecutionTime(calculationResult.getTotalExecutionTime()))
                .append(CRLF)
                .append(SEPARATOR).append(CRLF);
        return stringBuilder.toString();
    }
}
