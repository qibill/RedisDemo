package com.qibill.redis;

import com.qibill.redis.util.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis重入锁
 */
public class RedisReentrantLock {
    private ThreadLocal<Map<String, Integer>> lockers = new ThreadLocal<>();

    private Jedis jedis;

    public RedisReentrantLock(Jedis jedis) {
        this.jedis = jedis;
    }

    private boolean _lock(String key) {
        SetParams setParams = SetParams.setParams().nx().px(1000L);
        return jedis.set(key, "", setParams) != null;
    }

    private void _unlock(String key) {
        jedis.del(key);
    }

    private Map<String, Integer> currentLockers() {
        Map<String, Integer> refs = lockers.get();
        if (refs != null) {
            return refs;
        }
        lockers.set(new HashMap<>());
        return lockers.get();
    }

    public boolean lock(String key) {
        Map<String, Integer> refs = currentLockers();
        Integer refCnt = refs.get(key);
        if (refCnt != null) {
            refs.put(key, refCnt + 1);
            return true;
        }
        boolean ok = this._lock(key);
        if (!ok) {
            return false;
        }
        refs.put(key, 1);
        return true;
    }

    public boolean unlock(String key) {
        Map<String, Integer> refs = currentLockers();
        Integer refCnt = refs.get(key);
        if (refCnt == null) {
            return false;
        }
        refCnt -= 1;
        if (refCnt > 0) {
            refs.put(key, refCnt);
        } else {
            refs.remove(key);
            this._unlock(key);
        }
        return true;
    }

    public static void main(String[] args) {
        RedisReentrantLock redis = new RedisReentrantLock(RedisUtil.redis);
        RedisReentrantLock redis2 = new RedisReentrantLock(RedisUtil.redis);
        System.out.println(redis.lock("codehole"));
        System.out.println(redis.lock("codehole"));
        System.out.println(redis2.lock("codehole"));
        System.out.println(redis.unlock("codehole"));
        System.out.println(redis.unlock("codehole"));
    }
}
