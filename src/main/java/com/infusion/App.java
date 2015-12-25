package com.infusion;

import com.infusion.calculation.InstrumentMeanValuesCalculationEngine;
import org.springframework.context.support.GenericGroovyApplicationContext;

import java.sql.SQLException;

public class App {
    //this is done in order to pass the path to input file to spring
    private static String PATH_TO_FILE = "/";

    public static void main( String[] args ) throws SQLException {
        GenericGroovyApplicationContext ctx = new GenericGroovyApplicationContext("classpath:beans.groovy");
        InstrumentMeanValuesCalculationEngine calculationEngine = (InstrumentMeanValuesCalculationEngine) ctx.getBean("meanValuesCalculationEngine");
        calculationEngine.calculateMetrics();
    }

    public static void setPathToFile(String path){
        App.PATH_TO_FILE = path;
    }

    public static String getPathToFile(){
        return App.PATH_TO_FILE;
    }
}
