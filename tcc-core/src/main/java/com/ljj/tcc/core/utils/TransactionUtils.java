
package com.ljj.tcc.core.utils;

import com.ljj.tcc.api.Propagation;
import com.ljj.tcc.core.interceptor.CompensableMethodContext;

/**
 * Created by liangjinjing on 2/23/17.
 */
public class TransactionUtils {

    public static boolean isLegalTransactionContext(boolean isTransactionActive,
        CompensableMethodContext compensableMethodContext) {

        if (compensableMethodContext.getPropagation().equals(Propagation.MANDATORY) && !isTransactionActive
            && compensableMethodContext.getTransactionContext() == null) {
            return false;
        }
        return true;
    }
}
