package com.infusion.correction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class H2QueryRunner implements DatabaseQueryRunner {
    private static final String SELECT_MULTIPLIER_QUERY_TEMPLATE = "SELECT MULTIPLIER FROM INSTRUMENT_PRICE_MODIFIER WHERE NAME = ?";

    @Autowired
    private DataSource dataSource;

    public H2QueryRunner() {}

    @Override
    public double getMultiplierForInstument(String instrumentName) {
        try (Connection connection = dataSource.getConnection()) {
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
