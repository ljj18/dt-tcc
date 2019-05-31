package com.ljj.tcc.core.repository.helper;

import redis.clients.jedis.Jedis;

/**
 * Created by liangjinjing on 9/15/16.
 */
public interface JedisCallback<T> {

    public T doInJedis(Jedis jedis);
}