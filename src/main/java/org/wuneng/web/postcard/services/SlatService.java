package org.wuneng.web.postcard.services;

public interface SlatService {
    byte[] getSalt();

    String getSecurePassword(String password, byte[] salt);
}
