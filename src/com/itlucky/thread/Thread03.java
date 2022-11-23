package com.itlucky.thread;

/**
 * 创建线程方式2：实现Runnable接口，重写run()方法
 *         执行线程需要丢入Runnable接口的实现类，然后调用start()方法
 *
 *  【推荐使用方式2】
 *
 *
 */
public class Thread03 implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i<200; i++) {
            System.out.println("我在看代码"+i);
        }
    }

    public static void main(String[] args) {

        // 创建Runnable接口的实现类对象
        Thread03 t3 = new Thread03();
        // 将实现Runnable接口的类放进线程t中，然后调用start()方法启动
        // 创建线程对象，通过线程对象来开启我们的线程【代理】
//        Thread t = new Thread(t3);
//        t.start();
        new Thread(t3).start();

        for (int i = 0; i<200; i++) {
            System.out.println("=====打游戏"+i);
        }
    }
}
