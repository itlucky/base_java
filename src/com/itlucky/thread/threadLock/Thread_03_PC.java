package com.itlucky.thread.threadLock;

/**
 *
 * 线程之间通信
 *
 * 测试：生产者消费者模型--->利用缓冲区解决【管程法】
 *
 *  生产者、消费者、产品、缓冲区
 */
public class Thread_03_PC {

    public static void main(String[] args) {

       SynContainer container = new SynContainer();

       new Producer(container).start();
       new Comsumer(container).start();
    }
}

//生产者
class Producer extends Thread{

    SynContainer container;

    public Producer(SynContainer container) {
        this.container = container;
    }

    //生产
    @Override
    public void run() {
        for (int i = 1; i<100; i++) {
            System.out.println("生产了第" +i + "只鸡");
            try {
                container.push(new Chichen(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

//消费者
class Comsumer extends Thread{

    SynContainer container;

    public Comsumer(SynContainer container) {
        this.container = container;
    }
    //消费
    @Override
    public void run() {
        for (int i = 0; i<100; i++) {
            try {
                System.out.println("消费了---->第" + container.pop().getId() +"只鸡");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

//产品
class Chichen{
    //产品编号
    private int id;

    public Chichen(int id) {
        this.id = id;
    }

    /**
     * 取得id的值
     *
     * @return id 的值
     */
    public int getId() {
        return id;
    }

    /**
     * 设定id的值
     *
     * @param id 设定值
     */
    public void setId(int id) {
        this.id = id;
    }
}

//缓冲区[同步]
class SynContainer{
    // 需要一个容器大小
    Chichen[] chichens = new Chichen[10];
    // 容器计数器
    int count = 0;

    //生产者放入产品
    public synchronized void push(Chichen chichen)
        throws InterruptedException {
        //如果容器满了就需要等待消费者来消费
        if(count == chichens.length){
            this.wait();
        }
        //如果没有满，就需要丢入产品
        chichens[count] = chichen;
        count ++;

        //然后通知消费者消费
        this.notifyAll();
    }

    //消费者消费产品
    public synchronized Chichen pop()
        throws InterruptedException {
        //判断能否消费
        if(count ==0){
            //消费者等待
            this.wait();
        }
        //否则消费
        count --;
        Chichen chichen = chichens[count];
        //消费完通知生产者生产
        this.notifyAll();
        return chichen;
    }
}