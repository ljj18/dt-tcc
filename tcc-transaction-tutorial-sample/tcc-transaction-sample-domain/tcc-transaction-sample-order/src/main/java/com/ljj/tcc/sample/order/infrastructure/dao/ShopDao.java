package com.ljj.tcc.sample.order.infrastructure.dao;


import com.ljj.tcc.sample.order.domain.entity.Shop;

/**
 * Created by liangjinjing on 4/1/16.
 */
public interface ShopDao {
    Shop findById(long id);
}
