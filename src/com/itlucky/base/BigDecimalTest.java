package com.itlucky.base;

import java.math.BigDecimal;

/**
 *  BigDecimal类型数值大小比较
 */
public class BigDecimalTest {
    public static void main(String[] args) {
        BigDecimal x = new BigDecimal("0.01");
        BigDecimal y = new BigDecimal("0.01");
        int res = y.compareTo(x);
        System.out.println(res);

    }
}
