package org.wuneng.web.postcard.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wuneng.web.postcard.beans.CheckResult;

import java.io.IOException;

public interface UserService {


    CheckResult check_user(String stu_id, String password);

    CheckResult get_user_by_key(String key, int from, int size);

    CheckResult get_user_all_field(int id) throws JsonProcessingException;

    CheckResult validateJWT(String jwt);

    CheckResult update_user(JSONObject jsonObject,int id) ;

    CheckResult change_password(String password, int parseInt,String old_password);


    CheckResult get_stu_id_by_phone_number(long phone_number);

    CheckResult send_verification_code(long number);

    CheckResult get_change_token(long phone_number, String code);

    CheckResult change_password_by_token(String token, String password);

    CheckResult bind_phone(String token, long phone_number, String code);

    CheckResult send_bind_code(String token, long phone_number);

    CheckResult add_user(String json);

    String add_directoin(String token, JSONArray jsonArray);

    CheckResult get_large_directions();

    CheckResult get_children_direction(int id);

    String get_random_users(int start);

    String get_page();

    boolean add_friend(Integer send_user_id, Integer accept_user_id);

    String get_friends(String token);
}
