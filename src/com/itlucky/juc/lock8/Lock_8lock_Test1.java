package com.itlucky.juc.lock8;

import java.util.concurrent.TimeUnit;


/**
 * 8锁：就是关于锁的8个问题
 * 是因为锁的原因。
 *
 * 8锁问题1、标准情况下，两个线程执行结果。  1-发短信 2-打电话
 *
 * 8锁问题2、让发短信延迟4s。              1-发短信 2-打电话
 *
 */
public class Lock_8lock_Test1 {

    public static void main(String[] args) {

        Phone phone = new Phone();

        new Thread(()->{
            phone.sendMsg();
        },"A").start();

        // 暂停1s
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            phone.call();
        },"B").start();

    }
}

class Phone{

    // 锁的对象是方法的调用者
    // 这里发短信和打电话两个方法的用的是同一个锁，都是Phone对象。谁先拿到锁谁先执行。
    public synchronized void sendMsg(){

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    public synchronized void call(){
        System.out.println("----打电话----");
    }
}