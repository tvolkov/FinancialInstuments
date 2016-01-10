package com.infusion.correction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseMultiplierProvider implements MultiplierProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMultiplierProvider.class);
    private DatabaseQueryRunner databaseQueryRunner;

    public DatabaseMultiplierProvider(DatabaseQueryRunner databaseQueryRunner) {
        LOGGER.debug("creating non-cached DatabaseMultiplierProvider");
        this.databaseQueryRunner = databaseQueryRunner;
    }

    @Override
    public double getMultiplierForInstrument(String instrument) {
        return databaseQueryRunner.getMultiplierForInstument(instrument);
    }
}
