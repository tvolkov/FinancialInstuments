package com.infusion.correction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class H2QueryRunner implements DatabaseQueryRunner{

    private DBConnectionProvider dbConnectionProvider;

    @Override
    public String executeQuery(String query) {
        Connection connection;
        try {
            connection = dbConnectionProvider.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get connection to database");
        }

        System.out.println("Retrieved connection");
        try {
            Statement statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
