package com.itlucky.thread.threadmethod;

import java.text.SimpleDateFormat;
import java.util.Date;


//模拟倒计时
public class ThreadSleep2 {

    public static void main(String[] args)
        throws InterruptedException {
//        tenDown();
        // 打印当前系统时间
        Date startTime = new Date(System.currentTimeMillis());

        while (true){
            Thread.sleep(1000);
            System.out.println(new SimpleDateFormat("HH:mm:ss").format(startTime));
            //更新当前时间
            startTime = new Date(System.currentTimeMillis());
        }


    }


    public static void tenDown()
        throws InterruptedException {
        int num = 10;
        while (true){
            //隔一秒打印一个数字
            Thread.sleep(1000);
            System.out.println(num--);
            if(num <= 0){
                break;
            }
        }
    }


}
