package com.ljj.tcc.dubbo.proxy.jdk;

import java.lang.reflect.Proxy;

import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.proxy.InvokerInvocationHandler;
import org.apache.dubbo.rpc.proxy.jdk.JdkProxyFactory;


/**
 * Created by liangjinjing on 2/26/17.
 */
public class TccJdkProxyFactory extends JdkProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {

        T proxy = (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new InvokerInvocationHandler(invoker));

        T tccProxy = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new TccInvokerInvocationHandler(proxy,invoker));

        return tccProxy;
    }
}