package com.infusion.calculation;

import java.time.LocalDate;

public interface CalculationStrategy {
    void calculateInstrumentMetric(LocalDate date, double priceValue);
    double getResult();
}
