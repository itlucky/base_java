package com.itlucky.thread;

/**
 * 多线程操作同一个对象
 * ---买火车票的例子
 */
public class Thread04 implements Runnable{
    //票数
    private int ticketNums = 10;

    @Override
    public void run() {

        while (true){

            if(ticketNums <= 0){
                break;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 拿到一张票就执行--操作。
            System.out.println(Thread.currentThread().getName()+"拿到了第"+(ticketNums--)+"张票票");
        }
    }

    /**
     * 根据执行结果可以看到，会有同一张票被多个人抢到的情况，这就是并发带来的线程不安全问题
     *
     */
    public static void main(String[] args) {

        Thread04 t4 = new Thread04();
        // 多线程操作同一个资源
        new Thread(t4,"小明").start();
        new Thread(t4,"晓红").start();
        new Thread(t4,"黄牛").start();


    }
}
