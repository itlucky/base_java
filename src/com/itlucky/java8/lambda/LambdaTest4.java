package com.itlucky.java8.lambda;

import org.junit.Test;

/**
 *
 *
 *
 *
 *  将Lambda表达式像参数一样进行传递。
 */
public class LambdaTest4 {

    //需求：对两个Long型数据进行处理
    public void op(Long l1,Long l2,MyFun2<Long,Long> mf){
        System.out.println(mf.getValue(l1,l2));
    }

    @Test
    public void test(){
        op(20l,50l, (a,b) -> a+b);

        op(1l,3l,Long::sum);

        op(40l,30l,(x,y) -> x*y);
    }



}
