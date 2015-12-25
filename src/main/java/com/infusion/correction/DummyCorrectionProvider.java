package com.infusion.correction;

public class DummyCorrectionProvider implements CorrectionProvider{
    @Override
    public double getCorrectionForInstrument(String instrument) {
        return 1d;
    }
}
