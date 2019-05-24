package org.mengyun.tcctransaction.spring.support;

import org.mengyun.tcctransaction.support.BeanFactory;
<<<<<<< HEAD:tcc-transaction-spring/src/main/java/org/mengyun/tcctransaction/spring/support/TccApplicationContext.java
import org.mengyun.tcctransaction.support.BeanFactoryAdapter;
=======
import org.mengyun.tcctransaction.support.FactoryBuilder;
>>>>>>> master-1.2.x:tcc-transaction-spring/src/main/java/org/mengyun/tcctransaction/spring/support/SpringBeanFactory.java
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * Created by liangjinjing on 11/22/15.
 */
public class SpringBeanFactory implements BeanFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
<<<<<<< HEAD:tcc-transaction-spring/src/main/java/org/mengyun/tcctransaction/spring/support/TccApplicationContext.java
        BeanFactoryAdapter.setBeanFactory(this);
=======
        FactoryBuilder.registerBeanFactory(this);
    }

    @Override
    public boolean isFactoryOf(Class clazz) {
        Map map = this.applicationContext.getBeansOfType(clazz);
        return map.size() > 0;
>>>>>>> master-1.2.x:tcc-transaction-spring/src/main/java/org/mengyun/tcctransaction/spring/support/SpringBeanFactory.java
    }

    @Override
    public <T> T getBean(Class<T> var1) {
        return this.applicationContext.getBean(var1);
    }
}
