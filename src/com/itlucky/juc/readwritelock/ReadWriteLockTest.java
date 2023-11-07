package com.itlucky.juc.readwritelock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 这里的
 * 写锁就是平常说的【独占锁】，一次只能被一个线程占有
 * 读锁就是平常说的【共享锁】，多个线程可以同时占有
 *
 * ReadWriteLock : 读写锁
 * 读 - 读  可以共存
 * 读 - 写  不能共存！
 * 写 - 写  不能共存
 *
 */
public class ReadWriteLockTest {
    public static void main(String[] args) {
        MyCacheLock myCache = new MyCacheLock();
        // 写入  多线程写
        for (int i = 1; i<=10; i++) {
            final int tmp = i;
            new Thread(()->{
                myCache.put(tmp+"",tmp+"");
            },String.valueOf(i)).start();
        }

        //读取  多线程读
        for (int i = 1; i<=10; i++) {
            final int tmp = i;
            new Thread(()->{
                myCache.get(tmp+"");
            },String.valueOf(i)).start();
        }
    }
}

/**
 * 自定义缓存【未加锁，会有各种问题】
 */
class MyCache{

    private volatile Map<String,Object> map = new HashMap<>();
    //存--写
    public void put(String key,Object obj){
        System.out.println(Thread.currentThread().getName() + "开始写入" + key);
        map.put(key,obj);
        System.out.println(Thread.currentThread().getName() + "写入OK");
    }

    //取--读
    public void get(String key){
        System.out.println(Thread.currentThread().getName() + "开始读取" + key);
        Object o = map.get(key);
        System.out.println(Thread.currentThread().getName() + "读取成功");
    }
}

/**
 * 自定义缓存【加锁】
 */
class MyCacheLock{

    private volatile Map<String,Object> map = new HashMap<>();

    //平常都是使用private Lock lock = new ReentrantLock();得到锁对象
    // 读写锁  这里是用ReentrantReadWriteLock,可以更加细粒度的控制，用法跟平常的ReentrantLock一样。
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    //存--写入的时候，只希望同时只有一个线程写
    public void put(String key,Object obj){

        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "开始写入" + key);
            map.put(key,obj);
            System.out.println(Thread.currentThread().getName() + "写入OK");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

    //取--读，所有线程都可读
    public void get(String key){

        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "开始读取" + key);
            Object o = map.get(key);
            System.out.println(Thread.currentThread().getName() + "读取成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}