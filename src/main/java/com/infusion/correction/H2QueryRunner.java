package com.infusion.correction;

import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class H2QueryRunner implements DatabaseQueryRunner{

    private DBConnectionProvider dbConnectionProvider;

    public H2QueryRunner() {
        init();
    }

    private void init() {
        try {
            // load the database driver (make sure this is in your classpath!)
            Class.forName("org.h2.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load database driver class");
        }

        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
             Statement statement = connection.createStatement();) {
            String sql = "CREATE TRIGGER MULTIPLIER_TRIGGER AFTER UPDATE ON INSTRUMENT_PRICE_MODIFIER FOR EACH ROW CALL \"com.infusion.correction.CorrectionTrigger\";";
            boolean result = statement.execute(sql);
            if (!result){
                throw new RuntimeException("error executing sql");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

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
        return null;
    }
}
