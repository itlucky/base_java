package com.itlucky.thread.threadsync;

import java.util.ArrayList;
import java.util.List;


/**
 * 线程不安全的集合
 */
public class UnSafeList {

    public static void main(String[] args)
        throws InterruptedException {

        List<String> list = new ArrayList<>();
        //for循环开启10000个线程,将每个线程的名字添加到集合当中去。
        for (int i = 0; i<10000; i++) {
            new Thread(()->{
//                synchronized (list){
//                    list.add(Thread.currentThread().getName());
//                }
                list.add(Thread.currentThread().getName());
            }).start();
        }
        //加一个延时，为了让循环写完，然后获取到list的大小
        Thread.sleep(1000);
        //最终得到集合的大小始终小于10000，说明了ArrayList是线程不安全的。不到10000是中间有不同的线程存放到了集合的同一个位置。
        System.out.println(list.size());
    }

}
