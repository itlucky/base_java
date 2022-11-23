package com.itlucky.thread.threadsync;

/**
 * 线程不安全的示例2:不安全的银行取钱
 *
 * 案例：两个人去银行取钱  账户
 *
 *如果不加锁，最终卡里的钱会出现负数的情况。
 *
 *
 */
public class UnSafe02_TakeMoney {

    public static void main(String[] args) {
        //账户
        Account account = new Account(100,"银行卡");

        TakeMoney you = new TakeMoney(account,50,"you");
        TakeMoney girlF = new TakeMoney(account,80,"girlF");

        you.start();
        girlF.start();
    }

}

//账户
class Account{
    //余额
    int money ;
   //卡名
    String name;

    public Account(int money, String name) {
        this.money = money;
        this.name = name;
    }
}

//银行：模拟取款
class TakeMoney extends Thread{
    //账户
    Account account;
    //取了多少钱
    int takingMoney;
    //现在手里多少钱
    int nowMoney;

    public TakeMoney(Account account,int takingMoney,String name){
        super(name);
        this.account = account;
        this.takingMoney = takingMoney;
    }

    //取钱
    //这里给run方法加synchronized会发现锁不住，因为synchronized默认锁的是this[当前对象]
    //synchronized(Obj){}代码块，锁住当前要变化的量(Obj)：即要对当前资源进行增、删、改操作的。然后将操作的代码块放在花括号中，如下代码
    //同步块可以锁任何东西，一定要判断好当前需要锁的是什么
    @Override
    public void run() {
        // 默认就是synchronized(this)
        synchronized (account){
            if(account.money - takingMoney < 0){
                System.out.println(Thread.currentThread().getName()+"钱不够，取不了！");
                return;
            }
            //模拟延时
            // 在这里休眠1s 那么两个线程会同时到达这里，看到卡里的余额都是100，也就都能取，最终卡里会出现负金额。
            // 每个线程都有自己的内存空间，会先把要操作的数据读到自己的内存空间然后操作。
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //卡内余额
            account.money = account.money-takingMoney;
            //手里的钱
            nowMoney +=takingMoney;
            //这里 this.getName() == Thread.currentThread().getName()，因为这个类继承了Thread
            System.out.println(this.getName() + "手里的钱：" +nowMoney);
            System.out.println(account.name + "余额为：" + account.money);
        }
    }
}