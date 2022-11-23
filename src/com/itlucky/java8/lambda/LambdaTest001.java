package com.itlucky.java8.lambda;

/**
 * Lambda表达式推导2
 */
public class LambdaTest001 {

//    static class ILike implements Like{
//        @Override
//        public void ddd(String msg) {
//            System.out.println("i like : "+ msg);
//        }
//    }




    public static void main(String[] args) {

//        class ILike implements Like{
//            @Override
//            public void ddd(String msg) {
//                System.out.println("i like : "+ msg);
//            }
//        }
//
//
//        like.ddd("hhh");
//        Like like = new ILike();
//        like = new Like() {
//            @Override
//            public void ddd(String msg) {
//                System.out.println("[匿名内部类]i like " + msg);
//            }
//        };
//
//        like.ddd("jjj");
//
//        //lambda
//        like = (a) -> {
//            System.out.println("[Lambda] i like" + a);
//        };
//        like.ddd("QQQ");

        //简化lambda
        Like like= (a,b)-> {
            System.out.println("i like "+a);
            System.out.println("i like "+b + " too");
        };
        like.ddd("bbbb","cccc");

    }

}

interface Like{
    void ddd(String msg,String str);
}

//class ILike implements Like{
//
//    @Override
//    public void ddd(String msg) {
//        System.out.println("i like : "+ msg);
//    }
//}