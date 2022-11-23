package com.itlucky.juc.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class FutureTest {
    public static void main(String[] args)
        throws ExecutionException, InterruptedException {
        // 没有返回值的 runAsync 异步回调
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
//            System.out.println("开始执行");
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread().getName() + "执行");
//        });
//        System.out.println("完成?"+completableFuture.isDone());
//        System.out.println("1111111");//main线程
//        System.out.println("取消？"+completableFuture.isCancelled());
//        completableFuture.get(); // 获取阻塞执行结果

        //有返回值的异步回调
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName() +"supplyAsync===>Integer");
            int i = 10/0;
            return 1024;
        });

        System.out.println(completableFuture.whenComplete((t, u) -> {
            System.out.println("t--->"+t); //正常的返回结果
            System.out.println("u--->"+u); //异常的错误信息
        }).exceptionally((e) -> {
            System.out.println(e.getMessage());
            return 2333; //可以获取到错误的返回结果
        }).get());

    }
}
