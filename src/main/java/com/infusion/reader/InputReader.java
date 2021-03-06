package com.infusion.reader;

/**
 * Created by tvolkov on 12.12.15.
 */
public interface InputReader extends Runnable {

    /**
     * read the input file and pass its lines one by one
     */
    void processInputData();
}
