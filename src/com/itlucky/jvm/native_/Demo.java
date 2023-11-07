package com.itlucky.jvm.native_;

/**
 * Java 是不能直接控制线程的，由底层C语言库。
 */
public class Demo {

    public static void main(String[] args) {

        /**
         * 在操作线程的时候，点击start方法到源码中查看，得知方法在执行的时候调用了start0()方法，
         * 而start0()方法由native修饰。
         */
        new Thread(()->{

        },"Thread J").start();
    }

    // private native void start0();
    // native：凡是带了native关键字的，说明java的作用范围达不到了，会去调用底层C语言的库。
    // java本地方法接口（JNI: java native interface）

    /**
     * 说明：
     * 1 进入本地方法栈
     * 2 调用本地方法本地接口 JNI
     * 3 JNI作用：扩展java的使用，融合不同的编程语言为java所用。 比如 C, C++等
     *   java在内存区域中专门开辟了一块标记区域【本地方法栈】，登记native方法，
     *   在最终执行的时候，通过JNI加载本地方法库中的方法。
     *
     * [一般编程中很少用到native，比如java程序驱动打印机等]
     *
     *
     * 现在调用其他语言的方式有很多，比如:Socket, Webservice, http等。
     *
     *
     *
     */
}
