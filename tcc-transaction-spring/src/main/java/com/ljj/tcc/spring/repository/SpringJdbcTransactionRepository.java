package com.ljj.tcc.spring.repository;


import java.sql.Connection;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.ljj.tcc.core.repository.JdbcTransactionRepository;

/**
 * Created by liangjinjing on 10/30/15.
 */
public class SpringJdbcTransactionRepository extends JdbcTransactionRepository {

    protected Connection getConnection() {
        return DataSourceUtils.getConnection(this.getDataSource());
    }

    protected void releaseConnection(Connection con) {
        DataSourceUtils.releaseConnection(con, this.getDataSource());
    }
}
