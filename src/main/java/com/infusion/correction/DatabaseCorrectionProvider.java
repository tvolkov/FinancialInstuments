package com.infusion.correction;

import java.sql.SQLException;
import java.util.Map;

public class DatabaseCorrectionProvider implements CorrectionProvider {
    private DatabaseQueryRunner databaseQueryRunner;

    public DatabaseCorrectionProvider(DatabaseQueryRunner databaseQueryRunner) {
        this.databaseQueryRunner = databaseQueryRunner;
    }

    @Override
    public double getCorrectionForInstrument(String instrument) {
        System.out.println("getting correction for instrument " + instrument);
//        return databaseQueryRunner.executeQuery("");
        return 1d;
    }

}
