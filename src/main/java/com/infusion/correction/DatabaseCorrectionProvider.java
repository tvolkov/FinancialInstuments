package com.infusion.correction;

import java.util.Map;

public class DatabaseCorrectionProvider implements CorrectionProvider {

    private Map<String, String> correctionCache;


    @Override
    public double getCorrectionForInstrument(String instrument) {
        return 0;
    }
}
