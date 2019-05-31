package com.ljj.tcc.core.recover;

import java.util.Set;

/**
 * Created by liangjinjing on 6/1/16.
 */
public interface RecoverConfig {

    /**
     * 最大重试次数
     */
    int getMaxRetryCount();

    /**
     * 恢复间隔时间,单位：秒
     * @return
     */
    int getRecoverDuration();

    /**
     * Cron表达式
     * @return
     */
    String getCronExpression();

    /**
     * 延迟取消异常集合
     * @return
     */
    Set<Class<? extends Exception>> getDelayCancelExceptions();

    /**
     * 设置延迟取消异常集合
     * @param delayRecoverExceptions
     */
    void setDelayCancelExceptions(Set<Class<? extends Exception>> delayRecoverExceptions);

    /**
     * 获取异步线程池Size
     * @return
     */
    int getAsyncTerminateThreadPoolSize();
}
