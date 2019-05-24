package com.ljj.tcc.sample.dubbo.redpacket.api;

import com.ljj.tcc.api.Compensable;
import com.ljj.tcc.sample.dubbo.redpacket.api.dto.RedPacketTradeOrderDto;

/**
 * Created by liangjinjing on 4/1/16.
 */
public interface RedPacketTradeOrderService {

    @Compensable
    public String record(RedPacketTradeOrderDto tradeOrderDto);
}
