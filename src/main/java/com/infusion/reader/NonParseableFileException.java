package com.infusion.reader;

/**
 * Created by tvolkov on 13.12.2015.
 */
public class NonParseableFileException extends RuntimeException {

    public NonParseableFileException(String message){
        super(message);
    }
}
