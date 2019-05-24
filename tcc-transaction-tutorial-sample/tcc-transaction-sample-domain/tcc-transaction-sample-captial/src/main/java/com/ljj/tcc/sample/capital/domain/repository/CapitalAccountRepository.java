package com.ljj.tcc.sample.capital.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ljj.tcc.sample.capital.domain.entity.CapitalAccount;
import com.ljj.tcc.sample.capital.infrastructure.dao.CapitalAccountDao;
import com.ljj.tcc.sample.exception.InsufficientBalanceException;

/**
 * Created by liangjinjing on 4/2/16.
 */
@Repository
public class CapitalAccountRepository {

    @Autowired
    CapitalAccountDao capitalAccountDao;

    public CapitalAccount findByUserId(long userId) {

        return capitalAccountDao.findByUserId(userId);
    }

    public void save(CapitalAccount capitalAccount) {
        int effectCount = capitalAccountDao.update(capitalAccount);
        if (effectCount < 1) {
            throw new InsufficientBalanceException();
        }
    }
}
