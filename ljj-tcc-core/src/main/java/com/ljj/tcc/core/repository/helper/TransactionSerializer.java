package com.ljj.tcc.core.repository.helper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.ljj.tcc.core.ColumnName;
import com.ljj.tcc.core.Transaction;
import com.ljj.tcc.core.serializer.ObjectSerializer;

/**
 * Transaction 序列化 
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
@ SuppressWarnings({"rawtypes", "unchecked"})
public class TransactionSerializer {

    
    public static byte[] serialize(ObjectSerializer serializer, Transaction transaction) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put(ColumnName.GLOBAL_TX_ID, transaction.getXid().getGlobalTransactionId());
        map.put(ColumnName.BRANCH_QUALIFIER, transaction.getXid().getBranchQualifier());
        map.put(ColumnName.PHASE, transaction.getPhase().getId());
        map.put(ColumnName.TRANSACTION_TYPE, transaction.getTransactionType().getId());
        map.put(ColumnName.RETRIED_COUNT, transaction.getRetriedCount());
        map.put(ColumnName.GMT_CREATE, transaction.getGmtCreate());
        map.put(ColumnName.GMT_MODIFIED, transaction.getGmtMidified());
        map.put(ColumnName.VERSION, transaction.getVersion());
        map.put(ColumnName.CONTENT, serializer.serialize(transaction));
        return serializer.serialize(map);
    }

    public static Transaction deserialize(ObjectSerializer<Transaction> serializer, byte[] value) {

        Map<String, Object> map = (Map<String, Object>) serializer.deserialize(value);

        byte[] content = (byte[]) map.get(ColumnName.CONTENT);
        Transaction transaction = (Transaction) serializer.deserialize(content);
        transaction.resetRetriedCount((Integer) map.get(ColumnName.RETRIED_COUNT));
        transaction.setGmtMidified((LocalDateTime) map.get(ColumnName.GMT_MODIFIED));
        transaction.setVersion((Long) map.get(ColumnName.VERSION));
        return transaction;
    }
}
