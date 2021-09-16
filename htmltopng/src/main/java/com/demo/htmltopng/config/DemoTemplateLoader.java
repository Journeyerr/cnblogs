package com.demo.htmltopng.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.htmltopng.model.entity.DemoFreemarkerTemplate;
import com.demo.htmltopng.service.DemoTemplateService;
import freemarker.cache.TemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Component("DemoTemplateLoader")
public class DemoTemplateLoader implements TemplateLoader {

    @Autowired
    private DemoTemplateService demoTemplateService;

    // 返回一个模版
    @Override
    public Object findTemplateSource(String s) throws IOException {
        return  demoTemplateService.getOne(new QueryWrapper<DemoFreemarkerTemplate>().lambda().eq(DemoFreemarkerTemplate::getCode, s));
    }

    // 获取最后更新时间
    @Override
    public long getLastModified(Object o) {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    // 得到Reader
    @Override
    public Reader getReader(Object o, String s) throws IOException {
        return new StringReader(((DemoFreemarkerTemplate) o).getValue());
    }

    // 关闭模版源
    @Override
    public void closeTemplateSource(Object o) throws IOException {

    }
}
