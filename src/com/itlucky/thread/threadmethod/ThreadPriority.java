package com.itlucky.thread.threadmethod;

/**
 * 线程优先级：
 *
 * Java提供一个线程调度器来监控程序中启动后进入就绪状态的所有线程，线程调度器按照优先级决定应该调度哪个线程来执行。
 *
 * 优先级低只是意味着获得调度的概率低.并不是优先级低就不会被调用了.这都是看CPU的调度。
 * 一般情况下，线程优先级越高，越容易被CPU优先执行到。
 *
 *  性能倒置:cpu有时并不一定就会按照优先级来调度，原本优先级高的线程却后执行。
 *
 */
public class ThreadPriority {

    public static void main(String[] args) {

        //主线程main的优先级，默认的就是5
        System.out.println(Thread.currentThread().getName() + "---->优先级："+ Thread.currentThread().getPriority());

        MyPriority myPriority = new MyPriority();

        Thread t1 = new Thread(myPriority,"t1");
        Thread t2 = new Thread(myPriority,"t2");
        Thread t3 = new Thread(myPriority,"t3");
        Thread t4 = new Thread(myPriority,"t4");
        Thread t5 = new Thread(myPriority,"t5");
        Thread t6 = new Thread(myPriority,"t6");

        t1.start();
        //先设置优先级在启动，否则优先级不起效
        t2.setPriority(2);
        t2.start();

        t3.setPriority(Thread.NORM_PRIORITY);
        t3.start();

        t4.setPriority(4);
        t4.start();

        t5.setPriority(7);
        t5.start();

        t6.setPriority(Thread.MAX_PRIORITY);
        t6.start();


    }
}

class MyPriority implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "---->优先级："+ Thread.currentThread().getPriority());
    }
}
