package com.itlucky.thread.threadmethod;

/**
 * 线程状态
 */
public class ThreadStatus {

    public static void main(String[] args)
        throws InterruptedException {

        Thread thread = new Thread(()->{
            for (int i = 0; i<6; i++) {
                try {
                    Thread.sleep(500); //线程等待0.5s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("%%%%%%%%%%%%%%%~");
        });
        //线程启动前的状态
        Thread.State state = thread.getState();
        System.out.println(state); //NEW
        //线程启动
        thread.start();
        state = thread.getState();
        System.out.println(state); //RUNNABLE

        //只要线程不终止，就一直输出线程的状态
        while (state != Thread.State.TERMINATED){
            //TIMED_WAITING : 线程等待状态
            Thread.sleep(1000);
            state = thread.getState();
            System.out.println(state);
        }
    }

}
