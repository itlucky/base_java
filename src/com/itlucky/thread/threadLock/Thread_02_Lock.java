package com.itlucky.thread.threadLock;

import java.util.concurrent.locks.ReentrantLock;


/**
 * 测试Lock锁
 */
public class Thread_02_Lock {

    public static void main(String[] args) {

        BuyTicket buyTicket = new BuyTicket();

        new Thread(buyTicket,"小名").start();
        new Thread(buyTicket,"小黑").start();
        new Thread(buyTicket,"小白").start();
    }


}


class BuyTicket implements Runnable{

    //ReentrantLock 可重入锁。这里的写法参照安全集合：CopyOnWriteArrayList中源码的写法
    final transient ReentrantLock lock = new ReentrantLock();

    private int ticketNum = 10;

    private boolean flag = true;

    @Override
    public void run() {
        while (flag){
            try {
                // 加锁
                lock.lock();
                buyticket();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 解锁
                lock.unlock();
            }
        }
    }

    private void buyticket()
        throws InterruptedException {
        if(ticketNum <= 0){
            flag = false;
            return;
        }
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getName() + "抢到了第" + ticketNum-- +"张票");

    }
}