package com.itlucky.juc.jucutilclass;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


/**
 * Semaphore 信号量
 * 模拟抢车位： 6个车3个停车位
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        //线程数量：代表停车位
        Semaphore semaphore = new Semaphore(3);

        for (int i = 1; i<=6; i++) {
            final int tmp = i;
            new Thread(() -> {
                try {
                    semaphore.acquire();//获取
                    System.out.println(tmp+"号车抢到了车位");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();//释放
                    System.out.println(tmp+"号车离开了车位");
                }
            }).start();
        }
    }
}
