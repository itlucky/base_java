package com.itlucky.java8.lambda;

import org.junit.Test;

/**
 * 需求：
 * 声明函数式接口，接口中声明抽象方法，String getValue(String str);
 * 声明类 TestLambda，类中编写方法使用接口作为参数，将一个字符串转换成大写，并作为方法的返回值;
 * 再将一个字符串的第二个和第四个索引位置进行截取字串.
 */
public class LambdaTest3 {

    //用于字符串的处理
    public String strHandler(String str,StrFun sf){
        return sf.getValue(str);
    }

    @Test
    public void test(){
        String s = strHandler("  as123.  ", a -> a.trim());
        String sa = strHandler("ddd  v  ", String::trim);
        System.out.println(s);

        String s1 = strHandler(" yrwed", String::toUpperCase);
        System.out.println(s1);

        String s2 = strHandler("中华人民共和国", x -> x.substring(0,4));
        System.out.println(s2);
    }

}
