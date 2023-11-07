package com.itlucky.juc.singleton;

/**
 * 单例模式：懒汉式
 */
public class Single02LazyMan {

    // 构造器私有
    private Single02LazyMan() {
        System.out.println(Thread.currentThread().getName()+"实例化了Single02LazyMan");
    }

    // 静态对象变量，先不赋值，等需要的时候再实例化
    // 加上关键字volatile，避免指令重排，因为lazyMan = new Single02LazyMan();不是一个原子性操作。
    // 实例化[1:为对象分配内存空间,2:初始化对象,3:将内存空间的地址赋值给对应的引用]
    // 如果2、3发生了重排序就会导致第二个判断会出错，singleton != null，但是它其实仅仅只是一个地址而已，此时对象还没有被初始化，所以return的singleton对象是一个没有被初始化的对象
    // 所以需要加上volatile禁止指令重排，确保安全。
    private volatile static Single02LazyMan lazyMan;

    public static Single02LazyMan getInstance() {
        // 双重检测锁模式的懒汉式单例  叫做：DCL懒汉式
        if (lazyMan==null) {  //1 这一步判断是避免synchronized开销
            synchronized (Single02LazyMan.class) { //2 synchronized加锁，防止并发带来问题
                if (lazyMan==null) { //3 再次判断非空给实例化
                    lazyMan = new Single02LazyMan();
                }
            }
        }
        return lazyMan;
    }

    /**
     * 在单线程的情况下，如上的单例是没有问题的，但是并发下就不能用了。
     * 举例多线程并发：结果可能会有多个线程都能实例化Single02LazyMan的对象
     *               因为实例化过程不是原子操作，
     *               但由于jvm编译器的优化产生的重排序缘故，步骤2、3可能会发生重排序：
     */
    public static void main(String[] args) {
        for (int i = 1; i<=20; i++) {
            new Thread(() -> {
                Single02LazyMan.getInstance();
            }).start();
        }
    }
}
