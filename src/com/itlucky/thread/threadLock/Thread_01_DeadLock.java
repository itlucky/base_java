package com.itlucky.thread.threadLock;

/**
 * 死锁：
 *      多个线程互相抱着对方需要的资源，然后形成僵持
 */
public class Thread_01_DeadLock {
    public static void main(String[] args) {
        MakeUp g1 = new MakeUp(0,"一个女孩");
        MakeUp g2 = new MakeUp(1,"白雪公主");

        g1.start();
        g2.start();
    }
}

//口红
class Lipstick{

}

//镜子
class Mirrior{

}

//化妆：  需要口红、镜子
class MakeUp extends Thread{

    //需要的资源只有一份，这里用static来保证只有一份
    static Lipstick lipstick = new Lipstick();
    static Mirrior mirrior = new Mirrior();
    //选择
    int choice;
    //使用化妆品的人
    String girlName;

    MakeUp(int choice,String girlName){
        this.choice = choice;
        this.girlName = girlName;
    }


    @Override
    public void run() {
        try {
            this.makeup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void makeup()
        throws InterruptedException {
        if(choice ==0){
            /*这里synchronized锁住了两个资源，解决办法就是将里面的synchronized代码块放到外面即可。else里面的同理*/
            synchronized (lipstick){
                System.out.println(this.girlName + "获得口红的锁");
                Thread.sleep(1000);
                //一秒后想获得镜子
                synchronized (mirrior){
                    System.out.println(this.girlName+"获得镜子的锁");
                }
            }
        }else {
            synchronized (mirrior){
                System.out.println(this.girlName + "获得镜子的锁");
                Thread.sleep(1000);
                //一秒后想获得口红
                synchronized (lipstick){
                    System.out.println(this.girlName + "获得口红的锁");
                }
            }
        }
    }

}
