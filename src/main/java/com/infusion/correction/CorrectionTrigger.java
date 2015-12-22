package com.infusion.correction;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.SQLException;

class CorrectionTrigger implements Trigger {
    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
        System.out.println("trigger init");
    }

    @Override
    public void fire(Connection connection, Object[] objects, Object[] objects1) throws SQLException {
        System.out.println("trigger fire");
    }

    @Override
    public void close() throws SQLException {
        System.out.println("trigger close");
    }

    @Override
    public void remove() throws SQLException {
        System.out.println("trigger remove");
    }
}
