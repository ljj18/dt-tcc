package com.ljj.tcc.dubbo.context;

import java.lang.reflect.Method;

import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.RpcContext;

import com.alibaba.fastjson.JSON;
import com.ljj.tcc.api.TransactionContext;
import com.ljj.tcc.api.TransactionContextEditor;
import com.ljj.tcc.dubbo.constants.TransactionContextConstants;

/**
 * Created by liangjinjing on 1/19/17.
 */
public class DubboTransactionContextEditor implements TransactionContextEditor {
    @Override
    public TransactionContext get(Object target, Method method, Object[] args) {

        String context = RpcContext.getContext().getAttachment(TransactionContextConstants.TRANSACTION_CONTEXT);

        if (StringUtils.isNotEmpty(context)) {
            return JSON.parseObject(context, TransactionContext.class);
        }

        return null;
    }

    @Override
    public void set(TransactionContext transactionContext, Object target, Method method, Object[] args) {

        RpcContext.getContext().setAttachment(TransactionContextConstants.TRANSACTION_CONTEXT, JSON.toJSONString(transactionContext));
    }
}
