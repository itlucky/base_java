package com.itlucky.java8.streamAPI;

import com.itlucky.entity.CustInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 一、什么是Stream?
 * 是数据渠道，用于操作数据源（集合、数组等）所生成的元素序列。
 *   ----集合讲的是数据，流讲的是计算！
 * 注意：①Stream不会自己存储元素；
 *      ②Stream不会改变源对象。相反，他们会返回一个持有结果的新Stream；
 *      ③Stream操作是延迟执行的。这意味着他们会等到需要结果的时候才执行。
 *
 * 二、Stream的操作三个步骤
 *    1.创建Stream
 *      一个数据源（如：集合、数组），获取一个流
 *    2.中间操作
 *      一个中间操作链，对数据源的数据进行处理
 *    3.终止操作（终端操作）
 *      一个终止操作，执行中间操作链，并产生结果
 */
public class StreamTest {

    /**
     * 创建流
     * 4种方式
     */
    @Test
    public void testCreate(){

        /**
         * 1.集合流
         *   -- Collection.stream() 串行流
         *   -- Collection.parallelStream() 并行流
         */
        List<String> list = new ArrayList<>();
        Stream stream1 = list.stream();
//        Stream sm = list.parallelStream();

        /**
         * 2.数组流
         * Arrays.stream(array)
         * 可以将任何类型的对象数组转换成对应泛型的流
         */
        CustInfo[] custInfos = new CustInfo[10];
        Stream<CustInfo> stream2 = Arrays.stream(custInfos);

        /**
         * 3.Stream 静态方法
         * Stream.of(....)
         */
        Stream stream3 = Stream.of(1,2,3);

        /**
         * 4.1无限流--迭代
         * 参数1-seed:种子，就是起始值
         * 参数2-UnaryOperator 一元运算继承函数式接口
         * 也就是按照一元运算的规律产生一个无限流
         */
        Stream<Integer> stream4 = Stream.iterate(0,(x) -> x+2);
        stream4.limit(10)
               .forEach(System.out::println);

        /**
         * 4.2无限流--生成
         * 通过generate调用供给型函数式接口产生无限流
         */
        Stream.generate(() -> Math.random())
              .limit(10)
              .forEach(System.out::println);

    }

}
