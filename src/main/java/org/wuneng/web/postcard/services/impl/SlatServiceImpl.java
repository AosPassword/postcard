package org.wuneng.web.postcard.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.services.SlatService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

@Service
public class SlatServiceImpl implements SlatService {

    @Value("${salt.config.algorithm}")
    private String algorithm;
    @Value("${salt.config.provider}")
    private String provider;
    @Value("${salt.config.encode}")
    private String encode;
    @Value("${salt.config.error_slat}")
    private String error_slat;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getSalt(){
        SecureRandom sr = null;
        byte[] salt = new byte[20];

        try {
            sr = SecureRandom.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.toString());
        } catch (NoSuchProviderException e) {
            logger.error(e.toString());
        }
        if (sr!=null) {
            sr.nextBytes(salt);
            return salt.toString().substring(3);
        }
        return error_slat;
    }

    @Override
    public String getSecurePassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance(encode);
            //Add password bytes to digest
            salt += "[B@";
            md.update(salt.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
