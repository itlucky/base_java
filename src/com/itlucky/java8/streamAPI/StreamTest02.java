package com.itlucky.java8.streamAPI;

import com.itlucky.entity.CustInfo;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Stream终止操作：
 * <p>
 * 一、查找与匹配
 * allMatch：检查是否匹配所有元素
 * anyMatch：检查是否至少匹配一个元素
 * noneMatch：检查是否没有匹配所有元素
 * findFirst：返回第一个元素
 * findAny：返回当前流中的任意元素
 * count：返回流中元素的总个数
 * max：返回流中最大值
 * min：返回流中最小值
 */
public class StreamTest02 {

    List<CustInfo> custInfos = Arrays.asList(
            new CustInfo(1001, "332101", "张三", 18, "高新区", BigDecimal.valueOf(9999.91), CustInfo.Status.BUSY),
            new CustInfo(1002, "332102", "李四", 16, "蜀山区", BigDecimal.valueOf(8888.01), CustInfo.Status.FREE),
            new CustInfo(1003, "332103", "王五", 17, "瑶海区", BigDecimal.valueOf(10888.01), CustInfo.Status.VOCATION),
            new CustInfo(1004, "332104", "赵六", 19, "经开区", BigDecimal.valueOf(7888.01), CustInfo.Status.FREE),
            new CustInfo(1005, "332105", "李七", 18, "新站区", BigDecimal.valueOf(6666.61), CustInfo.Status.BUSY),
            new CustInfo(1006, "332106", "刘八", 19, "包河区", BigDecimal.valueOf(5555.55), CustInfo.Status.BUSY),
            new CustInfo(1007, "332107", "王明", 15, "庐阳区", BigDecimal.valueOf(4444.333), CustInfo.Status.BUSY),
            new CustInfo(1008, "332108", "小红", 16, "巢湖区", BigDecimal.valueOf(2222.333), CustInfo.Status.FREE),
            new CustInfo(1001, "332109", "信息F", 17, "高新区", BigDecimal.valueOf(7177.91), CustInfo.Status.BUSY),
            new CustInfo(1001, "332110", "XXX", 29, "高新区", BigDecimal.valueOf(8877.91), CustInfo.Status.BUSY)
    );

    @Test
    public void test1() {
        //是否所有员工都处于BUSY状态
        boolean b1 = custInfos.stream()
                .allMatch(e -> e.getStatus().equals(CustInfo.Status.BUSY));
        System.out.println(b1);

        boolean b2 = custInfos.stream()
                .anyMatch(a -> a.getStatus().equals(CustInfo.Status.VOCATION));
        System.out.println(b2);

        boolean b3 = custInfos.stream()
                .noneMatch(a -> a.getStatus().equals(CustInfo.Status.VOCATION));
        System.out.println(b3);

        Optional<CustInfo> custInfo = custInfos.stream()
                .filter(c -> "高新区".equals(c.getAddress()))
                .sorted((c1, c2) -> -c1.getMoney().compareTo(c2.getMoney()))
                .findFirst();
        System.out.println("高新区工资最高的对象：" + custInfo.get());

        Optional<CustInfo> custInfo1 = custInfos.parallelStream()
                .filter(e -> e.getAge() > 16)
                .findAny();
        System.out.println("年龄大于16的任意一个对象:" + custInfo1.get());

        long count = custInfos.stream()
                .filter(e -> e.getAddress().equals("瑶海区"))
                .count();
        System.out.println("住址在瑶海区的总人数：" + count);

        Optional<Integer> age = custInfos.stream()
                .map(CustInfo::getAge)
                .max(Integer::compareTo);
        System.out.println("年龄最大的:" + age.orElse(-1));
    }

    @Test
    public void test2() {
        Optional<CustInfo> custInfo = custInfos.stream()
                .max(Comparator.comparing(CustInfo::getMoney));
        System.out.println("工资最高的人：" + custInfo.get());

        Optional<Integer> age = custInfos.stream()
                 .map(CustInfo::getAge)
                 .min(Integer::compare);
        System.out.println("最小年龄是: " + age.get());

    }

}
