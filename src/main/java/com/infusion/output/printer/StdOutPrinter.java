package com.infusion.output.printer;

public class StdOutPrinter implements Printer {
    @Override
    public void printResult(String content) {
        System.out.println(content);
    }
}
