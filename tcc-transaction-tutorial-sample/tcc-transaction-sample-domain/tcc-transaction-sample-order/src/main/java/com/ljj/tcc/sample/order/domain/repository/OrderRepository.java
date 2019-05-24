package com.ljj.tcc.sample.order.domain.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import com.ljj.tcc.sample.order.domain.entity.Order;
import com.ljj.tcc.sample.order.domain.entity.OrderLine;
import com.ljj.tcc.sample.order.infrastructure.dao.OrderDao;
import com.ljj.tcc.sample.order.infrastructure.dao.OrderLineDao;

/**
 * Created by liangjinjing on 4/1/16.
 */
@Repository
public class OrderRepository {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderLineDao orderLineDao;

    public void createOrder(Order order) {
        orderDao.insert(order);

        for (OrderLine orderLine : order.getOrderLines()) {
            orderLineDao.insert(orderLine);
        }
    }

    public void updateOrder(Order order) {
        order.updateVersion();
        int effectCount = orderDao.update(order);

        if (effectCount < 1) {
            throw new OptimisticLockingFailureException("update order failed");
        }
    }

    public Order findByMerchantOrderNo(String merchantOrderNo) {
        return orderDao.findByMerchantOrderNo(merchantOrderNo);
    }
}
