package com.itlucky.jvm.stack;

/**
 * 栈  先入后出，后入先出
 */
public class JvmStack {

    public static void main(String[] args) {

        new JvmStack().a();  // java.lang.StackOverflowError  循环调用，会导致无限压栈，最终栈溢出。
    }

    public void a (){
        b();
    }

    public void b() {
        a();
    }

}
