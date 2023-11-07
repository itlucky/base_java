package com.itlucky.functioninterf;

import java.util.function.Predicate;


/**
 * 断定型接口：有一个输入参数，返回值只能是布尔值~
 */
public class PredicateTest {

    public static void main(String[] args) {

//        Predicate<String> predicate = new Predicate<String>() {
//            @Override
//            public boolean test(String str) {
//                return str.isEmpty();
//            }
//        };

        //Lambda表达式
//        Predicate<String> predicate = a -> {return a.isEmpty();};
        //简化写法
        Predicate<String> predicate = String::isEmpty;

        System.out.println(predicate.test("11111"));
    }
}
