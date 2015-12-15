package com.infusion.correction;

/**
 * Created by tvolkov on 12.12.15.
 */
public interface CorrectionProvider {

    //TODO we will cache the correction data.
    //TODO Cache will be invalidated either on db trigger or on quartz timer (need to decide)
    double getCorrectionForInstrument(String instrument);
}
