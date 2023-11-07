package com.itlucky.base;

import com.itlucky.entity.CustInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;


public class Reflection {

    public static void main(String[] args)
        throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException,
        NoSuchFieldException {

        Class aClass = Class.forName("com.itlucky.entity.CustInfo");
        System.out.println(aClass.getName());

        Constructor[] constructors = aClass.getConstructors();
        Arrays.stream(constructors).forEach(System.out::println);

        Constructor[] declaredConstructors = aClass.getDeclaredConstructors();
        Arrays.stream(declaredConstructors).forEach(a-> System.out.println("##"+a));

        Method[] methods = aClass.getMethods();
        Arrays.stream(methods).forEach(System.out::println);

        Arrays.stream(aClass.getDeclaredMethods()).forEach(m -> System.out.println("$$$"+m));

        //反射构造一个对象
        CustInfo custInfo = (CustInfo)aClass.newInstance();
        System.out.println("反射实例化:"+custInfo);

        //通过构造器构造一个对象
        Constructor constructor = aClass.getConstructor(Integer.class, String.class);
        CustInfo custInfo2 = (CustInfo)constructor.newInstance(12, "中华小子");
        System.out.println("反射获取构造器实例化："+custInfo2);

        //通过反射调用普通方法
        // invoke:激活的意思  (对象,"方法的值")
        Method setName = aClass.getDeclaredMethod("setName", String.class);
        setName.invoke(custInfo,"普京");
        System.out.println(custInfo.getName());

        System.out.println("=================================");
        //通过反射操作属性
        CustInfo custInfo3 = (CustInfo)aClass.newInstance();
        Field name = aClass.getDeclaredField("name");
        //不能直接操作私有属性，需要关闭程序的安全检测。属性/方法都可以通过setAccessible(true)来关闭
        name.setAccessible(true);
        name.set(custInfo3,"俄罗斯-普京");
        System.out.println(custInfo3.getName());

    }


}
