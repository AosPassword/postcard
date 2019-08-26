package org.wuneng.web.postcard.services;

import com.aliyuncs.exceptions.ClientException;

public interface SMSService {

    String sendSMS(long phone_number, String number) throws ClientException;
}
