package com.infusion.correction;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class BoneCPConnectionProvider implements ConnectionProvider{

    private static final String URL = "jdbc:h2:tcp://localhost:11527/mem:instruments;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    private BoneCP boneCP;

    public BoneCPConnectionProvider(){
        init();
    }

    private void init() {
        try {
            Class.forName("org.h2.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load database driver class");
        }

        try {
            BoneCPConfig boneCPConfig = new BoneCPConfig();
            boneCPConfig.setJdbcUrl(URL);
            boneCPConfig.setUser(USERNAME);
            boneCPConfig.setPassword(PASSWORD);
            boneCPConfig.setMinConnectionsPerPartition(50);
            boneCPConfig.setMaxConnectionsPerPartition(100);
            boneCPConfig.setPartitionCount(1);
            this.boneCP = new BoneCP(boneCPConfig);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Connection getConnection() {
        try {
            return boneCP.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
