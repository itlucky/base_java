package com.itlucky.test;

import java.util.HashMap;
import java.util.Map;


public class Test01 {
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("cusName","123");
        System.out.println(map.get("123"));
    }
}
