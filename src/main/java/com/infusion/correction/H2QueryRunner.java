package com.infusion.correction;

import java.sql.*;

public class H2QueryRunner implements DatabaseQueryRunner{
    private static final String SELECT_MULTIPLIER_QUERY_TEMPLATE = "SELECT MULTIPLIER FROM INSTRUMENT_PRICE_MODIFIER WHERE NAME = ?";
    private static final String URL = "jdbc:h2:tcp://localhost:11527/mem:instruments;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

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
    }

    @Override
    public double getMultiplierForInstument(String instrumentName) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MULTIPLIER_QUERY_TEMPLATE);
            preparedStatement.setString(1, instrumentName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            } else {
                throw new RuntimeException("incorrect table state");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
