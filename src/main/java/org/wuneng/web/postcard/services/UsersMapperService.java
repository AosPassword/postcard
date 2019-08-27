package org.wuneng.web.postcard.services;

import org.json.JSONArray;
import org.wuneng.web.postcard.beans.ChangePasswordBean;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.beans.Login;
import org.wuneng.web.postcard.beans.User;

public interface UsersMapperService {

    User get_user_all_field(Integer id);

    Login get_id_password(String stu_id);

    Integer update_user(User user);

    Integer update_photo(String file_name, Integer id);

    Integer change_password(int parseInt, String password,byte[] slat);

    String get_stu_id_by_phone_number(long phone_number);

    byte[] get_salt_by_id(Integer id);

    String get_password_by_id(int parseInt);

    Integer change_password_by_phone(String phone_number, String new_password, byte[] slat);

    Integer update_phone_number(int parseInt, long phone_number);

    int update_insert(User user);

    Integer log_in(Integer id);

    ChangePasswordBean get_change_bean(int parseInt);
}
