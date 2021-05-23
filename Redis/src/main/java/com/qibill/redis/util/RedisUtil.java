package com.qibill.redis.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis操作工具类.</br>
 * (基于jedis)
 * @author qibill
 * 2018年7月19日下午2:56:24
 */
@Component
public class RedisUtil {

    public static final String REDIS_URL = "192.168.50.236";
    public static final int REDIS_PORT = 6379;
    public static final Jedis redis = new JedisPool(REDIS_URL, REDIS_PORT).getResource();

    /**
     * 读取缓
     *
     * @param key
     * @return
     */
    public static String get(final String key) {
        return redis.get(key);
    }

    /**
     * 写入缓存
     */
    public static boolean set(final String key, String value) {
        boolean result = false;
        try {
            redis.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除缓存
     */
    public boolean delete(final String key) {
        boolean result = false;
        try {
            redis.del(key);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}