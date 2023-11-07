package com.itlucky.java8.streamAPI;

import java.util.concurrent.RecursiveTask;

/**
 * 模拟Fork/Join框架的实现
 * <p>
 * 需求：实现计算功能，按照10000为单位拆分计算然后合并结果
 */
public class ForkJoinCalc extends RecursiveTask<Long> {

    private static final long serialVersionUID = 77777777777777L;

    private final long start;
    private final long end;

    private static final long THRESHPLD = 1000;

    public ForkJoinCalc(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long length = end - start;

        if (length < THRESHPLD) {
            long sum = 0;
            for (long i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            long middle = (start + end) / 2;
            ForkJoinCalc calcTask1 = new ForkJoinCalc(start, middle);
            calcTask1.fork();  //拆分子任务，压入线程队列

            ForkJoinCalc calcTask2 = new ForkJoinCalc(middle + 1, end);
            calcTask2.fork();

            return calcTask1.join() + calcTask2.join();

        }

        return null;
    }
}
