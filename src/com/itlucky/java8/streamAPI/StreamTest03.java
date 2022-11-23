package com.itlucky.java8.streamAPI;

import com.itlucky.entity.CustInfo;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Stream终止操作：
 * <p>
 * 二、归约/ 收集
 * 归约：reduce(T identity, BinaryOperator) / reduce(BinaryOperator) 可以将流中的数据反复结合起来，得到一个值
 * 收集：collect 将流转换成其他形式；接收一个 Collector 接口的实现，用于给流中元素做汇总的方法
 */
public class StreamTest03 {

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

    /**
     *  reduce --归约
     *
     *  T reduce(T identity, BinaryOperator<T> accumulator); --有起始值的，返回T，因为不会存在为空的情况
     *  Optional<T> reduce(BinaryOperator<T> accumulator);  --无起始值，返回Optional<T>，因为有可能返回NULL的情况
     *
     */
    @Test
    public void test() {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9);
        //反复的结合，如下的逻辑是：先把起始值0作为x,然后把1作为y；二者相加之后的值作为x,然后把2作为y；相加之后结果作为x,把3作为y,依次类推的一个结果。
        Integer sum = list.stream()
            .reduce(0,(x,y) -> x+y);
        System.out.println(sum);

        System.out.println("-----------------------");
        //工资求和
        Optional<BigDecimal> b = custInfos.stream()
                 .map(CustInfo::getMoney)
                 .reduce(BigDecimal::add);
        System.out.println(b.get());
    }

    /**
     * 收集：collect 将流转换成其他形式；接收一个 Collector 接口的实现，用于给流中元素做汇总的方法
     */
    @Test
    public void test2(){

       List<String> list =  custInfos.stream()
                 .map(CustInfo::getName)
                 .collect(Collectors.toList());
       list.forEach(System.out::println);

        System.out.println("-----------------");

        //收集集合中所有的名字并去重
        Set<String> set = custInfos.stream()
                 .map(CustInfo::getName)
                 .collect(Collectors.toSet());
        set.forEach(System.out::println);

        System.out.println("------------------");

        HashSet<String> hs = custInfos.stream()
                 .map(CustInfo::getName)
                 .collect(Collectors.toCollection(HashSet::new));
        hs.forEach(System.out::println);
    }

    @Test
    public void test3(){
        //总数
//        long count = custInfos.size();
        long count = custInfos.stream()
                              .collect(Collectors.counting());
        System.out.println(count);

        //平均年龄
        Double avg = custInfos.stream()
                 .collect(Collectors.averagingInt(CustInfo::getAge));
        System.out.println(avg);

        //总和
        Integer in = custInfos.stream()
                 .collect(Collectors.summingInt(CustInfo::getId));
        System.out.println(in);

        //最大值
//        Optional<CustInfo> op = custInfos.stream().max(Comparator.comparing(CustInfo::getMoney));
        Optional<CustInfo> op = custInfos.stream()
                                         .collect(Collectors.maxBy((c1,c2) -> c1.getMoney().compareTo(c2.getMoney())));
        System.out.println("工资最高的员工信息：" + op.get());

        //最小值
        Optional<Integer> age = custInfos.stream()
                 .map(CustInfo::getAge)
                 .collect(Collectors.minBy(Integer::compare));
        System.out.println("最小年龄是：" + age.get());
    }

    //分组
    @Test
    public void test4(){
        //按照员工状态分组
        Map<CustInfo.Status,List<CustInfo>> map = custInfos.stream()
                 .collect(Collectors.groupingBy(CustInfo::getStatus));
        System.out.println(map);

    }

    //多级分组
    @Test
    public void test5(){
        //先按员工状态分组，然后按照年龄段分组
        Map<CustInfo.Status,Map<String,List<CustInfo>>> map = custInfos.stream()
                 .collect(Collectors.groupingBy(CustInfo::getStatus,Collectors.groupingBy(e -> {
                     if(e.getAge()<=25){
                         return "青年";
                     }else if(e.getAge()<=50){
                         return "中年";
                     }else {
                         return "老年";
                     }
                 })));
        System.out.println(map);
    }

    //分区
    @Test
    public void test6(){
        //按照工资是否超过5000进行分区
        Map<Boolean,List<CustInfo>> map = custInfos.stream()
                 .collect(Collectors.partitioningBy(e -> e.getMoney().compareTo(BigDecimal.valueOf(5000)) > 0));
        System.out.println(map);
    }

    //多种方式实现
    @Test
    public void test7(){
        IntSummaryStatistics dss = custInfos.stream()
                 .collect(Collectors.summarizingInt(CustInfo::getAge));
        //年龄最大值
        System.out.println(dss.getMax());
        //年龄平均值
        System.out.println(dss.getAverage());
        System.out.println(dss.getMin());
        System.out.println(dss.getCount());
    }

    //连接
    @Test
    public void test8(){
        String str = custInfos.stream()
                 .map(CustInfo::getName)
                //字符串连接，可以指定连接符，或者首尾字符
                 .collect(Collectors.joining("|","==","99"));
        System.out.println(str);
    }


}
