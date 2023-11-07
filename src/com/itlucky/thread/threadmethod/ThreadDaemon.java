package com.itlucky.thread.threadmethod;

/**
 * 守护线程
 *
 *  线程分为用户线程和守护线程；
 *  虚拟机必须确保用户线程执行完毕；
 *  虚拟机不用等待守护线程执行完毕；
 *  守护线程应用:后台记录操作日志,监控内存,垃圾回收等待.
 *
 *
 */
public class ThreadDaemon {

    //根据执行结果，可以看出来：当用户线程执行结束之后，不管守护线程是否执行结束，程序也就会结束。
    public static void main(String[] args) {

        DaemonThread dt = new DaemonThread();
        Thread thread = new Thread(dt);
        thread.setDaemon(true); // 设置为true表明该线程是一个守护线程。正常默认值是false，就是一个个用户线程

        //守护线程启动
        thread.start();

        //用户线程启动
        new Thread(new UserThread()).start();
    }
}

//守护线程
class DaemonThread implements Runnable{

    @Override
    public void run() {
        while (true){
            System.out.println("守护线程在执行！");
        }
    }
}

//用户线程
class UserThread implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i<300; i++) {
            System.out.println("#####用户线程在执行#####");
        }
    }
}