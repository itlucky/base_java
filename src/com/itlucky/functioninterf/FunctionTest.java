package com.itlucky.functioninterf;

import java.util.function.Function;


/**
 * 函数式接口
 */
public class FunctionTest {

    public static void main(String[] args) {
        //传统写法
//        Function function = new Function<String,Integer>() {
//            @Override
//            public Integer apply(String str) {
//                return str.length();
//            }
//        };
        //lambda表达式写法
//        Function<String,Integer> function = s -> {return s.length();};
        //简化
        Function<String,Integer> function = String::length;

        System.out.println(function.apply("rrrewgg"));
    }
}
