package com.qibill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PublisherTest {

    @Autowired
    private Publisher publisher;

    @Test
    public void publishTest(){
        publisher.publish(123);
    }
    @Test
    public void redisDelayQueueTest(){
        publisher.redisDelayQueue(123);
    }
}
