package com.qibill.lock.controller;

import com.qibill.lock.config.RedisUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author qibill
 * @date 2021/5/17 0017 上午 10:34
 */
@RestController
public class GoodController {

    public static final String REDIS_LOCK = "REDIS_LOCK";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private Redisson redisson;

    @GetMapping("/buy_goods")
    public String buy_Goods() {

        RLock redissonLock = redisson.getLock(REDIS_LOCK);
        redissonLock.lock();

        try {
            String result = stringRedisTemplate.opsForValue().get("goods:001");// get key ====看看库存的数量够不够
            int goodsNumber = result == null ? 0 : Integer.parseInt(result);
            if (goodsNumber > 0) {
                int realNumber = goodsNumber - 1;
                stringRedisTemplate.opsForValue().set("goods:001", String.valueOf(realNumber));
                System.out.println("成功买到商品，库存还剩下: " + realNumber + " 件" + "\t服务提供端口" + serverPort);
                return "成功买到商品，库存还剩下:" + realNumber + " 件" + "\t服务提供端口" + serverPort;
            } else {
                System.out.println("商品已经售完/活动结束/调用超时,欢迎下次光临" + "\t服务提供端口" + serverPort);
            }

            return "商品已经售完/活动结束/调用超时,欢迎下次光临" + "\t服务提供端口" + serverPort;
        } finally {
            redissonLock.unlock();
        }
    }

    @GetMapping("/m")
    public String m() {
        String value = UUID.randomUUID() + Thread.currentThread().getName();

        try {
            Boolean flag = stringRedisTemplate.opsForValue()//使用另一个带有设置超时操作的方法
                    .setIfAbsent(REDIS_LOCK, value, 10L, TimeUnit.SECONDS);
            //设定时间
            //stringRedisTemplate.expire(REDIS_LOCK, 10L, TimeUnit.SECONDS);

            if (!flag) {
                return "抢锁失败";
            }

            //业务逻辑

        } finally {


            String script = "if redis.call('get', KEYS[1]) == ARGV[1] "
                    + "then "
                    + "    return redis.call('del', KEYS[1]) "
                    + "else "
                    + "    return 0 "
                    + "end";
            Jedis jedis = null;
            try {
                jedis = RedisUtils.getJedis().getResource();
                Object o = jedis.eval(script, Collections.singletonList(REDIS_LOCK),
                        Collections.singletonList(value));

                if ("1".equals(o.toString())) {
                    System.out.println("---del redis lock ok.");
                } else {
                    System.out.println("---del redis lock error.");
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jedis != null)
                    jedis.close();
            }
        }
        return value;
    }

    @GetMapping("/doSomething")
    public String doSomething() {

        RLock redissonLock = redisson.getLock(REDIS_LOCK);
        redissonLock.lock();
        try {
            //doSomething
            return null;
        } finally {
            //添加后，更保险
            if (redissonLock.isLocked() && redissonLock.isHeldByCurrentThread()) {
                redissonLock.unlock();
            }
        }
    }
}