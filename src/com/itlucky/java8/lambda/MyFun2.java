package com.itlucky.java8.lambda;

@FunctionalInterface
public interface MyFun2<T,R> {

    public R getValue(T t1, T t2);

}
