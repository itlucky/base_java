package com.itlucky.base;

/**
 * 递归的思想
 *
 *
 */
public class DiGui {

    public static void main(String[] args)
        throws Exception {
        DiGui dg = new DiGui();
        System.out.println(dg.f(-2));

    }

    /**
     * 求n!  n的阶层
     *  1!=1
     *  2!=2*1
     *  3!=3*2*1
     *  4!=4*3*2*1
     */
    public int f (int n)
        throws Exception {
        if(n<0){
           throw new Exception("参数不能为负！");
        }else if(n==1 || n==0) {
            return 1;
        }else {
            // 递归
            return n*f(n-1);
        }
    }
}
