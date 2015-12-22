package com.infusion.correction;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import javax.swing.text.html.HTMLDocument;
import java.sql.Connection;
import java.sql.SQLException;

//todo make it a singleton
class BoneCPConnectionProvider implements DBConnectionProvider {

    private BoneCP connectionPool;

    public BoneCPConnectionProvider(){
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

        try {
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl("jdbc:h2:mem:test");
            config.setUsername("sa");
            config.setPassword("");
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            connectionPool = new BoneCP(config);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("unable to create connection pool");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }
}
