package com.ljj.tcc.sample.dubbo.capital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljj.tcc.sample.capital.domain.repository.CapitalAccountRepository;
import com.ljj.tcc.sample.dubbo.capital.api.CapitalAccountService;

import java.math.BigDecimal;

/**
 * Created by twinkle.zhou on 16/11/11.
 */
@Service("capitalAccountService")
public class CapitalAccountServiceImpl implements CapitalAccountService{


    @Autowired
    CapitalAccountRepository capitalAccountRepository;

    @Override
    public BigDecimal getCapitalAccountByUserId(long userId) {
        return capitalAccountRepository.findByUserId(userId).getBalanceAmount();
    }
}
