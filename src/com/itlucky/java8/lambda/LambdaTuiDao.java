package com.itlucky.java8.lambda;

// Lambda表达式推导练习【可删掉重写，不会再参照LambdaTest00.java】
public class LambdaTuiDao {



    public static void main(String[] args) {

        class IRun implements Run {

            @Override
            public void test(String a, int b) {
                System.out.println(a +"今天跑了"+b+"步");
            }
        }

        Run iRun = new IRun();
        iRun.test("局部内部类",5000);

        iRun = new Run() {
            @Override
            public void test(String a, int b) {
                System.out.println(a +"今天跑了"+b+"步");
            }
        };
        iRun.test("匿名内部类",6666);

        // 由匿名内部类演化成Lambda表达式
        iRun = (a,b)-> System.out.println(a +"今天跑了"+b+"步");
        iRun.test("Lambda表达式",7777);

        // 如果方法体不止一行内容，需要用花括号进行括起来。
        iRun = (a,b)->{
            System.out.println("我是:"+a);
            System.out.println("我的数字是:"+b);
        };
        iRun.test("简化的Lambda",8888);

    }

}

//函数式接口是Lambda表达式的前提
interface Run{
    void test(String a,int b);
}

