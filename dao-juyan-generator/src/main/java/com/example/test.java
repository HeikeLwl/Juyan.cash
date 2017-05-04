package com.example;

/**
 * Created by Administrator102 on 2016/12/29.
 */

public class test {
    public static void main(String[] args) throws Exception {
        long ts = System.currentTimeMillis();
        String sign = MD5Util.getMD5String("100013"+"100013"+ts+"jfelevenwujf654123js");
        System.out.print(ts+"\n"+sign);
    }
}
