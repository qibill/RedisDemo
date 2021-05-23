package com.qibill.redis;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @Description: 基于redis的分布式锁
 * @Author: 敖丙
 * @date: 2020-04-12
 **/
public class RedisTest {

    static CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(;;){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        System.out.println("AAA");
                        cyclicBarrier.await();
                        System.out.println("BBB");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


}


}
