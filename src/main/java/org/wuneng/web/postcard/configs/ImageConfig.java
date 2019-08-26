package org.wuneng.web.postcard.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ImageConfig extends WebMvcConfigurerAdapter {

    @Value("${static_file_config.path}")
    private String filePath; //配置文件配置的物理保存地址

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + filePath);
        super.addResourceHandlers(registry);
    }
}
