package org.wuneng.web.postcard.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.wuneng.web.postcard.services.MathService;
import org.wuneng.web.postcard.services.UserService;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    @Autowired
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MathService mathService;

    @Override
    public void run(String... var1) throws Exception{
        objectMapper.disableDefaultTyping();
        String user = null;
        user = "{\n" +
                "          \"is_man\" : \"true\",\n" +
                "          \"in_school\" : \"true\",\n"+
                "          \"city\" : \"重庆市\",\n" +
                "          \"graduation_year\" : 2017,\n" +
                "          \"major_name\" : \"自动化\",\n" +
                "          \"wechat_account\" : \"aiosdnfioaisef\",\n" +
                "          \"qq_account\" : 1"+mathService.create_random_number(9)+",\n" +
                "          \"stu_id\" : \"2017212881\",\n" +
                "          \"is_deleted\" : false,\n" +
                "          \"directions\" : [\n" +
                "            \"sdfsdf\",\n" +
                "            \"python\",\n" +
                "            \"php\"\n" +
                "          ],\n" +
                "          \"name\" : \"张三\",\n" +
                "          \"slogan\" : \"asdofoasdijfao\",\n" +
                "          \"email\" : \"asodnfoasdn\",\n" +
                "          \"password\":\"123456\"," +
                "          \"company\":\"tencent\", " +
                "          \"job\":\"划水 \"" +
                "        }";
        userService.add_user(user);
        String user1 = "{\n" +
                "          \"is_man\" : \"true\",\n" +
                "          \"in_school\" : \"true\",\n"+
                "          \"city\" : \"重庆市\",\n" +
                "          \"graduation_year\" : 2017,\n" +
                "          \"major_name\" : \"自动化\",\n" +
                "          \"wechat_account\" : \"aiosdnfioaisef\",\n" +
                "          \"qq_account\" : 1"+mathService.create_random_number(9)+",\n" +
                "          \"stu_id\" : \"987654321\",\n" +
                "          \"is_deleted\" : false,\n" +
                "          \"directions\" : [\n" +
                "            \"sdfsdf\",\n" +
                "            \"python\",\n" +
                "            \"php\"\n" +
                "          ],\n" +
                "          \"name\" : \"李四\",\n" +
                "          \"slogan\" : \"asdofoasdijfao\",\n" +
                "          \"email\" : \"asodnfoasdn\",\n" +
                "          \"password\":\"123456\"," +
                "          \"company\":\"tencent\", " +
                "          \"job\":\"划水 \"" +
                "        }";
        String user2 = "{\n" +
                "          \"is_man\" : \"true\",\n" +
                "          \"in_school\" : \"true\",\n"+
                "          \"city\" : \"重庆市\",\n" +
                "          \"graduation_year\" : 2017,\n" +
                "          \"major_name\" : \"自动化\",\n" +
                "          \"wechat_account\" : \"aiosdnfioaisef\",\n" +
                "          \"qq_account\" : 1"+mathService.create_random_number(9)+",\n" +
                "          \"stu_id\" : \"123456789\",\n" +
                "          \"is_deleted\" : true,\n" +
                "          \"directions\" : [\n" +
                "            \"sdfsdf\",\n" +
                "            \"python\",\n" +
                "            \"php\"\n" +
                "          ],\n" +
                "          \"name\" : \"王五\",\n" +
                "          \"slogan\" : \"asdofoasdijfao\",\n" +
                "          \"email\" : \"asodnfoasdn\",\n" +
                "          \"password\":\"123456\"," +
                "          \"company\":\"tencent\", " +
                "          \"job\":\"划水\"" +
                "        }";
        userService.add_user(user1);
        userService.add_user(user2);
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
}
