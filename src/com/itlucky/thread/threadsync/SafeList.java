package com.itlucky.thread.threadsync;

import java.util.concurrent.CopyOnWriteArrayList;


//java并发包下面提供的线程安全的list
// JUC里面的线程安全的集合之一：CopyOnWriteArrayList
public class SafeList {

    public static void main(String[] args)
        throws InterruptedException {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
        // 这里就不需要加锁，查看源码可以看到，这里的集合用的是transient volatile修饰，并且用了ReentrantLock锁
        for (int i = 0; i<10000; i++) {
            new Thread(()->{
                list.add(Thread.currentThread().getName());
            }).start();
        }
        //防止循环还没结束就打印结果造成不准
        Thread.sleep(1000);
        System.out.println(list.size());
    }

}
