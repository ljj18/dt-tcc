package com.ljj.tcc.sample.order.infrastructure.dao;

import com.ljj.tcc.sample.order.domain.entity.Order;

/**
 * Created by liangjinjing on 4/1/16.
 */
public interface OrderDao {

    public int insert(Order order);

    public int update(Order order);

    Order findByMerchantOrderNo(String merchantOrderNo);
}
