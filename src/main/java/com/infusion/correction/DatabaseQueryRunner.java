package com.infusion.correction;

import java.sql.ResultSet;

public interface DatabaseQueryRunner {
    double getMultiplierForInstument(String instrumentName);
}
