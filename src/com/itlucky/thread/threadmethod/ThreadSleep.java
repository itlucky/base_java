package com.itlucky.thread.threadmethod;

/**
 * 模拟线程网络延时
 *      作用：1.放大问题的发生性；
 *
 *  每个对象都有一个锁，sleep()不会释放锁
 */
public class ThreadSleep implements Runnable{

    private int ticketNum = 10;


    @Override
    public void run() {
        while (true){
            if(ticketNum <=0){
                break;
            }
            //模拟网路延时
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "抢到了第"+ticketNum--+"张票");
        }
    }

    public static void main(String[] args) {
        ThreadSleep ts = new ThreadSleep();
        new Thread(ts,"张三").start();
        new Thread(ts,"李四").start();
        new Thread(ts,"赵六").start();
    }


}
