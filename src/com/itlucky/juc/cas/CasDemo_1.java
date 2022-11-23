package com.itlucky.juc.cas;

import java.util.concurrent.atomic.AtomicLong;


public class CasDemo_1 {

    public static void main(String[] args) {

        AtomicLong atomicLong = new AtomicLong(2020);

        //public final boolean compareAndSet(long expect, long update)
        //如果期望的值拿到了，就更新，否则不更新  CAS是CPU的并发原语
        System.out.println(atomicLong.compareAndSet(2020, 2021));
        System.out.println(atomicLong.get());
        atomicLong.getAndIncrement();

        System.out.println(atomicLong.compareAndSet(2021, 6666));
        System.out.println(atomicLong.get());
    }
}
