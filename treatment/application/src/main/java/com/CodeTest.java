package com;

import org.apache.commons.lang3.ArrayUtils;

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

    /*
    * 生成数组：长度L，不重复，最大值不大于M
    * */

 /*   private List<Integer> getArray(int len,int)*/


 private static Double random(double n){
     Random random = new Random(new Random().nextInt()*80);

     return random.nextDouble()*(n-0+1);
 }


    public static void main(String[] args) {

        List<Double> x = Arrays.asList(25.0,25.0,33.0,25.0,25.0,25.0,25.0,25.0,25.0,25.0);

        List<Double> y = new ArrayList<>();


        x.forEach(new Consumer<Double>() {
            @Override
            public void accept(Double x) {
                Double t =  (x*0.7+(0.3*random(10d)));
                y.add(new BigDecimal(String.valueOf(t)).setScale(0, BigDecimal.ROUND_DOWN).doubleValue());
            }
        });

        System.out.println("转换公式：y=Kx+(1-k)e   e=random(N) 随机生成0-N的整数");

        System.out.println("转换前的数据x=");
        x.forEach(d -> System.out.print(d+" "));
        System.out.println();
        System.out.println("转换后的数据y=");
        y.forEach(d -> System.out.print(d+" "));











    }



}
