package com.infusion.correction;


import java.sql.Connection;
import java.sql.SQLException;

interface DBConnectionProvider {
    Connection getConnection() throws SQLException;
}
