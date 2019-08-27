package org.wuneng.web.postcard.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wuneng.web.postcard.annotations.RequestTimes;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.beans.User;
import org.wuneng.web.postcard.services.ProfilePhotoService;
import org.wuneng.web.postcard.services.UserService;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    ObjectMapper objectMapper ;
    @Autowired
    UserService userService;
    @Autowired
    ProfilePhotoService profilePhotoService;
    @Value("${token.login.subject}")
    String user_log_in;
    @Value("${token.change.subject}")
    String change_password;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestTimes
    @RequestMapping(value = "/user/userUpdate",method = RequestMethod.POST)
    public String userInsert(@RequestBody String user,
                             @RequestHeader("token") String token) throws JsonProcessingException {
        CheckResult result = new CheckResult();
            Claims claims = (Claims) userService.validateJWT(token).getPayload();
            if (claims.getSubject().equals(user_log_in)) {
                Integer id = Integer.valueOf(claims.getId());
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(user);
                jsonObject.put("id", id);
                result = userService.update_user(jsonObject,id);
                result.setPayload(null);
                try {
                    return objectMapper.writeValueAsString(result);
                } catch (JsonProcessingException e) {
                    logger.error(e.toString());
                }
            } else {
                result.setSuccess(false);
                result.setPayload(null);
                try {
                    return objectMapper.writeValueAsString(result);
                } catch (JsonProcessingException e) {
                    logger.error(e.toString());
                }
            }
            return "error";
        }


    @RequestTimes
    @RequestMapping(value = "/user/log_in",method = RequestMethod.POST)
    public String user_log_in(@RequestParam("stu_id") String stu_id,
                              @RequestParam("password") String password) throws JsonProcessingException {
        CheckResult result = userService.check_user(stu_id,password);
        if (result.isSuccess()){
            return objectMapper.writeValueAsString(result);
        }else {
            return result.getErrCode();
        }
    }

    @RequestTimes
    @RequestMapping(value = "/user/get_user_by_token",method = RequestMethod.POST)
    public String get_user(@RequestHeader("token") String token) throws JsonProcessingException {
        CheckResult result = userService.validateJWT(token);
        if (result.isSuccess()) {
            Claims claims = (Claims) result.getPayload();
            String id = claims.getId();
            result = userService.get_user_all_field(Integer.parseInt(claims.getId()));
            return (String) result.getPayload();
        }
        return objectMapper.writeValueAsString(result);
    }

    @RequestTimes
    @RequestMapping(value = "/user/get_user_by_id",method = RequestMethod.POST)
    public String get_user(@RequestParam("id") int id) throws JsonProcessingException {
        CheckResult result ;
        result = userService.get_user_all_field(id);
        if (result.isSuccess()) {
            return (String) result.getPayload();
        }else {
            return objectMapper.writeValueAsString(result);
        }
    }

    @RequestTimes
    @RequestMapping(value = "/user/upload_profile_photo",method = RequestMethod.POST)
    public String upload_profile(@RequestHeader("token")String token,
                                 @RequestParam("profile_photo")MultipartFile file) throws JsonProcessingException {
        CheckResult result = userService.validateJWT(token);
        if (result.isSuccess()) {
            Claims c = (Claims) result.getPayload();
            result = profilePhotoService.downloadPhoto(file, Integer.parseInt(c.getId()));
        }
        result.setPayload(null);
        return objectMapper.writeValueAsString(result);
    }

    @RequestTimes
    @RequestMapping(value = "/user/token_change_password",method = RequestMethod.POST)
    public String change_password_by_token(@RequestHeader("token") String token,
                                           @RequestParam("password") String password,
                                           @RequestParam("old_password")String old_password) throws JsonProcessingException {
        CheckResult result = userService.validateJWT(token);
        if (result.isSuccess()) {
            Claims claims = (Claims) result.getPayload();
            if (claims.getSubject().equals(user_log_in)) {
                result = userService.change_password(password, Integer.parseInt(claims.getId()), old_password);
            }
        }
        result.setPayload(null);
        return objectMapper.writeValueAsString(result);
    }

    @RequestTimes(count = 1,time = 1000*60)
    @RequestMapping(value = "/user/get_verification_code",method = RequestMethod.POST)
    public String get_verification_code(@RequestParam("phone_number")long phone_number) throws JsonProcessingException {
        CheckResult result = userService.send_verification_code(phone_number);
        return objectMapper.writeValueAsString(result);
    }

    @RequestTimes
    @RequestMapping(value = "/user/change_by_code",method = RequestMethod.POST)
    public String change_password(@RequestHeader("token") String token,
                                  @RequestParam("pass") String pass) throws JsonProcessingException {
        CheckResult result = userService.change_password_by_token(token,pass);
        result.setPayload(null);
        return objectMapper.writeValueAsString(result);
    }

    @RequestTimes
    @RequestMapping(value = "/user/get_change_token",method = RequestMethod.POST)
    public String change_password(@RequestParam("phone_number") long phone_number,
                                  @RequestParam("code") String code) throws JsonProcessingException {
        CheckResult result = userService.get_change_token(phone_number,code);
        return objectMapper.writeValueAsString(result);
    }

    @RequestTimes(count = 1,time = 1000)
    @RequestMapping(value = "/user/send_bind_code")
    public String send_bind_code(@RequestHeader("token") String token,
                                 @RequestParam("phone_number")long phone_number) throws JsonProcessingException {
        CheckResult result = userService.send_bind_code(token,phone_number);
        return objectMapper.writeValueAsString(result);
    }

    @RequestTimes
    @RequestMapping(value = "/user/bind_phone")
    public String bind_phone(@RequestHeader("token")String token,
                             @RequestParam("phone_number")long phone_number,
                             @RequestParam("code")String code) throws JsonProcessingException {
        CheckResult result = userService.bind_phone(token,phone_number,code);
        return objectMapper.writeValueAsString(result);
    }

    @RequestTimes
    @RequestMapping(value = "/get_user_by_key")
    public String get_user_by_key(@RequestParam("key")String key,
                                  @RequestParam("page")int page,
                                  @RequestParam("size") int size) throws JsonProcessingException {
        int from = size * (page - 1);
        return objectMapper.writeValueAsString(userService.get_user_by_key(key,from,size));
    }

    @RequestMapping(value = "/add_user")
    public void add_user(@RequestParam("user") String user){
        userService.add_user(user);
    }


    @RequestTimes
    @PostMapping(value = "/get_large_directions")
    public String get_large_directions() {
        return (String) userService.get_large_directions().getPayload();
    }

    @RequestTimes
    @PostMapping(value = "/get_small_directions")
    public String get_small_directions(@RequestParam("direction_id") int direction_id){
        return (String) userService.get_children_direction(direction_id).getPayload();
    }

    @RequestTimes(count = 10,time = 10000)
    @PostMapping(value = "/get_users")
    public String get_users(@RequestParam("start")Integer start){
        return userService.get_random_users(start);
    }

    @RequestTimes
    @PostMapping(value = "/get_page")
    public String get_pages(){
        return userService.get_page();
    }

    @RequestTimes
    @PostMapping(value = "/user/add_direction")
    public String add_direction(@RequestHeader("token") String token,
                                @RequestParam("directions")String directions){
        JSONArray jsonArray = new JSONArray(directions);
        return userService.add_directoin(token,jsonArray);
    }

    @RequestTimes
    @PostMapping(value = "/user/get_friend")
    public String get_friends(@RequestHeader("token")String token){
        return userService.get_friends(token);
    }

    @RequestTimes
    @PostMapping(value = "/user/delete_friend")
    public String delete_friend(@RequestHeader("token") String token,
                                @RequestParam("friend_id")Integer id){
        return userService.delete_friend(token,id);
    }
}
