package com.infusion.correction;

import javax.xml.transform.Result;
import java.lang.Thread.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class H2QueryRunner implements DatabaseQueryRunner{

    private static final String URL = "jdbc:h2:mem:test";
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
    public ResultSet executeQuery(String query) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean execute(String sql) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            return statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
