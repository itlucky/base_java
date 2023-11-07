package com.itlucky.java8.streamAPI;

import com.itlucky.entity.CustInfo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


/**
 * 筛选：
 * ID是偶数
 * 年龄大于16
 * 地址转大写
 * 薪资倒序
 * 输出一个用户
 */
public class MyTest {
    public static void main(String[] args) {
        List<CustInfo> custInfos = Arrays.asList(
            new CustInfo(1001,"332101","张三",18,"高新区aa", BigDecimal.valueOf(9999.91)),
            new CustInfo(1002,"332102","李四",16,"蜀山区bb", BigDecimal.valueOf(8888.01)),
            new CustInfo(1003,"332103","王五",17,"瑶海区cc", BigDecimal.valueOf(10888.01)),
            new CustInfo(1004,"332104","赵六",19,"经开区dd", BigDecimal.valueOf(7888.01)),
            new CustInfo(1005,"332105","李七",18,"新站区ww", BigDecimal.valueOf(6666.61)),
            new CustInfo(1006,"332106","刘八",19,"包河区ee", BigDecimal.valueOf(5555.55)),
            new CustInfo(1007,"332107","王明",15,"庐阳区ff", BigDecimal.valueOf(4444.333)),
            new CustInfo(1008,"332108","小红",16,"巢湖区gg", BigDecimal.valueOf(2222.333)),
            new CustInfo(1009,"332102","李四",16,"蜀山区hh", BigDecimal.valueOf(8888.01)),
            new CustInfo(1010,"332102","李四",16,"蜀山区jj", BigDecimal.valueOf(8888.01)),
            new CustInfo(1011,"332102","李四",16,"蜀山区kk", BigDecimal.valueOf(8888.01))
        );

        //
        custInfos.stream()
                // ID是偶数
                 .filter(ci->{return ci.getId()%2==0;})
                // 年龄大于16
                 .filter(ci->{return ci.getAge()>16;})
                // 地址转大写
                 .map(custInfo -> custInfo.getAddress().toUpperCase())
                // 薪资倒序
                .sorted((a,b)->{return b.compareTo(a);})

                .limit(1)
            .forEach(System.out::println);
    }
}
