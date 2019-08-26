package org.wuneng.web.postcard.utils;

public class MathUtil {
    public static String create_random_number(int length){
        String s = new String();
        for (int i=0;i<length;i++){
            int digit = (int) (Math.random()*10);
            s += digit;
        }
        return s;
    }
}
