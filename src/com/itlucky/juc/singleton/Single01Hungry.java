package com.itlucky.juc.singleton;

/**
 * 单例模式：饿汉式
 * 单例模式里面，构造器都是私有，保证内存中对象只有一个！
 */
public class Single01Hungry {

    //饿汉式缺点: 如果饿汉式里面一开始进来就有开辟空间的操作，但是后面又没用到，就会造成空间浪费
    //如下,就会造成浪费空间。那么就想要用的时候再去创建一个对象，也就是懒汉式单例。
    private byte[] data1 = new byte[1024*1024];
    private byte[] data2 = new byte[1024*1024];
    private byte[] data3 = new byte[1024*1024];
    private byte[] data4 = new byte[1024*1024];

    //构造器私有
    private Single01Hungry(){
        System.out.println(Thread.currentThread().getName() + "实例化了 Single01Hungry");
    }
    // 所谓的饿汉式，就是上来不管需不需要，直接就给实例化一个对象
    private final static Single01Hungry HUNGRY = new Single01Hungry();

    public static Single01Hungry getInstance(){
        return HUNGRY;
    }

    public static void main(String[] args) {
        for (int i = 1; i<=20; i++) {
            new Thread(()->{
                Single01Hungry.getInstance();
            }).start();
        }
    }
}
