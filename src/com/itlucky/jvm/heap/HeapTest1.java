package com.itlucky.jvm.heap;

/**
 * OOM:内存溢出，原因就是堆内存满了
 *
 * 解决方案：1.尝试扩大堆内存再看结果；2.分析内存，看一下哪个地方出现了问题{专业工具分析}。
 */
public class HeapTest1 {

    public static void main(String[] args) {

        // 默认情况下：给堆分配的总内存是电脑内存的 1/4，而初始化的内存是：1/64.
        // 返回虚拟机试图使用的最大内存
        long maxMemory = Runtime.getRuntime().maxMemory();
        // 返回JVM的总内存
        long totalMemory = Runtime.getRuntime().totalMemory();

        System.out.println("maxMemory=" + maxMemory + "字节");
        System.out.println("maxMemory=" + maxMemory/1024/1024 + "M");

        System.out.println("totalMemory=" + totalMemory + "字节");
        System.out.println("totalMemory=" + totalMemory/1024/1024 + "M");

        /**
         * 配置VM:  -XX:+PrintGCDetails
         * 输出结果:
         * maxMemory=3633315840字节
         * maxMemory=3465M
         * totalMemory=245366784字节
         * totalMemory=234M
         * Heap
         *  PSYoungGen      total 72704K, used 6246K [0x000000076ed00000, 0x0000000773e00000, 0x00000007c0000000)
         *   eden space 62464K, 10% used [0x000000076ed00000,0x000000076f319b60,0x0000000772a00000)
         *   from space 10240K, 0% used [0x0000000773400000,0x0000000773400000,0x0000000773e00000)
         *   to   space 10240K, 0% used [0x0000000772a00000,0x0000000772a00000,0x0000000773400000)
         *  ParOldGen       total 166912K, used 0K [0x00000006cc600000, 0x00000006d6900000, 0x000000076ed00000)
         *   object space 166912K, 0% used [0x00000006cc600000,0x00000006cc600000,0x00000006d6900000)
         *  Metaspace       used 3136K, capacity 4496K, committed 4864K, reserved 1056768K
         *   class space    used 342K, capacity 388K, committed 512K, reserved 1048576K
         *
         *   结果分析：
         *
         *   1.PSYoungGen(新生代)分为伊甸区(eden space)、存活区0(from space)、存活区1(to space)。
         *     而from和to这两个区是来回切换的。
         *
         *
         *   2.重点：72704K(PSYoungGen)+166912K(ParOldGen) = 234M
         *   新生代内存+老年代内存刚好等于总堆内存，说明元空间的内存大小是不计在堆中的。【堆内存：元空间是逻辑上存在，物理上不存在的】
         *
         */

    }


}
