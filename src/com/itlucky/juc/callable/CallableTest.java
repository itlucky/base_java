package com.itlucky.juc.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class CallableTest {

    public static void main(String[] args)
        throws ExecutionException, InterruptedException {

        //这里结合源码理解Callable接口实现类的调用
        //正常启动线程的方式是 new Thread(new Runnable()).start();
        //因为这里是Callable接口，所以要找到Callable和Runnable的关系
        //结合API可以看到Runnable接口的实现类中有个FutureTask，FutureTask有个构造方法FutureTask(Callable<V> callable)入参就是Callable接口。
        //所以这里可以通过FutureTask把Callable和Runnable关联起来。
        //因此new Thread(new FutureTask<V>>).start(); 等价于 new Thread(new Runnable()).start();
        //所以可以通过new Thread(new FutureTask<V>(Callable)).start();启动Callable
        //题外：跟踪源码，FutureTask实现RunnableFuture，而RunnableFuture是一个子接口，继承了Runnable和 Future接口

        MyThread myThread = new MyThread();

        FutureTask futureTask = new FutureTask(myThread);

        new Thread(futureTask,"A").start();
        new Thread(futureTask,"B").start(); //这里再开启一个线程，发现打印的结果call只执行一次，因为有缓存

        //获得返回值,通过get()方法。这里的get方法可能产生阻塞(如果call方法非常耗时)，一般把它放在最后或者采用异步通信来处理。
        System.out.println(futureTask.get());
    }
}

class MyThread implements Callable<String> {

    //方法返回值的类型要跟实现Callable接口中定义的泛型一致。
    @Override
    public String call() {
        System.out.println("call()方法执行~");
        return "123";
    }
}