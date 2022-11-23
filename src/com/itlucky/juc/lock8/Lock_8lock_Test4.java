package com.itlucky.juc.lock8;

import java.util.concurrent.TimeUnit;


/**
 * 8锁问题7：一个静态同步方法，一个普通同步方法，一个对象。
 *          这里其实是两把锁：{锁的是Class类模板}{锁的是方法调用的对象}，所以不相关的锁。谁先拿到资源谁先执行
 *
 * 8锁问题8：一个静态同步方法，一个普通同步方法，两个对象。
 *          同上面一样，还是跟锁有关，两把不同的锁。谁先拿到资源谁先执行。
 */
public class Lock_8lock_Test4 {

    public static void main(String[] args) {

        Phone4 phone1 = new Phone4();
        Phone4 phone2 = new Phone4();

        new Thread(()->{
            phone1.sendMsg();
        },"A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            phone2.call();
        },"B").start();
    }

}

class Phone4{

    //静态的同步方法  锁的是Class类模板
    public static synchronized void sendMsg(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    // 普通的同步方法 锁的是方法调用者
    public synchronized void call(){
        System.out.println("打电话");
    }

}