package com.infusion.correction;

import java.util.Map;

public class DatabaseCorrectionProvider implements CorrectionProvider {

    private Map<String, Double> correctionCache;
    private DBConnectionProvider dbConnectionProvider;

    public DatabaseCorrectionProvider(){
        dbConnectionProvider.getConnection().createStatement()
    }

    @Override
    public double getCorrectionForInstrument(String instrument) {
        return correctionCache.get(instrument);
    }
}
