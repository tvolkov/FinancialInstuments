package com.infusion.correction;

public class DatabaseMultiplierProvider implements MultiplierProvider {


    private DatabaseQueryRunner databaseQueryRunner;

    public DatabaseMultiplierProvider(DatabaseQueryRunner databaseQueryRunner) {
        this.databaseQueryRunner = databaseQueryRunner;
    }

    @Override
    public double getMultiplierForInstrument(String instrument) {
        return databaseQueryRunner.getMultiplierForInstument(instrument);
    }
}
