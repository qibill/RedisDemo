package com.qibill.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qibill
 * @date 2021/5/17 0017 上午 10:32
 */
@SpringBootApplication
public class RedisLockApplication {
    public static void main(String[] args){
        SpringApplication.run(RedisLockApplication.class, args);
    }
}
