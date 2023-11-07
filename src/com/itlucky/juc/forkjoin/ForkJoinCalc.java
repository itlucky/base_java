package com.itlucky.juc.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;


/**
 * 数据量越大，ForkJoin的优势才能体现出来，所以一般都是大数据的情况下才会使用。
 */
public class ForkJoinCalc {
    /**
     * 计算1-100亿求和
     */
    public static void main(String[] args)
        throws ExecutionException, InterruptedException {
//        test1();  //计算结果为：500000000500000000耗时：271
        test2();
//        test3(); //计算结果为：500000000500000000耗时：135
    }

    // 方式一：最笨的方式
    static void test1(){
        long count = 0;
        long start = System.currentTimeMillis();

        for (long i=1;i<=10_0000_0000L;i++){
            count +=i;
        }
        long end = System.currentTimeMillis();
        System.out.println("计算结果为：" + count +"耗时："+(end -start));
    }
    // 方式二：使用ForkJoin，可以调优
    static void test2()
        throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask task = new ForkJoinDemo(0L, 10_0000_0000L);
//        ForkJoinTask<Long> submit = pool.submit(task); //提交任务
//        Long sum = submit.get(); //这个方法会阻塞等待
        Long sum  = (Long)pool.invoke(task);
        long end = System.currentTimeMillis();
        System.out.println("计算结果为：" + sum +"耗时："+(end -start));

    }
    // 方式三：Stream并行流计算【高级，推荐】
    static void test3(){
        long start = System.currentTimeMillis();

        long sum = LongStream.rangeClosed(0, 100_0000_0000L).parallel().reduce(0, Long::sum);

        long end = System.currentTimeMillis();
        System.out.println("计算结果为：" + sum +"耗时："+(end -start));
    }
}
