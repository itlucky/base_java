package com.itlucky.java8.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author itlucky
 * @date 2023/6/17 21:56
 */
public class ListTest {

    public static void main(String[] args) {

        List<Student> list = new ArrayList<>();
        list.add(new Student("张三", "N0001", new BigDecimal("22222")));
        list.add(new Student("张三", "N0001", new BigDecimal("11111")));
        list.add(new Student("张三", "N0001", new BigDecimal("22222")));
        list.add(new Student("李四", "N0012", new BigDecimal("33333")));
        list.add(new Student("王五", "N0043", new BigDecimal("22222")));
        list.add(new Student("赵六", "N0024", new BigDecimal("55555")));
        list.add(new Student("梅西", "N0035", new BigDecimal("99999")));
        list.add(new Student("梅西", "N0035", new BigDecimal("99999")));
        list.add(new Student("梅西", "N0035", new BigDecimal("99999")));
        list.add(new Student("梅西", "N0035", new BigDecimal("99999")));
        list.add(new Student("梅西", "N0035", new BigDecimal("99999")));
        list.add(new Student("梅西", "N0035", new BigDecimal("99999")));
        list.add(new Student("梅西", "N0035", new BigDecimal("99999")));
        list.add(new Student("梅西", "N0035", new BigDecimal("99999")));
        list.add(new Student("梅西", "N0235", new BigDecimal("99999")));
        list.add(new Student("梅西", "N0135", new BigDecimal("99999")));
        list.add(new Student("梅西", "N0335", new BigDecimal("99999")));
        list.add(new Student("梅西1", "N0211", new BigDecimal("99999")));
        list.add(new Student("梅西2", "N0212", new BigDecimal("777777")));
        list.add(new Student("梅西3", "N0213", new BigDecimal("777777")));
        list.add(new Student("梅西4", "N0214", new BigDecimal("777777")));
        list.add(new Student("梅西5", "N0215", new BigDecimal("777777")));
        list.add(new Student("梅西6", "N0216", new BigDecimal("777777")));
        list.add(new Student("梅西7", "N0217", new BigDecimal("777777")));
        list.add(new Student("梅西8", "N0218", new BigDecimal("777777")));
        list.add(new Student("梅西9", "N0219", new BigDecimal("777777")));
        list.add(new Student("梅西10", "N0220", new BigDecimal("777777")));
        list.add(new Student("梅西11", "N0221", new BigDecimal("777777")));
        list.add(new Student("梅西12", "N0222", new BigDecimal("777777")));
        list.add(new Student("拜登", "N0026", new BigDecimal("44444")));
        list.add(new Student("拜登1", "N0226", new BigDecimal("44444")));
        list.add(new Student("拜登2", "N0236", new BigDecimal("44444")));
        list.add(new Student("拜登3", "N0246", new BigDecimal("44444")));
        list.add(new Student("特朗普", "N0017", new BigDecimal("22222")));
        list.add(new Student("泽连斯基", "N0028", new BigDecimal("88888")));
        list.add(new Student("秦始皇", "N0019", new BigDecimal("66666")));
        list.add(new Student("秦始皇1", "N0019", new BigDecimal("66666")));
        list.add(new Student("秦始皇2", "N0020", new BigDecimal("66666")));
        list.add(new Student("秦始皇2", "N0020", new BigDecimal("66666")));
        list.add(new Student("秦始皇2", "N0020", new BigDecimal("66666")));
        list.add(new Student("秦始皇2", "N0020", new BigDecimal("66666")));
        list.add(new Student("秦始皇2", "N0020", new BigDecimal("66666")));
        list.add(new Student("秦始皇2", "N0020", new BigDecimal("66666")));
        list.add(new Student("秦始皇2", "N0020", new BigDecimal("66666")));
        list.add(new Student("秦始皇2", "N0020", new BigDecimal("66666")));
        list.add(new Student("秦始皇3", "N0021", new BigDecimal("66666")));
        list.add(new Student("秦始皇4", "N0022", new BigDecimal("66666")));
        list.add(new Student("秦始皇5", "N0026", new BigDecimal("66666")));
        list.add(new Student("秦始皇6", "N0027", new BigDecimal("66666")));
        list.add(new Student("秦始皇7", "N0031", new BigDecimal("66666")));
        list.add(new Student("秦始皇8", "N0019", new BigDecimal("66666")));
        list.add(new Student("秦始皇9", "N0111", new BigDecimal("66666")));
        list.add(new Student("秦始皇10", "N0112", new BigDecimal("66666")));
        list.add(new Student("秦始皇10", "N0112", new BigDecimal("66666")));
        list.add(new Student("秦始皇10", "N0112", new BigDecimal("66666")));
        list.add(new Student("秦始皇10", "N0112", new BigDecimal("66666")));
        list.add(new Student("秦始皇10", "N0112", new BigDecimal("66666")));
        list.add(new Student("秦始皇10", "N0112", new BigDecimal("66666")));
        list.add(new Student("秦始皇10", "N0112", new BigDecimal("66666")));
        list.add(new Student("秦始皇10", "N0112", new BigDecimal("66666")));
        list.add(new Student("秦始皇10", "N0112", new BigDecimal("66666")));
        list.add(new Student("赵高", "N00010", new BigDecimal("77777")));

        test1(list);
        System.out.println("==============================================================================================");
        test2(list);
        System.out.println("==============================================================================================");
        test3(list);

    }

    // 按照金额从高到底展示，如果赎回金额一致，按照代码从小到大排序

    /**
     * 方式1
     *
     * @param list
     */
    private static void test1(List<Student> list) {

        Collections.sort(list, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if (o2.getMoney().compareTo(o1.getMoney()) > 0) {
                    return 1;
                } else if (o2.getMoney().compareTo(o1.getMoney()) == 0) {
                    if (o2.getCode().compareTo(o1.getCode()) > 0) {
                        return -1;
                    } else if (o2.getCode().compareTo(o1.getCode()) == 0) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return -1;
                }
            }
        });

        list.forEach(System.out::println);
    }

    /**
     * 方式2
     * @param list
     */
    public static void test2(List<Student> list) {
        list.sort((s1, s2) -> {
            if(s1.getMoney().compareTo(s2.getMoney()) == 0){
                return s1.getCode().compareTo(s2.getCode());
            }else {
                return -s1.getMoney().compareTo(s2.getMoney());
            }
        });
        list.forEach(System.out::println);
    }

    public static void test3(List<Student> list){
        list.sort((o1, o2) -> {
            if (o2.getMoney().compareTo(o1.getMoney()) > 0) {
                return 1;
            } else if (o2.getMoney().compareTo(o1.getMoney()) == 0) {
                if (o2.getCode().compareTo(o1.getCode()) > 0) {
                    return -1;
                } else if (o2.getCode().compareTo(o1.getCode()) == 0) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return -1;
            }
        });

        list.forEach(System.out::println);
    }

}
