package com.ljj.tcc.sample.order.domain.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljj.tcc.sample.order.domain.entity.Order;
import com.ljj.tcc.sample.order.domain.factory.OrderFactory;
import com.ljj.tcc.sample.order.domain.repository.OrderRepository;

import java.util.List;

/**
 * Created by liangjinjing on 3/25/16.
 */
@Service
public class OrderServiceImpl {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderFactory orderFactory;

    @Transactional
    public Order createOrder(long payerUserId, long payeeUserId, List<Pair<Long, Integer>> productQuantities) {
        Order order = orderFactory.buildOrder(payerUserId, payeeUserId, productQuantities);

        orderRepository.createOrder(order);

        return order;
    }

    public Order findOrderByMerchantOrderNo(String orderNo) {
        return orderRepository.findByMerchantOrderNo(orderNo);
    }
}
