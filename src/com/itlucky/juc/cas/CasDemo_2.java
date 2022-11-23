package com.itlucky.juc.cas;

import java.util.concurrent.atomic.AtomicLong;


public class CasDemo_2 {
    //ABA问题
    public static void main(String[] args) {

        AtomicLong atomicLong = new AtomicLong(2020);

        //public final boolean compareAndSet(long expect, long update)
        //如果期望的值拿到了，就更新，否则不更新  CAS是CPU的并发原语
        //================捣乱的线程================
        System.out.println(atomicLong.compareAndSet(2020, 2021));
        System.out.println(atomicLong.get());

        System.out.println(atomicLong.compareAndSet(2021, 2020));
        System.out.println(atomicLong.get());
        //================期望的线程================
        System.out.println(atomicLong.compareAndSet(2020, 6666));
        System.out.println(atomicLong.get());

        //打印结果可以看到都执行成功了。对于平常写的SQL,解决这种问题就是通过乐观锁解决。
    }
}
