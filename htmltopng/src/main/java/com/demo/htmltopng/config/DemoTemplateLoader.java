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

/**
 * @author AnYuan
 */

@Slf4j
@Component("DemoTemplateLoader")
public class DemoTemplateLoader implements TemplateLoader {

    @Autowired
    private DemoTemplateService demoTemplateService;

    /**
     * 从mysql根据 code 查询一个样式模版
     * @param code 查询条件
     * @return Object 模版html
     * @throws IOException IOException
     */
    @Override
    public Object findTemplateSource(String code) throws IOException {
        return  demoTemplateService.getOne(new QueryWrapper<DemoFreemarkerTemplate>().lambda().eq(DemoFreemarkerTemplate::getCode, code));
    }

    /**
     * 获取模版的最后更新时间 这里直接使用当前时间
     * @param o 模版
     * @return long 最后时间
     */
    @Override
    public long getLastModified(Object o) {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 根据查询的模版得到Reader
     * @param o 模版对象
     * @param s s
     * @return Reader
     * @throws IOException IOException
     */
    @Override
    public Reader getReader(Object o, String s) throws IOException {
        return new StringReader(((DemoFreemarkerTemplate) o).getValue());
    }

    /**
     * 关闭模版源
     * @param o o
     * @throws IOException IOException
     */
    @Override
    public void closeTemplateSource(Object o) throws IOException {

    }
}
