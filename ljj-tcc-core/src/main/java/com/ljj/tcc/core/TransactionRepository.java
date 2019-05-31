package com.ljj.tcc.core;

import java.time.LocalDateTime;
import java.util.List;

import com.ljj.tcc.api.TransactionXid;

/**
 * 事务持久化
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public interface TransactionRepository {

    /**
     * 保存事务
     * @param transaction
     * @return
     */
    int create(Transaction transaction);

    /**
     * 更新事务
     * @param transaction
     * @return
     */
    int update(Transaction transaction);

    /**
     * 删除事务
     * @param transaction
     * @return
     */
    int delete(Transaction transaction);

    /**
     * 根据全局事务查找事务
     * @param xid
     * @return
     */
    Transaction findByXid(TransactionXid xid);

    /**
     * 查找指定日期一直没有修改过的事务
     * @param date
     * @return
     */
    List<Transaction> findAllUnmodifiedSince(LocalDateTime date);
}
