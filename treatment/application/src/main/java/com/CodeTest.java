package com;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @ClassName CodeTest
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/27
 * @Version V1.0
 **/
public abstract class CodeTest {

    static class Ds {
        String id;
        String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }




    public static void main(String[] args) {

        String a = "4";
        String b = "54656";
        System.out.println(xorString(a,b));








    }


    private static Boolean xorString(String s1, String s2){
       return  !( s1 == null?"":s1).equals(s2 == null?"":s2) ;
    }



}
