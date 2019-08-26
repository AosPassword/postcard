package org.wuneng.web.postcard.services.impl;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.dao.ProfilePhotoMapper;
import org.wuneng.web.postcard.services.ProfilePhotoService;
import org.wuneng.web.postcard.services.RedisService;
import org.wuneng.web.postcard.services.UsersMapperService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Service
public class ProfilePhotoServiceImpl implements ProfilePhotoService {

    @Value("${static_file_config.path}")
    String file_path;
    @Autowired
    RedisService redisService;
    @Autowired
    AmqpTemplate amqpTemplate;
    @Value("${mq.config.exchange}")
    String exchange;

    @Autowired
    UsersMapperService usersMapperService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CheckResult downloadPhoto(MultipartFile file,Integer id){
        CheckResult result = new CheckResult();
        if (file.isEmpty()){
            result.setSuccess(false);
            result.setErrCode("file is empty");
        }else {
            try {
                BufferedImage image = ImageIO.read(file.getInputStream());
            }catch (IOException e){
                result.setSuccess(false);
                result.setErrCode(file.getName()+"is not a image");
                return result;
            }
            String old_file_name = file.getOriginalFilename();
            System.out.println(old_file_name);
            String type = old_file_name.substring(old_file_name.lastIndexOf("."));
            String file_name = UUID.randomUUID().toString().replace("-","")+type;
            File copy = new File(file_path+file_name);
            try{
                file.transferTo(copy);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",id);
                jsonObject.put("file_name",file_name);
                amqpTemplate.convertAndSend(exchange,"mysql.update.user.photo.log",jsonObject.toString());
                usersMapperService.update_photo(file_name, id);
                System.out.println("delete cache");
                redisService.delete_user(id);
                result.setSuccess(true);
                result.setPayload(copy.getName());
            } catch (IOException e) {
                result.setSuccess(false);
                e.printStackTrace();
                result.setErrCode(e.toString());
                logger.error(e.toString());
            }
        }
        return result;
    }
}
