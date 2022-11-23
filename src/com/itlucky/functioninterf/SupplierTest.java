package com.itlucky.functioninterf;

import java.util.function.Supplier;


/**
 * 供给型接口:没有参数，只有返回值
 */
public class SupplierTest {
    public static void main(String[] args) {

//        Supplier<String> supplier = new Supplier<String>() {
//            @Override
//            public String get() {
//                return "Hello Supplier!";
//            }
//        };

        Supplier<String> supplier = () -> "Hello Supplier!";
        System.out.println(supplier.get());
    }
}
