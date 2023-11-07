package com.itlucky.jvm.heap;

import java.util.ArrayList;


public class HeapDumpTest {

    public static void main(String[] args) {
        ArrayList<HeapDumpTest> list = new ArrayList<>();
        int count = 0;

        try{
            while (true){
                list.add(new HeapDumpTest());
                count ++;
            }
        }catch (Error e){
            System.out.println("创建了实例次数 :" + count);
            e.printStackTrace();
        }
    }
}
