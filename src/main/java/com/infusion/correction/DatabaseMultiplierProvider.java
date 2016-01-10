package com.infusion.correction;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * I decided to cache the db data using guava cache, though this approach it not 100% correct.
 * This cache invalidates on timer, however ideally it should invalidate on some db trigger, or jms message, or something else.
 * I just picked that one because it's not that sophisticated.
 */
public class DatabaseMultiplierProvider implements MultiplierProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMultiplierProvider.class);

    private LoadingCache<String, Double> instrumentPriceMultipliers = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build(
                    new CacheLoader<String, Double>() {
                        @Override
                        public Double load(String instrumentName) {
                            return loadMultiplierFromDb(instrumentName);
                        }
                    });

    private DatabaseQueryRunner databaseQueryRunner;

    public DatabaseMultiplierProvider(DatabaseQueryRunner databaseQueryRunner) {
        this.databaseQueryRunner = databaseQueryRunner;
    }

    @Override
    public double getMultiplierForInstrument(String instrument) {
        try {
            return instrumentPriceMultipliers.get(instrument);
        } catch (ExecutionException e) {
            LOGGER.error("caught execution exception while getting multiplier from cache: " + e);
            return 1d;
        }
    }

    private double loadMultiplierFromDb(String instrument) {
        return databaseQueryRunner.getMultiplierForInstument(instrument);
    }
}
