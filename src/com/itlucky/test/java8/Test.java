package com.itlucky.test.java8;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class Test {
    public static void main(String[] args) {

        String workdate = "20220325";
//        System.out.println("测试-》" + CompareDate(convertStringToDate_other("20220329"),convertStringToDate_other("20220328")));
        System.out.println(convertStringToDate_other("20220203"));
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("id",111);
        map1.put("name","你好111");
        map1.put("date","20220326");
        list.add(map1);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("id",222);
        map2.put("name","你好222");
        map2.put("date","20220327");
        list.add(map1);
        Map<String,Object> map3 = new HashMap<>();
        map3.put("id",333);
        map3.put("name","你好333");
        map3.put("date","20220327");
        list.add(map1);
        Map<String,Object> map4 = new HashMap<>();
        map4.put("id",444);
        map4.put("name","你好444");
        list.add(map4);

//       List<Map<String,Object>> aa =  list.stream()
//            .filter(a->a.get("date")!=null)
//            .filter(b->CompareDate(convertStringToDate_other((String)b.get("date")),convertStringToDate_other(workdate))>0)
//            .collect(Collectors.toList());
//        List<Map<String,Object>> aa =  list.stream()
//            .filter(a->a.entrySet().stream().filter(data->data.getKey().equals("date")).collect())
//            .filter(b->CompareDate(convertStringToDate_other((String)b.get("date")),convertStringToDate_other(workdate))>0)
//            .collect(Collectors.toList());
//
//        System.out.println(aa);

    }

    public static int CompareDate(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return 1;
        } else if (date1.getTime() == date2.getTime()) {
            return 0;
        } else {
            return -1;
        }
    }

    public static Date convertStringToDate_other(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 返回的日期
        Date resultDate;
        try {
            resultDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("数据转换异常");
        }
        return resultDate;
    }
}
