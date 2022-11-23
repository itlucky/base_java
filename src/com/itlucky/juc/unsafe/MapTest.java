package com.itlucky.juc.unsafe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 直接用HashMap,同样会出现并发修改异常：java.util.ConcurrentModificationException
 */
public class MapTest {

    public static void main(String[] args) {
        // new HashMap<>()默认等价于new HashMap<>(16,0.75)，查看源码可知--》参数1：初始大小(默认16)，参数2：加载因子(默认0.75)。
//        Map<String, String> map = new HashMap<>();
        // 解法1：用Collections工具类
//        Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
        //解法2：用ConcurrentHashMap
        Map<String, String> map =new ConcurrentHashMap<>();

        for (int i = 1; i<=30; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0,3));
                System.out.println(map);
            }).start();
        }

        System.out.println("1 << 30:" + (1 << 30));
    }
}
