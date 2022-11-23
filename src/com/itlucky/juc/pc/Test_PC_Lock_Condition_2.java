package com.itlucky.juc.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * 要实现A执行完调用B，B执行完调用C，C执行完调用A
 *
 */
public class Test_PC_Lock_Condition_2 {

    public static void main(String[] args) {

        Data3 data = new Data3();

        new Thread(() -> {
            for (int i = 0; i<10; i++) {
                data.printA();
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i<10; i++) {
                data.printB();
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i<10; i++) {
                data.printC();
            }
        }, "C").start();
    }
}

class Data3 { // 资源类

    private final Lock lock = new ReentrantLock();

    private final Condition condition1 = lock.newCondition();
    private final Condition condition2 = lock.newCondition();
    private final Condition condition3 = lock.newCondition();

    private int num = 1; //值 1:A 2:B 3:C

    public void printA() {
        lock.lock();
        try {
            // 等待--业务  --通知
            while (num!=1) {
                condition1.await();
            }
            num = 2;
            System.out.println(Thread.currentThread().getName()+"---》AAAAAAA");
            //唤醒B
            condition2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printB() {
        lock.lock();
        try {
            // 等待--业务  --通知
            while (num!=2) {
                condition2.await();
            }
            num = 3;
            System.out.println(Thread.currentThread().getName()+"---》BBBBBBBB");
            //唤醒C
            condition3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printC() {
        lock.lock();
        try {
            // 等待 --业务  --通知
            while (num!=3) {
                condition3.await();
            }
            num = 1;
            System.out.println(Thread.currentThread().getName()+"---》CCCCCCCCC");
            //唤醒A
            condition1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
