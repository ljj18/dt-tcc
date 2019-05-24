package com.ljj.tcc.sample.order.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ljj.tcc.sample.order.domain.entity.Shop;
import com.ljj.tcc.sample.order.infrastructure.dao.ShopDao;

/**
 * Created by liangjinjing on 4/1/16.
 */
@Repository
public class ShopRepository {

    @Autowired
    ShopDao shopDao;

    public Shop findById(long id) {

        return shopDao.findById(id);
    }
}
