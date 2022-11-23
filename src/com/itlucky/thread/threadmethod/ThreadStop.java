package com.itlucky.thread.threadmethod;

//线程停止stop
// 1.建议线程正常停止---->利用次数，不建议死循环
// 2.建议使用标志位----> 设置一个标志位
// 3.不要使用stop或者destroy等过时的或者JDK不建议使用的方法
public class ThreadStop implements Runnable{

    // 1.设置一个标志位
    private boolean flag = true;

    @Override
    public void run() {
        int j = 0;
        while (flag){
            System.out.println("run.....Thread"+ j++);
        }
    }

    //2.设置一个公开的方法停止线程，转换标志位
    public void stopThread(){
        this.flag = false;
    }

    public static void main(String[] args) {
        ThreadStop ts = new ThreadStop();
        Thread thread = new Thread(ts, "子线程AAA");
        thread.start();

        for (int i = 0; i<1000; i++) {
            System.out.println("main" + i);
            if(i==980){
                //调用停止线程的公开方法切换标志位，让线程停止
                ts.stopThread();
                System.out.println(thread.getName()+"该停止了！");
            }
        }
    }
}
