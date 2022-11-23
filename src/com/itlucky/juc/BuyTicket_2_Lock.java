package com.itlucky.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Lock方式，juc包下的。
 * 复制类：BuyTicket_1_Synchronized，用LOCK改造
 */

public class BuyTicket_2_Lock {

    public static void main(String[] args) {

        //并发：多个线程操作同一个资源类,把资源类丢入线程
        Ticket2 ticket = new Ticket2();

        // @FunctionalInterface函数式接口，用Lambda表达式操作 (参数)->{代码}
        new Thread(() -> { for (int i = 0; i<40; i++)  ticket.sale(); }, "A").start();
        new Thread(() -> { for (int i = 0; i<40; i++)  ticket.sale(); }, "B").start();
        new Thread(() -> { for (int i = 0; i<40; i++)  ticket.sale(); }, "C").start();

    }

}


// 资源类 OOP思想
class Ticket2 {

    private int number = 30;

    Lock lock = new ReentrantLock();

    public void sale() {
        lock.lock();
        try {
            if (number>0) {
                System.out.println(Thread.currentThread().getName()+"卖出了第"+number--+"张票，剩余-->"+number+"张");
            }
        } catch (Exception e) {
            //TODO
        } finally {
            lock.unlock();
        }
    }
}

