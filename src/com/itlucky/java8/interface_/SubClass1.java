package com.itlucky.java8.interface_;

/**
 * 子类同时实现两个接口，两个接口中都有相同入参的同名方法。
 *
 *  必须方法覆盖解决该冲突
 */
public class SubClass1 implements MyFun, MyInterface{

    @Override
    public String getName() {
        return MyInterface.super.getName();
    }
}
