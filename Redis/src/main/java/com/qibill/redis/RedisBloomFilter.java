package com.qibill.redis;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class RedisBloomFilter {
    public static void main(String[] args) {
        // 创建布隆过滤器对象
        BloomFilter<Integer> filter = BloomFilter.create(
                Funnels.integerFunnel(),
                1500,
                0.01);
        // 判断指定元素是否存在
        // 将元素添加进布隆过滤器
        filter.put(1);
        filter.put(2);
        filter.mightContain(1);
    }
}
