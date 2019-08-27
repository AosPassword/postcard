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
        jsonObject.put("is_deleted",true);
        jsonObject.put("password","123456");
        jsonObject.put("in_school",true);
        String file_path = "D:\\data\\cqupt-spider\\studentInfo";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String s = null;
        readFile(file_path,fileReader,bufferedReader,jsonfile,jsonObject,s);


//        user = "{\n" +
//                "          \"is_man\" : \"true\",\n" +
//                "          \"in_school\" : \"true\",\n"+
//                "          \"city\" : \"重庆市\",\n" +
//                "          \"graduation_year\" : 2017,\n" +
//                "          \"major_name\" : \"自动化\",\n" +
//                "          \"wechat_account\" : \"aiosdnfioaisef\",\n" +
//                "          \"qq_account\" : 1"+mathService.create_random_number(9)+",\n" +
//                "          \"stu_id\" : \"2017212881\",\n" +
//                "          \"is_deleted\" : false,\n" +
//                "          \"directions\" : [\n" +
//                "            \"sdfsdf\",\n" +
//                "            \"python\",\n" +
//                "            \"php\"\n" +
//                "          ],\n" +
//                "          \"name\" : \"张三\",\n" +
//                "          \"slogan\" : \"asdofoasdijfao\",\n" +
//                "          \"email\" : \"asodnfoasdn\",\n" +
//                "          \"password\":\"123456\"," +
//                "          \"company\":\"tencent\", " +
//                "          \"job\":\"划水 \"" +
//                "        }";
//        userService.add_user(user);

//        for (int i = 0;i<2;i++) {
//            String phone_number = mathService.create_random_number(10);
//            user = "{\n" +
//                    "          \"gender\" : \"男\",\n" +
//                    "          \"city\" : \"重庆市\",\n" +
//                    "          \"graduation_year\" : 2017,\n" +
//                    "          \"major_name\" : \"自动化\",\n" +
//                    "          \"wechat_account\" : \"aiosdnfioaisef\",\n" +
//                    "          \"qq_account\" : 1"+mathService.create_random_number(9)+",\n" +
//                    "          \"stu_id\" : \""+mathService.create_random_number(10)+"\",\n" +
//                    "          \"is_deleted\" : false,\n" +
//                    "          \"directions\" : [\n" +
//                    "            \"sdfsdf\",\n" +
//                    "            \"python\",\n" +
//                    "            \"php\"\n" +
//                    "          ],\n" +
//                    "          \"name\" : \"oqij\",\n" +
//                    "          \"phone_number\" : 1" + phone_number + ",\n" +
//                    "          \"slogan\" : \"asdofoasdijfao\",\n" +
//                    "          \"email\" : \"asodnfoasdn\",\n" +
//                    "          \"password\":\"123456\",\n" +
//                    "          \"company\":\"tencent\", " +
//                    "          \"job\":\"划水 \"" +
//                    "        }";
//            userService.add_user(user);
//        }
    }

    private void readFile(String filePath,FileReader reader,BufferedReader bufferedReader,
                          JSONObject jsonfile,JSONObject jsonObject,String s){
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
                    readFile(f.getAbsolutePath(),reader,bufferedReader,jsonfile,jsonObject,s);
                }
            }
        }
    }
}
