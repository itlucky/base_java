package com.itlucky.java8.lambda;

import com.itlucky.entity.CustInfo;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 需求：调用 Collections.sort() 方法，
 *      通过定制排序 比较两个 CustInfo (先按照年龄比，年龄相同按照薪资比)，
 *      使用 Lambda 表达式作为参数传递
 */
public class LambdaTest2 {

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

    /**
     * 按照年龄排序，年龄相同的情况下按照薪资排序
     */
    @Test
    public void test1(){
        custInfos.sort((c1, c2) -> {
            if (c1.getAge() == c2.getAge()) {
                return c1.getMoney().compareTo(c2.getMoney());
            } else {
                //前面加个负号，表示年龄倒序。去掉就是正常升序。
                return Integer.compare(c1.getAge(), c2.getAge());
            }
        });

        for(CustInfo ci : custInfos){
            System.out.println(ci);
        }
    }
}
