package com.infusion.calculation;

import java.time.LocalDate;

public class Instrument {
    private final String instrumentName;
    private final LocalDate date;
    private final double price;

    public Instrument(String instrumentName, LocalDate date, double price) {
        this.instrumentName = instrumentName;
        this.date = date;
        this.price = price;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }
}
