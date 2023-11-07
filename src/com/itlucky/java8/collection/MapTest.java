package com.itlucky.java8.collection;

import org.junit.Test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class MapTest {


    @Test
    public void test(){
        //LinkedHashMap 可以保证有序输出
        //HashMap是无序输出的
        Map<String,String> map = new LinkedHashMap<>();

        map.put("custName","李四");
        map.put("money","123.3");

//        String str = map.values().toString();
        String mapStr = map.toString();

//        Map<String,String> map1 = MapUtils
        System.out.println(map.isEmpty());

        System.out.println();

        Hashtable hb = new Hashtable();

        HashMap hm = new HashMap();

        ConcurrentHashMap chm = new ConcurrentHashMap();
    }


}
