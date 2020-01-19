package com.gmail.supersonicleader.repository.impl;

import java.sql.Connection;
import java.sql.SQLException;

import com.gmail.supersonicleader.repository.ConnectionRepository;
import com.gmail.supersonicleader.util.PropertyUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import static com.gmail.supersonicleader.repository.constant.ConnectionConstant.DATABASE_PASSWORD;
import static com.gmail.supersonicleader.repository.constant.ConnectionConstant.DATABASE_URL;
import static com.gmail.supersonicleader.repository.constant.ConnectionConstant.DATABASE_USERNAME;
import static com.gmail.supersonicleader.repository.constant.ConnectionConstant.HIKARI_CACHE_PREP_STMTS;
import static com.gmail.supersonicleader.repository.constant.ConnectionConstant.HIKARI_PREP_STMT_CACHE_SIZE;
import static com.gmail.supersonicleader.repository.constant.ConnectionConstant.HIKARI_PREP_STMT_CACHE_SQL_LIMIT;

public class ConnectionRepositoryImpl implements ConnectionRepository {

    private static ConnectionRepository instance;

    private ConnectionRepositoryImpl() {
    }

    public static ConnectionRepository getInstance() {
        if (instance == null) {
            instance = new ConnectionRepositoryImpl();
        }
        return instance;
    }

    private static HikariDataSource ds;

    @Override
    public Connection getConnection() throws SQLException {
        if (ds == null) {
            PropertyUtil propertyUtil = new PropertyUtil();
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(propertyUtil.getProperty(DATABASE_URL));
            config.setUsername(propertyUtil.getProperty(DATABASE_USERNAME));
            config.setPassword(propertyUtil.getProperty(DATABASE_PASSWORD));
            config.addDataSourceProperty(HIKARI_CACHE_PREP_STMTS, propertyUtil.getProperty(HIKARI_CACHE_PREP_STMTS));
            config.addDataSourceProperty(HIKARI_PREP_STMT_CACHE_SIZE, propertyUtil.getProperty(HIKARI_PREP_STMT_CACHE_SIZE));
            config.addDataSourceProperty(HIKARI_PREP_STMT_CACHE_SQL_LIMIT, propertyUtil.getProperty(HIKARI_PREP_STMT_CACHE_SQL_LIMIT));
            ds = new HikariDataSource(config);
        }
        return ds.getConnection();
    }

}
