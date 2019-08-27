package org.wuneng.web.postcard.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.beans.*;
import org.wuneng.web.postcard.services.*;
import org.wuneng.web.postcard.utils.DateUtil;
import org.wuneng.web.postcard.utils.JWTUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UsersMapperService usersMapperService;
    @Autowired
    RedisService redisService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    FriendService friendService;
    @Autowired
    DirectionService directionService;
    @Autowired
    BloomFilterService filterService;
    @Autowired
    SlatService slatService;
    @Autowired
    MathService mathService;
    @Autowired
    UserESSearchService userESSearchService;

    @Value("${mq.config.exchange}")
    String exchange;
    @Value("${redis_config.code_prefix}")
    String code_prefix;
    @Value("${redis_config.stu_prefix}")
    String stu_prefix;
    @Value("${token.login.subject}")
    String user_log_in;
    @Value("${token.change.subject}")
    String change_password;
    @Value("${redis_config.bind_prefix}")
    String bind_prefix;
    @Value("${token.change.ttlMillis}")
    long change_ttlmillis;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CheckResult check_user(String stu_id, String password) {
        CheckResult result = new CheckResult();
        Login user = null;
        if (filterService.stu_id_might_contain(stu_id)) {
            user = usersMapperService.get_id_password(stu_id);
            if (user != null) {
                if (user.getPassword().equals(slatService.getSecurePassword(password, user.getSlat()))) {
                    LogInResponse response = new LogInResponse();
                    response.setId(user.getId());
                    response.setIn_school(user.isIn_school());
                    if (user.isIs_deleted()) {
                        response.setFirst(true);
                        if (user.getGraduation_year()<=DateUtil.get_year()){
                            set_log_in(user.getId());
                        }
                    }else {
                        response.setFirst(false);
                    }
                    response.setToken(JWTUtil.createJWT(String.valueOf(user.getId()),user_log_in,0));
                    result.setSuccess(true);
                    result.setPayload(response);
                } else {
                    result.setSuccess(false);
                    result.setErrCode("密码错误");
                }
            } else {
                result.setSuccess(false);
                result.setErrCode("mysql不含有用户名");
            }
        } else {
            result.setSuccess(false);
            result.setErrCode("filter不含有用户名");
        }
        return result;
    }

    private void set_log_in(Integer id){
        String all = null;
        try {
            all = objectMapper.writeValueAsString(usersMapperService.get_user_all_field(id));
            redisService.put_user(all, id);
            amqpTemplate.convertAndSend(exchange, "es.update.user.info",all);
            usersMapperService.log_in(id);
        } catch (JsonProcessingException e) {
            logger.error(e.toString());
        }
    }


    @Override
    public CheckResult get_user_by_key(String key, int from, int size) {
        CheckResult result = new CheckResult();
        JSONArray jsonArray;
        try {
            Long number = Long.parseLong(key);
            jsonArray = userESSearchService.get_user_by_keyword(key,Constant.NUMBER_SEARCH,from,size);
        }catch (Exception e){
            jsonArray = userESSearchService.get_user_by_keyword(key, Constant.STRING_SEARCH, from, size);
        }finally {
            result.setSuccess(false);
        }
        if (jsonArray.isEmpty()){
            result.setSuccess(false);
        }else {
            result.setSuccess(true);
            result.setPayload(jsonArray);
        }
        return result;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public CheckResult get_user_all_field(int id) {
        CheckResult result = new CheckResult();
        //id布隆过滤器过滤
        if (filterService.id_might_constain(id)) {
            //redis里有就查redis，没有就查mysql之后更新redis
            if (redisService.has_user(id)) {
                String user = redisService.get_user(id);
                System.out.println("redis get user" + user);
                result.setPayload(user);
                result.setSuccess(true);
                return result;
            } else {
                User user = usersMapperService.get_user_all_field(id);
                if  (user!=null) {
                    System.out.println("mysql get user");
                    List<String> direction = directionService.get_direcitons(id);
                    user.setDirections((ArrayList<String>) direction);
                    String s = null;
                    try {
                        s = objectMapper.writeValueAsString(user);
                    } catch (JsonProcessingException e) {
                        logger.error(e.toString());
                    }
                    redisService.put_user(s, id);
                    result.setPayload(s);
                    result.setSuccess(true);
                    return result;
                }else {
                    result.setSuccess(false);
                    result.setPayload("[UserServiceImpl.get_user_all_field]:id isn't exist");
                }
            }
        }
        result.setSuccess(false);
        result.setErrCode("[UserServiceImpl.get_user_all_field]:id isn't exist");
        return result;
    }

    @Override
    public CheckResult validateJWT(String jwt) {
        return JWTUtil.validateJWT(jwt);
    }

    /**
     * @param jsonObject
     * @return
     * @throws IOException
     */
    @Override
    public CheckResult update_user(JSONObject jsonObject,int id) {
        CheckResult result = new CheckResult();
        User user = null;
        try {
            user = objectMapper.readValue(jsonObject.toString(),User.class);
        } catch (IOException e) {
            logger.error(e.toString());
        }
        if (user!=null) {
            int i = usersMapperService.update_user(user);
            if (i == 1){
                redisService.delete_user(user.getId());
                amqpTemplate.convertAndSend(exchange,"es.update.user.log", jsonObject.toString());
                result.setSuccess(true);
                return result;
            }

        }
        result.setSuccess(false);
        return result;
    }

    /**
     * 根据新旧密码和用户id来改变用户密码
     *
     * @param password
     * @param parseInt
     * @param old_password
     * @return
     */
    @Override
    public CheckResult change_password(String password, int parseInt, String old_password) {
        CheckResult result = new CheckResult();
        ChangePasswordBean bean = usersMapperService.get_change_bean(parseInt);
        String slat_old_password = slatService.getSecurePassword(password, bean.getSlat());
        logger.debug(slat_old_password);
        logger.debug(bean.getPassword());
        if (!bean.getPassword().isEmpty()) {
            if (bean.getPassword().equals(slat_old_password)) {
                byte[] new_salt = slatService.getSalt();
                String new_password = slatService.getSecurePassword(password, new_salt);
                int i = usersMapperService.change_password(parseInt, new_password, new_salt);
                if (i != 1) {
                    result.setSuccess(false);
                } else {
                    result.setSuccess(true);
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * 通过手机号码来获得用户的验证码
     *
     * @param phone_number
     * @return
     */
    @Override
    public CheckResult get_stu_id_by_phone_number(long phone_number) {
        CheckResult result = new CheckResult();
        String stu_id;
        if (filterService.phone_number_might_contain(phone_number)) {
            if (redisService.has_phone_number(phone_number, stu_prefix)) {
                stu_id = redisService.get_key_by_phone_number(phone_number, stu_prefix);
                result.setSuccess(true);
                result.setPayload(stu_id);
            } else {
                stu_id = usersMapperService.get_stu_id_by_phone_number(phone_number);
                if (stu_id != null) {
                    result.setSuccess(true);
                    result.setPayload(stu_id);
                    redisService.put_phone_number(phone_number, stu_id, stu_prefix, 0);
                } else {
                    result.setSuccess(false);
                    result.setPayload("mysql don't has this phone number:" + phone_number);
                }
            }
        }
        return result;
    }

    /**
     * 发送用于更改密码的短信验证码
     *
     * @param number
     * @return
     */
    @Override
    public CheckResult send_verification_code(long number) {
        CheckResult result = new CheckResult();
        if (filterService.phone_number_might_contain(number)) {
            result = get_stu_id_by_phone_number(number);
            if (result.isSuccess()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("phone_number", number);
                jsonObject.put("prefix", code_prefix);
                jsonObject.put("code", mathService.create_random_number(6));
                amqpTemplate.convertAndSend(exchange, "sms.send.code.log", jsonObject.toString());
                result.setSuccess(true);
            }
        } else {
            result.setSuccess(false);
            result.setErrCode("number is not exists");
        }
        return result;
    }

    /**
     * 通过电话号码和已经发送的短信验证码获得可以更改密码的token
     *
     * @param phone_number
     * @param code
     * @return
     */
    @Override
    public CheckResult get_change_token(long phone_number, String code) {
        CheckResult result = new CheckResult();
        if (redisService.has_phone_number(phone_number, code_prefix)) {
            if (code.equals(redisService.get_key_by_phone_number(phone_number, code_prefix))) {
                result.setSuccess(true);
                result.setPayload(JWTUtil.createJWT(String.valueOf(phone_number), change_password, change_ttlmillis));
            } else {
                result.setSuccess(false);
            }
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 根据更改密码的token和新密码更改密码
     *
     * @param token
     * @param password
     * @return
     */
    @Override
    public CheckResult change_password_by_token(String token, String password) {
        CheckResult result = JWTUtil.validateJWT(token);
        if (result.isSuccess()) {
            Claims claims = (Claims) result.getPayload();
            if (claims.getSubject().equals(change_password)) {
                String phone_number = claims.getId();
                byte[] slat = slatService.getSalt();
                String new_password = slatService.getSecurePassword(password, slat);
                int i = usersMapperService.change_password_by_phone(phone_number, new_password, slat);
                if (i == 1) {
                    result.setSuccess(true);
                    return result;
                }
            }
        }
        result.setSuccess(false);
        return result;
    }

    /**
     * 根据登陆token，手机号和已经发送的短信来绑定手机号
     *
     * @param token
     * @param phone_number
     * @param code
     * @return
     */
    @Override
    public CheckResult bind_phone(String token, long phone_number, String code) {
        CheckResult result = validateJWT(token);
        if (result.isSuccess()) {
            Claims claims = (Claims) result.getPayload();
            if (claims.getSubject().equals(user_log_in) &&
                    redisService.get_key_by_phone_number(phone_number, bind_prefix).equals(code)) {
                System.out.println("redis get key" + redisService.get_key_by_phone_number(phone_number, bind_prefix));
                System.out.println("input code" + code);
                int i = usersMapperService.update_phone_number(Integer.parseInt(claims.getId()), phone_number);
                if (i == 1) {
                    filterService.phone_number_put(phone_number);
                    result.setPayload(null);
                    return result;
                }
            }
        }
        result.setSuccess(false);
        result.setPayload(null);
        return result;
    }
    /**
     * 根据登陆token来向所要绑定的手机号码发送短信
     *
     * @param token
     * @param phone_number
     * @return
     */
    @Override
    public CheckResult send_bind_code(String token, long phone_number) {
        CheckResult checkResult = validateJWT(token);
        if (checkResult.isSuccess()) {
            Claims claims = (Claims) checkResult.getPayload();
            if (claims.getSubject().equals(user_log_in)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("prefix", bind_prefix);
                jsonObject.put("phone_number", phone_number);
                jsonObject.put("code", mathService.create_random_number(6));
                amqpTemplate.convertAndSend(exchange, "sms.send.code.log", jsonObject.toString());
                return checkResult;
            }
        }
        checkResult.setSuccess(false);
        checkResult.setPayload(null);
        return checkResult;
    }

    /**
     * 添加用户
     *
     * @param json
     * @return
     */
    @Override
    public CheckResult add_user(String json) {
        CheckResult result = new CheckResult();
        User user = null;
        try {
            user = objectMapper.readValue(json, User.class);
            System.out.println("is deleted:"+user.isIs_deleted());
        } catch (IOException e) {
            logger.error(e.toString());
        }
        JSONObject jsonObject = new JSONObject(json);
        if (user != null) {
            user.setSlat(slatService.getSalt());
            user.setPassword(slatService.getSecurePassword(user.getPassword(), user.getSlat()));
            int id = usersMapperService.update_insert(user);
            filterService.stu_id_put(user.getStu_id());
            filterService.id_put(id);
            if (user.getPhone_number() != 0) {
                filterService.phone_number_put(user.getPhone_number());
            }
            jsonObject.put("id", id);
            jsonObject.remove("password");
            if  (id%2==1) {
                redisService.put_user(jsonObject.toString(), id);
            }
            amqpTemplate.convertAndSend(exchange, "es.update.user.log", jsonObject.toString());
            result.setSuccess(true);
        }
        return result;
    }

    @Override
    public String add_directoin(String token, JSONArray jsonArray) {
        CheckResult result = new CheckResult();
        if (jsonArray.length() > 3) {
            result.setSuccess(false);
            result.setErrCode("length is too long");
        } else {
            result = validateJWT(token);
            if (result.isSuccess()) {
                Claims claims = (Claims) result.getPayload();
                if (claims.getSubject().equals(user_log_in)) {
                    Integer id = Integer.parseInt(claims.getId());
                    List<Integer> ids = directionService.get_ids(id);
                    Iterator iterator =  jsonArray.iterator();
                    while (iterator.hasNext()){
                        Integer i = (Integer) iterator.next();
                        if (ids.contains(i)){
                            jsonArray.remove(i);
                            ids.remove(i);
                        }
                    }
                    for (Integer integer: ids){
                        directionService.delete_derections(id,integer);
                    }
                    directionService.insert_directions(jsonArray, id);
                    result.setSuccess(true);
                } else {
                    result.setSuccess(false);
                }
            } else {
                result.setSuccess(false);
                result.setErrCode("no token,plz log in");
            }
        }
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            logger.error(e.toString());
            return Constant.FALSE;
        }
    }

    @Override
    public CheckResult get_large_directions() {
        redisService.delete_large_directions();
        CheckResult result = new CheckResult();
        String directions = redisService.get_large_directions();
        if (directions != null) {
            result.setPayload(directions);
            result.setSuccess(true);
        } else {
            try {
                directions = objectMapper.writeValueAsString(directionService.get_large_directions());
            } catch (JsonProcessingException e) {
                logger.error(e.toString());
            }
            if (directions!=null) {
                result.setSuccess(true);
                result.setPayload(directions);
                redisService.put_large_directions(directions);
            }else {
                result.setSuccess(false);
            }
        }
        return result;
    }

    @Override
    public CheckResult get_children_direction(int id) {
        CheckResult result = new CheckResult();
        String directions = redisService.get_children_directions(id);
        if (directions != null) {
            result.setPayload(directions);
            result.setSuccess(true);
        } else {
            try {
                directions = objectMapper.writeValueAsString(directionService.get_children_directions(id));
            } catch (JsonProcessingException e) {
                logger.error(e.toString());
            }
            if  (directions!=null) {
                result.setSuccess(true);
                result.setPayload(directions);
                redisService.put_children_directions(id, directions);
            }else {
                result.setSuccess(false);
            }
        }
        return result;
    }

    @Override
    public String get_random_users(int start){
        List<String> list = redisService.get_random_users(start);
        return list.toString();
    }

    @Override
    public String get_page(){
        String page = redisService.get_page();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page",page);
        return jsonObject.toString();
    }

    @Override
    public boolean add_friend(Integer send_user_id, Integer accept_user_id) {
        boolean b = friendService.send_add_request(send_user_id,accept_user_id);
        return b;
    }

    @Override
    public String get_friends(String token)  {
        CheckResult result = JWTUtil.validateJWT(token);
        if (result.isSuccess()){
            Claims claims = (Claims) result.getPayload();
            Integer id = Integer.parseInt(claims.getId());
            result = friendService.get_friends_ids(id);
            try {
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                logger.error(e.toString());
            }
        }
        return Constant.FALSE;
    }

    @Override
    public String delete_friend(String token, Integer id) {
        CheckResult checkResult = JWTUtil.validateJWT(token);
        if (checkResult.isSuccess()){
            checkResult.setPayload(null);
            Claims claims = (Claims) checkResult.getPayload();
            Integer send_id =Integer.parseInt(claims.getId());
            int i = friendService.delete_friend(send_id,id);
            if (i >0){
                try {
                    return objectMapper.writeValueAsString(checkResult);
                } catch (JsonProcessingException e) {
                    logger.debug(e.toString());
                }
            }
        }
        return Constant.FALSE;
    }
}
