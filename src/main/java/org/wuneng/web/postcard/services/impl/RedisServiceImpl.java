package org.wuneng.web.postcard.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.services.RedisService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisServiceImpl implements RedisService {

    @Autowired
    StringRedisTemplate template;
    @Autowired
    ObjectMapper objectMapper;
    @Value("${redis_config.user_prefix}")
    String user_prefix;
    @Value("${redis_config.large_directions}")
    String large_directions;
    @Value("${redis_config.dir_prefix}")
    String dir_prefix;
    @Value("${redis_config.page_number}")
    String page_number;
    @Value("${redis_config.add_prefix}")
    String add_prefix;
    @Value("${redis_config.friend_prefix}")
    String friend_prefix;
    @Value("${redis_config.accept_prefix}")
    String accept_prefix;
    @Value("${redis_config.random_user_prefix}")
    String random_user_prefix;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String get_page(){
        return template.opsForValue().get(page_number);
    }

    @Override
    public void put_page(long hit){
        template.opsForValue().set(page_number, String.valueOf(hit));
        template.expire(page_number,1,TimeUnit.HOURS);
    }

    @Override
    public boolean put_user(String user, int id) {
        try {
            template.opsForValue().set(user_prefix + id, user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete_user(int id) {
        return template.delete(user_prefix + id);
    }

//    @Override
//    public Boolean has_key(int id, String field){
//        return template.opsForHash().hasKey(prefix+id,field);
//    }

    @Override
    public Boolean has_user(int id) {
        return template.hasKey(user_prefix + id);
    }

//    @Override
//    public Object get_user_field(int id, String field){
//        try {
//            return template.opsForHash().get(prefix + id, field);
//        }catch (Exception e){
//            return null;
//        }
//    }

    @Override
    public String get_user(int id) {
        String string = template.opsForValue().get(user_prefix + id);
        return string;
    }

    @Override
    public Boolean has_phone_number(long phone_number, String prefix) {
        return template.hasKey(prefix + phone_number);
    }

    @Override
    public Boolean delete_phone_number(long phone_number, String prefix) {
        return template.delete(prefix + phone_number);
    }

    @Override
    public String get_key_by_phone_number(long phone_number, String prefix) {
        return template.opsForValue().get(prefix + phone_number);
    }

    /**
     * @param phone_number key
     * @param stu_or_code  value
     * @param prefix       key's prefix
     * @param time         overtime/ms
     * @return boolean
     */
    @Override
    public Boolean put_phone_number(long phone_number, String stu_or_code, String prefix, Integer time) {
        try {
            template.opsForValue().set(prefix + phone_number, stu_or_code);
            if (time > 0) {
                template.expire(prefix + phone_number, time, TimeUnit.MILLISECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String get_large_directions() {
        return template.opsForValue().get(large_directions);
    }

    @Override
    public void put_large_directions(String directions) {
        template.opsForValue().set(large_directions, directions);
    }

    @Override
    public void delete_large_directions() {
        template.delete(large_directions);
    }

    @Override
    public String get_children_directions(int id) {
        return template.opsForValue().get(dir_prefix + id);
    }

    @Override
    public void put_children_directions(int id, String small) {
        template.opsForValue().set(dir_prefix + id, small);
    }

    @Override
    public void delete_children_directions(int id) {
        template.delete(dir_prefix + id);
    }

//    @Override
//    public List<String> get_random_users(int start) {
//        List<String> users = new ArrayList<>();
//        Integer page = Integer.valueOf(get_page());
//        long account = get_user_account();
//        for (int i = start;i<account;i=i+page){
//            users.add(user_prefix+i);
//        }
//        return template.opsForValue().multiGet(users);
//    }

    @Override
    public long put_add_request(Integer send, Integer accept) {
        logger.debug("send user:"+send+" put add request to accept user:"+accept);
        return template.opsForSet().add(add_prefix+accept, String.valueOf(send));
    }

    @Override
    public Long delete_add_request(Integer send, Integer accept){
        return template.opsForSet().remove(add_prefix+accept,String.valueOf(send));
    }

    @Override
    public boolean delete_add_request(Integer accept_id){
        return template.delete(add_prefix+accept_id);
    }
    @Override
    public Set<String> get_add_request(Integer accept){
        return template.opsForSet().members(add_prefix+accept);
    }


    @Override
    public Boolean has_add_request(Integer send, Integer accept){
        return template.opsForSet().isMember(add_prefix+accept,String.valueOf(send));
    }

    @Override
    public Set<String> get_friends(Integer id){
        return template.opsForSet().members(friend_prefix+id);
    }

    @Override
    public void put_friends(Integer id, Set<Integer> list) {
        for (int i: list){
            template.opsForSet().add(friend_prefix, String.valueOf(i));
        }
    }
    @Override
    public Boolean has_friend(Integer id, Integer friend_id){
        return template.opsForSet().isMember(friend_prefix+id,friend_id);
    }

    @Override
    public void delete_friends(Integer id){
        template.delete(friend_prefix+id);
    }

    @Override
    public long put_accept_response(Integer send, Integer accept) {
        return template.opsForSet().add(accept_prefix+accept, String.valueOf(send));
    }

    @Override
    public Boolean has_accept_response(Integer send, Integer accept){
        return template.opsForSet().isMember(accept_prefix+accept,String.valueOf(send));
    }

    @Override
    public Long delete_accept_response(Integer send, Integer accept){
        return template.opsForSet().remove(accept_prefix+accept,String.valueOf(send));
    }

    @Override
    public Set<String> get_accept_response(Integer accept){
        return template.opsForSet().members(accept_prefix+accept);
    }

    @Override
    public String get_random_users(int start) {
        return template.opsForValue().get(random_user_prefix+start);
    }

    @Override
    public void put_random_user(int start,String random_users){
        template.opsForValue().set(random_user_prefix+start,random_users);
        template.expire(random_user_prefix+start,1,TimeUnit.MILLISECONDS.HOURS);
    }


//    @Override
//    public Boolean put_user_field(int id, String field, Object o){
//        try {
//            template.opsForHash().put(prefix + id, field, o);
//            return true;
//        }catch (Exception e){
//            return false;
//        }
//    }


//    public <T> void listToMedel(List<String> fieldList, List<Object> objects, T t){
//        if (fieldList==null||objects==null){
//            return;
//        }
//        Field[] fields = t.getClass().getFields();
//        if (fields.length<=0){
//            return;
//        }
//        List<String> fieldsName = new ArrayList<>();
//        for (Field field:fields){
//            fieldsName.add(field.getName());
//        }
//        String fieldName;
//        for (int i =0;i<fieldList.size();i++) {
//            if (objects.get(i) != null) {
//                fieldName = fieldsName.get(i);
//                if (fieldsName.contains(fieldName)) {
//                    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase()
//                            + fieldName.substring(1);
//                    Method method = null;
//                    Class<?> clazz = t.getClass();
//                    try {
//                        method = clazz.getMethod(setMethodName, new Class[]{objects.get(i).getClass()});
//                    } catch (NoSuchMethodException e) {
//                        e.printStackTrace();
//                    }
//                    if (method == null) {
//                        return;
//                    }
//                    try {
//                        method.invoke(t, objects.get(i));
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
}
