package com.itlucky.juc.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * juc实现生产者消费者模型。
 * 基于synchronized版改造
 */
public class Test_PC_Lock_Condition {

    public static void main(String[] args) {

        Data2 data2 = new Data2();

        new Thread(()->{
            for (int i = 0; i<10; i++) {
                data2.increment();
            }
        },"A").start();
        new Thread(()->{
            for (int i = 0; i<10; i++) {
                data2.decrement();
            }
        },"B").start();
        new Thread(()->{
            for (int i = 0; i<10; i++) {
                data2.increment();
            }
        },"C").start();
        new Thread(()->{
            for (int i = 0; i<10; i++) {
                data2.decrement();
            }
        },"D").start();

    }

}

class Data2{

    private int num ;

    // 写法参照jdk中Condition的使用
    final Lock lock = new ReentrantLock();
    final Condition condition  = lock.newCondition();

    public void increment(){

        lock.lock();
        try {
            while (num !=0){
                //等待
                condition.await();
            }
            num ++;
            System.out.println(Thread.currentThread().getName() + "++++>完成了+操作"+num);
            //唤醒其他
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void decrement(){
        lock.lock();
        try {
            while (num !=1){
                //等待
                condition.await();
            }
            num --;
            System.out.println(Thread.currentThread().getName() + "------->完成了-操作"+num);
            //唤醒
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}