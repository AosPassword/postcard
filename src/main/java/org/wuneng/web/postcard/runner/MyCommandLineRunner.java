package org.wuneng.web.postcard.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.Temperature;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.wuneng.web.postcard.services.MathService;
import org.wuneng.web.postcard.services.UserService;

import java.io.*;
import java.util.Random;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    @Autowired
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MathService mathService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(String... var1) throws Exception{
        objectMapper.disableDefaultTyping();
        JSONObject jsonfile = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("password","123456");
        jsonObject.put("in_school",true);
        String file_path = "D:\\data\\cqupt-spider\\studentInfo";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String s = null;
        Random random = new Random();
        readFile(file_path,fileReader,bufferedReader,jsonfile,jsonObject,s,random);
    }

    private void readFile(String filePath,FileReader reader,BufferedReader bufferedReader,
                          JSONObject jsonfile,JSONObject jsonObject,String s,Random r){
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.isDirectory()) {
                try {
                    reader = new FileReader(file);
                } catch (FileNotFoundException e) {
                    logger.error(e.toString());
                }
                bufferedReader = new BufferedReader(reader);
                try {
                    while (!(s = bufferedReader.readLine()).isEmpty()) {
                        logger.debug("s->"+s);
                        if  (s.contains("{")) {
                            JSONArray jsonArray = new JSONObject(s).getJSONArray("returnData");
                            if  (!jsonArray.isEmpty()) {
                                jsonfile = jsonArray.getJSONObject(0);
                                jsonObject.put("stu_id", jsonfile.getString("xh"));
                                jsonObject.put("name", jsonfile.getString("xm"));
                                jsonObject.put("major_name", jsonfile.getString("zym"));
                                jsonObject.put("graduation_year", Integer.parseInt(jsonfile.getString("nj")));
                                if (jsonfile.getString("xb").equals("男")) {
                                    jsonObject.put("is_man", true);
                                } else {
                                    jsonObject.put("is_man", false);
                                }
                                int random =r.nextInt(2);
                                if (random==0){
                                    jsonObject.put("is_deleted",true);
                                }else {
                                    jsonObject.put("is_deleted",false);
                                }
                                userService.add_user(jsonObject.toString());
                            }
                        }
                    }
                } catch (IOException e) {
                    logger.error(e.toString());
                    try {
                        bufferedReader.close();
                    } catch (IOException e1) {
                        logger.error(e1.toString());
                    }
                }
            }else {
                File[] files = file.listFiles();
                for (File f : files){
                    logger.debug(f.getAbsolutePath()+f.getName());
                    readFile(f.getAbsolutePath(),reader,bufferedReader,jsonfile,jsonObject,s,r);
                }
            }
        }
    }
}
