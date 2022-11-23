package com.itlucky.functioninterf;

import java.util.function.Consumer;


/**
 * 消费型接口：
 * 只有入参，没有出参
 */
public class ConsumerTest {
    public static void main(String[] args) {

//        Consumer<String> consumer = new Consumer<String>() {
//            @Override
//            public void accept(String str) {
//                System.out.println(str + " 啦啦啦啦啦~");
//            }
//        };
        //lambda表达式
        Consumer<String> consumer = a ->{
            System.out.println(a + " 噜啦噜啦嘞");
        };
        consumer.accept("牛逼class");
    }
}
