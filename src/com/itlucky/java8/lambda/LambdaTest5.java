package com.itlucky.java8.lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Java8内置的 4大核心函数式接口
 *
 * 1、Consumer<T> :消费型接口
 *      void accept(T t);
 *
 * 2、Supplier<T> : 供给型接口
 *      T get();
 *
 * 3、Function(T,R) : 函数型接口
 *      R apply(T t);
 *
 * 4、Predicate(T) : 断言型接口  (用于做一些判断操作)
 *      boolean test(T t);
 *
 *  java8 基于以上核心接口，还做了好多扩展子接口，同样可以使用Lambda表达式来使用，用法类似,可以满足实际业务需求。
 */
public class LambdaTest5 {

    //1、Consumer<T> :消费型接口
    @Test
    public void testConsumer(){
        happy(1000, m -> System.out.println("本次购物花销：" + m + "元" ));
    }
            //需求:购物花钱
    public void happy(double money, Consumer<Double> consumer){

        consumer.accept(money);
    }

    //2、Supplier<T> : 供给型接口
    @Test
    public void testSupp(){
        List<Integer> numList = genMumList(10,() -> (int)(Math.random() * 100));

        for (int num : numList){
            System.out.println(num);
        }
    }
            //需求：随机生成指定个数的整数
    public List<Integer> genMumList(Integer num, Supplier<Integer> supplier){
        List<Integer> list = new ArrayList<>();
        for(int i=0; i< num; i++){
            int n = supplier.get();
            list.add(n);
        }
        return list;
    }

    //3、Function(T,R) : 函数型接口
    @Test
    public void testFun(){
        //字符串截取
        String str1 = strHandler("jjloveuui",x -> x.substring(2,6));
        System.out.println(str1);

        //字母转大写
        String str2 = strHandler("datg好的dd", String :: toUpperCase);
        System.out.println(str2);

        //字符串长度统计
        int len = strHandler2("rtx hi   ", String :: length);
        System.out.println(len);

    }

            //字符串处理
    public String strHandler(String str, Function<String,String> function){
        return function.apply(str);
    }
            //字符串处理2
    public Integer strHandler2(String str, Function<String,Integer> f){
        return f.apply(str);
    }

    //4、Predicate(T) : 断言型接口
    @Test
    public void testPre(){
        List<String> list = Arrays.asList("yedfg","iiiii","ks","usa","china");
        //保留字符串长度大于3的
        List<String> strs = filterStr(list, a -> a.length() > 3);
        for (String str : strs){
            System.out.println(str);
        }
    }

        //需求：筛选字符串
    public List<String> filterStr(List<String> list, Predicate<String> pre){
        List<String> resList = new ArrayList<>();

        for(String str : list){
            if(pre.test(str)){
                resList.add(str);
            }
        }
        return resList;
    }

}
