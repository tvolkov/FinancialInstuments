import com.infusion.App
import com.infusion.calculation.InstrumentMeanValuesCalculationEngine
import com.infusion.calculation.MeanCalculator
import com.infusion.calculation.StdOutResultWriter
import com.infusion.correction.DatabaseMultiplierProvider
import com.infusion.correction.DummyMultiplierProvider
import com.infusion.correction.H2QueryRunner
import com.jolbox.bonecp.BoneCPDataSource
import org.apache.commons.math3.stat.descriptive.moment.Mean
import org.springframework.beans.factory.config.MapFactoryBean

import java.time.LocalDate

beans {
    xmlns context:"http://www.springframework.org/schema/context"
    context.'component-scan'('base-package': "com.infusion")

    defaultMeanCalculator(MeanCalculator)
    November2014MeanCalculator(MeanCalculator, new Mean(), LocalDate.of(2014, 11, 1), LocalDate.of(2014, 11, 30))
    Year2014MeanCalculator(MeanCalculator, new Mean(), LocalDate.of(2014, 1, 1), LocalDate.of(2014, 12, 31))

    meanCalculatorMap(MapFactoryBean) {
        sourceMap = [
                "INSTRUMENT1": defaultMeanCalculator,
                "INSTRUMENT2": November2014MeanCalculator,
                "INSTRUMENT3": Year2014MeanCalculator,
        ]
    }

    dummyMultiplierProvider(DummyMultiplierProvider)

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

    meanValuesCalculationEngine(InstrumentMeanValuesCalculationEngine, App.getPathToFile(), meanCalculatorMap, multiplierProvider, resultWriter)
}