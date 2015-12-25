package com.infusion.correction;

import java.sql.Connection;

public interface ConnectionProvider {
    Connection getConnection();
}
