package com.infusion.output;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.infusion.calculation.CalculationStrategy;
import com.infusion.output.printer.Printer;

import java.io.StringWriter;
import java.util.*;

public class HtmlResultWriter extends AbstractResultWriter {

    public HtmlResultWriter(Printer printer) {
        super(printer);
    }

    @Override
    protected String createContent(CalculationResult calculationResult) {
        MustacheFactory mf = new DefaultMustacheFactory();
        HashMap<String, Object> scopes = new HashMap<>();
        scopes.put("totalInstruments", calculationResult.getNumberOfInstruments());
        scopes.put("numberOfLinesProcessed", calculationResult.getNumberOfLinesProcessed());
        scopes.put("timeElapsed", calculateExecutionTime(calculationResult.getTotalExecutionTime()));
        scopes.put("calculatedValues", getCalculatedValuesList(calculationResult.getIterator()));
        Mustache mustache = mf.compile("template.mustache");
        StringWriter stringWriter = new StringWriter();
        mustache.execute(stringWriter, scopes);
        return stringWriter.toString();
    }

    private List<String> getCalculatedValuesList(Iterator<Map.Entry<String, CalculationStrategy>> iterator){
        List<String> list = new ArrayList<>();
        iterator.forEachRemaining(entry -> list.add(entry.getKey() + ": " + entry.getValue().getResult()));
        return list;
    }
}
