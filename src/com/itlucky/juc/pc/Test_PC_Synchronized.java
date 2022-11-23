package com.itlucky.juc.pc;

/**
 * 线程之间通信--生产者、消费者问题.  等待唤醒，通知唤醒
 *
 * 线程交替执行，A  B 操作同一变量。
 *
 * num = 0
 *
 * A: num+1
 * B: num-1
 */
public class Test_PC_Synchronized {

    public static void main(String[] args) {
        Data data = new Data();
        
        new Thread(()->{
            for (int i = 0; i<30; i++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();

        new Thread(()->{
            for (int i = 0; i<30; i++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();

        new Thread(()->{
            for (int i = 0; i<30; i++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"C").start();

        new Thread(()->{
            for (int i = 0; i<30; i++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"D").start();
    }
}


/**
 * 资源类口诀：判断等待，业务处理，通知
 */
class Data{ //要操作的资源类:{资源类一般只有属性和方法}

    private int num = 0;

    //+1操作
    public synchronized void increment()
        throws InterruptedException {

        //注意：这里不能用if判断！！！会出现虚假唤醒问题，官方文档也指出需要用while循环解决该问题
        //因为if判断只会判断一次。while是每次都执行。
        while (num !=0){
            //等待
            this.wait();
        }
        //
        num ++;
        System.out.println(Thread.currentThread().getName()+"---->"+num);
        //通知其他线程，我+1完毕
        notifyAll();

    }

    //-1操作
    public synchronized void decrement()
        throws InterruptedException {
        while (num ==0){
            //等待
            this.wait();
        }
        num --;
        System.out.println(Thread.currentThread().getName()+"++++>"+num);
        //通知其他线程，我-1完毕了
        this.notifyAll();
    }

}