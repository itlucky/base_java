package com.itlucky.thread.threadmethod;

/**
 * join()方法可以理解为插队，强制执行。
 */

public class ThreadJoin implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i<300; i++) {
            System.out.println("线程VIP来了"+i);
        }
    }

    public static void main(String[] args)
        throws InterruptedException {

        ThreadJoin tj = new ThreadJoin();
        Thread thread = new Thread(tj);
        thread.start();

        // main 线程执行的循环
        for (int j = 0; j<200; j++) {
            // 当main执行到100的时候，线程VIP调用join强制执行,主线程就会等待VIP线程的内容全部执行完才会执行
            if(j==100){
                thread.join();
            }
            System.out.println("main线程执行" + j);
        }

//        for (Thread.State c : Thread.State.values())
//            System.out.println(c);
    }

}
