package com.itlucky.juc.jucutilclass;

import javax.print.attribute.HashAttributeSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


/**
 * CountDownLatch:计数器
 * 减法计数器
 */
public class CountDownLatchTest2 {


    public static void main(String[] args)
            throws InterruptedException {

        // 这里的参数可以根据实际配置调整
        ExecutorService executorService = new ThreadPoolExecutor(4,
                8,
                1L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        // 获取：要发给基金公司开户的基金公司个数
        String[] ta = {"64","01","02","03","04","05","06"};

        Map<String,String> resMap = new HashMap<>();
        // 初始化计数器个数
        CountDownLatch latch = new CountDownLatch(ta.length);

        for (int i = 0; i < ta.length; i++) {

            executorService.submit(new SendTAOpen(latch, ta[i], resMap));
        }
        // 等所有基金公司完成了开户申请
        latch.await();

        // TODO : 开始开户之后的功能

        System.out.println("do:签约其他步骤");

        System.out.println("===签约 Complete!===");
    }


    static class SendTAOpen implements Runnable {

        private final CountDownLatch latch;

        private final String taNo;

        Map<String,String> resMap;

        SendTAOpen(CountDownLatch latch, String taNo,Map<String,String> resMap) {
            this.latch = latch;
            this.taNo = taNo;
            this.resMap = resMap;
        }

        @Override
        public void run() {
            try {
                // TODO :业务功能实现,发送TA开户，保存开户成功数据等
                System.out.println("发送基金公司_" + taNo + "开户");
                //模拟开户发生异常的情况
                System.out.println(1 / 0);


            } catch (Exception e) {
//                log.error("execute action error, e:{}",e.getMessage());
                System.out.println("execute action error, e: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }
    }
}


