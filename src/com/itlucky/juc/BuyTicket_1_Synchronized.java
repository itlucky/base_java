package com.itlucky.juc;

/**
 * 真正的多线程开发，【公司中的开发，降低耦合性】
 * 线程就是一个单独的资源类，没有任何的附属操作
 *
 *  属性、方法
 */

//传统的synchronized关键字方式
//synchronized本质：队列+锁

public class BuyTicket_1_Synchronized {

    public static void main(String[] args) {

        //并发：多个线程操作同一个资源类,把资源类丢入线程
        Ticket ticket = new Ticket();

        // @FunctionalInterface函数式接口，用Lambda表达式操作 (参数)->{代码}
        new Thread(() -> {
            for (int i = 0; i<40; i++) {
                ticket.sale();
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i<40; i++) {
                ticket.sale();
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i<40; i++) {
                ticket.sale();
            }
        }, "C").start();
    }

}


// 资源类 OOP思想
class Ticket {

    //属性、方法
    //票数
    private int number = 30;

    //卖票的方法  synchronized:
    public synchronized void sale() {
        if (number>0) {
            System.out.println(Thread.currentThread().getName()+"卖出了第"+number--+"张票，剩余-->"+number+"张");
        }
    }
}