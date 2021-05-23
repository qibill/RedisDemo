package com.qibill.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author qibill
 * @date 2021/4/27 0027 下午 8:27
 */
@Service
public class Publisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Publisher.class);

    @Autowired
    private RedisTemplate<String, Object> redis;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    public void publish(Object msg) {
        redis.convertAndSend("demo-channel", msg);
    }

    public void redisDelayQueue(Object msg) {
        // 实际开发建议使用业务 ID 和随机生成的唯一 ID 作为 value, 随机生成的唯一 ID 可以保证消息的唯一性, 业务 ID 可以避免 value 携带的信息过多
        zSetOperations.add("delay_queue", msg, System.currentTimeMillis() + 5000);
/*
        new Thread() {
            @Override
            public void run() {
                while (true) {
                   Set<String> resultList;
// 只获取第一条数据, 只获取不会移除数据
                    resultList = jedis.zrangebyscore(key, System.currentTimeMillis(), 0, 1);
                    if (resultList.size == 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace;
                            break;
                        }
                    } else {
// 移除数据获取到的数据
                        if (jedis.zrem(key, resultList.get(0)) > 0) {
                            String orderId = resultList.get(0);
                            log.info("orderId = {}", resultList.get(0));
                            this.handleMsg(orderId);
                        }
                    }
                }

            }

        }.start;
*/

    }

    public void redisDelayQueue() {
        new Thread(() -> {
            while (true) {
// 只获取第一条数据, 只获取不会移除数据
                Set<Object> resultList = zSetOperations.rangeByScore("delay_queue", System.currentTimeMillis(), -1);
                if (resultList.size() == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }else {
                    for (Object o : resultList) {
                        publish(o);
                    }
                    zSetOperations.removeRangeByScore("delay_queue", 0, -1);
                }
            }
        }).start();

    }

}
