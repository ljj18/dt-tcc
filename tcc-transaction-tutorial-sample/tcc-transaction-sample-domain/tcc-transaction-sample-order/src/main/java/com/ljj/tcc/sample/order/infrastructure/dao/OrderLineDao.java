package com.ljj.tcc.sample.order.infrastructure.dao;


import com.ljj.tcc.sample.order.domain.entity.OrderLine;

/**
 * Created by liangjinjing on 4/1/16.
 */
public interface OrderLineDao {
    void insert(OrderLine orderLine);
}
