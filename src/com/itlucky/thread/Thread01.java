package com.itlucky.thread;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * 创建线程方式1：继承Thread类,
 *
 * 实现功能：实现多线程下载网络图片
 *
 * 1.自定义线程类继承Thread类
 * 2.重写run()方法，编写线程执行体
 * 3.创建线程对象，调用start()方法启动线程
 *
 */
public class Thread01 extends Thread {
    //网络图片地址
    private String url;
    //保存的文件名
    private String name;

    public Thread01(String url,String name) {
        this.url = url;
        this.name = name;
    }

    // 下载图片 线程的执行体
    @Override
    public void run() {
        WebDownLoader wdl = new WebDownLoader();
        wdl.downloader(url,name);
        System.out.println("下载了文件名为："+name);
    }

    // 这里每次执行结果的打印的顺序都不一样，可见是多线程执行的
    public static void main(String[] args) {

        Thread01 t1 = new Thread01("https://img1.baidu.com/it/u=573497149,567826026&fm=253&fmt=auto&app=138&f=JPEG?w=935&h=500","B站LOGO111.jpg");
        Thread01 t2 = new Thread01("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F180a319f682220a5fe8c6cdd2408da51efb0223e.jpg&refer=http%3A%2F%2Fi0.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1649420725&t=8d86c8efecee9ac849396161e9767411","B站LOGO222.jpg");
        Thread01 t3 = new Thread01("https://pics2.baidu.com/feed/3b292df5e0fe9925c447f659956688d98cb17141.jpeg?token=d456ba53944c62449d7d7929d7732a9e","B站LOGO333.jpg");

        // 启动线程
        t1.start();
        t2.start();
        t3.start();
    }
}


class WebDownLoader{

    // 下载方法
    public void downloader(String url,String fileName){
        try {
            FileUtils.copyURLToFile(new URL(url),new File(fileName));

        } catch (IOException e) {
            System.out.println("IO异常，downloader方法出现问题！");
            e.printStackTrace();
        }
    }

}