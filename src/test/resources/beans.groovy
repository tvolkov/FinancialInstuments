import com.infusion.App
import com.infusion.calculation.InstrumentMeanValuesCalculationEngine
import com.infusion.calculation.MeanCalculator
import com.infusion.correction.DummyCorrectionProvider
import org.springframework.beans.factory.config.MapFactoryBean

beans {
    xmlns context:"http://www.springframework.org/schema/context"
    context.'component-scan'('base-package': "spring.groovy.scan")

    meanCalculatorMap(MapFactoryBean) {
        sourceMap = [
                "INSTRUMENT1": new MeanCalculator(),
                "INSTRUMENT2": new MeanCalculator("Nov-2014"),
                "INSTRUMENT3": new MeanCalculator("2014"),
        ]
    }

    correctionProvider(DummyCorrectionProvider)

    meanValuesCalculationEngine(InstrumentMeanValuesCalculationEngine, App.getPathToFile(), meanCalculatorMap, correctionProvider)
}