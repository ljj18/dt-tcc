package com.ljj.tcc.api;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事务Context
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public class TransactionContext implements Serializable {

    private static final long serialVersionUID = -8199390103169700387L;
    /*
     * XID
     */
    private TransactionXid xid;

    /*
     * 事务阶段(Try, Confirm, Cancel) 
     */
    private int phase;

    /*
     * 事务参数
     */
    private Map<String, String> attachments = new ConcurrentHashMap<String, String>();

    public TransactionContext() {

    }

    public TransactionContext(TransactionXid xid, int phase) {
        this.xid = xid;
        this.phase = phase;
    }

    public void setXid(TransactionXid xid) {
        this.xid = xid;
    }

    public TransactionXid getXid() {
        return xid.clone();
    }

    public void setAttachments(Map<String, String> attachments) {
        if (attachments != null && !attachments.isEmpty()) {
            this.attachments.putAll(attachments);
        }
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return phase;
    }


}
