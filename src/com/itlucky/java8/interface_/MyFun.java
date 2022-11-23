package com.itlucky.java8.interface_;


/**
 *  jdk8以前，接口中只能有全局静态常量和静态方法。
 *  以后：还可以有默认的方法，用default修饰
 */
public interface MyFun {

    default String getName() {
        return "myFun_getName";
    }
}
