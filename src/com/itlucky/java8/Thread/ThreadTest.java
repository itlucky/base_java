package com.itlucky.java8.Thread;

public class ThreadTest {
    public static void main(String[] args) {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("444");
                throw new RuntimeException("ex");
            }
        });

        Thread thread = new Thread(()->{
            System.out.println("111");
            throw new RuntimeException("ex");

        });

        thread1.start();

        thread.start();
    }
}
