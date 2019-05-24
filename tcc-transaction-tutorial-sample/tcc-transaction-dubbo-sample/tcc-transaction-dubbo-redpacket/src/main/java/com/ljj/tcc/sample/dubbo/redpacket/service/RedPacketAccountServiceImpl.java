package com.ljj.tcc.sample.dubbo.redpacket.service;

import org.mengyun.tcctransaction.sample.dubbo.redpacket.api.RedPacketAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljj.tcc.sample.redpacket.domain.repository.RedPacketAccountRepository;

import java.math.BigDecimal;

/**
 * Created by twinkle.zhou on 16/11/11.
 */
@Service("redPacketAccountService")
public class RedPacketAccountServiceImpl implements RedPacketAccountService {

    @Autowired
    RedPacketAccountRepository redPacketAccountRepository;

    @Override
    public BigDecimal getRedPacketAccountByUserId(long userId) {
        return redPacketAccountRepository.findByUserId(userId).getBalanceAmount();
    }
}
