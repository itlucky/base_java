package com.itlucky.juc.forkjoin;

import java.util.concurrent.RecursiveTask;


public class ForkJoinDemo extends RecursiveTask<Long>{

    private Long start;

    private Long end;

    public ForkJoinDemo(Long start, Long end) {
        this.start = start;
        this.end = end;
    }
    // 临界值
    private Long tmp = 10000L;

    @Override
    protected Long compute() {
        if((end - start) < tmp){
            long sum = 0L;
            for(long i=start;i<end;i++){
                sum += i;
            }
            return sum;
        }else { // 本质就是递归处理
            long middle = (start+end)/2;
            ForkJoinDemo task1 = new ForkJoinDemo(start,middle);
            task1.join(); //拆分任务，把任务task1压入线程队列
            ForkJoinDemo task2 = new ForkJoinDemo(middle+1,end);
            task2.join();// 拆分任务，把任务task2压入线程队列
            return task1.compute() + task2.compute();
        }
    }
}