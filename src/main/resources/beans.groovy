import com.infusion.App
import com.infusion.calculation.CalculationStrategyProvider
import com.infusion.calculation.InstrumentMetricsCalculationEngine
import com.infusion.calculation.MeanCalculationStrategy
import com.infusion.output.StdOutResultWriter
import com.infusion.correction.DatabaseMultiplierProvider

import com.infusion.correction.H2QueryRunner
import com.jolbox.bonecp.BoneCPDataSource
import org.springframework.beans.factory.config.MapFactoryBean

import java.time.LocalDate

beans {
    xmlns context:"http://www.springframework.org/schema/context"
    context.'component-scan'('base-package': "com.infusion")

    totalMeanCalculationStrategy(MeanCalculationStrategy)
    november2014MeanCalculationStrategy(MeanCalculationStrategy, LocalDate.of(2014, 11, 1), LocalDate.of(2014, 11, 30))
    year2014MeanCalculator(MeanCalculationStrategy, LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31))

    metricCalculators(MapFactoryBean) {
        sourceMap = [
                "INSTRUMENT1": totalMeanCalculationStrategy,
                "INSTRUMENT2": november2014MeanCalculationStrategy,
                "INSTRUMENT3": year2014MeanCalculator,
        ]
    }

    calculationStrategiesProvider(CalculationStrategyProvider, metricCalculators)

    connectionProvider(BoneCPDataSource) {
        driverClass = 'org.h2.Driver'
        jdbcUrl = 'jdbc:h2:tcp://localhost:11527/mem:instruments;DB_CLOSE_DELAY=-1'
        user = 'sa'
        password = ''
        minConnectionsPerPartition = 500
        maxConnectionsPerPartition = 1000
        partitionCount = 2
    }

    databaseQueryRunner(H2QueryRunner)

    multiplierProvider(DatabaseMultiplierProvider, databaseQueryRunner)

    resultWriter(StdOutResultWriter)

    meanValuesCalculationEngine(InstrumentMetricsCalculationEngine, App.getPathToFile(), calculationStrategiesProvider, multiplierProvider, resultWriter)
}