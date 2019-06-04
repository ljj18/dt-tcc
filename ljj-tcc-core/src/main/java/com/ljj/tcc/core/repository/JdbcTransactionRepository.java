
package com.ljj.tcc.core.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.xa.Xid;

import org.apache.commons.lang3.StringUtils;

import com.ljj.tcc.api.TransactionPhase;
import com.ljj.tcc.core.ColumnName;
import com.ljj.tcc.core.Transaction;
import com.ljj.tcc.core.serializer.KryoPoolSerializer;
import com.ljj.tcc.core.serializer.ObjectSerializer;
import com.ljj.tcc.core.utils.DateUtils;

/**
 * 事务持久化到数据库
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public class JdbcTransactionRepository extends CachableTransactionRepository {

    /*
     * 事务表名
     */
    private String tableName = "tcc_transaction";
    
    /*
     * Domain, 可以理解成应用
     */
    private String domain;
    /*
     * 数据源 
     */
    private DataSource dataSource;

    /*
     *  
     */
    private ObjectSerializer<Transaction> serializer = new KryoPoolSerializer<Transaction>();

    /**
     * 
     * @param tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setSerializer(ObjectSerializer<Transaction> serializer) {
        this.serializer = serializer;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
    
    protected int doCreate(Transaction transaction) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = this.getConnection();
            StringBuilder builder = new StringBuilder(256);
            builder.append("INSERT INTO ");
            builder.append(getTableName());
            builder.append(" (");
            builder.append(ColumnName.GLOBAL_TX_ID);
            builder.append(",");
            builder.append(ColumnName.BRANCH_QUALIFIER);
            builder.append(",");
            builder.append(ColumnName.TRANSACTION_TYPE);
            builder.append(",");
            builder.append(ColumnName.CONTENT);
            builder.append(",");
            builder.append(ColumnName.RETRIED_COUNT);
            builder.append(",");
            builder.append(ColumnName.GMT_CREATE);
            builder.append(",");
            builder.append(ColumnName.GMT_MODIFIED);
            builder.append(",");
            builder.append(ColumnName.VERSION);
            if (StringUtils.isNotEmpty(domain)) {
                builder.append(",");
                builder.append(ColumnName.DOMAIN);
                builder.append(" ) VALUES (?,?,?,?,?,?,?,?,?,?)");
            } else {
                builder.append(" ) VALUES (?,?,?,?,?,?,?,?,?)");
            }
            stmt = connection.prepareStatement(builder.toString());
            stmt.setBytes(1, transaction.getXid().getGlobalTransactionId());
            stmt.setBytes(2, transaction.getXid().getBranchQualifier());
            stmt.setInt(3, transaction.getTransactionType().getId());
            stmt.setBytes(4, serializer.serialize(transaction));
            stmt.setInt(5, transaction.getPhase().getId());
            stmt.setInt(6, transaction.getRetriedCount());
            stmt.setDate(7, new java.sql.Date(DateUtils.localDateTimeToDate(transaction.getGmtCreate()).getTime()));
            stmt.setDate(8, new java.sql.Date(DateUtils.localDateTimeToDate(transaction.getGmtMidified()).getTime()));
            stmt.setLong(9, transaction.getVersion());
            if (StringUtils.isNotEmpty(domain)) {
                stmt.setString(10, domain);
            }
            stmt.executeUpdate();
            return 1;
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                return 0;
            } else {
                throw new TransactionIOException(e);
            }
        } catch (Throwable throwable) {
            throw new TransactionIOException(throwable);
        }
        finally {
            closeStatement(stmt);
            this.releaseConnection(connection);
        }
    }

    protected int doUpdate(Transaction transaction) {
        Connection connection = null;
        PreparedStatement stmt = null;
        LocalDateTime lastLdt = transaction.getGmtMidified();
        long currentVersion = transaction.getVersion();
        transaction.updateTime();
        transaction.updateVersion();
        try {
            connection = this.getConnection();
            StringBuilder builder = new StringBuilder(256);
            // 更新的值
            builder.append("UPDATE ");
            builder.append(getTableName());
            builder.append(" SET ");
            builder.append(ColumnName.CONTENT);
            builder.append(" = ?,");
            builder.append(ColumnName.PHASE);
            builder.append(" = ?,");
            builder.append(ColumnName.GMT_MODIFIED);
            builder.append(" = ?,");
            builder.append(ColumnName.RETRIED_COUNT);
            builder.append(" = ?,");
            builder.append(ColumnName.VERSION);
            builder.append(" = " + ColumnName.VERSION + " + 1, ");
            // 更新条件
            builder.append(" WHERE ");
            builder.append(ColumnName.GLOBAL_TX_ID);
            builder.append(" = ? AND ");
            builder.append(ColumnName.BRANCH_QUALIFIER);
            builder.append(" = ? AND ");
            builder.append(ColumnName.VERSION);
            builder.append(" = ? ");
            if (StringUtils.isNotEmpty(domain)) {
                builder.append(" AND ");
                builder.append(ColumnName.DOMAIN);
                builder.append(" = ? ");
            }
            stmt = connection.prepareStatement(builder.toString());
            stmt.setBytes(1, serializer.serialize(transaction));
            stmt.setInt(2, transaction.getPhase().getId());
            stmt.setDate(3, new java.sql.Date(DateUtils.localDateTimeToDate(transaction.getGmtMidified()).getTime()));
            stmt.setInt(4, transaction.getRetriedCount());
            stmt.setBytes(5, transaction.getXid().getGlobalTransactionId());
            stmt.setBytes(6, transaction.getXid().getBranchQualifier());
            stmt.setLong(7, currentVersion);
            if (StringUtils.isNotEmpty(domain)) {
                stmt.setString(8, domain);
            }
            return stmt.executeUpdate();
        } catch (Throwable e) {
            transaction.setGmtMidified(lastLdt);
            transaction.setVersion(currentVersion);
            throw new TransactionIOException(e);
        }
        finally {
            closeStatement(stmt);
            this.releaseConnection(connection);
        }
    }

    protected int doDelete(Transaction transaction) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = this.getConnection();
            StringBuilder builder = new StringBuilder();
            builder.append("DELETE FROM ");
            builder.append(getTableName());
            builder.append(" WHERE ");
            builder.append(ColumnName.GLOBAL_TX_ID);
            builder.append(" = ? AND ");
            builder.append(ColumnName.BRANCH_QUALIFIER);
            builder.append(" = ? ");
            if (StringUtils.isNotEmpty(domain)) {
                builder.append(" AND ");
                builder.append(ColumnName.DOMAIN);
                builder.append(" = ? ");
            }
            stmt = connection.prepareStatement(builder.toString());
            stmt.setBytes(1, transaction.getXid().getGlobalTransactionId());
            stmt.setBytes(2, transaction.getXid().getBranchQualifier());
            if (StringUtils.isNotEmpty(domain)) {
                stmt.setString(3, domain);
            }
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new TransactionIOException(e);
        }
        finally {
            closeStatement(stmt);
            this.releaseConnection(connection);
        }
    }

    protected Transaction doFindOne(Xid xid) {

        List<Transaction> transactions = doFind(Arrays.asList(xid));
        if (transactions != null && transactions.size() > 0) {
            return transactions.get(0);
        }
        return null;
    }

    @ Override
    protected List<Transaction> doFindAllUnmodifiedSince(LocalDateTime date) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = this.getConnection();
            
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT ");
            builder.append(ColumnName.GLOBAL_TX_ID);
            builder.append(", ");
            builder.append(ColumnName.BRANCH_QUALIFIER);
            builder.append(", ");
            builder.append(ColumnName.CONTENT);
            builder.append(", ");
            builder.append(ColumnName.PHASE);
            builder.append(", ");
            builder.append(ColumnName.TRANSACTION_TYPE);
            builder.append(", ");
            builder.append(ColumnName.GMT_CREATE);
            builder.append(", ");
            builder.append(ColumnName.GMT_MODIFIED);
            builder.append(", ");
            builder.append(ColumnName.RETRIED_COUNT);
            builder.append(", ");
            builder.append(ColumnName.VERSION);
            if (StringUtils.isNotEmpty(domain)) {
                builder.append(", ");    
                builder.append(ColumnName.DOMAIN);
            }
            builder.append(" FROM ");
            builder.append(getTableName());
            builder.append(" WHERE ");
            builder.append(ColumnName.GMT_MODIFIED);
            builder.append(" < ? AND ");
            builder.append(ColumnName.DELETED);
            builder.append(" = 0 ");
            if (StringUtils.isNotEmpty(domain)) {
                builder.append(" AND ");    
                builder.append(ColumnName.DOMAIN);
                builder.append(" = ? ");
            }
            stmt = connection.prepareStatement(builder.toString());
            
            stmt.setDate(1, new java.sql.Date(DateUtils.localDateTimeToDate(date).getTime()));
            if (StringUtils.isNotEmpty(domain)) {
                stmt.setString(2, domain);
            }
            ResultSet resultSet = stmt.executeQuery();
            this.constructTransactions(resultSet, transactions);
        } catch (Throwable e) {
            throw new TransactionIOException(e);
        }
        finally {
            closeStatement(stmt);
            this.releaseConnection(connection);
        }

        return transactions;
    }

    /**
     * 
     * @param xids
     * @return
     */
    protected List<Transaction> doFind(List<Xid> xids) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        if (xids == null || xids.size() == 0) {
            return transactions;
        }
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = this.getConnection();
            StringBuilder builder = new StringBuilder(256);
            builder.append("SELECT ");
            builder.append(ColumnName.GLOBAL_TX_ID);
            builder.append(", ");
            builder.append(ColumnName.BRANCH_QUALIFIER);
            builder.append(", ");
            builder.append(ColumnName.CONTENT);
            builder.append(", ");
            builder.append(ColumnName.PHASE);
            builder.append(", ");
            builder.append(ColumnName.TRANSACTION_TYPE);
            builder.append(", ");
            builder.append(ColumnName.GMT_CREATE);
            builder.append(", ");
            builder.append(ColumnName.GMT_MODIFIED);
            builder.append(", ");
            builder.append(ColumnName.RETRIED_COUNT);
            builder.append(", ");
            builder.append(ColumnName.VERSION);
            if (StringUtils.isNotEmpty(domain)) {
                builder.append(", ");    
                builder.append(ColumnName.DOMAIN);
            }
            builder.append(" FROM ");
            builder.append(getTableName());
            builder.append(" WHERE ");
            for (int i = 0; i < xids.size(); i++) {
                builder.append(" ( ");
                builder.append(ColumnName.GLOBAL_TX_ID);
                builder.append("= ? AND ");
                builder.append(ColumnName.BRANCH_QUALIFIER);
                builder.append("= ? ) OR");
            }
            builder.delete(builder.length() - 2, builder.length());
            if (StringUtils.isNotEmpty(domain)) {
                builder.append(" AND ");    
                builder.append(ColumnName.DOMAIN);
                builder.append(" = ? ");
            }
            stmt = connection.prepareStatement(builder.toString());
            int i = 0;
            for (Xid xid : xids) {
                stmt.setBytes(++i, xid.getGlobalTransactionId());
                stmt.setBytes(++i, xid.getBranchQualifier());
            }
            if (StringUtils.isNotEmpty(domain)) {
                stmt.setString(++i, domain);
            }
            ResultSet resultSet = stmt.executeQuery();
            this.constructTransactions(resultSet, transactions);
        } catch (Throwable e) {
            throw new TransactionIOException(e);
        }
        finally {
            closeStatement(stmt);
            this.releaseConnection(connection);
        }

        return transactions;
    }
    
    
    /**
     * 
     * @param resultSet
     * @param transactions
     * @throws SQLException
     */
    protected void constructTransactions(ResultSet resultSet, List<Transaction> transactions) throws SQLException {
        while (resultSet.next()) {
            byte[] transactionBytes = resultSet.getBytes(3);
            Transaction transaction = (Transaction)serializer.deserialize(transactionBytes);
            transaction.changePhase(TransactionPhase.valueOf(resultSet.getInt(4)));
            transaction.setGmtMidified(DateUtils.dateToLocalDateTimeBySqlDate(resultSet.getDate(7)));
            transaction.setVersion(resultSet.getLong(9));
            transaction.resetRetriedCount(resultSet.getInt(8));
            transactions.add(transaction);
        }
    }

    protected Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new TransactionIOException(e);
        }
    }

    protected void releaseConnection(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            throw new TransactionIOException(e);
        }
    }

    private void closeStatement(Statement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (Exception ex) {
            throw new TransactionIOException(ex);
        }
    }

    private String getTableName() {
        return tableName;
    }
}
