package com.itlucky.thread.threadmethod;

//线程礼让
//礼让不一定成功，还是看CPU的调度心情

public class ThreadYield {

    /**
     * 如果没有加线程礼让，打印结果是：
     * A线程开始执行
     * A线程停止执行
     * B线程开始执行
     * B线程停止执行
     *
     * 加了线程礼让，打印结果有可能还是上面的，但是也会出现如下这样的结果：
     * A线程开始执行
     * B线程开始执行
     * A线程停止执行
     * B线程停止执行
     *
     * 说明加了线程礼让，有可能出现B线程先执行的情况，但不是一定的，线程礼让出来，如何执行还是看CPU如何调度。
     *
     */
    public static void main(String[] args) {

        MyYield myYield = new MyYield();

        new Thread(myYield,"A").start();
        new Thread(myYield,"B").start();

    }
}

class MyYield implements Runnable{

    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程开始执行");
        //线程礼让
        Thread.yield();
        System.out.println(Thread.currentThread().getName() + "线程停止执行");

    }
}
