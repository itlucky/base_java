package com.itlucky.thread.test;

import java.util.Map;

/**
 * @author itlucky
 * @date 2023/6/8 21:43
 */
public class TestTask implements Runnable {

    private String tradNo = "--tradNo--";

    private int num = 2;

    private final Map<String, String> map;

    public TestTask(int num, Map<String, String> map) {
        this.num = num;
        this.map = map;
    }

    @Override
    public void run() {

//        System.out.println("线程：" + Thread.currentThread().getName() + "执行了~");

        tradNo += Thread.currentThread().getName();

//        System.out.println(tradNo);

//        System.out.println("num=" + ++num);

        if ("pool-1-thread-1".equals(Thread.currentThread().getName())) {
            map.put("x", "222");
            System.out.println( Thread.currentThread().getName() +"==="+map);
        } else {
            map.put("x", "9999");
        }

    }
}
