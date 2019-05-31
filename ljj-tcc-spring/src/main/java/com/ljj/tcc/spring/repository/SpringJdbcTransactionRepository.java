package com.ljj.tcc.spring.repository;


import java.sql.Connection;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.ljj.tcc.core.repository.JdbcTransactionRepository;

/**
 * 事务持久化(数据库)
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public class SpringJdbcTransactionRepository extends JdbcTransactionRepository {

    protected Connection getConnection() {
        return DataSourceUtils.getConnection(this.getDataSource());
    }

    protected void releaseConnection(Connection con) {
        DataSourceUtils.releaseConnection(con, this.getDataSource());
    }
}
