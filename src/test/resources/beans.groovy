import com.infusion.App
import com.infusion.calculation.InstrumentMeanValuesCalculationEngine
import com.infusion.calculation.MeanCalculator
import com.infusion.correction.BoneCPConnectionProvider
import com.infusion.correction.DatabaseMultiplierProvider
import com.infusion.correction.DummyMultiplierProvider
import com.infusion.correction.H2QueryRunner
import org.springframework.beans.factory.config.MapFactoryBean

beans {
    xmlns context:"http://www.springframework.org/schema/context"
    context.'component-scan'('base-package': "com.infusion")

    meanCalculatorMap(MapFactoryBean) {
        sourceMap = [
                "INSTRUMENT1": new MeanCalculator(),
                "INSTRUMENT2": new MeanCalculator("Nov-2014"),
                "INSTRUMENT3": new MeanCalculator("2014"),
        ]
    }

    dummyCorrectionProvider(DummyMultiplierProvider)

    connectionProvider(BoneCPConnectionProvider)

    databaseQueryRunner(H2QueryRunner, connectionProvider)
    correctionProvider(DatabaseMultiplierProvider, databaseQueryRunner)

    meanValuesCalculationEngine(InstrumentMeanValuesCalculationEngine, App.getPathToFile(), meanCalculatorMap, correctionProvider)
}