package com;

/**
 * @ClassName CodeTest
 * @Description: TODO
 * @Author xizhonghuai
 * @Date 2020/4/27
 * @Version V1.0
 **/
public abstract class CodeTest {

    public static void main(String[] args) {

        StringBuilder stringBuffer = new StringBuilder();

        stringBuffer.append("<div class=\"progress\">");
        stringBuffer.append("<div class=\"progress-bar progress-bar-info\" role=\"progressbar\" aria-valuenow=\"60\"");
        stringBuffer.append(" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: " + 30 + "%;\">");
        // stringBuffer.append(" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: 40%;\">");
        stringBuffer.append("<span class=\"sr-only\">40% 完成</span> ");
        stringBuffer.append("</div>");
        stringBuffer.append(" </div>");
        stringBuffer.append("");
        stringBuffer.append("");

        System.out.println(stringBuffer.toString());


    }



}
