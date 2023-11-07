package com.itlucky.java8.lambda;

import org.junit.Test;

/**
 * 函数式接口：接口中只有一个抽象方法的接口 @FunctionalIterface
 */
public class LambdaTest1 {

    /**
     * 用自定义函数式接口
     */
    @Test
    public void test1(){
        MyFun myFun1 = (a,b) -> a + b;
        MyFun myFun2 = (x,y) -> x - y;
        MyFun myFun3 = (a,b) -> a / b;
        MyFun myFun4 = (a,b) -> a * b;
        System.out.println(myFun1.count(1,3));
        System.out.println(myFun2.count(1,3));
        System.out.println(myFun3.count(1,3));
        System.out.println(myFun4.count(1,3));
    }

    public Integer operation(Integer a, Integer b,MyFun myFun){
        return myFun.count(a,b);
    }

    @Test
    public void test2(){
        Integer res = operation(1,2 ,(x,y) -> x*y);
        System.out.println(res);
    }








}
