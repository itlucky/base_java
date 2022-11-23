package com.itlucky.java8.interface_;

public interface MyInterface {

    default String getName(){
        return "myInterface_getName";
    }

    public static void show(){
        System.out.println("接口中的静态方法。。");
    }
}
