package com.itlucky.java8.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class MyTest {

    @Test
    public void test(){
//        List<String> arrayList = new ArrayList<>();
//
//
//        List<String> linkList = new LinkedList<>();
        System.out.println(1 << 4);
    }

    public static void remove(ArrayList<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.equals("bb")) {
                it.remove();
            }
        }
    }

}
