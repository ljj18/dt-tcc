package com.ljj.tcc.core.repository.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ljj.tcc.api.TransactionPhase;
import com.ljj.tcc.core.ColumnName;
import com.ljj.tcc.core.Transaction;
import com.ljj.tcc.core.serializer.ObjectSerializer;
import com.ljj.tcc.core.utils.ByteUtils;

/**
 * 扩展 Transaction 序列化 
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public class ExpandTransactionSerializer {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    
    public static Map<byte[], byte[]> serialize(ObjectSerializer<Transaction> serializer, Transaction transaction) {

        Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
        
        map.put(ColumnName.GLOBAL_TX_ID.getBytes(), transaction.getXid().getGlobalTransactionId());
        map.put(ColumnName.BRANCH_QUALIFIER.getBytes(), transaction.getXid().getBranchQualifier());
        map.put(ColumnName.PHASE.getBytes(), ByteUtils.intToBytes(transaction.getPhase().getId()));
        map.put(ColumnName.TRANSACTION_TYPE.getBytes(), ByteUtils.intToBytes(transaction.getTransactionType().getId()));
        map.put(ColumnName.RETRIED_COUNT.getBytes(), ByteUtils.intToBytes(transaction.getRetriedCount()));
        map.put(ColumnName.GMT_CREATE.getBytes(), DTF.format(transaction.getGmtCreate()).getBytes());
        map.put(ColumnName.GMT_MODIFIED.getBytes(), DTF.format(transaction.getGmtMidified()).getBytes());
        map.put(ColumnName.VERSION.getBytes(), ByteUtils.longToBytes(transaction.getVersion()));
        map.put(ColumnName.CONTENT.getBytes(), serializer.serialize(transaction));
        map.put("content_view".getBytes(), JSON.toJSONString(transaction).getBytes());
        return map;
    }

    public static Transaction deserialize(ObjectSerializer<Transaction> serializer, Map<byte[], byte[]> map1) {
        Map<String, byte[]> propertyMap = new HashMap<String, byte[]>();
        for (Map.Entry<byte[], byte[]> entry : map1.entrySet()) {
            propertyMap.put(new String(entry.getKey()), entry.getValue());
        }
        byte[] content = propertyMap.get(ColumnName.CONTENT);
        Transaction transaction = (Transaction) serializer.deserialize(content);
        transaction.changePhase(TransactionPhase.valueOf(ByteUtils.bytesToInt(propertyMap.get(ColumnName.PHASE))));
        transaction.resetRetriedCount(ByteUtils.bytesToInt(propertyMap.get(ColumnName.RETRIED_COUNT)));
        transaction.setGmtMidified(LocalDateTime.parse(new String(propertyMap.get(ColumnName.GMT_MODIFIED)), DTF));
        transaction.setVersion(ByteUtils.bytesToLong(propertyMap.get(ColumnName.VERSION)));
        return transaction;
    }
}
