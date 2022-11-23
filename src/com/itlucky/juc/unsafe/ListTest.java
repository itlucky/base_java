package com.itlucky.juc.unsafe;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * java.util.ConcurrentModificationException 并发修改异常
 */
public class ListTest {
    public static void main(String[] args) {
//        List<String> list = Arrays.asList("1","2","3");
//        list.forEach(System.out::println);
        // 并发下，ArrayList是不安全的。
        // 解决方案1：ArrayList 改为Vector,因为Vector集合是线程安全的。
        //          查看Vector源码，可以知道Vector中的add方法用了synchronized关键字。
        //          思考：为啥不直接给ArrayList的add方法加上synchronized使其线程安全？
        //          其实Vector的add方法在1.1版本就有，而ArrayList的是在1.2版本才出来的。
        //          所以没有加上同步处理，并不是没有考虑到线程安全的问题。
        //
        // 解决方案2：工具类转换成线程安全的结合对象Collections.synchronizedList()
        //          在实际开发中，工具类的作用很大，可以多看看。
        //
        // 解决方案3：ArrayList改用CopyOnWriteArrayList
        //           查看源码可知，CopyOnWriteArrayList的add方法用的是ReentrantLock，
        //           而且存储用的数组是transient volatile修饰。
        //
        //           CopyOnWrite：写入时复制  cow 计算机程序设计领域的一种优化策略；
        //           多个线程调用的时候，list,读取的时候固定，写入(覆盖)
        //           读写分离，在写入的时候避免覆盖，造成数据问题！
        //
        // 思考：为什么推荐使用CopyOnWriteArrayList而不是Vector？
        //      其一：因为Vector底层用的synchronized，效率低(只要用到synchronized就会想到效率低)
        //

        //  List<String> list = new ArrayList<>();
        //  List<String> list = new Vector<>();
        //  List<String> list = Collections.synchronizedList(new ArrayList<>());

        List<String> list = new CopyOnWriteArrayList<>();

        for (int i =1; i<=10; i++) {
//            list.add(UUID.randomUUID().toString().substring(0,3));
//            System.out.println(list);
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,3));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
