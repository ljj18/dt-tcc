/**
 * 文件名称:          		TransactionDTO.java
 * 版权所有@ 2019-2020 	无锡爱超信息技术有限公司
 * 编译器:           		JDK1.8
 */

package com.ljj.tcc.core;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 事务DTO
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-06-03 13:53
 * 
 */
public class TransactionDTO {
    /*
     * Id
     */
    private Long id;
    /*
     * Domain
     */
    private String domain;
    /*
     * 全局事务ID
     */
    private String globalTxId;
    /*
     * 分支限定符
     */
    private String branchQualifier;
    /*
     * 事务序列化值
     */
    private byte[] content;
    /*
     * 事务阶段: 1=try,2=confirm,3=cancel
     */
    private int phase;
    /*
     * 事务类型: 1=root, 2=branch
     */
    private int transactionType;
    /*
     * 是否删除
     */
    private int deleted;
    /*
     * 重试次数
     */
    private int retriedCount;
    /*
     * 事务版本
     */
    private int version;
    /*
     * 创建日期
     */
    private LocalDateTime gmtCreate;
    /*
     * 修改日期
     */
    private LocalDateTime gmtMidified;

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Returns the domain.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain The domain to set.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @return Returns the globalTxId.
     */
    public String getGlobalTxId() {
        return globalTxId;
    }

    /**
     * @param globalTxId The globalTxId to set.
     */
    public void setGlobalTxId(String globalTxId) {
        this.globalTxId = globalTxId;
    }

    /**
     * @return Returns the branchQualifier.
     */
    public String getBranchQualifier() {
        return branchQualifier;
    }

    /**
     * @param branchQualifier The branchQualifier to set.
     */
    public void setBranchQualifier(String branchQualifier) {
        this.branchQualifier = branchQualifier;
    }

    /**
     * @return Returns the content.
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * @param content The content to set.
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * @return Returns the phase.
     */
    public int getPhase() {
        return phase;
    }

    /**
     * @param phase The phase to set.
     */
    public void setPhase(int phase) {
        this.phase = phase;
    }

    /**
     * @return Returns the transactionType.
     */
    public int getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType The transactionType to set.
     */
    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * @return Returns the deleted.
     */
    public int getDeleted() {
        return deleted;
    }

    /**
     * @param deleted The deleted to set.
     */
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    /**
     * @return Returns the retriedCount.
     */
    public int getRetriedCount() {
        return retriedCount;
    }

    /**
     * @param retriedCount The retriedCount to set.
     */
    public void setRetriedCount(int retriedCount) {
        this.retriedCount = retriedCount;
    }

    /**
     * @return Returns the version.
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version The version to set.
     */
    public void setVersion(int version) {
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
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     * 
     */
    @ Override
    public String toString() {
        return "TransactionDTO [id=" + id + ", domain=" + domain + ", globalTxId=" + globalTxId + ", branchQualifier="
            + branchQualifier + ", content=" + Arrays.toString(content) + ", phase=" + phase + ", transactionType="
            + transactionType + ", deleted=" + deleted + ", retriedCount=" + retriedCount + ", version=" + version
            + ", gmtCreate=" + gmtCreate + ", gmtMidified=" + gmtMidified + "]";
    }

}
