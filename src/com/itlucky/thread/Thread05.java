package com.itlucky.thread;

// 模拟龟兔赛跑
public class Thread05 implements Runnable{

    // 胜利者
    private static String winner;

    @Override
    public void run() {
        for (int i = 0; i<=100; i++) {
            // 模拟兔子跑20步就睡眠2ms
            if(Thread.currentThread().getName().equals("小白兔") && i%20==0){
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //判断比赛是否结束
            if(gameOver(i)){
                break;
            }

            System.out.println(Thread.currentThread().getName() +"--》跑了"+i+"步！");
        }
    }

    private boolean gameOver(int steps){
        if(winner != null){
            return true;
        } else {
            if(steps >= 100){
                winner = Thread.currentThread().getName();
                System.out.println("winner is:" + winner);
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Thread05可以看成一个赛道，共享这条赛道
        Thread05 t5 = new Thread05();

        new Thread(t5,"乌龟").start();
        new Thread(t5,"小白兔").start();
    }

}
