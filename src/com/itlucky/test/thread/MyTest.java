package com.itlucky.test.thread;

/**
 * 可重入锁(同一个线程可以多次使用已经获得的锁,无须阻塞等待)
 * 验证: 这里用synchronized 。
 * 分析：这里的对象锁只有一个，就是mt对象的锁。当执行mt的testA方法时，该线程获得rt对象的锁。
 *      在testA方法内执行testB方法时再次请求mt对象的锁，因为synchronized是可重入锁，
 *      所以又可以得到该锁。循环这个过程。假设不是可重入锁的话，那么请求的过程中会出现阻塞，从而导致死锁。
 * 原理：每一个锁关联一个线程持有者和计数器，当计数器为 0 时表示该锁没有被任何线程持有，
 *      那么任何线程都可能获得该锁而调用相应的方法；当某一线程请求成功后，JVM会记下锁的持有线程，
 *      并且将计数器置为 1；此时其它线程请求该锁，则必须等待；而该持有锁的线程如果再次请求这个锁，
 *      就可以再次拿到这个锁，同时计数器会递增1；当线程退出同步代码块时，计数器会递减1，如果计数器为 0，则释放该锁。
 */
public class MyTest {

    public static void main(String[] args) {

        MyThread mt = new MyThread();
        //无线循环
        while (true){
            new Thread(()->{
                mt.testA();
            }).start();
        }
    }
}

class MyThread{

    public synchronized void testA(){
        System.out.println("testA---" +Thread.currentThread().getName());
        testB();
    }

    public synchronized void testB(){
        System.out.println("testB---" +Thread.currentThread().getName());
    }
}