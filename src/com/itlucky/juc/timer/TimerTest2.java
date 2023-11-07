package com.itlucky.juc.timer;

import java.util.Date;
import java.util.Timer;

public class TimerTest2 {


    public static void main(String[] args) {

        Timer timer = new Timer();
        System.out.println("二次发起定时器准备调用任务执行了" +"===="+ Thread.currentThread().getName());
        // 5000表示多少时间运行一次
        timer.schedule(new MyTimerTask(),new Date(),5000);


    }

}
