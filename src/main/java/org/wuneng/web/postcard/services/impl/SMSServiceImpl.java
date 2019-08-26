package org.wuneng.web.postcard.services.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.services.MathService;
import org.wuneng.web.postcard.services.SMSService;

import javax.swing.text.html.FormSubmitEvent;

@Service
public class SMSServiceImpl implements SMSService {
    @Autowired
    MathService mathService;
    @Value("${SMS.product}")
    private String product;//短信API产品名称（短信产品名固定，无需修改）
    @Value("${SMS.domain}")
    private String domain;//短信API产品域名（接口地址固定，无需修改）
    @Value("${SMS.accessKeyId}")
    private String accessKeyId;//你的accessKeyId,参考本文档步骤2
    @Value("${SMS.accessKeySecret}")
    private String accessKeySecret;//你的accessKeySecret，参考本文档步骤2
    @Value("${SMS.regionId}")
    private String regionId;
    @Value("${SMS.templateCode}")
    private String templateCode;
    @Value("${SMS.signName}")
    private String signName;

    private Logger logger = LoggerFactory.getLogger(getClass());


    //初始化ascClient,暂时不支持多region（请勿修改）
    @Override
    public String sendSMS(long phone_number, String number) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile("default",accessKeyId,accessKeySecret);

        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        CommonRequest request = new CommonRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setAction("SendSms");
        request.setProtocol(ProtocolType.HTTPS);
        request.setVersion("2017-05-25");
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.putQueryParameter("PhoneNumbers", String.valueOf(phone_number));
        request.putQueryParameter("TemplateParam","{\"code\":\""+number+"\"}");
        request.putQueryParameter("TemplateCode",templateCode);
        request.putQueryParameter("SignName",signName);//可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者

        //请求失败这里会抛ClientException异常
        try {
            CommonResponse sendSmsResponse = acsClient.getCommonResponse(request);
            return sendSmsResponse.getData();
        }catch (Exception e){
            e.printStackTrace();
        }
        return number;
    }
}
