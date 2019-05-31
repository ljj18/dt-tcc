
package com.ljj.tcc.core.utils;

import com.ljj.tcc.api.Propagation;
import com.ljj.tcc.core.interceptor.CompensableMethodContext;

/**
 * 事务工具类
 * 
 * Version 1.0.0
 * 
 * @author liangjinjing
 * 
 * Date 2019-05-24 11:34
 * 
 */
public class TransactionUtils {

    /**
     * 
     * @param isTransactionActive
     * @param compensableMethodContext
     * @return
     */
    public static boolean isLegalTransactionContext(boolean isTransactionActive,
        CompensableMethodContext compensableMethodContext) {

        if (compensableMethodContext.getPropagation().equals(Propagation.MANDATORY) && !isTransactionActive
            && compensableMethodContext.getTransactionContext() == null) {
            return false;
        }
        return true;
    }
}
