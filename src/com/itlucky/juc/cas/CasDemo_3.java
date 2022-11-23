package com.itlucky.juc.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;


public class CasDemo_3 {
    //解决ABA问题
    public static void main(String[] args) {
        //改为原子引用
//        AtomicLong atomicLong = new AtomicLong(2020);
        //
        AtomicStampedReference<Long> atomicReference = new AtomicStampedReference<Long>(10L,1);

        new Thread(()->{
            //获得版本号(也就是时间戳)
            int stamp = atomicReference.getStamp();
            System.out.println("A1--->>" + stamp);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // public boolean compareAndSet(V   expectedReference, 期望的值
            //                                 V   newReference,   新的值
            //                                 int expectedStamp,  期望的时间戳
            //                                 int newStamp        新的时间戳)
            System.out.println(atomicReference.compareAndSet(10L, 22L, atomicReference.getStamp(), atomicReference.getStamp()+1));

            System.out.println("A2--->>" + atomicReference.getStamp());
            System.out.println(atomicReference.compareAndSet(22L, 10L, atomicReference.getStamp(), atomicReference.getStamp()+1));

            System.out.println("A3--->>" + atomicReference.getStamp());

        },"A").start();

        new Thread(()->{
            //获得版本号 (也就是时间戳)
            int stamp = atomicReference.getStamp();
            System.out.println("B1--->>" + stamp);

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(atomicReference.compareAndSet(10L, 66L, stamp, stamp+1));
            System.out.println("B2--->>" + atomicReference.getStamp());

        },"B").start();

        //执行打印结果可以看到，线程B就不会操作成功，因为版本号不是一开始拿到的1，
        // 线程A执行了一些列操作之后，虽然预期的值没变，但是版本号已经发生变化了。
    }
}
