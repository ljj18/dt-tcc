package com.ljj.tcc.sample.order.infrastructure.dao;


import java.util.List;

import com.ljj.tcc.sample.order.domain.entity.Product;

/**
 * Created by twinkle.zhou on 16/11/10.
 */
public interface ProductDao {

    Product findById(long productId);

    List<Product> findByShopId(long shopId);
}
