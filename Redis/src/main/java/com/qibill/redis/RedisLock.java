package com.qibill.redis;

import com.qibill.redis.util.RedisUtil;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * @Description: Redis分布式锁
 * @Author: 敖丙
 * @date: 2020-04-13
 **/
@Component
public class RedisLock {

    private final String LOCK_KEY = "redis_lock";

    protected long INTERNAL_LOCK_LEASE_TIME = 3;

    private final long timeout = 1000;

    private final SetParams params = SetParams.setParams().nx().px(INTERNAL_LOCK_LEASE_TIME);

    JedisPool jedisPool = new JedisPool(RedisUtil.REDIS_URL, RedisUtil.REDIS_PORT);

    /**
     * 加锁
     *
     * @param id
     * @return
     */
    public boolean lock(String id) {
        long start = System.currentTimeMillis();
        try (Jedis jedis = jedisPool.getResource()) {
            while (true) {
                //SET命令返回OK ，则证明获取锁成功
                String lock = jedis.set(LOCK_KEY, id, params);
                if ("OK".equals(lock)) {
                    return true;
                }
                //否则循环等待，在timeout时间内仍未获取到锁，则获取失败
                long l = System.currentTimeMillis() - start;
                if (l >= timeout) {
                    return false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解锁
     *
     * @param id
     * @return
     */
    public boolean unlock(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            String script =
                    "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                            "   return redis.call('del',KEYS[1]) " +
                            "else" +
                            "   return 0 " +
                            "end";
            String result = jedis.eval(script, Collections.singletonList(LOCK_KEY), Collections.singletonList(id)).toString();
            return "1".equals(result);
        }
    }
}
