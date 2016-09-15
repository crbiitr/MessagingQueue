package com.example.messagingqueue.datasource;

import org.skife.jdbi.v2.DBI;

/**
 * Created by Chetan
 */
public class DataSourceFactory {
    public static DataSource getDataSource(String dataSourceName, DBI jdbi) {
        if (dataSourceName.equalsIgnoreCase("MySQL")) {
            if (jdbi == null) {
                throw new IllegalArgumentException("Jdbi cannot be null");
            }
            return new MySQLDataBaseDataSource(jdbi);
        }
        throw new IllegalArgumentException("No Such DataSource is available");
    }
}
