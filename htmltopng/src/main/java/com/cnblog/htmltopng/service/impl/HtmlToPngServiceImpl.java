package com.cnblog.htmltopng.service.impl;

import com.cnblog.htmltopng.service.HtmlToPngService;
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
 * 生成png服务实现
 */

@Slf4j
@Service
public class HtmlToPngServiceImpl implements HtmlToPngService {

    /**
     * Mock 数据
     * @return Map<String, String>
     */
    private Map<String, String> getUser() {

        HashMap info = new HashMap(2);
        info.put("age", "18");
        info.put("url", "https://pic.cnblogs.com/avatar/2319511/20210304170859.png");

        Map user = new HashMap(2);
        user.put("user", "安逺");
        user.put("info", info);

        return user;
    }

    @Autowired
    private FreeMarkerConfigurer configuration;

    @Override
    public void htmlToPng() throws Exception {

        // 查询模版条件
        String templateCode = "T001";

        // png图片宽度
        int width = 220;
        // png图片高度
        int height = 150;

        // 从数据库查询一个模版
        Template template = configuration.getConfiguration().getTemplate(templateCode);
        // 将数据替换模版里面的参数
        String readyParsedTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, getUser());

        // 创建一个字节流
        InputStream is = new ByteArrayInputStream(readyParsedTemplate.getBytes(StandardCharsets.UTF_8));
        // 创建一个文档资源
        DocumentSource docSource = new StreamDocumentSource(is, null, "text/html; charset=utf-8");
        // 创建一个文件流
        String fileName = UUID.randomUUID().toString();
        FileOutputStream out = new FileOutputStream("./" + new File(fileName + ".png"));

        try {

            // 解析输入文档
            DOMSource parser = new DefaultDOMSource(docSource);
            // 创建CSS解析器
            DOMAnalyzer da = new DOMAnalyzer(parser.parse(), docSource.getURL());

            // 设置样式属性
            da.attributesToStyles();
            da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT);
            da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT);
            da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT);
            da.getStyleSheets();
            BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, docSource.getURL());
            contentCanvas.createLayout(new Dimension(width, height));

            // 生成png文件
            ImageIO.write(contentCanvas.getImage(), "png", out);

        } catch (Exception e) {
           log.info("HtmlToPng Exception", e);

        } finally {
            out.close();
            is.close();
            docSource.close();
        }
    }
}
