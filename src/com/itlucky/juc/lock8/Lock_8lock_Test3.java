package com.itlucky.juc.lock8;

import java.util.concurrent.TimeUnit;


/**
 * 8锁问题5：两个静态的同步方法,只有一个对象 1发短信 2打电话
 *
 * 8锁问题6：两个静态的同步方法,有两个对象 {static修饰的，两个对象的锁是同一个Class,所以还还是谁先拿到资源谁先执行}
 *          1发短信 2打电话
 */
public class Lock_8lock_Test3 {
    public static void main(String[] args) {
        //两个对象的类模板只有一个
        Phone3 phone1 = new Phone3();
        Phone3 phone2 = new Phone3();

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

class Phone3{

    // static 静态方法，说明是类加载的时候就已经存在。并且是全局唯一。这里synchronized锁的就是Class
    //
    public static synchronized void sendMsg(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    public static synchronized void call(){
        System.out.println("打电话");
    }

}