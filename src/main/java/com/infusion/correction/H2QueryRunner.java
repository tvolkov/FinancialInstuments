package com.infusion.correction;

import java.sql.*;

public class H2QueryRunner implements DatabaseQueryRunner{
    private static final String SELECT_MULTIPLIER_QUERY_TEMPLATE = "SELECT MULTIPLIER FROM INSTRUMENT_PRICE_MODIFIER WHERE NAME = ?";

    private ConnectionProvider connectionProvider;

    public H2QueryRunner(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public double getMultiplierForInstument(String instrumentName) {
        try (Connection connection = connectionProvider.getConnection()) {
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
