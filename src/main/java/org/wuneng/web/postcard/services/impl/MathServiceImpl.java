package org.wuneng.web.postcard.services.impl;

import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.services.MathService;

@Service
public class MathServiceImpl implements MathService {

    @Override
    public String create_random_number(int length){
        String s = new String();
        for (int i=0;i<length;i++){
            int digit = (int) (Math.random()*10);
            s += digit;
        }
        return s;
    }
}
