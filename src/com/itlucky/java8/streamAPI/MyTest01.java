package com.itlucky.java8.streamAPI;

import com.itlucky.entity.CustInfo;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MyTest01 {

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
        new CustInfo(1001, "332110", "XXX", 59, "高新区", BigDecimal.valueOf(8877.91), CustInfo.Status.BUSY)
    );

    @Test
    public void test1(){
        Map<String,List<CustInfo>> maps = custInfos.stream()
                                                   .collect(Collectors.groupingBy(CustInfo::getAddress));
        System.out.println(maps);
    }

    /**
     * 获取年龄小于30的姓名
     */
    @Test
    public void test2(){
      List<String> na =  custInfos.stream().filter(c->c.getAge()<30).map(CustInfo::getName).collect(Collectors.toList());
        System.out.println(na);
    }

    @Test
    public void test3(){
        List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        List<Integer> nums2 = nums.stream().map(c -> c*2).collect(Collectors.toList());
        List<Integer> num3 = nums.stream().filter(c->c%2==0).skip(1).collect(Collectors.toList());
        System.out.println(nums2+"\n"+num3);

        String concat = Stream.of("A", "B", "C", "D").reduce("-1", String::concat);
        System.out.println(concat);

        int sumValue = Stream.of(1, 2, 3, 4).reduce(-2, Integer::sum);
        System.out.println(sumValue);
    }

    @Test
    public void test4(){
        List<Integer> list = Arrays.asList(7, 6, 9, 3, 8, 2, 1);
//        list.forEach(System.out::print);
        list.stream().filter(x-> x>3).forEach(System.out::print);

        Optional<Integer> first = list.stream().filter(a->a<0).findAny();
        System.out.println(first.isPresent());

    }

    @Test
    public void test5(){
        CustInfo custInfo = custInfos.stream().filter(c->c.getAge()>18).findFirst().orElse(new CustInfo());
        System.out.println(custInfo);
    }

    @Test
    public void test6(){
        
    }

}
