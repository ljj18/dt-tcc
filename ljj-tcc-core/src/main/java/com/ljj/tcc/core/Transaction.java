package com.ljj.tcc.core;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.xa.Xid;

import com.ljj.tcc.api.TransactionContext;
import com.ljj.tcc.api.TransactionPhase;
import com.ljj.tcc.api.TransactionType;
import com.ljj.tcc.api.TransactionXid;

/**
 * 
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public class Transaction implements Serializable {

    private static final long serialVersionUID = 7291423944314337931L;

    private TransactionXid xid;

    private TransactionPhase phase;

    private TransactionType transactionType;

    private volatile int retriedCount = 0;

    private LocalDateTime gmtCreate = LocalDateTime.now();

    private LocalDateTime gmtMidified = LocalDateTime.now();

    private long version = 1;

    private List<Participant> participants = new ArrayList<Participant>();

    private Map<String, Object> attachments = new ConcurrentHashMap<String, Object>();

    public Transaction() {

    }

    public Transaction(TransactionContext transactionContext) {
        this.xid = transactionContext.getXid();
        this.phase = TransactionPhase.TRYING;
        this.transactionType = TransactionType.BRANCH;
    }

    public Transaction(TransactionType transactionType) {
        this.xid = new TransactionXid();
        this.phase = TransactionPhase.TRYING;
        this.transactionType = transactionType;
    }

    public Transaction(Object uniqueIdentity,TransactionType transactionType) {

        this.xid = new TransactionXid(uniqueIdentity);
        this.phase = TransactionPhase.TRYING;
        this.transactionType = transactionType;
    }

    public void enlistParticipant(Participant participant) {
        participants.add(participant);
    }


    public Xid getXid() {
        return xid.clone();
    }

    public TransactionPhase getPhase() {
        return phase;
    }


    public List<Participant> getParticipants() {
        return participants;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void changePhase(TransactionPhase phase) {
        this.phase = phase;
    }


    public void commit() {
        for (Participant participant : participants) {
            participant.commit();
        }
    }

    public void rollback() {
        for (Participant participant : participants) {
            participant.rollback();
        }
    }

    public int getRetriedCount() {
        return retriedCount;
    }

    public void addRetriedCount() {
        this.retriedCount++;
    }

    public void resetRetriedCount(int retriedCount) {
        this.retriedCount = retriedCount;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public long getVersion() {
        return version;
    }

    public void updateVersion() {
        this.version++;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    /**
     * @return Returns the gmtCreate.
     */
    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    /**
     * @param gmtCreate The gmtCreate to set.
     */
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * @return Returns the gmtMidified.
     */
    public LocalDateTime getGmtMidified() {
        return gmtMidified;
    }

    /**
     * @param gmtMidified The gmtMidified to set.
     */
    public void setGmtMidified(LocalDateTime gmtMidified) {
        this.gmtMidified = gmtMidified;
    }

    /**
     * 
     */
    public void updateTime() {
        this.gmtMidified =  LocalDateTime.now();
    }

}
