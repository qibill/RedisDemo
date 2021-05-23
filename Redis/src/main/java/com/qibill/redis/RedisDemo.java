package com.qibill.redis;

import com.qibill.redis.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: redis测试类
 * @Author: 敖丙
 * @date: 2020-05-10
 **/
public class RedisDemo {
    final static List<Integer> mysql = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    public static void main(String[] args) {
        // 查询请求进来
        Integer id = 2;

        // 查询缓存
        String str = RedisUtil.get(id.toString());
        if (StringUtils.isNotBlank(str)) {
            System.out.println(str);
            return;
        }

        // 第三方缓存

        // 查询数据库
        if (mysql.contains(id)) {
            System.out.println("在数据库 id:" + id);
            RedisUtil.set(id.toString(), "xxx");
            return;
        }

        System.out.println("不在数据库 set redis");
    }
}
