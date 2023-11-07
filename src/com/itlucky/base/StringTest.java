package com.itlucky.base;

/**
 * String、StringBuffer、StringBuilder
 * 性能比较：StringBuilder > StringBuffer > String
 */
public class StringTest {

    public static void main(String[] args) {

        String str = "12";

        StringBuffer sbf = new StringBuffer();
        sbf.append("aaa");

        StringBuilder sbd = new StringBuilder();
        sbd.append("cccc");

    }


}
