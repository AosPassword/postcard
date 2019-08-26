package org.wuneng.web.postcard.services.impl;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.beans.Login;
import org.wuneng.web.postcard.beans.User;
import org.wuneng.web.postcard.dao.ProfilePhotoMapper;
import org.wuneng.web.postcard.dao.UserMapper;
import org.wuneng.web.postcard.services.UsersMapperService;
@Repository
public class UsersMapperServiceImpl implements UsersMapperService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    ProfilePhotoMapper profilePhotoMapper;

    @Override
    public User get_user_all_field(Integer id){
        return userMapper.get_user_by_id(id);
    }

    @Override
    public Login get_id_password(String stu_id) {
        return userMapper.get_id_password(stu_id);
    }

    @Override
    public Integer update_user(User user){
        return userMapper.update_user(user);
    }

    @Override
    public Integer update_photo(String file_name, Integer id){
        return profilePhotoMapper.update_photo(id,file_name);
    }

    @Override
    public Integer change_password(int parseInt, String password,String slat) {
        return userMapper.change_password(parseInt,password,slat);
    }

    @Override
    public String get_stu_id_by_phone_number(long phone_number){
        return userMapper.get_stu_id_by_phone_number(phone_number);
    }

    @Override
    public String get_salt_by_id(Integer id) {
        return userMapper.get_slat_by_id(id);
    }

    @Override
    public String get_password_by_id(int parseInt) {
        return userMapper.get_password_by_id(parseInt);
    }

    @Override
    public Integer change_password_by_phone(String phone_number, String new_password, String slat) {
        return userMapper.change_password_by_phone_number(phone_number,new_password,slat);
    }

    @Override
    public Integer update_phone_number(int parseInt, long phone_number) {
        return userMapper.update_user_number(parseInt,phone_number);
    }

    @Override
    public int update_insert(User user) {
        userMapper.userInsert(user);
        return user.getId();
    }

    @Override
    public Integer log_in(Integer id) {
        return userMapper.log_in(id);
    }


}
