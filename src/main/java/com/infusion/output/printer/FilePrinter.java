package com.infusion.output.printer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FilePrinter implements Printer {
    private final String pathToFile;

    FilePrinter(String pathToFile){
        this.pathToFile = pathToFile;
    }

    @Override
    public void printResult(String content) {
        try (PrintWriter printWriter = new PrintWriter(new File(pathToFile))){
            printWriter.println(content);
            printWriter.flush();
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }
    }
}
