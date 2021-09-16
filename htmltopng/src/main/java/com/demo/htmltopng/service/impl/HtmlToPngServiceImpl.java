package com.demo.htmltopng.service.impl;

import com.demo.htmltopng.service.HtmlToPngService;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;
import org.fit.cssbox.layout.BrowserCanvas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author AnYuan
 * 生成png服务接口
 */

@Slf4j
@Service
public class HtmlToPngServiceImpl implements HtmlToPngService {

    private Map<String, String> getUser() {
        Map info = new HashMap(2);
        info.put("age", "18");
        info.put("url", "https://static.heytea.com/taro_trial/v1/img/my/me_img_head_login.png");

        Map user = new HashMap(2);
        user.put("user", "张三");
        user.put("info", info);
        return user;
    }

    @Autowired
    private FreeMarkerConfigurer configuration;

    @Override
    public void htmlToPng() throws Exception {

        int width = 220;
        int height = 150;

        // 查询一个模版
        Template template = configuration.getConfiguration().getTemplate("T001");
        // 替换模版参数
        String readyParsedTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, getUser());

        InputStream is = new ByteArrayInputStream(readyParsedTemplate.getBytes(StandardCharsets.UTF_8));
        DocumentSource docSource = new StreamDocumentSource(is, null, "text/html; charset=utf-8");

        FileOutputStream out = new FileOutputStream("./" + new File(UUID.randomUUID().toString() + ".png"));
        try {
            DOMSource parser = new DefaultDOMSource(docSource);

            DOMAnalyzer da = new DOMAnalyzer(parser.parse(), docSource.getURL());
            da.attributesToStyles();
            da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT);
            da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT);
            da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT);
            da.getStyleSheets();

            BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, docSource.getURL());
            contentCanvas.createLayout(new Dimension(width, height));

            ImageIO.write(contentCanvas.getImage(), "png", out);

//
//            String md5;
//            try (FileInputStream fileInputStream = new FileInputStream(tmpFile)) {
//                md5 = DigestUtils.md5Hex(fileInputStream);
//            }

        } catch (Exception e) {
           log.info("HtmlToPng Exception", e);
        } finally {
            out.close();
            is.close();
            docSource.close();
        }
    }
}
