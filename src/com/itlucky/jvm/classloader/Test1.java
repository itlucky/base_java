package com.itlucky.jvm.classloader;

import com.itlucky.jvm.entity.Car;


/**
 *
 */
public class Test1 {

    public static void main(String[] args) {

        // 类是模板，理解为抽象概念。对象是具体的，是对类的实例化
        Car car1 = new Car();
        Car car2 = new Car();
        Car car3 = new Car();

        // 打印的结果不一样，说明从一个类模板中实例化出了不同的对象
        System.out.println(car1.hashCode());
        System.out.println(car2.hashCode());
        System.out.println(car3.hashCode());

        Class<? extends Car> aClass1 = car1.getClass();
        Class<? extends Car> aClass2 = car2.getClass();
        Class<? extends Car> aClass3 = car3.getClass();

        // 打印结果一样，说明不同对象的类模板都是同一个
        System.out.println(aClass1.hashCode());
        System.out.println(aClass2.hashCode());
        System.out.println(aClass3.hashCode());

        // 查看类加载器
        ClassLoader classLoader = aClass1.getClassLoader();
        System.out.println(classLoader); // AppClassLoader
        System.out.println(classLoader.getParent()); //ExtClassLoader
        System.out.println(classLoader.getParent().getParent()); //null (不存在 或者 java程序获取不到[BootStrap]) rt.jar

    }
}
