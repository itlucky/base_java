package com.itlucky.thread;

import java.util.concurrent.*;


/**
 *  创建线程的方式3：实现callable接口，需要返回值类型；
 *  重写call()方法,需要抛出异常；
 *  创建目标对象；
 *  然后利用线程池，分4步处理。
 */
public class Thread06 implements Callable<Boolean> {

    //网络图片地址
    private String url;
    //保存的文件名
    private String name;

    public Thread06(String url,String name) {
        this.url = url;
        this.name = name;
    }

    // 下载图片 线程的执行体
    @Override
    public Boolean call() throws Exception{
        WebDownLoader wdl = new WebDownLoader();
        wdl.downloader(url,name);
        System.out.println("下载了文件名为："+name);

        return true;
    }

    // 这里每次执行结果的打印的顺序都不一样，可见是多线程执行的
    public static void main(String[] args)
        throws ExecutionException, InterruptedException {

        Thread06 t1 = new Thread06("https://img1.baidu.com/it/u=573497149,567826026&fm=253&fmt=auto&app=138&f=JPEG?w=935&h=500","B站LOGO111.jpg");
        Thread06 t2 = new Thread06("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F180a319f682220a5fe8c6cdd2408da51efb0223e.jpg&refer=http%3A%2F%2Fi0.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1649420725&t=8d86c8efecee9ac849396161e9767411","B站LOGO222.jpg");
        Thread06 t3 = new Thread06("https://pics2.baidu.com/feed/3b292df5e0fe9925c447f659956688d98cb17141.jpeg?token=d456ba53944c62449d7d7929d7732a9e","B站LOGO333.jpg");

        //step1:创建执行服务
        ExecutorService es = Executors.newFixedThreadPool(3);
        //step2:提交执行
        Future<Boolean> s1 = es.submit(t1);
        Future<Boolean> s2 = es.submit(t2);
        Future<Boolean> s3 = es.submit(t3);
        //step3:获取结果
        boolean b1 = s1.get();
        boolean b2 = s2.get();
        boolean b3 = s3.get();
        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);
        //step4:关闭服务
        es.shutdown();
    }
}
