package org.wuneng.web.postcard.services;

import java.util.List;
import java.util.Set;

public interface RedisService {

    String get_page();

    boolean put_user(String user, int id);

    boolean delete_user(int id);

    Boolean has_user(int id);

    String get_user(int id);

    Boolean has_phone_number(long phone_number, String prefix);

    Boolean delete_phone_number(long phone_number, String prefix);

    String get_key_by_phone_number(long phone_number, String prefix);

    Boolean put_phone_number(long phone_number, String stu_or_code, String prefix,Integer time);

    String get_large_directions();

    void put_large_directions(String directions);

    void delete_large_directions();

    String get_children_directions(int id);

    void put_children_directions(int id, String small);

    void delete_children_directions(int id);

    List<String> get_random_users(int start);

    long put_add_request(Integer send, Integer accept);

    Long delete_add_request(Integer send, Integer accept);

    boolean delete_add_request(Integer accept_id);

    Set<String> get_add_request(Integer accept);

    Boolean has_add_request(Integer send, Integer accept);

    Set<String> get_friends(Integer id);

    void put_friends(Integer id, Set<Integer> list);

    Boolean has_friend(Integer id, Integer friend_id);

    void delete_friends(Integer id);

    long put_accept_response(Integer send, Integer accept);

    Boolean has_accept_response(Integer send, Integer accept);

    Long delete_accept_response(Integer send, Integer accept);

    Set<String> get_accept_response(Integer accept);

}
