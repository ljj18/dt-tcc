package com.ljj.tcc.sample.redpacket.infrastructure.dao;

import com.ljj.tcc.sample.redpacket.domain.entity.RedPacketAccount;

/**
 * Created by liangjinjing on 4/2/16.
 */
public interface RedPacketAccountDao {

    RedPacketAccount findByUserId(long userId);

    int update(RedPacketAccount redPacketAccount);
}
