package com.infusion.correction;

/**for tests only*/
public class DummyMultiplierProvider implements MultiplierProvider {
    @Override
    public double getMultiplierForInstrument(String instrument) {
        return 1d;
    }
}
