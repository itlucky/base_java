package com.itlucky.thread.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author itlucky
 * @date 2023/6/8 21:42
 */
public class ThreadTest {

    public static void main(String[] args) {

        ExecutorService service = Executors.newFixedThreadPool(10);

        System.out.println("1--------");
        Map<String, String> map = new HashMap<>();
        map.put("x","999");
        for (int i = 0; i < 20; i++) {
            service.execute(new TestTask(22, map));
        }
        System.out.println("map1---" + map);
        System.out.println("2--------");

        service.shutdown();
        System.out.println("map2---" + map);
    }
}
