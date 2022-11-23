package com.itlucky.java8.lambda.task;

import com.itlucky.entity.CustInfo;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Lambda练习
 */
public class LambdaTask {

    List<CustInfo> custInfos = Arrays.asList(
            new CustInfo(1001,"332101","张三",18,"高新区", BigDecimal.valueOf(9999.91)),
            new CustInfo(1002,"332102","李四",16,"蜀山区", BigDecimal.valueOf(8888.01)),
            new CustInfo(1003,"332103","王五",17,"瑶海区", BigDecimal.valueOf(10888.01)),
            new CustInfo(1004,"332104","赵六",19,"经开区", BigDecimal.valueOf(7888.01)),
            new CustInfo(1005,"332105","李七",18,"新站区", BigDecimal.valueOf(6666.61)),
            new CustInfo(1006,"332106","刘八",19,"包河区", BigDecimal.valueOf(5555.55)),
            new CustInfo(1007,"332107","王明",15,"庐阳区", BigDecimal.valueOf(4444.333)),
            new CustInfo(1008,"332108","小红",16,"巢湖区", BigDecimal.valueOf(2222.333))
    );

    //先按照年龄比，年龄相同按照姓名比
    @Test
    public void test1() {
        Stream<CustInfo> custInfoStream = custInfos.stream()
                 .sorted((x,y) -> {
                     if(x.getAge() == y.getAge()){
                        return x.getName().compareTo(y.getName());
                     }else {
                         return Integer.compare(x.getAge(),y.getAge());
                     }
                 });
        custInfoStream.forEach(System.out::println);
    }


}
