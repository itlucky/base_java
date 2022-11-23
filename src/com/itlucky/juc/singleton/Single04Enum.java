package com.itlucky.juc.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * 枚举实现单例：
 *
 * 默认枚举实例的创建是线程安全的，并且在任何情况下都是单例(天生保证序列化单例)，
 *      上述讲的几种单例模式实现中，有一种情况下他们会重新创建对象，那就是反序列化(反射)，
 *      将一个单例实例对象写到磁盘再读回来，从而获得了一个实例。
 *
 * enum本身也是一个class类。
 */

//举例：声明一个枚举，用于获取数据库连接。
public enum Single04Enum {

    DATASOURCE;

    private DBConnection connection = null;

    // 构造器私有
    private Single04Enum() {
        connection = new DBConnection();
    }

    public DBConnection getConnection() {
        return connection;
    }
}

class DBConnection {
}

class Main{
    public static void main(String[] args)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        DBConnection con1 = Single04Enum.DATASOURCE.getConnection();
//        DBConnection con2 = Single04Enum.DATASOURCE.getConnection();
//        System.out.println(con1 == con2); //结果为true，表明是同一个实例化对象

        Single04Enum enumInstance = Single04Enum.DATASOURCE;
        // 这里需要jad反编译class文件可以看到枚举类是有一个有参构造的。(String,int)
        Constructor<Single04Enum> enumConstructor = Single04Enum.class.getDeclaredConstructor(String.class,int.class);
        enumConstructor.setAccessible(true);
        Single04Enum enumInstance2 = enumConstructor.newInstance();
        // java.lang.NoSuchMethodException: com.itlucky.juc.singleton.Single04Enum.<init>()
        System.out.println(enumInstance);
        // java.lang.IllegalArgumentException: Cannot reflectively create enum objects
        // 出现上面的异常就说明了，反射不能破坏枚举的单例模式
        System.out.println(enumInstance2);

    }
}