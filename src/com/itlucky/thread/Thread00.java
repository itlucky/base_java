package com.itlucky.thread;

public class Thread00 extends Thread{

    @Override
    public void run() {
        for (int i = 0; i<200; i++) {
            System.out.println("学习多线程" + i);
        }
    }

    // 主线程
    public static void main(String[] args) {

        Thread00 t0 = new Thread00();
        // 调用start()方法开启线程，t0和主线程main()的结果是交叉执行的，说明是多线程
        t0.start(); // 如果改为t0.run()可以发现是t0执行完才会执行主线程的内容，还是单线程

        for (int i = 0; i<200; i++) {
            System.out.println("打游戏"+i);
        }
    }

}


