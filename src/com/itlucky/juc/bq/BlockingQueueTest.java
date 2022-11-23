package com.itlucky.juc.bq;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * 阻塞队列
 */
public class BlockingQueueTest {

    public static void main(String[] args)
        throws InterruptedException {

        test1();
//        test2();
//        test3();
//        test4();
    }

    /**
     * 抛出异常
     */
    public static void test1(){
        //入参代表队列大小
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        //add()方法会有返回值
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));

        //查询队首元素
        System.out.println(blockingQueue.element());
        //java.util.IllegalStateException: Queue full 超过队列大小会抛出该异常
        //System.out.println(blockingQueue.add("d"));

        //队列FIFO(先入先出)，所以remove的时候也是先添加的先出
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());

        //remove超过队列大小的次数，也会抛出异常
        //java.util.NoSuchElementException
        System.out.println(blockingQueue.remove());
    }

    /**
     * 不抛异常，有返回值
     */
    public static void test2(){

        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        //
        System.out.println(blockingQueue.offer("aa"));
        System.out.println(blockingQueue.offer("bb"));
        System.out.println(blockingQueue.offer("cc"));
        //查询队首元素
        System.out.println(blockingQueue.peek());
        //System.out.println(blockingQueue.offer("d")); //超过队列大小，再添加不抛异常，返回false

        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());

        //这里poll返回null,不抛出异常
        System.out.println(blockingQueue.poll());
    }

    /**
     * 等待，阻塞（一直阻塞，死等）
     */
    public static void test3()
        throws InterruptedException {
        //队列的大小
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        // 一直阻塞
        blockingQueue.put("a");
        blockingQueue.put("b");
        blockingQueue.put("c");
        //队列没有位置存了，就会一直阻塞在这里
//        blockingQueue.put("d");

        //take()方法有返回值
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        //没有这个元素，会一直阻塞。
        System.out.println(blockingQueue.take());
    }

    /**
     * 等待，阻塞（超时等待，超过一定时间就不等待了）
     */
    public static void test4()
        throws InterruptedException {
        //队列的大小
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);

        blockingQueue.offer("a");
        blockingQueue.offer("b");
        blockingQueue.offer("c");
        //超过队列大小，设置超时等待时间，这里等待超过3s就退出
//        blockingQueue.offer("d",3, TimeUnit.SECONDS);

        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        //等待超过3秒就退出
        System.out.println(blockingQueue.poll(3,TimeUnit.SECONDS));

    }
}

