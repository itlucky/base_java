package com.itlucky.java8.lambda;

import org.junit.Test;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 *  演变过程：
 *  垃圾代码 --> 策略模式 --> 匿名内部类 --> Lambda表达式
 *
 *  基础语法：
 * - 操作符：->
 * - 左侧：参数列表
 * - 右侧：执行代码块 / Lambda 体
 *
 *
 */
public class LambdaTest {

    @Test
    public void test1(){
        //匿名内部类
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1,o2);
            }

            public boolean equals(Object obj){
                return false;
            }
        };
        //调用
        TreeSet<Integer> set = new TreeSet<>(comparator);
    }


    @Test
    public void test2(){
        //Lambda表达式
        Comparator<Integer> comparator = (x,y) -> Integer.compare(x,y);

        Comparator<Integer> comparator1 = Integer::compare;

        TreeSet ts = new TreeSet(comparator);
    }

    /**
     * 语法1：无参数、无返回值： ()->sout
     */
    int num = 10;  //jdk1.7以前必须用final修饰，8以上省去，默认有final修饰。

    @Test
    public void test3(){
        new Runnable(){
            @Override
            public void run() {
                //在局部类中引用同级局部变量
                //只读
                System.out.println("Hello World!" + num);
            }
        }.run();
    }

    @Test
    public void _test3(){
        Runnable runnable = () -> System.out.println("Hello World!");
        runnable.run();
    }

    /**
     * 语法2：a:有一个参数无返回值
     *
     *       b:一个参数小括号可以省略不写。
     */
    @Test
    public void test4(){
        Consumer<String> consumer = (a) -> System.out.println(a);
        consumer.accept("Hello ~");
    }

    @Test
    public void _test4(){
        Consumer<String> consumer = a -> System.out.println(a);
        consumer.accept("Hello !");

        Consumer<String> cm = System.out::println;
        cm.accept("3333");
    }

    /**
     * 语法3： 有两个及以上的参数，有返回值，并且Lambda体中有多条语句
     *        Lambda体用中括号括起来。
     */
    @Test
    public void test5(){
        Comparator<Integer> comparator = (a,b) -> {
            System.out.println("比较接口！");
            return a.compareTo(b);
        };

       int res = comparator.compare(3,19);
        System.out.println(res);
    }

    /**
     * 语法4： 有两个及以上的参数，有返回值，并且 Lambda 体中只有1条语句 （大括号 与 return 都可以省略不写）
     */
    @Test
    public void _test5(){
        Comparator<Integer> comparator = (x,y) -> x.compareTo(y);

        Comparator<Integer> comparator1 = Integer::compareTo;

        int res = comparator.compare(99,25);
        System.out.println(res);
    }

    /**
     * 总结：
     * Lambda 表达式 参数的数据类型可以省略不写 Jvm可以自动进行 “类型推断”
     *
     */


}
