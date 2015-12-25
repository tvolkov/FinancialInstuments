import com.infusion.App
import com.infusion.calculation.InstrumentMeanValuesCalculationEngine
import com.infusion.calculation.MeanCalculator
import com.infusion.correction.DatabaseCorrectionProvider
import com.infusion.correction.DummyCorrectionProvider
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

    dummyCorrectionProvider(DummyCorrectionProvider)

    databaseQueryRunner(H2QueryRunner)
    correctionProvider(DatabaseCorrectionProvider, databaseQueryRunner)

    meanValuesCalculationEngine(InstrumentMeanValuesCalculationEngine, App.getPathToFile(), meanCalculatorMap, correctionProvider)
}