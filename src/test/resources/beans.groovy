import com.infusion.App
import com.infusion.calculation.CalculationStrategyProvider
import com.infusion.calculation.InstrumentMetricsCalculationEngine
import com.infusion.calculation.MeanCalculationStrategy
import com.infusion.correction.CachedDatabaseMultiplierProvider
import com.infusion.correction.DatabaseMultiplierProvider
import com.infusion.correction.H2QueryRunner
import com.infusion.output.HtmlResultWriter
import com.infusion.output.PlainTextResultWriter
import com.infusion.output.printer.FilePrinter
import com.infusion.output.printer.StdOutPrinter
import org.springframework.beans.factory.config.MapFactoryBean
import org.springframework.core.io.FileSystemResource
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator

import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

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
        ] as ConcurrentHashMap
    }

    calculationStrategiesProvider(CalculationStrategyProvider, metricCalculators, 10)

    connectionProvider(SimpleDriverDataSource){
        driverClass = 'org.h2.Driver'
        url = 'jdbc:h2:mem:instruments;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE'
        username = 'sa'
        password = ''
    }

    createDatabaseScript(FileSystemResource, 'src/test/resources/create_database.sql')
//    createTriggerScript(FileSystemResource, 'src/test/resources/create_db_trigger.sql')

    initialDatabasePopulator(ResourceDatabasePopulator, [createDatabaseScript/*, createTriggerScript*/])

    dataSourceInitializer(DataSourceInitializer){
        dataSource = connectionProvider
        databasePopulator = initialDatabasePopulator
    }

    databaseQueryRunner(H2QueryRunner)


    if (System.properties.'useCachedMultiplierProvider' == 'true'){
        multiplierProvider(CachedDatabaseMultiplierProvider, databaseQueryRunner)
    } else {
        multiplierProvider(DatabaseMultiplierProvider, databaseQueryRunner)
    }

    stdOutPrinter(StdOutPrinter)
    filePrinter(FilePrinter, 'target/result.html')
    stdOutPlainTextResultWriter(PlainTextResultWriter, stdOutPrinter)
    htmlFileResultWriter(HtmlResultWriter, filePrinter)


    meanValuesCalculationEngine(InstrumentMetricsCalculationEngine, App.getPathToFile(), calculationStrategiesProvider, multiplierProvider, [stdOutPlainTextResultWriter, htmlFileResultWriter])
}