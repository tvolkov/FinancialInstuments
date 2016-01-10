package com.infusion.output;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.infusion.calculation.CalculationStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class HtmlResultWriter extends FileResultWriter {

    public HtmlResultWriter(String filePath) {
        super(filePath);
    }

    @Override
    public void writeResults(Iterator<Map.Entry<String, CalculationStrategy>> iterator,
                             int numberOfInstruments, long numberOfLinesProcessed, long totalExecutionTime) {
        MustacheFactory mf = new DefaultMustacheFactory();
        HashMap<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("totalInstruments", numberOfInstruments);
        scopes.put("numberOfLinesProcessed", numberOfLinesProcessed);
        scopes.put("timeElapsed", totalExecutionTime);
        scopes.put("calculatedValues", getCalculatedValuesList(iterator));
        Mustache mustache = mf.compile("template.mustache");
        try {
            mustache.execute(new PrintWriter(new File(filePath)), scopes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<String> getCalculatedValuesList(Iterator<Map.Entry<String, CalculationStrategy>> iterator){
        List<String> list = new ArrayList<>();
        iterator.forEachRemaining(entry -> {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey()).append(": ").append(entry.getValue().getResult());
            list.add(sb.toString());
        });
        return list;
    }
}
