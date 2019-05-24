package com.ljj.tcc.sample.order.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ljj.tcc.sample.order.domain.entity.Product;
import com.ljj.tcc.sample.order.infrastructure.dao.ProductDao;

import java.util.List;

/**
 * Created by twinkle.zhou on 16/11/10.
 */
@Repository
public class ProductRepository {

    @Autowired
    ProductDao productDao;

    public Product findById(long productId){
        return productDao.findById(productId);
    }

    public List<Product> findByShopId(long shopId){
        return productDao.findByShopId(shopId);
    }
}
