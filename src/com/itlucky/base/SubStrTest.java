package com.itlucky.base;

import org.junit.Test;


public class SubStrTest {

    @Test
    public void test1() {

        String treeNo0 = "37101";
        String treeNo1 = "37101_37651";
        String treeNo2 = "37101_37651_37660";
        String treeNo3 = "37101_37651_37660_37661";

        String str0 = treeNo1.substring(0, 5);
        String str1 = treeNo1.substring(6, 11);
        String str2 = treeNo2.substring(6, 11);
        String str3 = treeNo3.substring(12, 17);

        System.out.println(treeNo2.indexOf("_"));
        System.out.println(treeNo2.contains("_"));
        System.out.println(treeNo1.split("_").length);
        System.out.println(treeNo3.lastIndexOf("_"));
        System.out.println(treeNo3.concat("_"));

        System.out.println(str0+"\n"+str1+"\n"+str2+"\n"+str3);

        System.out.println(treeNo0.split("_").length);
    }

    @Test
    public void testIndexOf(){
        System.out.println(":2123:124".indexOf(":"));
    }
}
