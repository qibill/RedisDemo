package com.qibill.lock.config;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author qibill
 * @date 2021/5/17 0017 上午 10:41
 */
public class RedisUtils {
    private static JedisPool jedisPool;

    static {
        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxTotal(20);
        jpc.setMaxIdle(10);
        jedisPool = new JedisPool(jpc);
    }

    public static JedisPool getJedis() throws Exception{
        if(jedisPool == null)
            throw new NullPointerException("JedisPool is not OK.");
        return jedisPool;
    }

}
