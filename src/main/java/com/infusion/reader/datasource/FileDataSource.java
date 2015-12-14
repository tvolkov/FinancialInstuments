package com.infusion.reader.datasource;

import javafx.scene.input.InputMethodTextRun;

import java.io.*;
import java.util.Scanner;

/**
 * Created by tvolkov on 13.12.2015.
 */
public class FileDataSource implements InputDataSource{

    private String pathToFile;
    private FileInputStream fileInputStream;
//    private BufferedReader bufferedReader;
    private Scanner scanner;
    private boolean hasNextLine = true;


    public FileDataSource(String pathToFile) throws FileNotFoundException {
        this.pathToFile = pathToFile;
        this.fileInputStream = new FileInputStream(pathToFile);
//        this.bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        this.scanner = new Scanner(fileInputStream, "UTF-8");
    }

    public String getNextLine() throws IOException {
        String line = scanner.nextLine();
        if (scanner.ioException() != null){
            throw scanner.ioException();
        }

        if (!scanner.hasNextLine()){
            fileInputStream.close();
            scanner.close();
        }
        return line;
    }

    public boolean hasNextLine(){
        return scanner.hasNextLine();
    }
}
