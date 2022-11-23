package com.itlucky.java8.lambda;

/**
 * Lambda表达式推导：
 *
 */
public class LambdaTest00 {

    //2.静态内部类
//    static class Ilove implements Love{
//        public void test(){
//            System.out.println("I love 666");
//        }
//    }

    public static void main(String[] args) {

        //3.局部内部类
        class Ilove implements Love{
            public void test(){
                System.out.println("I love 666");
            }
        }

        Love ilove = new Ilove();
        ilove.test();



        // 4.匿名内部类[没有类的名称，借助父类或接口实现]
        ilove = new Love() {
            @Override
            public void test() {
                System.out.println("i love 匿名内部类");
            }
        };

        ilove.test();

        // 5.lambda表达式
        ilove = ()->{
            System.out.println("i love lambda");
        };

        ilove.test();

    }


}

//接口
interface Love{
    public void test();
}


//1.接口实现类
//class Ilove implements Love{
//    public void test(){
//        System.out.println("I love 666");
//    }
//}

