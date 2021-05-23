package com.qibill.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RedissonTest {

    private static volatile Integer inventory = 1;
    private static final int NUM = 1000;
    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
    volatile static CountDownLatch countDownLatch = new CountDownLatch(10);

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(inventory, inventory, 10L, TimeUnit.SECONDS, linkedBlockingQueue);
        long start = System.currentTimeMillis();
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.50.236:6379");
        final RedissonClient client = Redisson.create(config);
        final RLock lock = client.getLock("lock1");

        for (int i = 0; i <= NUM; i++) {
            threadPoolExecutor.execute(() -> {
                lock.lock();
                inventory--;
                System.out.println(inventory);
                lock.unlock();
            });
        }
        long end = System.currentTimeMillis();
        System.out.println("执行线程数:" + NUM + "   总耗时:" + (end - start) + "  库存数为:" + inventory);

    }
}
