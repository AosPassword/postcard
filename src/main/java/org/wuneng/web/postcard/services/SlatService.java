package org.wuneng.web.postcard.services;

public interface SlatService {
    String getSalt();

    String getSecurePassword(String password, String salt);
}
