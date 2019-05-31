
package com.ljj.tcc.core;

import java.io.Serializable;

import com.ljj.tcc.api.TransactionContext;
import com.ljj.tcc.api.TransactionContextEditor;
import com.ljj.tcc.api.TransactionPhase;
import com.ljj.tcc.api.TransactionXid;

/**
 * Created by liangjinjing on 10/27/15.
 */
public class Participant implements Serializable {

    private static final long serialVersionUID = 4127729421281425247L;

    private TransactionXid xid;

    private InvocationContext confirmInvocationContext;

    private InvocationContext cancelInvocationContext;

    private Terminator terminator = new Terminator();

    Class< ? extends TransactionContextEditor> transactionContextEditorClass;

    public Participant() {

    }

    public Participant(TransactionXid xid, InvocationContext confirmInvocationContext,
        InvocationContext cancelInvocationContext,
        Class< ? extends TransactionContextEditor> transactionContextEditorClass) {
        this.xid = xid;
        this.confirmInvocationContext = confirmInvocationContext;
        this.cancelInvocationContext = cancelInvocationContext;
        this.transactionContextEditorClass = transactionContextEditorClass;
    }

    public Participant(InvocationContext confirmInvocationContext, InvocationContext cancelInvocationContext,
        Class< ? extends TransactionContextEditor> transactionContextEditorClass) {
        this.confirmInvocationContext = confirmInvocationContext;
        this.cancelInvocationContext = cancelInvocationContext;
        this.transactionContextEditorClass = transactionContextEditorClass;
    }

    public void setXid(TransactionXid xid) {
        this.xid = xid;
    }

    public void rollback() {
        terminator.invoke(new TransactionContext(xid, TransactionPhase.CANCELLING.getId()), cancelInvocationContext,
            transactionContextEditorClass);
    }

    public void commit() {
        terminator.invoke(new TransactionContext(xid, TransactionPhase.CONFIRMING.getId()), confirmInvocationContext,
            transactionContextEditorClass);
    }

    public InvocationContext getConfirmInvocationContext() {
        return confirmInvocationContext;
    }

    public InvocationContext getCancelInvocationContext() {
        return cancelInvocationContext;
    }

    public Terminator getTerminator() {
        return terminator;
    }

    public TransactionXid getXid() {
        return xid;
    }

}
