package com.itlucky.thread.threadsync;

/**
 * 线程不安全的示例1:不安全的买票
 *
 *
 * 每个线程都有自己的工作内存，如果控制不当就会导致数据出现紊乱。
 * 比如这里买票，如果不加锁，在某一个时间点，多个线程读到的票余量都是20张，然后都放到各自的内存中，
 * 然后进行业务处理，这样就出现了多个人买到同一张票的情况，或者是最后出现票数剩余为负数的情况。
 *
 */
public class UnSafe01_BuyTicket{


    public static void main(String[] args) {

        BuyTicket buyTicket = new BuyTicket();

        new Thread(buyTicket,"李四").start();
        new Thread(buyTicket,"黄牛").start();
        new Thread(buyTicket,"臧三").start();

    }

}

class BuyTicket implements Runnable{
    //票数
    private int ticketNum = 10;
    // 线程停止标识【外部停止方式】
    boolean flag = true;

    //synchronized:关键字加载方法上，实现线程锁机制,这里如果不加，就会出现多个人抢到同一张票或出现抢到负数的票，
    // 这都属于线程不安全的现象
    @Override
    public void run() {
        while (flag){
            buy();
        }
    }

    private synchronized void buy(){
        if(ticketNum <= 0){
            flag = false;
            return;
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "抢到了第" + ticketNum-- +"张票！");
    }
}
