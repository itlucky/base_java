package com.itlucky.jvm.heap;

import com.itlucky.jvm.entity.Car;

import java.util.Random;


/**
 * -Xms 设置初始化内存分配大小 默认：1/64
 * -Xmx 设置最大分配内存 默认1/4
 * -XX:+PrintGCDetails 打印GC详细信息
 * -XX:+HeapDumpOnOutOfMemoryError   生成OOM错误的dump
 *
 *
 *
 *
 * -Xms1m -Xmx1m -XX:+PrintGCDetails   打印GC堆信息
 *
 * java.lang.OutOfMemoryError: Java heap space
 *
 * Heap
 *  PSYoungGen      total 1024K, used 512K [0x00000000ffe80000, 0x0000000100000000, 0x0000000100000000)
 *   eden space 512K, 100% used [0x00000000ffe80000,0x00000000fff00000,0x00000000fff00000)
 *   from space 512K, 0% used [0x00000000fff80000,0x00000000fff80000,0x0000000100000000)
 *   to   space 512K, 0% used [0x00000000fff00000,0x00000000fff00000,0x00000000fff80000)
 *  ParOldGen       total 512K, used 481K [0x00000000ffe00000, 0x00000000ffe80000, 0x00000000ffe80000)
 *   object space 512K, 94% used [0x00000000ffe00000,0x00000000ffe78600,0x00000000ffe80000)
 *  Metaspace       used 3778K, capacity 4536K, committed 4864K, reserved 1056768K
 *   class space    used 414K, capacity 428K, committed 512K, reserved 1048576K
 *
 *
 * -Xms1m -Xmx1m -XX:+HeapDumpOnOutOfMemoryError 生成OOM堆dump文件,
 * 同样的，这里可以指定其他异常或错误dump文件生成。比如 -XX:HeapDumpOnClassNotFound 类未找到异常dump。
 *
 *
 *
 *
 */
public class JvmHeap {

    public static void main(String[] args) {

        String str = "dsddsfrtreg";

        while (true){
            str += new Random().nextInt(999999999) + new Random().nextInt(999999999);
        }

    }
}
