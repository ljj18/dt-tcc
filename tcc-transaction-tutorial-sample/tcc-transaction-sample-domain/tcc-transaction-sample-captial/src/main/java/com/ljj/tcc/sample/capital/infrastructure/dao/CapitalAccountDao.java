package com.ljj.tcc.sample.capital.infrastructure.dao;

import com.ljj.tcc.sample.capital.domain.entity.CapitalAccount;

/**
 * Created by liangjinjing on 4/2/16.
 */
public interface CapitalAccountDao {

    CapitalAccount findByUserId(long userId);

    int update(CapitalAccount capitalAccount);
}
