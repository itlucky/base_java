package com.itlucky.juc.lock8;

import java.util.concurrent.TimeUnit;


/**
 * 8锁问题3：增加了一个普通方法后，执行顺序为： 1-hello  2-发短信
 *
 * 8锁问题4：两个对象，两个同步方法【两个调用者，两把锁】，谁先拿到谁先执行。  1-打电话  2-发短信
 *
 */
public class Lock_8lock_Test2 {

    public static void main(String[] args) {
        // 两个对象，两个调用者，两把锁
        Phone2 phone1 = new Phone2();
        Phone2 phone2 = new Phone2();

        new Thread(()->{
            phone1.sendMsg();
        },"AAA").start();

        new Thread(()->{
            phone2.call();
        },"BBB").start();

    }
}

class Phone2{
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

    //这里没有锁，不是同步方法，不受锁的影响。
    public void hello(){
        System.out.println("hello ~~");
    }

}
