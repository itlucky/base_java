package com.itlucky.java8.lambda;

import com.itlucky.entity.CustInfo;
import org.junit.Test;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.*;

/**
 * 一、方法引用：若Lambda 体中的内容有方法已经实现了，我们可以使用“方法引用”
 *          （可以理解为方法引用是 Lambda 表达式的另外一种表现形式）
 *
 *      主要有三种语法格式
 *
 *      对象::实例方法名
 *
 *      类::静态方法名
 *
 *      类::实例方法名
 *
 *      注意：
 *          ① Lambda体中调用方法的参数列表和返回值类型，要与函数式接口中抽象方法的参数列表和返回值类型保持一致。
 *          ② 若 Lambda参数列表中的第一个参数是实例方法的调用者，且第二个参数是实例方法的参数时，可以使用ClassName::method的方式
 *
 * 二、构造器引用：
 *     格式   ClassName::new
 *
 *  注意：需要调用的构造器的参数列表要与函数式接口中抽象方法的参数列表保持一致。
 *
 *
 * 三、数组引用：
 *    格式   Type[]::new
 *
 *
 */
public class LambdaTest6_MethodRef {

    //对象::实例方法名
    @Test
    public void test1(){

        PrintStream ps1 = System.out;
        Consumer<String> con = (x) -> ps1.println(x);
        con.accept("111");
        //方法引用
        PrintStream ps = System.out;
        Consumer<String> con1 = ps::println;
        con1.accept("222");
        //或者
        Consumer<String> con2 = System.out::println;
        con2.accept("333");
    }

    @Test
    public void test2(){
        CustInfo cs = new CustInfo();

        Supplier<String> sup = ()->cs.getName();
        String str = sup.get();
        System.out.println(str);
        //方法引用
        Supplier<Integer> sup2 = cs::getAge;
        Integer num = sup2.get();
        System.out.println(num);
    }

    //类::静态方法名
    @Test
    public void test3(){
        Comparator<Integer> com = (a,b) -> Integer.compare(a,b);
        System.out.println(com.compare(1,2));
        //方法引用
        Comparator<Integer> com2 = Integer::compare;
        System.out.println(com2.compare(5,3));

    }

    //类::实例方法名
    @Test
    public void test4(){
        //比较两个字符串是否相等
        BiPredicate<String,String> bp = (x,y) -> x.equals(y);
        System.out.println(bp.test("da加","dada"));
        //方法引用
        BiPredicate<String,String> bp2 = String::equals;
        System.out.println(bp2.test("你好","你好"));
        //正常调用实例方法是通过对象::实例方法，这里直接类::实例方法
        //注意：当第一个参数是方法的调用者，第二个参数是被调用方法的入参时，可以使用类::实例方法名实现。
    }

    //构造器引用
    @Test
    public void test5(){
        Supplier<CustInfo> sup = () -> new CustInfo();

        //构造器引用方式
        Supplier<CustInfo> sup2 = CustInfo::new;
        CustInfo cs = sup2.get();
        System.out.println(cs);
    }

    //构造器引用
    @Test
    public void test6(){
        Function<Integer,CustInfo> mf1 = (x) -> new CustInfo(x);
        //构造器引用:一个参数
        Function<Integer,CustInfo> mf = CustInfo::new;
        CustInfo cs = mf.apply(112);
        System.out.println(cs);
        //构造器引用:两个参数
        BiFunction<Integer,String,CustInfo> bf = CustInfo::new;
        CustInfo cs2 = bf.apply(666,"小明");
        System.out.println(cs2);

    }

    //数组引用
    @Test
    public void test7(){
        //返回一个指定长度的数组
        Function<Integer,String[]> mf = (x) -> new String[x];

        Function<Integer,String[]> mf2 = String[]::new;
        String[] strs = mf2.apply(10);
        System.out.println(strs.length);

        Function<Integer,Integer[]> mf3 = Integer[]::new;
    }

}
