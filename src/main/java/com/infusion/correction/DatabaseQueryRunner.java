package com.infusion.correction;

import java.sql.ResultSet;

public interface DatabaseQueryRunner {
    ResultSet executeQuery(String query);
    boolean execute(String sql);
}
