package com.ljj.tcc.sample.dubbo.capital.api;

import com.ljj.tcc.api.Compensable;
import com.ljj.tcc.sample.dubbo.capital.api.dto.CapitalTradeOrderDto;

/**
 * Created by liangjinjing on 4/1/16.
 */
public interface CapitalTradeOrderService {

    @Compensable
    public String record(CapitalTradeOrderDto tradeOrderDto);

}
