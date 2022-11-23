package com.itlucky.base;

import com.itlucky.entity.CustInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


//分析性能问题
public class Reflection2 {
    // 普通方式调用
    public static void test01(){
        CustInfo ci = new CustInfo(111,"普京");
        long start = System.currentTimeMillis();
        for (int i=0;i<1000000000;i++){
            ci.getName();
        }
        long end = System.currentTimeMillis();
        System.out.println("普通方式执行10亿次getName()耗时："+(end-start)+"ms");
    }

    // 反射方式调用
    public static void test02()
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        CustInfo ci = new CustInfo();
        Class c = ci.getClass();
        Method getName = c.getDeclaredMethod("getName", null);

        long start = System.currentTimeMillis();
        for (int i=0;i<1000000000;i++){
            getName.invoke(ci,null);
        }
        long end = System.currentTimeMillis();
        System.out.println("反射方式执行10亿次getName()耗时："+(end-start)+"ms");
    }

    // 反射方式调用， 关闭检测
    public static void test03()
        throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CustInfo ci = new CustInfo();
        Class c = ci.getClass();
        Method getName = c.getDeclaredMethod("getName", null);
        // 关闭检测
        getName.setAccessible(true);
        long start = System.currentTimeMillis();
        for (int i=0;i<1000000000;i++){
            getName.invoke(ci,null);
        }
        long end = System.currentTimeMillis();
        System.out.println("反射方式关闭检测检测后执行10亿次getName()耗时："+(end-start)+"ms");
    }

    /**
     * 执行结果：
     * 普通方式执行10亿次getName()耗时：4ms
     * 反射方式执行10亿次getName()耗时：1333ms
     * 反射方式关闭检测检测后执行10亿次getName()耗时：1153ms
     *
     * 结论：
     *      1.反射相对来说比较耗性能；
     *      2.如果反射使用频率比较多的情况下，可以关闭检测setAccessible(true)，提高效率
     *
     */
    public static void main(String[] args)
        throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        test01();
        test02();
        test03();


    }

}
