package com.itlucky.juc.singleton;

/**
 * 静态内部类实现单例:
 * 该解决方案的根本就在于：利用classloder的机制来保证初始化instance时只有一个线程。
 *                      JVM在类初始化阶段会获取一个锁，这个锁可以同步多个线程对同一个类的初始化。
 *
 * 也是一种懒加载：加载一个类时，其内部类不会同时被加载。
 *              一个类被加载，当且仅当其某个静态成员（静态域、构造器、静态方法等）被调用时发生。
 *  【内部类加载：https://www.iteye.com/blog/yongliang567-904467】
 */
public class Single03Inner {

    private Single03Inner(){
        System.out.println(Thread.currentThread().getName() + "初始化了Single03Inner");
    }

    static class InnerClass{
        private static final Single03Inner singleton = new Single03Inner();
    }

    public static Single03Inner getInstance(){
        return InnerClass.singleton;
    }

    public static void main(String[] args) {
        for (int i = 1; i<=100; i++) {
            new Thread(()->{
                Single03Inner.getInstance();
            }).start();
        }
    }
}
