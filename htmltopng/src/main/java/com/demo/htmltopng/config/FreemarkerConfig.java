package com.demo.htmltopng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


/**
 * @author AnYuan
 */

@Configuration
public class FreemarkerConfig{

    /**
     * 自定义的模版加载器
     * @param demoTemplateLoader demoTemplateLoader 自定义从数据库取样式
     * @return FreeMarkerConfigurer
     */

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(DemoTemplateLoader demoTemplateLoader) {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setPreTemplateLoaders(demoTemplateLoader);
        return configurer;
    }
}
