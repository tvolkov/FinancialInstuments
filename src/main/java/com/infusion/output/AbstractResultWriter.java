package com.infusion.output;

import com.infusion.output.printer.Printer;

public abstract class AbstractResultWriter implements ResultWriter {

    private Printer printer;

    public AbstractResultWriter(Printer printer){
        this.printer = printer;
    }

    @Override
    public final void writeResults(CalculationResult calculationResult) {
        printer.printResult(createContent(calculationResult));
    }

    protected abstract String createContent(CalculationResult calculationResult);


    protected String calculateExecutionTime(long executionTimeLong){
        if (executionTimeLong < 1000){
            return executionTimeLong + "ms";
        } else if (executionTimeLong > 1000 && executionTimeLong < 60000){
            return executionTimeLong / 1000 + "s " + executionTimeLong % 1000 + "ms";
        } else {
            return executionTimeLong / 60000 + "m " + executionTimeLong % 60000 / 1000 + "s ";
        }
    }
}
