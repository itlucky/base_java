package com.itlucky.juc.timer;

import java.util.*;
import java.util.concurrent.*;

public class MyTimerTask extends TimerTask {

    @Override
    public void run() {

        // 这里的参数可以根据实际配置调整
        ExecutorService executorService = new ThreadPoolExecutor(4,
                8,
                1L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        try {

            // 模拟：根据查询结果，将预约发起失败的数据，经过筛选封装成待调用4806交易的入参集合
            List<Map<String,Map<String,String>>> inputList = new ArrayList<>();

            Map<String,Map<String,String>> map1 = new HashMap<>();
            Map<String,String> _map1 = new HashMap<>();
            _map1.put("AccountNo","6211111111");
            _map1.put("ID","11");
            _map1.put("tablename","tblTimerIPOSubsBook");
            _map1.put("TransTime","093000");
            _map1.put("PassWdType","0");
            map1.put("INPUT_MAP",_map1);
            inputList.add(map1);

            Map<String,Map<String,String>> map2 = new HashMap<>();
            Map<String,String> _map2 = new HashMap<>();
            _map2.put("AccountNo","62222222");
            _map2.put("ID","22");
            _map2.put("tablename","tblTimerIPOSubsBook");
            _map2.put("TransTime","093000");
            _map2.put("PassWdType","0");
            map2.put("INPUT_MAP",_map2);
            inputList.add(map2);

            if(inputList.size() > 0){
                // 初始化计数器个数
                CountDownLatch latch = new CountDownLatch(inputList.size());

                for (int i = 0; i < inputList.size(); i++) {

                    executorService.submit(new Call4806(latch,inputList.get(i)));
                }
                // 全部发起完成后
                latch.await();
                //TODO 删除发起记录表中成功的记录
                System.out.println("删除发起记录表中成功的记录" +"==="+Thread.currentThread().getName());
                //TODO 二次发起结束，触发定投优惠券不可用提醒任务
                System.out.println("二次发起结束，触发定投优惠券不可用提醒任务" +"==="+Thread.currentThread().getName());

            }else {
                System.out.println("没有需要二次发起的数据处理。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    class Call4806 implements Runnable{

        private final CountDownLatch latch;

        private Map inputMap;

        Call4806(CountDownLatch latch,Map inputMap) {
            this.latch = latch;
            this.inputMap = inputMap;
        }

        @Override
        public void run() {
            try {
                // TODO :业务功能实现，根据入参调用4806交易
                // 注意：这里是使用的ofbiz框架拉起的4806服务
                //Map result = dispacther.runSync("LaunchBook4806",inputMap);
                System.out.println("===根据入参调用4806交易===" + Thread.currentThread().getName());
                System.out.println(inputMap +"====="+ Thread.currentThread().getName());
                // TODO 根据4806交易处理结果进行业务处理
                System.out.println("===根据4806交易处理结果进行业务处理==="+ Thread.currentThread().getName());

            } catch (Exception e) {
                System.out.println("execute action error, e: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }
    }
}
