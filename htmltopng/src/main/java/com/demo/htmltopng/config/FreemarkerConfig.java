package com.demo.htmltopng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


/**
 * freemarker加载这个模板加载器
 */

@Configuration
public class FreemarkerConfig{

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(DemoTemplateLoader demoTemplateLoader) {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setPreTemplateLoaders(demoTemplateLoader);
        return configurer;
    }

}
