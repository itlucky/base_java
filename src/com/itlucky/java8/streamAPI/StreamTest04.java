package com.itlucky.java8.streamAPI;

import com.itlucky.entity.CustInfo;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Stream API 练习
 */
public class StreamTest04 {

    List<CustInfo> custInfos = Arrays.asList(
            new CustInfo(1001, "332101", "张三", 18, "高新区", BigDecimal.valueOf(9999.91), CustInfo.Status.BUSY),
            new CustInfo(1002, "332102", "李四", 16, "蜀山区", BigDecimal.valueOf(8888.01), CustInfo.Status.FREE),
            new CustInfo(1003, "332103", "王五", 17, "瑶海区", BigDecimal.valueOf(10888.01), CustInfo.Status.VOCATION),
            new CustInfo(1004, "332104", "赵六", 19, "经开区", BigDecimal.valueOf(7888.01), CustInfo.Status.FREE),
            new CustInfo(1005, "332105", "李七", 28, "新站区", BigDecimal.valueOf(6666.61), CustInfo.Status.BUSY),
            new CustInfo(1006, "332106", "刘八", 39, "包河区", BigDecimal.valueOf(5555.55), CustInfo.Status.BUSY),
            new CustInfo(1007, "332107", "王明", 45, "庐阳区", BigDecimal.valueOf(4444.333), CustInfo.Status.BUSY),
            new CustInfo(1008, "332108", "小红", 56, "巢湖区", BigDecimal.valueOf(2222.333), CustInfo.Status.FREE),
            new CustInfo(1001, "332109", "信息F", 17, "高新区", BigDecimal.valueOf(7177.91), CustInfo.Status.BUSY),
            new CustInfo(1001, "332110", "XXX", 39, "高新区", BigDecimal.valueOf(8877.91), CustInfo.Status.BUSY),
            new CustInfo(1001, "332110", "XXX", 39, "高新区", BigDecimal.valueOf(8877.91), CustInfo.Status.BUSY)
    );

    /**
     * 1. 给定一个数字列表，如何返回一个由每个数的平方构成的列表呢？(如：给定【1，2，3，4，5】，返回【1，4，9，16，25】)
     */

    @Test
    public void test1 () {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6);
        Stream<Integer> sm = list.stream()
            .map(x -> x*x);
        sm.forEach(System.out::println);
    }


    /**
     * 2.怎样使用 map 和 reduce 数一数流中有多少个 CustInfo 呢？
     */
    @Test
    public void test2(){
        Optional<Integer> count = custInfos.stream()
                 .map( a -> 1)
                 .reduce(Integer::sum);
        System.out.println(count.get());
    }

}
